package co.edu.poli.Examen2.aplication.port.out;

/**
 * Formatos disponibles para la Strategy de serialización.
 */
public enum FormatoSerializacion {
    JSON("JSON");

    private final String etiqueta;

    FormatoSerializacion(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
