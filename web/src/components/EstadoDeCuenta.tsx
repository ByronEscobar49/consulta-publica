import {
  formatearFecha,
  formatearFechaHora,
  formatearMetrosCubicos,
  formatearMonto,
  formatearPeriodo,
} from "@/lib/formato";
import type { EstadoBoleta, EstadoContador, EstadoDeCuenta as Datos } from "@/lib/tipos";
import estilos from "./EstadoDeCuenta.module.css";

const ETIQUETA_BOLETA: Record<EstadoBoleta, string> = {
  PENDIENTE: "Pendiente",
  VENCIDA: "Vencida",
  PAGADA: "Pagada",
  ANULADA: "Anulada",
};

const ETIQUETA_CONTADOR: Record<EstadoContador, string> = {
  ACTIVO: "Activo",
  INACTIVO: "Inactivo",
  DANADO: "Dañado",
};

/** El estado se comunica con TEXTO y color (no solo color), para que lo entienda todo el mundo. */
function InsigniaBoleta({ estado }: { estado: EstadoBoleta }) {
  return <span className={`${estilos.insignia} ${estilos[estado]}`}>{ETIQUETA_BOLETA[estado]}</span>;
}

/** Vista de solo presentacion: recibe los datos ya validados por el servidor y los dibuja. */
export default function EstadoDeCuenta({ datos }: { datos: Datos }) {
  const { contador, titular, resumen, boletas, pagos } = datos;
  const alDia = resumen.saldoPendiente <= 0;

  return (
    <section className={estilos.vista} aria-labelledby="titulo-estado">
      <div className={estilos.tarjeta}>
        <h2 id="titulo-estado">Contador {contador.codigo}</h2>
        <dl className={estilos.datos}>
          <div>
            <dt>Titular</dt>
            <dd>{titular}</dd>
          </div>
          <div>
            <dt>Ubicación del servicio</dt>
            <dd>{contador.ubicacion || "No registrada"}</dd>
          </div>
          <div>
            <dt>Caudal contratado</dt>
            <dd>{contador.paja}</dd>
          </div>
          <div>
            <dt>Estado del contador</dt>
            <dd>{ETIQUETA_CONTADOR[contador.estado]}</dd>
          </div>
        </dl>
      </div>

      <div className={`${estilos.tarjeta} ${alDia ? estilos.alDia : estilos.conSaldo}`}>
        {alDia ? (
          <>
            <p className={estilos.etiquetaResumen}>Saldo pendiente</p>
            <p className={estilos.monto}>{formatearMonto(0)}</p>
            <p>Estás al día: no tienes boletas pendientes de pago.</p>
          </>
        ) : (
          <>
            <p className={estilos.etiquetaResumen}>Saldo pendiente</p>
            <p className={estilos.monto}>{formatearMonto(resumen.saldoPendiente)}</p>
            <p>
              {resumen.boletasVencidas > 0 && (
                <strong>
                  {resumen.boletasVencidas} {resumen.boletasVencidas === 1 ? "boleta vencida" : "boletas vencidas"}
                </strong>
              )}
              {resumen.boletasVencidas > 0 && resumen.boletasPendientes > 0 && " · "}
              {resumen.boletasPendientes > 0 && (
                <span>
                  {resumen.boletasPendientes} {resumen.boletasPendientes === 1 ? "boleta pendiente" : "boletas pendientes"}
                </span>
              )}
            </p>
            <p className={estilos.ayuda}>Puedes pagar en la oficina con el folio de cada boleta.</p>
          </>
        )}
      </div>

      <div className={estilos.tarjeta}>
        <h3>Boletas</h3>
        {boletas.length === 0 ? (
          <p className={estilos.vacio}>Todavía no hay boletas emitidas para este contador.</p>
        ) : (
          <div className={estilos.tablaContenedor}>
            <table className={estilos.tabla}>
              <caption className={estilos.soloLectores}>Últimas boletas del contador</caption>
              <thead>
                <tr>
                  <th scope="col">Periodo</th>
                  <th scope="col">Folio</th>
                  <th scope="col" className={estilos.num}>Consumo (m³)</th>
                  <th scope="col" className={estilos.num}>Monto</th>
                  <th scope="col" className={estilos.num}>Pagado</th>
                  <th scope="col" className={estilos.num}>Saldo</th>
                  <th scope="col">Vence</th>
                  <th scope="col">Estado</th>
                </tr>
              </thead>
              <tbody>
                {boletas.map((b) => (
                  <tr key={b.folio}>
                    <td data-label="Periodo">{formatearPeriodo(b.periodo)}</td>
                    <td data-label="Folio">{b.folio}</td>
                    <td className={estilos.num} data-label="Consumo (m³)">{formatearMetrosCubicos(b.consumoM3)}</td>
                    <td className={estilos.num} data-label="Monto">{formatearMonto(b.monto)}</td>
                    <td className={estilos.num} data-label="Pagado">{formatearMonto(b.montoPagado)}</td>
                    <td className={estilos.num} data-label="Saldo">{formatearMonto(b.saldo)}</td>
                    <td data-label="Vence">{formatearFecha(b.fechaVencimiento)}</td>
                    <td data-label="Estado"><InsigniaBoleta estado={b.estado} /></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <div className={estilos.tarjeta}>
        <h3>Pagos recientes</h3>
        {pagos.length === 0 ? (
          <p className={estilos.vacio}>Todavía no hay pagos registrados.</p>
        ) : (
          <div className={estilos.tablaContenedor}>
            <table className={estilos.tabla}>
              <caption className={estilos.soloLectores}>Últimos pagos del contador</caption>
              <thead>
                <tr>
                  <th scope="col">Fecha</th>
                  <th scope="col">Recibo</th>
                  <th scope="col">Boleta</th>
                  <th scope="col">Método</th>
                  <th scope="col" className={estilos.num}>Monto</th>
                </tr>
              </thead>
              <tbody>
                {pagos.map((p) => (
                  <tr key={p.folio}>
                    <td data-label="Fecha">{formatearFecha(p.fechaPago)}</td>
                    <td data-label="Recibo">{p.folio}</td>
                    <td data-label="Boleta">{p.folioBoleta}</td>
                    <td data-label="Método">{p.metodo}</td>
                    <td className={estilos.num} data-label="Monto">{formatearMonto(p.monto)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <p className={estilos.nota}>
        Consulta realizada el {formatearFechaHora(datos.consultadoEn)}. Esta información es de carácter
        informativo; para aclaraciones acude a la oficina.
      </p>
    </section>
  );
}
