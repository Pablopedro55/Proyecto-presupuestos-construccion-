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

    // Método original (mantener compatibilidad)
    public void agregarProyecto(String nombre, double presupuesto) {
        Proyecto proyecto = new Proyecto(nombre, presupuesto);
        repo.guardar(proyecto);
    }

    // Nuevo método que acepta objeto Proyecto completo
    public void agregarProyecto(Proyecto proyecto) {
        // Validar campos obligatorios
        if (proyecto.getNombre() == null || proyecto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proyecto es obligatorio");
        }

        if (proyecto.getPresupuesto() <= 0) {
            throw new IllegalArgumentException("El presupuesto debe ser mayor a 0");
        }

        // Validar fechas si están presentes
        if (proyecto.getFechaInicio() != null && proyecto.getFechaFin() != null) {
            if (proyecto.getFechaFin().isBefore(proyecto.getFechaInicio())) {
                throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
            }
        }

        repo.guardar(proyecto);
    }

    public void actualizarProyecto(Proyecto proyecto) {
        // Validar que el proyecto existe
        Proyecto existente = repo.obtenerPorId(proyecto.getId());
        if (existente == null) {
            throw new IllegalArgumentException("El proyecto no existe");
        }

        // Validar fechas
        if (proyecto.getFechaInicio() != null && proyecto.getFechaFin() != null) {
            if (proyecto.getFechaFin().isBefore(proyecto.getFechaInicio())) {
                throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
            }
        }

        // Validar presupuesto
        if (proyecto.getPresupuesto() <= 0) {
            throw new IllegalArgumentException("El presupuesto debe ser mayor a 0");
        }

        repo.actualizar(proyecto);
    }

    public Proyecto obtenerPorId(int proyectoId) {
        return repo.obtenerPorId(proyectoId);
    }

    public List<Proyecto> listar() {
        return repo.listarTodos();
    }
}