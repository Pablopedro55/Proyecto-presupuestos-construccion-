// 📦 infraestructura/configuracion/ConexionBD.java
package infraestructura.configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    public static Connection conectar() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/presupuestos";
        String usuario = "postgres";
        String clave = "pablopedro55";
        return DriverManager.getConnection(url, usuario, clave);
    }
}