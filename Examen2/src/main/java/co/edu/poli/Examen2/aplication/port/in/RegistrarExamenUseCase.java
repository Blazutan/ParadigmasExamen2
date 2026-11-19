package co.edu.poli.Examen2.aplication.port.in;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;

/** Caso de uso de entrada para registrar exámenes de sangre. */
public interface RegistrarExamenUseCase {
    /**
     * Registra un examen si cumple las reglas de negocio.
     * @param examen examen a registrar
     * @return examen registrado
     */
    ExamenSangre registrarExamen(ExamenSangre examen);
}
