package test.integracion;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import dominio.Proyecto;
import infraestructura.configuracion.ConexionBD;
import infraestructura.mysql.RepositorioProyectosMySQL;

/**
 * Pruebas de integración para RepositorioProyectosMySQL
 * 
 * Estas pruebas se ejecutan contra la base de datos real 'presupuestos_test'
 * y verifican que las operaciones CRUD funcionen correctamente.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepositorioProyectosIntegrationTest {

    private static Connection conexion;
    private RepositorioProyectosMySQL repositorio;

    @BeforeAll
    static void setUpClass() throws SQLException {
        // Conectar a la BD de pruebas
        conexion = ConexionBD.conectarTest();
        System.out.println("✓ Conexión establecida con presupuestos_test");
    }

    @BeforeEach
    void setUp() throws SQLException {
        repositorio = new RepositorioProyectosMySQL(conexion);

        // Limpiar la tabla antes de cada prueba
        Statement stmt = conexion.createStatement();
        stmt.executeUpdate("DELETE FROM materiales");
        stmt.executeUpdate("DELETE FROM mano_obra_asignada");
        stmt.executeUpdate("DELETE FROM maquinaria_asignada");
        stmt.executeUpdate("DELETE FROM actividades");
        stmt.executeUpdate("DELETE FROM proyectos");
        stmt.close();
    }

    @Test
    @Order(1)
    @DisplayName("Debería guardar un proyecto correctamente")
    void testGuardarProyecto() {
        // Arrange
        Proyecto proyecto = new Proyecto("Edificio Residencial", 500000.0);

        // Act
        repositorio.guardar(proyecto);
        List<Proyecto> proyectos = repositorio.listarTodos();

        // Assert
        assertFalse(proyectos.isEmpty(), "La lista de proyectos no debería estar vacía");
        assertEquals(1, proyectos.size(), "Debería haber exactamente 1 proyecto");

        Proyecto proyectoGuardado = proyectos.get(0);
        assertEquals("Edificio Residencial", proyectoGuardado.getNombre());
        assertEquals(500000.0, proyectoGuardado.getPresupuesto(), 0.01);
        assertNotEquals(0, proyectoGuardado.getId(), "El ID debería ser asignado automáticamente");

        System.out.println("✓ Proyecto guardado con ID: " + proyectoGuardado.getId());
    }

    @Test
    @Order(2)
    @DisplayName("Debería listar múltiples proyectos")
    void testListarMultiplesProyectos() {
        // Arrange
        Proyecto proyecto1 = new Proyecto("Centro Comercial", 1000000.0);
        Proyecto proyecto2 = new Proyecto("Puente Vehicular", 750000.0);
        Proyecto proyecto3 = new Proyecto("Parque Municipal", 250000.0);

        // Act
        repositorio.guardar(proyecto1);
        repositorio.guardar(proyecto2);
        repositorio.guardar(proyecto3);

        List<Proyecto> proyectos = repositorio.listarTodos();

        // Assert
        assertEquals(3, proyectos.size(), "Deberían haberse guardado 3 proyectos");

        // Verificar que todos tienen IDs únicos
        long idsUnicos = proyectos.stream()
                .map(Proyecto::getId)
                .distinct()
                .count();
        assertEquals(3, idsUnicos, "Todos los proyectos deberían tener IDs únicos");

        System.out.println("✓ Listados " + proyectos.size() + " proyectos correctamente");
    }

    @Test
    @Order(3)
    @DisplayName("Debería obtener el presupuesto de un proyecto específico")
    void testObtenerPresupuesto() {
        // Arrange
        Proyecto proyecto = new Proyecto("Hospital General", 2000000.0);
        repositorio.guardar(proyecto);

        List<Proyecto> proyectos = repositorio.listarTodos();
        int idProyecto = proyectos.get(0).getId();

        // Act
        double presupuesto = repositorio.obtenerPresupuesto(idProyecto);

        // Assert
        assertEquals(2000000.0, presupuesto, 0.01, "El presupuesto debería coincidir");
        System.out.println("✓ Presupuesto obtenido: $" + presupuesto);
    }

    @Test
    @Order(4)
    @DisplayName("Debería retornar 0 para proyecto inexistente")
    void testObtenerPresupuestoProyectoInexistente() {
        // Act
        double presupuesto = repositorio.obtenerPresupuesto(99999);

        // Assert
        assertEquals(0.0, presupuesto, 0.01, "Debería retornar 0 para ID inexistente");
        System.out.println("✓ Manejo correcto de proyecto inexistente");
    }

    @Test
    @Order(5)
    @DisplayName("Debería manejar proyectos con presupuesto cero")
    void testProyectoConPresupuestoCero() {
        // Arrange
        Proyecto proyecto = new Proyecto("Proyecto Preliminar", 0.0);

        // Act
        repositorio.guardar(proyecto);
        List<Proyecto> proyectos = repositorio.listarTodos();

        // Assert
        assertEquals(1, proyectos.size());
        assertEquals(0.0, proyectos.get(0).getPresupuesto(), 0.01);
        System.out.println("✓ Proyecto con presupuesto 0 manejado correctamente");
    }

    @AfterAll
    static void tearDownClass() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
            System.out.println("✓ Conexión cerrada");
        }
    }
}
