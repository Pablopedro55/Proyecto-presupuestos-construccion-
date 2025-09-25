// 📦 dominio/repositorio/RepositorioEjecuciones.java
package dominio.repositorio;

public interface RepositorioEjecuciones {
    void registrar(int actividadId);

    int contarPorActividad(int actividadId);
}