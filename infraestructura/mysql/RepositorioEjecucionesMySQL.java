// 📦 infraestructura/mysql/RepositorioEjecucionesMySQL.java
package infraestructura.mysql;

import dominio.repositorio.RepositorioEjecuciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RepositorioEjecucionesMySQL implements RepositorioEjecuciones {
    private final Connection con;

    public RepositorioEjecucionesMySQL(Connection con) {
        this.con = con;
    }

    @Override
    public void registrar(int actividadId) {
        try {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO ejecuciones (actividad_id, fecha) VALUES (?, now())");
            ps.setInt(1, actividadId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int contarPorActividad(int actividadId) {
        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM ejecuciones WHERE actividad_id = ?");
            ps.setInt(1, actividadId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}