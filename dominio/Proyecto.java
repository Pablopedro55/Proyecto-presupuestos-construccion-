// 📦 dominio/Proyecto.java
package dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Proyecto {
    private int id;
    private String nombre;
    private double presupuesto;
    private String cliente;
    private String ubicacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String tipoObra; // "Edificación", "Vial", "Hidráulica", "Sanitaria", etc.
    private String estado; // "Planificación", "En Ejecución", "Finalizado", "Suspendido"
    private List<Actividad> actividades = new ArrayList<>();

    // Constructor original (mantener compatibilidad)
    public Proyecto(String nombre, double presupuesto) {
        this.nombre = nombre;
        this.presupuesto = presupuesto;
        this.estado = "Planificación"; // Estado por defecto
    }

    // Constructor completo
    public Proyecto(String nombre, double presupuesto, String cliente, String ubicacion,
            LocalDate fechaInicio, LocalDate fechaFin, String tipoObra) {
        this.nombre = nombre;
        this.presupuesto = presupuesto;
        this.cliente = cliente;
        this.ubicacion = ubicacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipoObra = tipoObra;
        this.estado = "Planificación";
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getTipoObra() {
        return tipoObra;
    }

    public void setTipoObra(String tipoObra) {
        this.tipoObra = tipoObra;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }
}