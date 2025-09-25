// 📦 infraestructura/mysql/RepositorioMaquinariaMySQL.java
package infraestructura.mysql;

import dominio.Maquinaria;
import dominio.repositorio.RepositorioMaquinaria;

import java.sql.*;
import java.util.*;

public class RepositorioMaquinariaMySQL implements RepositorioMaquinaria {
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