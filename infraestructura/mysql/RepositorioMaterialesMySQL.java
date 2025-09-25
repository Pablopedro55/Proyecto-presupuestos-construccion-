// 📦 infraestructura/mysql/RepositorioMaterialesMySQL.java
package infraestructura.mysql;

import dominio.Material;
import dominio.repositorio.RepositorioMateriales;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioMaterialesMySQL implements RepositorioMateriales {
    private final Connection conn;

    public RepositorioMaterialesMySQL(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Material> listarPorActividad(int actividadId) {
        List<Material> lista = new ArrayList<>();
        String sql = """
            SELECT m.id, c.nombre, c.unidad, c.precio_unitario, m.cantidad
            FROM materiales m
            JOIN catalogo_materiales c ON m.catalogo_id = c.id
            WHERE m.actividad_id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, actividadId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Material mat = new Material();
                mat.setId(rs.getInt("id"));
                mat.setNombre(rs.getString("nombre"));
                mat.setUnidad(rs.getString("unidad"));
                mat.setPrecioUnitario(rs.getDouble("precio_unitario"));
                mat.setCantidad(rs.getDouble("cantidad"));
                lista.add(mat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void agregarMaterial(int actividadId, int catalogoId, double cantidad) {
        String sql = "INSERT INTO materiales (catalogo_id, actividad_id, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, catalogoId);
            stmt.setInt(2, actividadId);
            stmt.setDouble(3, cantidad);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Material> listarCatalogo() {
        List<Material> catalogo = new ArrayList<>();
        String sql = "SELECT id, nombre, unidad, precio_unitario FROM catalogo_materiales ORDER BY nombre";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Material m = new Material();
                m.setId(rs.getInt("id"));
                m.setNombre(rs.getString("nombre"));
                m.setUnidad(rs.getString("unidad"));
                m.setPrecioUnitario(rs.getDouble("precio_unitario"));
                catalogo.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return catalogo;
    }

    @Override
    public void crearEnCatalogo(String nombre, String unidad, double precioUnitario) {
        String sql = "INSERT INTO catalogo_materiales (nombre, unidad, precio_unitario) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, unidad);
            stmt.setDouble(3, precioUnitario);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



