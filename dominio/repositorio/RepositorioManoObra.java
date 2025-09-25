package dominio.repositorio;

// 📦 repositorio/RepositorioManoObra.java
import dominio.ManoObra;
import java.util.List;

public interface RepositorioManoObra {
    void guardar(ManoObra manoObra);

    List<ManoObra> listarTodos();
}