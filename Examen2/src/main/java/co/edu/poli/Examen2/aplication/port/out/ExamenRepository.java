package co.edu.poli.Examen2.aplication.port.out;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;

import java.util.List;

/** Puerto de salida para almacenar y consultar exámenes de sangre. */
public interface ExamenRepository {
    /**
     * Guarda o reemplaza un examen según su código.
     * @param examen examen que se almacenará
     * @return examen almacenado
     */
    ExamenSangre guardar(ExamenSangre examen);

    /** @return todos los exámenes registrados */
    List<ExamenSangre> buscarTodos();

    /**
     * Sustituye el conjunto completo de exámenes almacenados.
     * @param examenes nueva colección de exámenes
     */
    void reemplazarTodos(List<ExamenSangre> examenes);
}
