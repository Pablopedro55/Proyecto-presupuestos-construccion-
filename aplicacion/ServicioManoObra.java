// 📦 aplicacion/ServicioManoObra.java
package aplicacion;

import dominio.ManoObra;
import dominio.repositorio.RepositorioManoObra;
import java.util.List;

public class ServicioManoObra {
    private final RepositorioManoObra repo;

    public ServicioManoObra(RepositorioManoObra repo) {
        this.repo = repo;
    }

    public void agregar(String descripcion, String unidad, double precio) {
        repo.guardar(new ManoObra(descripcion, unidad, precio));
    }

    public List<ManoObra> listar() {
        return repo.listarTodos();
    }
}
