// 📦 repositorio/RepositorioMaquinaria.java
package dominio.repositorio;

import dominio.Maquinaria;
import java.util.List;

public interface RepositorioMaquinaria {
    void agregarMaquinariaAsignada(int actividadId, int catalogoId, double cantidad);

    List<dominio.MaquinariaAsignada> listarMaquinariaPorActividad(int actividadId);

    void guardar(Maquinaria maquinaria);

    List<Maquinaria> listarTodos();
}