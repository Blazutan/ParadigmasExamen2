package co.edu.poli.Examen2.aplication.port.in;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;

import java.util.List;

/** Caso de uso de entrada para consultar los exámenes registrados. */
public interface ListarExamenesUseCase {
    /** @return lista de exámenes registrados */
    List<ExamenSangre> listarExamenes();
}
