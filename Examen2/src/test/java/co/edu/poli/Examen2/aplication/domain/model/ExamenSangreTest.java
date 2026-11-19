package co.edu.poli.Examen2.aplication.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class ExamenSangreTest {

    @Test
    void creaExamenConDatosValidos() {
        ExamenSangre examen = new ExamenSangre(
                "  EX-001  ", " Ana Pérez ", new BigDecimal("125000"),
                TipoSangre.O, FactorRh.POSITIVO);

        assertEquals("EX-001", examen.getCodigo());
        assertEquals("Ana Pérez", examen.getNombrePaciente());
        assertEquals(new BigDecimal("125000"), examen.getCosto());
        assertEquals(TipoSangre.O, examen.getTipoSangre());
        assertEquals(FactorRh.POSITIVO, examen.getFactorRh());
    }

    @Test
    void rechazaCodigoObligatorio() {
        assertThrows(IllegalArgumentException.class,
                () -> new ExamenSangre(" ", "Ana", BigDecimal.ZERO,
                        TipoSangre.A, FactorRh.NEGATIVO));
    }

    @Test
    void rechazaCostoNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ExamenSangre("EX-001", "Ana", new BigDecimal("-1"),
                        TipoSangre.A, FactorRh.NEGATIVO));
    }
}
