package dominio.repositorio;

// 📦 repositorio/RepositorioManoObra.java
import dominio.ManoObra;
import java.util.List;

public interface RepositorioManoObra {
    void agregarManoObraAsignada(int actividadId, int catalogoId, double cantidad);

    List<dominio.ManoObraAsignada> listarManoObraPorActividad(int actividadId);

    void guardar(ManoObra manoObra);

    List<ManoObra> listarTodos();
}