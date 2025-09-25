// 📦 dominio/repositorio/RepositorioActividades.java
package dominio.repositorio;

import dominio.Actividad;
import java.util.List;

public interface RepositorioActividades {
    void guardar(Actividad actividad, int proyectoId);

    List<Actividad> listarPorProyecto(int proyectoId);
}
