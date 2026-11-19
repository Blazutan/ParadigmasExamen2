package co.edu.poli.Examen2.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;
import co.edu.poli.Examen2.aplication.domain.model.FactorRh;
import co.edu.poli.Examen2.aplication.domain.model.TipoSangre;

class RepositorioExamenesEnMemoriaTest {

    @Test
    void reemplazarTodosReiniciaElContenido() {
        RepositorioExamenesEnMemoria repositorio = new RepositorioExamenesEnMemoria();
        repositorio.guardar(examen("EX-001"));

        repositorio.reemplazarTodos(Arrays.asList(examen("EX-002"), examen("EX-003")));

        assertEquals(2, repositorio.buscarTodos().size());
        assertEquals("EX-002", repositorio.buscarTodos().get(0).getCodigo());
    }

    @Test
    void buscarTodosDevuelveUnaListaIndependiente() {
        RepositorioExamenesEnMemoria repositorio = new RepositorioExamenesEnMemoria();
        repositorio.guardar(examen("EX-001"));

        List<ExamenSangre> copia = repositorio.buscarTodos();
        copia.clear();

        assertEquals(1, repositorio.buscarTodos().size());
        assertNotSame(copia, repositorio.buscarTodos());
    }

    private ExamenSangre examen(String codigo) {
        return new ExamenSangre(codigo, "Paciente", BigDecimal.ONE,
                TipoSangre.B, FactorRh.NEGATIVO);
    }
}
