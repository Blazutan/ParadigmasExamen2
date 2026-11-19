package co.edu.poli.Examen2.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;
import co.edu.poli.Examen2.aplication.domain.model.FactorRh;
import co.edu.poli.Examen2.aplication.domain.model.TipoSangre;

class SerializadorJsonTest {
    @TempDir
    Path temporal;

    @Test
    void serializaYDeserializaExamenes() throws Exception {
        Path archivo = temporal.resolve("examenes.json");
        SerializadorJson serializador = new SerializadorJson(archivo);
        ExamenSangre examen = new ExamenSangre(
                "EX-001", "Ana Pérez", new BigDecimal("125000"),
                TipoSangre.O, FactorRh.POSITIVO);

        serializador.serializar(Arrays.asList(examen));
        List<ExamenSangre> cargados = serializador.deserializar();

        assertEquals(1, cargados.size());
        assertEquals("EX-001", cargados.get(0).getCodigo());
        assertEquals("Ana Pérez", cargados.get(0).getNombrePaciente());
        assertEquals(TipoSangre.O, cargados.get(0).getTipoSangre());
        assertEquals(FactorRh.POSITIVO, cargados.get(0).getFactorRh());
    }

    @Test
    void rechazaArchivoJsonInvalido() throws Exception {
        Path archivo = temporal.resolve("examenes.json");
        Files.write(archivo, "[{\"codigo\":\"EX-1\"}]".getBytes("UTF-8"));

        SerializadorJson serializador = new SerializadorJson(archivo);

        assertThrows(IllegalStateException.class, serializador::deserializar);
    }

    @Test
    void serializaCaracteresEspeciales() throws Exception {
        Path archivo = temporal.resolve("especiales.json");
        SerializadorJson serializador = new SerializadorJson(archivo);
        ExamenSangre examen = new ExamenSangre(
                "EX\\\"-1", "Ana\nPérez\tPrueba\b\f\r/",
                new BigDecimal("12.50"), TipoSangre.AB, FactorRh.NEGATIVO);

        serializador.serializar(Collections.singletonList(examen));
        ExamenSangre cargado = serializador.deserializar().get(0);

        assertEquals("EX\\\"-1", cargado.getCodigo());
        assertEquals("Ana\nPérez\tPrueba\b\f\r/", cargado.getNombrePaciente());
        assertEquals(new BigDecimal("12.50"), cargado.getCosto());
    }

    @Test
    void serializaListaVacia() throws Exception {
        Path archivo = temporal.resolve("vacio.json");
        SerializadorJson serializador = new SerializadorJson(archivo);

        serializador.serializar(Collections.emptyList());

        assertEquals("[]", Files.readAllLines(archivo).stream()
                .reduce("", String::concat));
        assertEquals(Collections.emptyList(), serializador.deserializar());
    }

    @Test
    void rechazaArchivoJsonVacio() throws Exception {
        Path archivo = temporal.resolve("archivo-vacio.json");
        Files.write(archivo, new byte[0]);

        SerializadorJson serializador = new SerializadorJson(archivo);

        IllegalStateException error =
                assertThrows(IllegalStateException.class, serializador::deserializar);
        assertTrue(error.getMessage().contains("está vacío"));
    }

    @Test
    void rechazaEscapeJsonInvalido() throws Exception {
        Path archivo = temporal.resolve("escape-invalido.json");
        Files.write(archivo,
                ("[{\"codigo\":\"EX-1\",\"nombrePaciente\":\"Ana\\q\",\"costo\":1,"
                        + "\"tipoSangre\":\"A\",\"factorRh\":\"POSITIVO\"}]")
                        .getBytes("UTF-8"));

        SerializadorJson serializador = new SerializadorJson(archivo);

        assertThrows(IllegalStateException.class, serializador::deserializar);
    }

    @Test
    void deserializaEscapeUnicode() throws Exception {
        Path archivo = temporal.resolve("unicode.json");
        Files.write(archivo,
                ("[{\"codigo\":\"EX-1\",\"nombrePaciente\":\"Jos\\u00e9\","
                        + "\"costo\":1,\"tipoSangre\":\"A\",\"factorRh\":\"POSITIVO\"}]")
                        .getBytes("UTF-8"));

        SerializadorJson serializador = new SerializadorJson(archivo);

        assertEquals("José", serializador.deserializar().get(0).getNombrePaciente());
    }
}
