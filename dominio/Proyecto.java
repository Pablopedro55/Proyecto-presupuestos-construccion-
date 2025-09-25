// 📦 dominio/Proyecto.java
package dominio;

import java.util.ArrayList;
import java.util.List;

public class Proyecto {
    private int id;
    private String nombre;
    private double presupuesto;
    private List<Actividad> actividades = new ArrayList<>();

    public Proyecto(String nombre, double presupuesto) {
        this.nombre = nombre;
        this.presupuesto = presupuesto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }
}