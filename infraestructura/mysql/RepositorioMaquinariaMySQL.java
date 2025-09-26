// 📦 infraestructura/mysql/RepositorioMaquinariaMySQL.java
package infraestructura.mysql;

import dominio.Maquinaria;
import dominio.MaquinariaAsignada;
import dominio.repositorio.RepositorioMaquinaria;

import java.sql.*;
import java.util.*;

public class RepositorioMaquinariaMySQL implements RepositorioMaquinaria {
    public double calcularCostoMaquinaria(int actividadId) {
        double total = 0;
        String sql = "SELECT cantidad * precio_unitario AS subtotal FROM maquinaria_asignada m JOIN catalogo_maquinaria c ON m.catalogo_id = c.id WHERE m.actividad_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, actividadId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                total += rs.getDouble("subtotal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // Asignar maquinaria a una actividad
    public void agregarMaquinariaAsignada(int actividadId, int catalogoId, double cantidad) {
        String sql = "INSERT INTO maquinaria_asignada (actividad_id, catalogo_id, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, actividadId);
            stmt.setInt(2, catalogoId);
            stmt.setDouble(3, cantidad);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Listar maquinaria asignada por actividad
    public List<MaquinariaAsignada> listarMaquinariaPorActividad(int actividadId) {
        List<MaquinariaAsignada> lista = new ArrayList<>();
        String sql = "SELECT m.id, c.descripcion, c.unidad, c.precio_unitario, m.cantidad FROM maquinaria_asignada m JOIN catalogo_maquinaria c ON m.catalogo_id = c.id WHERE m.actividad_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, actividadId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MaquinariaAsignada maq = new MaquinariaAsignada();
                maq.setId(rs.getInt("id"));
                maq.setDescripcion(rs.getString("descripcion"));
                maq.setUnidad(rs.getString("unidad"));
                maq.setPrecioUnitario(rs.getDouble("precio_unitario"));
                maq.setCantidad(rs.getDouble("cantidad"));
                lista.add(maq);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private final Connection conexion;

    public RepositorioMaquinariaMySQL(Connection conexion) {
        this.conexion = conexion;
    }

    public void guardar(Maquinaria m) {
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "INSERT INTO catalogo_maquinaria (descripcion, unidad, precio_unitario) VALUES (?, ?, ?)");
            ps.setString(1, m.getDescripcion());
            ps.setString(2, m.getUnidad());
            ps.setDouble(3, m.getPrecioUnitario());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Maquinaria> listarTodos() {
        List<Maquinaria> lista = new ArrayList<>();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM catalogo_maquinaria");
            while (rs.next()) {
                Maquinaria m = new Maquinaria(rs.getString("descripcion"), rs.getString("unidad"),
                        rs.getDouble("precio_unitario"));
                m.setId(rs.getInt("id"));
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}