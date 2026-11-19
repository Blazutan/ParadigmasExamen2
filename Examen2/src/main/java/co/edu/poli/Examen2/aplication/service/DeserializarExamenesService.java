package co.edu.poli.Examen2.aplication.service;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;
import co.edu.poli.Examen2.aplication.port.in.DeserializarExamenesUseCase;
import co.edu.poli.Examen2.aplication.port.out.ExamenRepository;
import co.edu.poli.Examen2.aplication.port.out.SerializacionExamenesPort;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Implementación del caso de uso que carga exámenes desde persistencia.
 * Valida códigos nulos y duplicados antes de reemplazar el repositorio.
 */
public class DeserializarExamenesService implements DeserializarExamenesUseCase {
    private final ExamenRepository examenRepository;
    private final SerializacionExamenesPort serializacionPort;

    /**
     * Crea el servicio con el repositorio y el puerto de serialización.
     * @param examenRepository repositorio que recibirá los exámenes
     * @param serializacionPort adaptador utilizado para recuperarlos
     */
    public DeserializarExamenesService(ExamenRepository examenRepository,
                                       SerializacionExamenesPort serializacionPort) {
        this.examenRepository = examenRepository;
        this.serializacionPort = serializacionPort;
    }

    @Override
    public void deserializarExamenes() {
        List<ExamenSangre> examenes = serializacionPort.deserializar();
        Set<String> codigos = new HashSet<>();
        for (ExamenSangre examen : examenes) {
            if (examen == null || !codigos.add(examen.getCodigo().toUpperCase(Locale.ROOT))) {
                throw new IllegalArgumentException("El archivo contiene exámenes inválidos o códigos repetidos.");
            }
        }
        examenRepository.reemplazarTodos(examenes);
    }
}
