// 📦 aplicacion/ServicioPresupuesto.java
package aplicacion;

import dominio.Actividad;
import dominio.repositorio.RepositorioActividades;
import java.util.List;

public class ServicioPresupuesto {
    private final RepositorioActividades repo;

    public ServicioPresupuesto(RepositorioActividades repo) {
        this.repo = repo;
    }

    public void agregarActividad(String descripcion, double costo, int proyectoId) {
        Actividad a = new Actividad(descripcion, costo);
        repo.guardar(a, proyectoId);
    }

    public List<Actividad> listarPorProyecto(int proyectoId) {
        return repo.listarPorProyecto(proyectoId);
    }
}