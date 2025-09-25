// 📦 aplicacion/ServicioProyecto.java
package aplicacion;

import dominio.Proyecto;
import dominio.repositorio.RepositorioProyectos;
import java.util.List;

public class ServicioProyecto {
    private final RepositorioProyectos repo;

    public ServicioProyecto(RepositorioProyectos repo) {
        this.repo = repo;
    }

    public void agregarProyecto(String nombre, double presupuesto) {
        Proyecto proyecto = new Proyecto(nombre, presupuesto);
        repo.guardar(proyecto);
    }

    public List<Proyecto> listar() {
        return repo.listarTodos();
    }
}