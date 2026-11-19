package co.edu.poli.Examen2.aplication.service;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;
import co.edu.poli.Examen2.aplication.port.in.ListarExamenesUseCase;
import co.edu.poli.Examen2.aplication.port.out.ExamenRepository;

import java.util.List;

/** Implementación del caso de uso para listar exámenes. */
public class ListarExamenesService implements ListarExamenesUseCase {
    private final ExamenRepository examenRepository;

    /**
     * Crea el servicio con su repositorio.
     * @param examenRepository repositorio consultado
     */
    public ListarExamenesService(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    @Override
    public List<ExamenSangre> listarExamenes() {
        return examenRepository.buscarTodos();
    }
}
