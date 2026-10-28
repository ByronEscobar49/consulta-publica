"use client";

import { useEffect, useImperativeHandle, useRef, useState, type Ref } from "react";

/** Metodos que el formulario puede pedirle al captcha. */
export interface RecaptchaHandle {
  /** Vuelve a pedir el captcha. Obligatorio tras cada consulta: un token solo sirve una vez. */
  reset: () => void;
}

interface Props {
  siteKey: string;
  /** Recibe el token cuando el visitante resuelve el captcha, o null si expira o falla. */
  onChange: (token: string | null) => void;
  ref?: Ref<RecaptchaHandle>;
}

interface ParametrosRender {
  sitekey: string;
  /** "compact" mide 164 px de ancho; "normal" mide 304 px. */
  size: "normal" | "compact";
  callback: (token: string) => void;
  "expired-callback": () => void;
  "error-callback": () => void;
}

declare global {
  interface Window {
    grecaptcha?: {
      render: (elemento: HTMLElement, parametros: ParametrosRender) => number;
      reset: (id?: number) => void;
    };
    __recaptchaListo?: () => void;
  }
}

/** Ancho en px del captcha en tamano normal. */
const ANCHO_CAPTCHA_NORMAL = 304;

let cargaDelScript: Promise<void> | null = null;

/** Carga el script de Google una sola vez para toda la aplicacion. */
function cargarScript(): Promise<void> {
  if (window.grecaptcha?.render) return Promise.resolve();
  if (!cargaDelScript) {
    cargaDelScript = new Promise<void>((resolve, reject) => {
      window.__recaptchaListo = () => resolve();
      const script = document.createElement("script");
      script.src = "https://www.google.com/recaptcha/api.js?onload=__recaptchaListo&render=explicit&hl=es";
      script.async = true;
      script.defer = true;
      script.onerror = () => {
        cargaDelScript = null; // permite reintentar
        reject(new Error("No se pudo cargar reCAPTCHA"));
      };
      document.head.appendChild(script);
    });
  }
  return cargaDelScript;
}

/**
 * Widget "No soy un robot" de reCAPTCHA v2.
 * El token que entrega NO prueba nada por si solo: el servidor (BFF) debe validarlo con Google.
 */
export default function Recaptcha({ siteKey, onChange, ref }: Props) {
  const contenedor = useRef<HTMLDivElement>(null);
  const widgetId = useRef<number | null>(null);
  const alCambiar = useRef(onChange);
  const [fallo, setFallo] = useState(false);

  useEffect(() => {
    alCambiar.current = onChange;
  });

  useImperativeHandle(ref, () => ({
    reset() {
      if (widgetId.current !== null) window.grecaptcha?.reset(widgetId.current);
      alCambiar.current(null);
    },
  }), []);

  useEffect(() => {
    let cancelado = false;
    // Un div nuevo por montaje: asi el modo estricto de React (que monta dos veces en desarrollo)
    // no intenta dibujar el captcha dos veces en el mismo elemento.
    const elemento = document.createElement("div");
    contenedor.current?.appendChild(elemento);

    cargarScript()
      .then(() => {
        if (cancelado || !window.grecaptcha) return;
        // El captcha normal mide 304 px. En celulares angostos (por ejemplo 360 px quedan ~288 px
        // utiles) se cortaria, asi que se usa la version compacta que ofrece Google.
        const anchoDisponible = contenedor.current?.clientWidth || ANCHO_CAPTCHA_NORMAL;
        widgetId.current = window.grecaptcha.render(elemento, {
          sitekey: siteKey,
          size: anchoDisponible < ANCHO_CAPTCHA_NORMAL ? "compact" : "normal",
          callback: (token) => alCambiar.current(token),
          "expired-callback": () => alCambiar.current(null),
          "error-callback": () => alCambiar.current(null),
        });
      })
      .catch(() => {
        if (!cancelado) setFallo(true);
      });

    return () => {
      cancelado = true;
      elemento.remove();
      widgetId.current = null;
    };
  }, [siteKey]);

  return (
    <div>
      <div ref={contenedor} />
      {fallo && (
        <p role="alert">No se pudo cargar el captcha. Revisa tu conexión a internet y recarga la página.</p>
      )}
    </div>
  );
}
