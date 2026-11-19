package co.edu.poli.Examen2.infrastructure.persistence;

import co.edu.poli.Examen2.aplication.domain.model.ExamenSangre;
import co.edu.poli.Examen2.aplication.port.out.ExamenRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Adaptador de repositorio que mantiene los exámenes en memoria. */
public class RepositorioExamenesEnMemoria implements ExamenRepository {
    private final Map<String, ExamenSangre> examenes = new LinkedHashMap<>();

    @Override
    public ExamenSangre guardar(ExamenSangre examen) {
        examenes.put(examen.getCodigo().toUpperCase(Locale.ROOT), examen);
        return examen;
    }

    @Override
    public List<ExamenSangre> buscarTodos() {
        return new ArrayList<>(examenes.values());
    }

    @Override
    public void reemplazarTodos(List<ExamenSangre> examenes) {
        this.examenes.clear();
        for (ExamenSangre examen : examenes) {
            guardar(examen);
        }
    }
}
