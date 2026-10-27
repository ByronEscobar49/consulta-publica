"use client";

import { useRef, useState, type FormEvent } from "react";
import type { EstadoDeCuenta as Datos } from "@/lib/tipos";
import EstadoDeCuenta from "./EstadoDeCuenta";
import Recaptcha, { type RecaptchaHandle } from "./Recaptcha";
import estilos from "./ConsultaForm.module.css";

// Mismo criterio que el contrato y el BFF: letras, numeros y guiones, de 3 a 30.
const CODIGO_VALIDO = /^[A-Za-z0-9-]{3,30}$/;
const SITE_KEY = process.env.NEXT_PUBLIC_RECAPTCHA_SITE_KEY;

type Estado =
  | { fase: "inicial" }
  | { fase: "cargando" }
  | { fase: "error"; mensaje: string }
  | { fase: "ok"; datos: Datos };

export default function ConsultaForm() {
  const [codigo, setCodigo] = useState("");
  const [errorCodigo, setErrorCodigo] = useState<string | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [estado, setEstado] = useState<Estado>({ fase: "inicial" });
  const captcha = useRef<RecaptchaHandle>(null);

  const cargando = estado.fase === "cargando";

  async function consultar(evento: FormEvent<HTMLFormElement>) {
    evento.preventDefault();
    const limpio = codigo.trim();

    if (!CODIGO_VALIDO.test(limpio)) {
      setErrorCodigo("Escribe el código de tu contador (letras, números y guiones), por ejemplo CTR-0001.");
      return;
    }
    if (!token) {
      setEstado({ fase: "error", mensaje: "Marca la casilla “No soy un robot” para continuar." });
      return;
    }

    setErrorCodigo(null);
    setEstado({ fase: "cargando" });

    try {
      const respuesta = await fetch("/api/consulta", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ codigo: limpio, captchaToken: token }),
      });
      const cuerpo = await respuesta.json().catch(() => null);

      if (respuesta.ok) {
        setEstado({ fase: "ok", datos: cuerpo as Datos });
      } else {
        let mensaje = (cuerpo as { message?: string } | null)?.message ?? "No se pudo completar la consulta.";
        const espera = respuesta.headers.get("Retry-After");
        if (respuesta.status === 429 && espera) mensaje += ` (espera ${espera} segundos)`;
        setEstado({ fase: "error", mensaje });
      }
    } catch {
      setEstado({ fase: "error", mensaje: "No hay conexión con el servidor. Revisa tu internet e inténtalo de nuevo." });
    } finally {
      // Cada token de captcha sirve una sola vez: hay que pedir uno nuevo para la siguiente consulta.
      captcha.current?.reset();
    }
  }

  return (
    <>
      <form className={estilos.formulario} onSubmit={consultar} noValidate>
        <label htmlFor="codigo" className={estilos.etiqueta}>Código del contador</label>
        <input
          id="codigo"
          name="codigo"
          className={estilos.campo}
          value={codigo}
          onChange={(e) => setCodigo(e.target.value)}
          placeholder="Ejemplo: CTR-0001"
          autoComplete="off"
          autoCapitalize="characters"
          maxLength={30}
          aria-invalid={errorCodigo !== null}
          aria-describedby={errorCodigo ? "error-codigo" : "ayuda-codigo"}
        />
        {errorCodigo ? (
          <p id="error-codigo" className={estilos.error} role="alert">{errorCodigo}</p>
        ) : (
          <p id="ayuda-codigo" className={estilos.ayuda}>Lo encuentras impreso en tu boleta de cobro.</p>
        )}

        <div className={estilos.captcha}>
          {SITE_KEY ? (
            <Recaptcha ref={captcha} siteKey={SITE_KEY} onChange={setToken} />
          ) : (
            <p role="alert" className={estilos.error}>
              Falta configurar NEXT_PUBLIC_RECAPTCHA_SITE_KEY en web/.env.local.
            </p>
          )}
        </div>

        <button type="submit" className={estilos.boton} disabled={cargando || !SITE_KEY}>
          {cargando ? "Consultando…" : "Consultar"}
        </button>
      </form>

      <div aria-live="polite">
        {estado.fase === "error" && <p className={estilos.alerta} role="alert">{estado.mensaje}</p>}
        {estado.fase === "ok" && <EstadoDeCuenta datos={estado.datos} />}
      </div>
    </>
  );
}
