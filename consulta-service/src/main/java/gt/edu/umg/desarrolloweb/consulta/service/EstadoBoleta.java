package gt.edu.umg.desarrolloweb.consulta.service;

/** Estado de una boleta. No existe en la BD del monolito: se calcula. */
public enum EstadoBoleta {
    PENDIENTE, VENCIDA, PAGADA, ANULADA
}
