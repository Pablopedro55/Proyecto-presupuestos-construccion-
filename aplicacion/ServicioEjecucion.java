// 📦 aplicacion/ServicioEjecucion.java
package aplicacion;

import dominio.repositorio.RepositorioEjecuciones;

public class ServicioEjecucion {
    private final RepositorioEjecuciones repo;

    public ServicioEjecucion(RepositorioEjecuciones repo) {
        this.repo = repo;
    }

    public void registrar(int actividadId) {
        repo.registrar(actividadId);
    }

    public int contarEjecuciones(int actividadId) {
        return repo.contarPorActividad(actividadId);
    }
}