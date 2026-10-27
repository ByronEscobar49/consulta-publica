// Formateo de lo que se le muestra al vecino. Funciones puras: faciles de probar.

const quetzales = new Intl.NumberFormat("es-GT", { style: "currency", currency: "GTQ" });

const metros = new Intl.NumberFormat("es-GT", { maximumFractionDigits: 2 });

const MESES = [
  "enero", "febrero", "marzo", "abril", "mayo", "junio",
  "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre",
];

export function formatearMonto(valor: number): string {
  return quetzales.format(valor);
}

export function formatearMetrosCubicos(valor: number): string {
  return metros.format(valor);
}

/** "2026-09-30" -> "30/09/2026". Se parte el texto a mano: new Date("2026-09-30") correria el dia por la zona horaria. */
export function formatearFecha(iso: string): string {
  const [anio, mes, dia] = iso.split("-");
  return anio && mes && dia ? `${dia}/${mes}/${anio}` : iso;
}

/** "2026-09" -> "septiembre 2026". */
export function formatearPeriodo(periodo: string): string {
  const [anio, mes] = periodo.split("-");
  const nombre = MESES[Number(mes) - 1];
  return nombre ? `${nombre} ${anio}` : periodo;
}

/** Momento de la consulta (llega en UTC) mostrado en hora de Guatemala. */
export function formatearFechaHora(iso: string): string {
  const fecha = new Date(iso);
  if (Number.isNaN(fecha.getTime())) return iso;
  return new Intl.DateTimeFormat("es-GT", {
    dateStyle: "medium",
    timeStyle: "short",
    timeZone: "America/Guatemala",
  }).format(fecha);
}
