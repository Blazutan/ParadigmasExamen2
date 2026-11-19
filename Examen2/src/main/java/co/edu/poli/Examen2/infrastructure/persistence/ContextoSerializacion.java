package co.edu.poli.Examen2.infrastructure.persistence;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;
import co.edu.poli.Examen2.aplication.port.out.FormatoSerializacion;
import co.edu.poli.Examen2.aplication.port.out.SelectorFormatoSerializacion;
import co.edu.poli.Examen2.aplication.port.out.SerializacionExamenesPort;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Contexto de Strategy que delega en la estrategia de serialización seleccionada.
 */
public class ContextoSerializacion implements SerializacionExamenesPort, SelectorFormatoSerializacion {
    private final Map<FormatoSerializacion, SerializacionExamenesPort> estrategias =
            new EnumMap<>(FormatoSerializacion.class);
    private SerializacionExamenesPort estrategiaActiva;

    /**
     * Inicializa el contexto con el serializador JSON.
     * @param serializadorJson estrategia JSON predeterminada
     */
    public ContextoSerializacion(SerializacionExamenesPort serializadorJson) {
        estrategias.put(FormatoSerializacion.JSON, serializadorJson);
        seleccionar(FormatoSerializacion.JSON);
    }

    @Override
    public void seleccionar(FormatoSerializacion formato) {
        SerializacionExamenesPort estrategia = estrategias.get(formato);
        if (estrategia == null) {
            throw new IllegalArgumentException("El formato de serialización no está disponible.");
        }
        estrategiaActiva = estrategia;
    }

    @Override
    public void serializar(List<ExamenSangre> examenes) {
        estrategiaActiva.serializar(examenes);
    }

    @Override
    public List<ExamenSangre> deserializar() {
        return estrategiaActiva.deserializar();
    }
}
