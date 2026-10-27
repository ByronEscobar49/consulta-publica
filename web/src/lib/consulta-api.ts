// Cliente del consulta-service (Spring Boot). Solo se usa desde el servidor.

const BASE_URL = process.env.CONSULTA_API_URL ?? "http://localhost:8081";

export type RespuestaApi =
  | { tipo: "ok"; datos: unknown }
  | { tipo: "error"; status: number; mensaje: string; retryAfter?: string }
  | { tipo: "no-disponible" };

/** Mensajes que SI se le muestran al visitante; nunca se reenvia texto interno del servicio. */
const MENSAJES: Record<number, string> = {
  400: "El código del contador no es válido.",
  404: "No se encontró un contador con ese código.",
  429: "Demasiadas consultas. Intenta de nuevo en unos segundos.",
};

/**
 * Consulta el estado de cuenta. `ip` es la IP real del visitante: se envia en X-Forwarded-For
 * para que el rate limit del servicio cuente por visitante y no por servidor web.
 */
export async function consultarEstadoDeCuenta(codigo: string, ip?: string): Promise<RespuestaApi> {
  const headers: Record<string, string> = { Accept: "application/json" };
  if (ip) headers["X-Forwarded-For"] = ip;

  try {
    const res = await fetch(`${BASE_URL}/api/v1/consulta/${encodeURIComponent(codigo)}`, {
      headers,
      cache: "no-store", // es el estado de cuenta de alguien: jamas se cachea
      signal: AbortSignal.timeout(5000),
    });

    if (res.ok) return { tipo: "ok", datos: await res.json() };

    const mensaje = MENSAJES[res.status];
    if (mensaje) {
      return {
        tipo: "error",
        status: res.status,
        mensaje,
        retryAfter: res.headers.get("Retry-After") ?? undefined,
      };
    }
    console.error(`[consulta-api] Respuesta inesperada ${res.status}`);
    return { tipo: "no-disponible" };
  } catch (error) {
    console.error("[consulta-api] No se pudo contactar al servicio:", error);
    return { tipo: "no-disponible" };
  }
}
