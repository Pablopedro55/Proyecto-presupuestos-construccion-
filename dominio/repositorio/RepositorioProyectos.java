// 📦 dominio/repositorio/RepositorioProyectos.java
package dominio.repositorio;

import dominio.Proyecto;
import java.util.List;

public interface RepositorioProyectos {
    void guardar(Proyecto proyecto);

    List<Proyecto> listarTodos();
}