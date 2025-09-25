// 📦 infraestructura/mysql/RepositorioProyectosMySQL.java
package infraestructura.mysql;

import dominio.Proyecto;
import dominio.repositorio.RepositorioProyectos;

import java.sql.*;
import java.util.*;

public class RepositorioProyectosMySQL implements RepositorioProyectos {
    private final Connection conexion;

    public RepositorioProyectosMySQL(Connection conexion) {
        this.conexion = conexion;
    }

    public void guardar(Proyecto proyecto) {
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "INSERT INTO proyectos (nombre, presupuesto) VALUES (?, ?)");
            ps.setString(1, proyecto.getNombre());
            ps.setDouble(2, proyecto.getPresupuesto());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Proyecto> listarTodos() {
        List<Proyecto> lista = new ArrayList<>();
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM proyectos");
            while (rs.next()) {
                Proyecto p = new Proyecto(rs.getString("nombre"), rs.getDouble("presupuesto"));
                p.setId(rs.getInt("id"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public double obtenerPresupuesto(int proyectoId) {
        try {
            PreparedStatement ps = conexion.prepareStatement("SELECT presupuesto FROM proyectos WHERE id = ?");
            ps.setInt(1, proyectoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("presupuesto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void actualizarPresupuesto(int proyectoId, double nuevoPresupuesto) {
        try {
            PreparedStatement ps = conexion.prepareStatement("UPDATE proyectos SET presupuesto = ? WHERE id = ?");
            ps.setDouble(1, nuevoPresupuesto);
            ps.setInt(2, proyectoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
