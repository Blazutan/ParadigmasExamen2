package co.edu.poli.Examen2.aplication.service;

import co.edu.poli.Examen2.aplication.port.in.SerializarExamenesUseCase;
import co.edu.poli.Examen2.aplication.port.out.ExamenRepository;
import co.edu.poli.Examen2.aplication.port.out.SerializacionExamenesPort;

/** Implementación del caso de uso que serializa los exámenes registrados. */
public class SerializarExamenesService implements SerializarExamenesUseCase {
    private final ExamenRepository examenRepository;
    private final SerializacionExamenesPort serializacionPort;

    /**
     * Crea el servicio con el repositorio y el puerto de serialización.
     * @param examenRepository repositorio que suministra los exámenes
     * @param serializacionPort adaptador utilizado para persistirlos
     */
    public SerializarExamenesService(ExamenRepository examenRepository,
                                     SerializacionExamenesPort serializacionPort) {
        this.examenRepository = examenRepository;
        this.serializacionPort = serializacionPort;
    }

    @Override
    public void serializarExamenes() {
        serializacionPort.serializar(examenRepository.buscarTodos());
    }
}
