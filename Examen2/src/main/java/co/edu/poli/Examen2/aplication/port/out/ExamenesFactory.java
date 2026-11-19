package co.edu.poli.Examen2.aplication.port.out;

/**
 * Factory Method que crea las salidas de persistencia requeridas por la aplicación.
 */
public interface ExamenesFactory {
    ExamenRepository crearRepositorio();

    SerializacionExamenesPort crearSerializacion();

    SelectorFormatoSerializacion crearSelectorFormato();
}
