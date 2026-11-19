package co.edu.poli.Examen2.aplication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;
import co.edu.poli.Examen2.aplication.domain.model.FactorRh;
import co.edu.poli.Examen2.aplication.domain.model.TipoSangre;
import co.edu.poli.Examen2.infrastructure.persistence.RepositorioExamenesEnMemoria;

class RegistrarExamenServiceTest {
    private RepositorioExamenesEnMemoria repositorio;
    private RegistrarExamenService servicio;

    @BeforeEach
    void configurar() {
        repositorio = new RepositorioExamenesEnMemoria();
        servicio = new RegistrarExamenService(repositorio);
    }

    @Test
    void registraYListaUnExamen() {
        ExamenSangre examen = examen("EX-001");

        assertEquals(examen, servicio.registrarExamen(examen));
        assertEquals(1, repositorio.buscarTodos().size());
    }

    @Test
    void rechazaCodigoDuplicadoSinDistinguirMayusculas() {
        servicio.registrarExamen(examen("EX-001"));

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarExamen(examen("ex-001")));

        assertEquals("Ya existe un examen con el código ex-001.", error.getMessage());
    }

    @Test
    void rechazaExamenNulo() {
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarExamen(null));
    }

    private ExamenSangre examen(String codigo) {
        return new ExamenSangre(codigo, "Ana Pérez", new BigDecimal("50000"),
                TipoSangre.A, FactorRh.POSITIVO);
    }
}
