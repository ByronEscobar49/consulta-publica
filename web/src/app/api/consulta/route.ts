import { verificarCaptcha } from "@/lib/captcha";
import { consultarEstadoDeCuenta } from "@/lib/consulta-api";

// Mismo criterio que el contrato (consulta-api.yaml): letras, numeros y guiones, 3 a 30.
const CODIGO_VALIDO = /^[A-Za-z0-9-]{3,30}$/;
// IPv4 o IPv6 sin espacios (evita reenviar basura o saltos de linea a otro servicio).
const IP_VALIDA = /^[0-9a-fA-F:.]{3,45}$/;

function error(status: number, message: string, extra?: HeadersInit) {
  return Response.json({ status, message }, { status, headers: extra });
}

/**
 * IP del visitante: primer valor de X-Forwarded-For (Next.js lo completa con la IP del socket
 * si no viene). Detras de un proxy de confianza ese valor es fiable; si alguien habla directo
 * con este servidor podria falsearlo, pero cada consulta igual exige un captcha recien resuelto.
 */
function ipDelVisitante(request: Request): string | undefined {
  const primera = request.headers.get("x-forwarded-for")?.split(",")[0]?.trim();
  return primera && IP_VALIDA.test(primera) ? primera : undefined;
}

/**
 * BFF: POST /api/consulta  { codigo, captchaToken }
 *  1) valida el formato  2) valida el captcha con Google  3) llama a consulta-service.
 * El navegador nunca habla con consulta-service ni conoce su direccion.
 */
export async function POST(request: Request) {
  let cuerpo: unknown;
  try {
    cuerpo = await request.json();
  } catch {
    return error(400, "Solicitud inválida.");
  }

  const { codigo, captchaToken } = (cuerpo ?? {}) as { codigo?: unknown; captchaToken?: unknown };

  if (typeof codigo !== "string" || !CODIGO_VALIDO.test(codigo.trim())) {
    return error(400, "El código del contador no es válido.");
  }
  if (typeof captchaToken !== "string" || captchaToken.length === 0 || captchaToken.length > 4096) {
    return error(400, "Resuelve el captcha para continuar.");
  }

  const ip = ipDelVisitante(request);

  // El captcha se valida ANTES de tocar el servicio: sin captcha valido no hay consulta.
  const captcha = await verificarCaptcha(captchaToken, ip);
  if (captcha === "invalido") {
    return error(403, "No se pudo verificar el captcha. Inténtalo de nuevo.");
  }
  if (captcha === "no-disponible") {
    return error(503, "No se pudo verificar el captcha en este momento. Inténtalo más tarde.");
  }

  const respuesta = await consultarEstadoDeCuenta(codigo.trim(), ip);

  switch (respuesta.tipo) {
    case "ok":
      return Response.json(respuesta.datos, { headers: { "Cache-Control": "no-store" } });
    case "error":
      return error(
        respuesta.status,
        respuesta.mensaje,
        respuesta.retryAfter ? { "Retry-After": respuesta.retryAfter } : undefined,
      );
    case "no-disponible":
      return error(502, "El servicio de consulta no está disponible. Inténtalo más tarde.");
  }
}
