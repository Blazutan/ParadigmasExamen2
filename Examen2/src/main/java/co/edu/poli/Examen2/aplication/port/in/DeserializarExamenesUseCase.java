package co.edu.poli.Examen2.aplication.port.in;

/** Caso de uso de entrada para recuperar exámenes persistidos. */
public interface DeserializarExamenesUseCase {
    /** Lee los exámenes almacenados y reemplaza el contenido del repositorio. */
    void deserializarExamenes();
}
