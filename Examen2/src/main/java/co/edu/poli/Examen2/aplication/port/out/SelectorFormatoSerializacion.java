package co.edu.poli.Examen2.aplication.port.out;

/**
 * Puerto que permite seleccionar la Strategy activa sin depender de su implementación.
 */
public interface SelectorFormatoSerializacion {
    void seleccionar(FormatoSerializacion formato);
}
