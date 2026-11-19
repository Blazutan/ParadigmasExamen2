package co.edu.poli.Examen2.infrastructure.persistence;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;
import co.edu.poli.Examen2.aplication.port.out.SerializacionExamenesPort;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** Adaptador que persiste exámenes mediante serialización estándar de Java. */
public class SerializadorJava implements SerializacionExamenesPort {
    private final Path archivo;

    /** Crea el serializador dirigido al archivo predeterminado. */
    public SerializadorJava() {
        this(Paths.get("examenes-sangre.ser"));
    }

    SerializadorJava(Path archivo) {
        this.archivo = archivo;
    }

    @Override
    public void serializar(List<ExamenSangre> examenes) {
        try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(archivo))) {
            salida.writeObject(new ArrayList<>(examenes));
        } catch (IOException e) {
            throw new IllegalStateException("No se pudieron serializar los exámenes en " + archivo + ".", e);
        }
    }

    @Override
    public List<ExamenSangre> deserializar() {
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(archivo))) {
            entrada.setObjectInputFilter(info -> {
                if (info.depth() > 10 || info.references() > 10000 || info.streamBytes() > 10_000_000) {
                    return ObjectInputFilter.Status.REJECTED;
                }
                Class<?> clase = info.serialClass();
                if (clase == null) {
                    return ObjectInputFilter.Status.UNDECIDED;
                }
                if (clase == ArrayList.class || clase == ExamenSangre.class
                        || clase == String.class || clase == BigDecimal.class
                        || clase == BigInteger.class || clase == Number.class || clase == Enum.class
                        || clase.isEnum()
                        || (clase.isArray()
                        && (clase.getComponentType() == Object.class || clase.getComponentType().isPrimitive()))) {
                    return ObjectInputFilter.Status.ALLOWED;
                }
                return ObjectInputFilter.Status.REJECTED;
            });
            Object contenido = entrada.readObject();
            if (!(contenido instanceof List<?>)) {
                throw new IllegalStateException("El archivo no contiene una lista de exámenes.");
            }

            List<ExamenSangre> examenes = new ArrayList<>();
            for (Object elemento : (List<?>) contenido) {
                if (!(elemento instanceof ExamenSangre)) {
                    throw new IllegalStateException("El archivo contiene datos que no son exámenes.");
                }
                examenes.add((ExamenSangre) elemento);
            }
            return examenes;
        } catch (EOFException e) {
            throw new IllegalStateException("El archivo de exámenes está vacío: " + archivo + ".", e);
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("No se pudieron deserializar los exámenes desde " + archivo + ".", e);
        }
    }
}
