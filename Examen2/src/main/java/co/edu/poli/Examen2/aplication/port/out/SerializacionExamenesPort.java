package co.edu.poli.Examen2.aplication.port.out;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;

import java.util.List;

/** Puerto de salida que abstrae la persistencia serializada de exámenes. */
public interface SerializacionExamenesPort {
    /**
     * Serializa una colección de exámenes.
     * @param examenes exámenes que serán guardados
     */
    void serializar(List<ExamenSangre> examenes);

    /** @return exámenes deserializados */
    List<ExamenSangre> deserializar();
}
