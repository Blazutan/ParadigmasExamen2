package co.edu.poli.Examen2.infrastructure.persistence;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;
import co.edu.poli.Examen2.aplication.domain.model.FactorRh;
import co.edu.poli.Examen2.aplication.domain.model.TipoSangre;
import co.edu.poli.Examen2.aplication.port.out.SerializacionExamenesPort;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Strategy concreta que guarda y carga exámenes como un arreglo JSON.
 */
public class SerializadorJson implements SerializacionExamenesPort {
    private final Path archivo;
    private static final Map<Character, String> ESCAPES_ESCRITURA;
    private static final Map<Character, Character> ESCAPES_LECTURA;

    static {
        Map<Character, String> escritura = new LinkedHashMap<>();
        escritura.put('"', "\\\"");
        escritura.put('\\', "\\\\");
        escritura.put('\b', "\\b");
        escritura.put('\f', "\\f");
        escritura.put('\n', "\\n");
        escritura.put('\r', "\\r");
        escritura.put('\t', "\\t");
        ESCAPES_ESCRITURA = Collections.unmodifiableMap(escritura);

        Map<Character, Character> lectura = new HashMap<>();
        lectura.put('"', '"');
        lectura.put('\\', '\\');
        lectura.put('/', '/');
        lectura.put('b', '\b');
        lectura.put('f', '\f');
        lectura.put('n', '\n');
        lectura.put('r', '\r');
        lectura.put('t', '\t');
        ESCAPES_LECTURA = Collections.unmodifiableMap(lectura);
    }

    /** Crea el serializador dirigido al archivo predeterminado. */
    public SerializadorJson() {
        this(Paths.get("examenes-sangre.json"));
    }

    SerializadorJson(Path archivo) {
        this.archivo = archivo;
    }

    @Override
    public void serializar(List<ExamenSangre> examenes) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < examenes.size(); i++) {
            ExamenSangre examen = examenes.get(i);
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"codigo\":");
            agregarCadena(json, examen.getCodigo());
            json.append(",\"nombrePaciente\":");
            agregarCadena(json, examen.getNombrePaciente());
            json.append(",\"costo\":").append(examen.getCosto().toPlainString());
            json.append(",\"tipoSangre\":");
            agregarCadena(json, examen.getTipoSangre().name());
            json.append(",\"factorRh\":");
            agregarCadena(json, examen.getFactorRh().name());
            json.append('}');
        }
        json.append(']');

        try {
            Files.write(archivo, json.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("No se pudieron serializar los exámenes en " + archivo + ".", e);
        }
    }

    @Override
    public List<ExamenSangre> deserializar() {
        final String contenido;
        try {
            contenido = new String(Files.readAllBytes(archivo), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudieron deserializar los exámenes desde " + archivo + ".", e);
        }
        if (contenido.trim().isEmpty()) {
            throw new IllegalStateException("El archivo JSON de exámenes está vacío: " + archivo + ".");
        }

        try {
            return new JsonReader(contenido).leerExamenes();
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("El archivo JSON contiene datos inválidos: " + e.getMessage(), e);
        }
    }

    private static void agregarCadena(StringBuilder json, String valor) {
        json.append('"');
        for (int i = 0; i < valor.length(); i++) {
            agregarCaracterEscapado(json, valor.charAt(i));
        }
        json.append('"');
    }

    private static void agregarCaracterEscapado(StringBuilder json, char caracter) {
        String escape = ESCAPES_ESCRITURA.get(caracter);
        if (escape != null) {
            json.append(escape);
        } else if (caracter < 0x20) {
            json.append("\\u");
            json.append(hex(caracter >> 12));
            json.append(hex(caracter >> 8));
            json.append(hex(caracter >> 4));
            json.append(hex(caracter));
        } else {
            json.append(caracter);
        }
    }

    private static char hex(int valor) {
        return "0123456789abcdef".charAt(valor & 0x0f);
    }

    /**
     * Lector mínimo del esquema JSON de exámenes, sin dependencias externas.
     */
    private static final class JsonReader {
        private final String json;
        private int posicion;

        private JsonReader(String json) {
            this.json = json;
        }

        private List<ExamenSangre> leerExamenes() {
            List<ExamenSangre> examenes = new ArrayList<>();
            espacios();
            esperar('[');
            espacios();
            if (!consumir(']')) {
                do {
                    espacios();
                    examenes.add(leerExamen());
                    espacios();
                } while (consumir(','));
                esperar(']');
            }
            espacios();
            if (posicion != json.length()) {
                invalidar("hay contenido después del arreglo");
            }
            return examenes;
        }

        private ExamenSangre leerExamen() {
            esperar('{');
            espacios();
            Map<String, String> campos = new HashMap<>();
            if (!consumir('}')) {
                while (true) {
                    espacios();
                    String nombreCampo = cadena();
                    if (!esCampoValido(nombreCampo)) {
                        invalidar("campo desconocido: " + nombreCampo);
                    }
                    espacios();
                    esperar(':');
                    espacios();
                    String valor = "costo".equals(nombreCampo) ? numero() : cadena();
                    if (campos.put(nombreCampo, valor) != null) {
                        invalidar("campo repetido: " + nombreCampo);
                    }
                    espacios();
                    if (!consumir(',')) {
                        esperar('}');
                        break;
                    }
                }
            }
            if (campos.size() != 5) {
                invalidar("cada examen debe tener cinco campos");
            }
            return new ExamenSangre(
                    campos.get("codigo"),
                    campos.get("nombrePaciente"),
                    new BigDecimal(campos.get("costo")),
                    TipoSangre.valueOf(campos.get("tipoSangre")),
                    FactorRh.valueOf(campos.get("factorRh")));
        }

        private boolean esCampoValido(String campo) {
            return "codigo".equals(campo)
                    || "nombrePaciente".equals(campo)
                    || "costo".equals(campo)
                    || "tipoSangre".equals(campo)
                    || "factorRh".equals(campo);
        }

        private String cadena() {
            esperar('"');
            StringBuilder resultado = new StringBuilder();
            while (posicion < json.length()) {
                char caracter = json.charAt(posicion++);
                if (caracter == '"') {
                    return resultado.toString();
                }
                if (caracter < 0x20) {
                    invalidar("carácter de control sin escapar");
                }
                if (caracter != '\\') {
                    resultado.append(caracter);
                    continue;
                }
                resultado.append(secuenciaEscape());
            }
            invalidar("cadena sin cerrar");
            return "";
        }

        private char secuenciaEscape() {
            if (posicion >= json.length()) {
                invalidar("secuencia de escape incompleta");
            }
            char escape = json.charAt(posicion++);
            if (escape == 'u') {
                return unicode();
            }
            Character caracter = ESCAPES_LECTURA.get(escape);
            if (caracter == null) {
                invalidar("secuencia de escape inválida");
            }
            return caracter;
        }

        private char unicode() {
            if (posicion + 4 > json.length()) {
                invalidar("escape Unicode incompleto");
            }
            int valor = 0;
            for (int i = 0; i < 4; i++) {
                int digito = Character.digit(json.charAt(posicion++), 16);
                if (digito < 0) {
                    invalidar("escape Unicode inválido");
                }
                valor = (valor << 4) + digito;
            }
            return (char) valor;
        }

        private String numero() {
            int inicio = posicion;
            consumir('-');
            if (!consumir('0')) {
                digitos();
            }
            if (consumir('.')) {
                digitos();
            }
            if (consumir('e') || consumir('E')) {
                if (!consumir('+')) {
                    consumir('-');
                }
                digitos();
            }
            if (inicio == posicion) {
                invalidar("se esperaba un número");
            }
            return json.substring(inicio, posicion);
        }

        private void digitos() {
            int inicio = posicion;
            while (posicion < json.length() && Character.isDigit(json.charAt(posicion))) {
                posicion++;
            }
            if (inicio == posicion) {
                invalidar("número incompleto");
            }
        }

        private void espacios() {
            while (posicion < json.length()) {
                char caracter = json.charAt(posicion);
                if (caracter != ' ' && caracter != '\n' && caracter != '\r' && caracter != '\t') {
                    return;
                }
                posicion++;
            }
        }

        private boolean consumir(char esperado) {
            if (posicion < json.length() && json.charAt(posicion) == esperado) {
                posicion++;
                return true;
            }
            return false;
        }

        private void esperar(char esperado) {
            if (!consumir(esperado)) {
                invalidar("se esperaba '" + esperado + "'");
            }
        }

        private void invalidar(String mensaje) {
            throw new IllegalArgumentException(mensaje + " en la posición " + posicion + ".");
        }
    }
}
