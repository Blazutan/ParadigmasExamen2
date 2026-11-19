package co.edu.poli.Examen2.aplication.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Representa un examen de sangre registrado en el sistema.
 */
public class ExamenSangre implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String codigo;
    private final String nombrePaciente;
    private final BigDecimal costo;
    private final TipoSangre tipoSangre;
    private final FactorRh factorRh;

    /**
     * Crea un examen validando sus datos obligatorios.
     *
     * @param codigo código único del examen
     * @param nombrePaciente nombre del paciente
     * @param costo costo mayor o igual a cero
     * @param tipoSangre tipo de sangre seleccionado
     * @param factorRh factor RH seleccionado
     * @throws IllegalArgumentException si algún dato no es válido
     */
    public ExamenSangre(String codigo, String nombrePaciente, BigDecimal costo,
                        TipoSangre tipoSangre, FactorRh factorRh) {
        this.codigo = validarTexto(codigo, "El código del examen es obligatorio.");
        this.nombrePaciente = validarTexto(nombrePaciente, "El nombre del paciente es obligatorio.");
        if (costo == null || costo.signum() < 0) {
            throw new IllegalArgumentException("El costo debe ser un valor mayor o igual a cero.");
        }
        if (tipoSangre == null || factorRh == null) {
            throw new IllegalArgumentException("Debe seleccionar el tipo de sangre y el factor RH.");
        }

        this.costo = costo;
        this.tipoSangre = tipoSangre;
        this.factorRh = factorRh;
    }

    private static String validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }

    /** @return código identificador del examen */
    public String getCodigo() {
        return codigo;
    }

    /** @return nombre del paciente */
    public String getNombrePaciente() {
        return nombrePaciente;
    }

    /** @return costo del examen */
    public BigDecimal getCosto() {
        return costo;
    }

    /** @return tipo de sangre */
    public TipoSangre getTipoSangre() {
        return tipoSangre;
    }

    /** @return factor RH */
    public FactorRh getFactorRh() {
        return factorRh;
    }
}
