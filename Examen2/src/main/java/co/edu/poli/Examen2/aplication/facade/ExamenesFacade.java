package co.edu.poli.Examen2.aplication.facade;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;
import co.edu.poli.Examen2.aplication.domain.model.FactorRh;
import co.edu.poli.Examen2.aplication.domain.model.TipoSangre;
import co.edu.poli.Examen2.aplication.port.in.DeserializarExamenesUseCase;
import co.edu.poli.Examen2.aplication.port.in.ListarExamenesUseCase;
import co.edu.poli.Examen2.aplication.port.in.RegistrarExamenUseCase;
import co.edu.poli.Examen2.aplication.port.in.SerializarExamenesUseCase;
import co.edu.poli.Examen2.aplication.port.out.ExamenesFactory;
import co.edu.poli.Examen2.aplication.port.out.FormatoSerializacion;
import co.edu.poli.Examen2.aplication.port.out.SelectorFormatoSerializacion;
import co.edu.poli.Examen2.aplication.port.out.ExamenRepository;
import co.edu.poli.Examen2.aplication.port.out.SerializacionExamenesPort;
import co.edu.poli.Examen2.aplication.service.DeserializarExamenesService;
import co.edu.poli.Examen2.aplication.service.ListarExamenesService;
import co.edu.poli.Examen2.aplication.service.RegistrarExamenService;
import co.edu.poli.Examen2.aplication.service.SerializarExamenesService;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Facade que coordina los casos de uso y presenta mensajes listos para la interfaz.
 */
public class ExamenesFacade {
    private final RegistrarExamenUseCase registrarExamen;
    private final ListarExamenesUseCase listarExamenes;
    private final SerializarExamenesUseCase serializarExamenes;
    private final DeserializarExamenesUseCase deserializarExamenes;
    private final SelectorFormatoSerializacion selectorFormato;

    /**
     * Construye la aplicación utilizando una fábrica de infraestructura.
     * @param factory fábrica que crea repositorio y servicios de serialización
     */
    public ExamenesFacade(ExamenesFactory factory) {
        ExamenRepository repositorio = factory.crearRepositorio();
        SerializacionExamenesPort serializacion = factory.crearSerializacion();
        selectorFormato = factory.crearSelectorFormato();
        registrarExamen = new RegistrarExamenService(repositorio);
        listarExamenes = new ListarExamenesService(repositorio);
        serializarExamenes = new SerializarExamenesService(repositorio, serializacion);
        deserializarExamenes = new DeserializarExamenesService(repositorio, serializacion);
    }

    /**
     * Registra un examen a partir de los datos capturados por la interfaz.
     * @param codigo código del examen
     * @param nombrePaciente nombre del paciente
     * @param costo costo como texto decimal
     * @param tipoSangre tipo de sangre
     * @param factorRh factor RH
     * @return mensaje de resultado para mostrar al usuario
     */
    public String registrarExamen(String codigo, String nombrePaciente, String costo,
                                  String tipoSangre, String factorRh) {
        try {
            if (tipoSangre == null) {
                throw new IllegalArgumentException("Debe seleccionar el tipo de sangre.");
            }
            ExamenSangre examen = new ExamenSangre(
                    codigo,
                    nombrePaciente,
                    new BigDecimal(costo.trim()),
                    TipoSangre.valueOf(tipoSangre),
                    FactorRh.valueOf(factorRh));
            registrarExamen.registrarExamen(examen);
            return "Examen registrado correctamente.";
        } catch (NumberFormatException e) {
            return "El costo debe ser un número válido.";
        } catch (IllegalArgumentException | IllegalStateException e) {
            return e.getMessage();
        }
    }

    /** @return lista de exámenes o un mensaje informativo */
    public String listarExamenes() {
        try {
            String resultado = listarExamenes.listarExamenes().stream()
                    .map(examen -> String.format(Locale.ROOT, "%s | %s | $%s | %s%s",
                            examen.getCodigo(),
                            examen.getNombrePaciente(),
                            examen.getCosto().toPlainString(),
                            examen.getTipoSangre(),
                            examen.getFactorRh() == FactorRh.POSITIVO ? "+" : "-"))
                    .collect(Collectors.joining(System.lineSeparator()));
            return resultado.isEmpty() ? "No hay exámenes registrados." : resultado;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return e.getMessage();
        }
    }

    /** @return mensaje de resultado de la serialización */
    public String serializarExamenes() {
        try {
            serializarExamenes.serializarExamenes();
            return "Exámenes serializados en examenes-sangre.json.";
        } catch (IllegalArgumentException | IllegalStateException e) {
            return e.getMessage();
        }
    }

    /** @return mensaje de resultado de la deserialización */
    public String deserializarExamenes() {
        try {
            deserializarExamenes.deserializarExamenes();
            return "Exámenes deserializados correctamente.";
        } catch (IllegalArgumentException | IllegalStateException e) {
            return e.getMessage();
        }
    }

    /**
     * Selecciona el formato de serialización activo.
     * @param formato formato disponible
     */
    public void seleccionarFormato(FormatoSerializacion formato) {
        selectorFormato.seleccionar(formato);
    }

    /** @return formatos de serialización disponibles */
    public FormatoSerializacion[] formatosDisponibles() {
        return FormatoSerializacion.values();
    }
}
