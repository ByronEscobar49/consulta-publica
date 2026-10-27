// Validacion del captcha. Se ejecuta SOLO en el servidor: la clave secreta nunca llega al navegador.

const VERIFY_URL =
  process.env.RECAPTCHA_VERIFY_URL ?? "https://www.google.com/recaptcha/api/siteverify";

export type ResultadoCaptcha = "valido" | "invalido" | "no-disponible";

/**
 * Le pregunta a Google si el token que resolvio el visitante es autentico.
 * Cada token sirve una sola vez y caduca en ~2 minutos.
 */
export async function verificarCaptcha(token: string, ip?: string): Promise<ResultadoCaptcha> {
  const secret = process.env.RECAPTCHA_SECRET_KEY;
  if (!secret) {
    console.error("[captcha] Falta RECAPTCHA_SECRET_KEY en .env.local");
    return "no-disponible";
  }

  const form = new URLSearchParams({ secret, response: token });
  if (ip) form.set("remoteip", ip);

  try {
    const res = await fetch(VERIFY_URL, {
      method: "POST",
      body: form,
      cache: "no-store",
      signal: AbortSignal.timeout(5000),
    });
    if (!res.ok) return "no-disponible";
    const data = (await res.json()) as { success?: boolean };
    return data.success === true ? "valido" : "invalido";
  } catch (error) {
    console.error("[captcha] No se pudo contactar al verificador:", error);
    return "no-disponible";
  }
}
