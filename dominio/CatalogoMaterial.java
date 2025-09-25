package dominio;

public class CatalogoMaterial {
    private int id;
    private String descripcion;
    private String unidad;
    private double precioUnitario;

    public CatalogoMaterial(String descripcion, String unidad, double precioUnitario) {
        this.descripcion = descripcion;
        this.unidad = unidad;
        this.precioUnitario = precioUnitario;
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

    public String getUnidad() {
        return unidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }
}
