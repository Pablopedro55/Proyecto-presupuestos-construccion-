// 📦 infraestructura/mysql/RepositorioManoObraMySQL.java
package infraestructura.mysql;

import dominio.ManoObra;
import dominio.ManoObraAsignada;
import dominio.repositorio.RepositorioManoObra;

import java.sql.*;
import java.util.*;

public class RepositorioManoObraMySQL implements RepositorioManoObra {
    public double calcularCostoManoObra(int actividadId) {
        double total = 0;
        String sql = "SELECT cantidad * precio_unitario AS subtotal FROM mano_obra_asignada mo JOIN catalogo_mano_obra c ON mo.catalogo_id = c.id WHERE mo.actividad_id = ?";
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

    // Asignar mano de obra a una actividad
    public void agregarManoObraAsignada(int actividadId, int catalogoId, double cantidad) {
        String sql = "INSERT INTO mano_obra_asignada (actividad_id, catalogo_id, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, actividadId);
            stmt.setInt(2, catalogoId);
            stmt.setDouble(3, cantidad);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Listar mano de obra asignada por actividad
    public List<ManoObraAsignada> listarManoObraPorActividad(int actividadId) {
        List<ManoObraAsignada> lista = new ArrayList<>();
        String sql = "SELECT mo.id, c.descripcion, c.unidad, c.precio_unitario, mo.cantidad FROM mano_obra_asignada mo JOIN catalogo_mano_obra c ON mo.catalogo_id = c.id WHERE mo.actividad_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, actividadId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ManoObraAsignada m = new ManoObraAsignada();
                m.setId(rs.getInt("id"));
                m.setDescripcion(rs.getString("descripcion"));
                m.setUnidad(rs.getString("unidad"));
                m.setPrecioUnitario(rs.getDouble("precio_unitario"));
                m.setCantidad(rs.getDouble("cantidad"));
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private final Connection conexion;

    public RepositorioManoObraMySQL(Connection conexion) {
        this.conexion = conexion;
    }

    public void guardar(ManoObra m) {
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "INSERT INTO catalogo_mano_obra (descripcion, unidad, precio_unitario) VALUES (?, ?, ?)");
            ps.setString(1, m.getDescripcion());
            ps.setString(2, m.getUnidad());
            ps.setDouble(3, m.getPrecioUnitario());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ManoObra> listarTodos() {
        List<ManoObra> lista = new ArrayList<>();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM catalogo_mano_obra");
            while (rs.next()) {
                ManoObra m = new ManoObra(rs.getString("descripcion"), rs.getString("unidad"),
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