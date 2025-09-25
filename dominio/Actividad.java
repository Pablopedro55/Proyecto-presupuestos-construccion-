// 📦 dominio/Actividad.java
package dominio;

public class Actividad {
    private int id;
    private String descripcion;
    private double costo;

    public Actividad(String descripcion, double costo) {
        this.descripcion = descripcion;
        this.costo = costo;
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
}
