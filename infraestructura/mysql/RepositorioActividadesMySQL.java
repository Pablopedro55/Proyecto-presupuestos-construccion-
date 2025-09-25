// 📦 infraestructura/mysql/RepositorioActividadesMySQL.java
package infraestructura.mysql;

import dominio.Actividad;
import dominio.repositorio.RepositorioActividades;

import java.sql.*;
import java.util.*;

public class RepositorioActividadesMySQL implements RepositorioActividades {
    private final Connection conexion;

    public RepositorioActividadesMySQL(Connection conexion) {
        this.conexion = conexion;
    }

    public void guardar(Actividad actividad, int proyectoId) {
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "INSERT INTO actividades (descripcion, costo, proyecto_id) VALUES (?, ?, ?)");
            ps.setString(1, actividad.getDescripcion());
            ps.setDouble(2, actividad.getCosto());
            ps.setInt(3, proyectoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Actividad> listarPorProyecto(int proyectoId) {
        List<Actividad> lista = new ArrayList<>();
        try {
            PreparedStatement ps = conexion.prepareStatement("SELECT * FROM actividades WHERE proyecto_id = ?");
            ps.setInt(1, proyectoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Actividad a = new Actividad(rs.getString("descripcion"), rs.getDouble("costo"));
                a.setId(rs.getInt("id"));
                lista.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Actividad> listarNoEjecutadas(int proyectoId) {
        List<Actividad> lista = new ArrayList<>();
        try {
            PreparedStatement ps = conexion
                    .prepareStatement("SELECT * FROM actividades WHERE proyecto_id = ? AND ejecutada = false");
            ps.setInt(1, proyectoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Actividad a = new Actividad(rs.getString("descripcion"), rs.getDouble("costo"));
                a.setId(rs.getInt("id"));
                lista.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void marcarEjecutada(int actividadId) {
        try {
            PreparedStatement ps = conexion.prepareStatement("UPDATE actividades SET ejecutada = true WHERE id = ?");
            ps.setInt(1, actividadId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double calcularCostoActividad(int actividadId) {
        double total = 0;
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "SELECT cantidad * precio_unitario AS subtotal FROM materiales m JOIN catalogo_materiales c ON m.catalogo_id = c.id WHERE m.actividad_id = ?");
            ps.setInt(1, actividadId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total += rs.getDouble("subtotal");
            }
            PreparedStatement ps2 = conexion.prepareStatement("UPDATE actividades SET costo = ? WHERE id = ?");
            ps2.setDouble(1, total);
            ps2.setInt(2, actividadId);
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}
