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
        // Validar descripción
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la actividad es obligatoria");
        }

        // Validar costo (puede ser 0 si aún no se asignan recursos)
        if (costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }

        Actividad a = new Actividad(descripcion, costo);
        repo.guardar(a, proyectoId);
    }

    public void cambiarEstadoActividad(int actividadId, String nuevoEstado) {
        // Validar estado
        if (!nuevoEstado.equals("Pendiente") &&
                !nuevoEstado.equals("En Progreso") &&
                !nuevoEstado.equals("Completada")) {
            throw new IllegalArgumentException("Estado inválido. Debe ser: Pendiente, En Progreso o Completada");
        }

        repo.actualizarEstado(actividadId, nuevoEstado);
    }

    public List<Actividad> listarPorProyecto(int proyectoId) {
        return repo.listarPorProyecto(proyectoId);
    }
}