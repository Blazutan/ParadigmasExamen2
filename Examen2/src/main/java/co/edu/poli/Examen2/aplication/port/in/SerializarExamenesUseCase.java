package co.edu.poli.Examen2.aplication.port.in;

/** Caso de uso de entrada para persistir los exámenes. */
public interface SerializarExamenesUseCase {
    /** Guarda el conjunto actual de exámenes mediante el puerto de serialización. */
    void serializarExamenes();
}
