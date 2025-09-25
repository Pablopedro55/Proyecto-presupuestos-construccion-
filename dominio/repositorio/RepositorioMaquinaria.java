// 📦 repositorio/RepositorioMaquinaria.java
package dominio.repositorio;

import dominio.Maquinaria;
import java.util.List;

public interface RepositorioMaquinaria {
    void guardar(Maquinaria maquinaria);

    List<Maquinaria> listarTodos();
}