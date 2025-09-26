// 📦 aplicacion/ServicioMaquinaria.java
package aplicacion;

import dominio.Maquinaria;
import dominio.repositorio.RepositorioMaquinaria;
import java.util.List;

public class ServicioMaquinaria {
    public void agregarMaquinariaAsignada(int actividadId, int catalogoId, double cantidad) {
        repo.agregarMaquinariaAsignada(actividadId, catalogoId, cantidad);
    }

    public List<dominio.MaquinariaAsignada> listarPorActividad(int actividadId) {
        return repo.listarMaquinariaPorActividad(actividadId);
    }

    private final RepositorioMaquinaria repo;

    public ServicioMaquinaria(RepositorioMaquinaria repo) {
        this.repo = repo;
    }

    public void agregar(String descripcion, String unidad, double precio) {
        repo.guardar(new Maquinaria(descripcion, unidad, precio));
    }

    public List<Maquinaria> listar() {
        return repo.listarTodos();
    }
}