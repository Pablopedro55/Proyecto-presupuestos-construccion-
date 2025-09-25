// 📦 dominio/repositorio/RepositorioMateriales.java
package dominio.repositorio;

import dominio.Material;
import java.util.List;

public interface RepositorioMateriales {
    List<Material> listarPorActividad(int actividadId);
    void agregarMaterial(int actividadId, int catalogoId, double cantidad);
    List<Material> listarCatalogo();
    void crearEnCatalogo(String nombre, String unidad, double precioUnitario);
}


