package co.edu.poli.Examen2.aplication.service;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;
import co.edu.poli.Examen2.aplication.port.in.RegistrarExamenUseCase;
import co.edu.poli.Examen2.aplication.port.out.ExamenRepository;

/** Implementación del caso de uso que registra exámenes y evita códigos duplicados. */
public class RegistrarExamenService implements RegistrarExamenUseCase {
    private final ExamenRepository examenRepository;

    /**
     * Crea el servicio con su repositorio de persistencia.
     * @param examenRepository repositorio utilizado
     */
    public RegistrarExamenService(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    @Override
    public ExamenSangre registrarExamen(ExamenSangre examen) {
        if (examen == null) {
            throw new IllegalArgumentException("El examen es obligatorio.");
        }
        boolean codigoExiste = examenRepository.buscarTodos().stream()
                .anyMatch(actual -> actual.getCodigo().equalsIgnoreCase(examen.getCodigo()));
        if (codigoExiste) {
            throw new IllegalArgumentException("Ya existe un examen con el código " + examen.getCodigo() + ".");
        }
        return examenRepository.guardar(examen);
    }
}
