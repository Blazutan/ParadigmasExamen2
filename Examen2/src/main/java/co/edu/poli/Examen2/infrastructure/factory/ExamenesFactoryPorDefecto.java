package co.edu.poli.Examen2.infrastructure.factory;

import co.edu.poli.Examen2.aplication.port.out.ExamenRepository;
import co.edu.poli.Examen2.aplication.port.out.ExamenesFactory;
import co.edu.poli.Examen2.aplication.port.out.SelectorFormatoSerializacion;
import co.edu.poli.Examen2.aplication.port.out.SerializacionExamenesPort;
import co.edu.poli.Examen2.infrastructure.persistence.ContextoSerializacion;
import co.edu.poli.Examen2.infrastructure.persistence.RepositorioExamenesEnMemoria;
import co.edu.poli.Examen2.infrastructure.persistence.SerializadorJson;

/**
 * Factory Method concreto que crea el repositorio y las estrategias de persistencia predeterminadas.
 */
public class ExamenesFactoryPorDefecto implements ExamenesFactory {
    private final ContextoSerializacion contextoSerializacion =
            new ContextoSerializacion(new SerializadorJson());

    @Override
    public ExamenRepository crearRepositorio() {
        return new RepositorioExamenesEnMemoria();
    }

    @Override
    public SerializacionExamenesPort crearSerializacion() {
        return contextoSerializacion;
    }

    @Override
    public SelectorFormatoSerializacion crearSelectorFormato() {
        return contextoSerializacion;
    }
}
