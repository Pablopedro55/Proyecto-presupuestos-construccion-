// 📦 infraestructura/mysql/RepositorioManoObraMySQL.java
package infraestructura.mysql;

import dominio.ManoObra;
import dominio.repositorio.RepositorioManoObra;

import java.sql.*;
import java.util.*;

public class RepositorioManoObraMySQL implements RepositorioManoObra {
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