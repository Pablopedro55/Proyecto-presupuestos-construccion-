// 📦 dominio/Actividad.java
package dominio;

public class Actividad {
    private int id;
    private String descripcion;
    private double costo;
    private String estado; // "Pendiente", "En Progreso", "Completada"

    public Actividad(String descripcion, double costo) {
        this.descripcion = descripcion;
        this.costo = costo;
        this.estado = "Pendiente"; // Estado por defecto
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getCosto() {
        return costo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
