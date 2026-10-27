// Tipos de la respuesta de consulta-service. Reflejan los esquemas de consulta-api.yaml:
// si el contrato cambia, hay que actualizar este archivo (a futuro se podria generar con
// openapi-typescript, igual que Spring genera sus DTOs).

export type EstadoBoleta = "PENDIENTE" | "VENCIDA" | "PAGADA" | "ANULADA";
export type EstadoContador = "ACTIVO" | "INACTIVO" | "DANADO";

export interface Contador {
  codigo: string;
  estado: EstadoContador;
  paja: string;
  ubicacion: string;
}

export interface Resumen {
  saldoPendiente: number;
  boletasPendientes: number;
  boletasVencidas: number;
  ultimoPeriodo?: string; // AAAA-MM
}

export interface Boleta {
  folio: string;
  periodo: string; // AAAA-MM
  consumoM3: number;
  monto: number;
  montoPagado: number;
  saldo: number;
  fechaEmision: string; // AAAA-MM-DD
  fechaVencimiento: string; // AAAA-MM-DD
  estado: EstadoBoleta;
}

export interface Pago {
  folio: string;
  fechaPago: string; // AAAA-MM-DD
  monto: number;
  metodo: string;
  folioBoleta: string;
}

export interface EstadoDeCuenta {
  contador: Contador;
  titular: string;
  resumen: Resumen;
  boletas: Boleta[];
  pagos: Pago[];
  consultadoEn: string; // ISO, UTC
}
