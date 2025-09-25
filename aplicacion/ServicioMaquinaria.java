// 📦 aplicacion/ServicioMaquinaria.java
package aplicacion;

import dominio.Maquinaria;
import dominio.repositorio.RepositorioMaquinaria;
import java.util.List;

public class ServicioMaquinaria {
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