package gt.edu.umg.desarrolloweb.consulta.service;

public class ContadorNoEncontradoException extends RuntimeException {

    public ContadorNoEncontradoException() {
        super("No se encontró un contador con ese código");
    }
}
