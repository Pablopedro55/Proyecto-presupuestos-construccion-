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
                    "INSERT INTO proyectos (nombre, presupuesto, cliente, ubicacion, fecha_inicio, fecha_fin, tipo_obra, estado) "
                            +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, proyecto.getNombre());
            ps.setDouble(2, proyecto.getPresupuesto());
            ps.setString(3, proyecto.getCliente());
            ps.setString(4, proyecto.getUbicacion());
            ps.setDate(5, proyecto.getFechaInicio() != null ? java.sql.Date.valueOf(proyecto.getFechaInicio()) : null);
            ps.setDate(6, proyecto.getFechaFin() != null ? java.sql.Date.valueOf(proyecto.getFechaFin()) : null);
            ps.setString(7, proyecto.getTipoObra());
            ps.setString(8, proyecto.getEstado());
            ps.executeUpdate();
            System.out.println("✅ Proyecto guardado: " + proyecto.getNombre() + " - Bs " + proyecto.getPresupuesto());
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar proyecto: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al guardar proyecto: " + e.getMessage(), e);
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

                // Cargar nuevos campos
                p.setCliente(rs.getString("cliente"));
                p.setUbicacion(rs.getString("ubicacion"));

                java.sql.Date fechaInicio = rs.getDate("fecha_inicio");
                if (fechaInicio != null) {
                    p.setFechaInicio(fechaInicio.toLocalDate());
                }

                java.sql.Date fechaFin = rs.getDate("fecha_fin");
                if (fechaFin != null) {
                    p.setFechaFin(fechaFin.toLocalDate());
                }

                p.setTipoObra(rs.getString("tipo_obra"));
                p.setEstado(rs.getString("estado"));

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

    public void actualizar(Proyecto proyecto) {
        try {
            PreparedStatement ps = conexion.prepareStatement(
                    "UPDATE proyectos SET nombre = ?, presupuesto = ?, cliente = ?, ubicacion = ?, " +
                            "fecha_inicio = ?, fecha_fin = ?, tipo_obra = ?, estado = ? WHERE id = ?");
            ps.setString(1, proyecto.getNombre());
            ps.setDouble(2, proyecto.getPresupuesto());
            ps.setString(3, proyecto.getCliente());
            ps.setString(4, proyecto.getUbicacion());
            ps.setDate(5, proyecto.getFechaInicio() != null ? java.sql.Date.valueOf(proyecto.getFechaInicio()) : null);
            ps.setDate(6, proyecto.getFechaFin() != null ? java.sql.Date.valueOf(proyecto.getFechaFin()) : null);
            ps.setString(7, proyecto.getTipoObra());
            ps.setString(8, proyecto.getEstado());
            ps.setInt(9, proyecto.getId());
            ps.executeUpdate();
            System.out.println("✅ Proyecto actualizado: " + proyecto.getNombre());
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar proyecto: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar proyecto: " + e.getMessage(), e);
        }
    }

    public Proyecto obtenerPorId(int proyectoId) {
        try {
            PreparedStatement ps = conexion.prepareStatement("SELECT * FROM proyectos WHERE id = ?");
            ps.setInt(1, proyectoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Proyecto p = new Proyecto(rs.getString("nombre"), rs.getDouble("presupuesto"));
                p.setId(rs.getInt("id"));
                p.setCliente(rs.getString("cliente"));
                p.setUbicacion(rs.getString("ubicacion"));

                java.sql.Date fechaInicio = rs.getDate("fecha_inicio");
                if (fechaInicio != null) {
                    p.setFechaInicio(fechaInicio.toLocalDate());
                }

                java.sql.Date fechaFin = rs.getDate("fecha_fin");
                if (fechaFin != null) {
                    p.setFechaFin(fechaFin.toLocalDate());
                }

                p.setTipoObra(rs.getString("tipo_obra"));
                p.setEstado(rs.getString("estado"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
