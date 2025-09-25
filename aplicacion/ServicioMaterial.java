// 📦 aplicacion/ServicioMaterial.java
package aplicacion;

import dominio.Material;
import dominio.repositorio.RepositorioMateriales;

import java.util.List;

public class ServicioMaterial {
    private final RepositorioMateriales repo;

    public ServicioMaterial(RepositorioMateriales repo) {
        this.repo = repo;
    }

    public List<Material> listarPorActividad(int actividadId) {
        return repo.listarPorActividad(actividadId);
    }

    public void agregarMaterial(int actividadId, int catalogoId, double cantidad) {
        repo.agregarMaterial(actividadId, catalogoId, cantidad);
    }

    public List<Material> listarCatalogo() {
        return repo.listarCatalogo();
    }

    public void crearEnCatalogo(String nombre, String unidad, double precioUnitario) {
        repo.crearEnCatalogo(nombre, unidad, precioUnitario);
    }
}

