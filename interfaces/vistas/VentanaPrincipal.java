// 📦 interfaces/vistas/VentanaPrincipal.java
package interfaces.vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.List;

import aplicacion.ServicioProyecto;
import aplicacion.ServicioMaterial;
import dominio.Proyecto;
import infraestructura.configuracion.ConexionBD;
import infraestructura.mysql.RepositorioProyectosMySQL;
import infraestructura.mysql.RepositorioMaterialesMySQL;

public class VentanaPrincipal extends JFrame {

    private JTable tablaProyectos;
    private JButton btnNuevoProyecto, btnVerProyecto;
    private ServicioProyecto servicio;
    private ServicioMaterial servicioMaterial;

    public VentanaPrincipal() {
        setTitle("Gestión de Presupuestos de Obras");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tablaProyectos = new JTable();
        btnNuevoProyecto = new JButton("Nuevo Proyecto");
        btnVerProyecto = new JButton("Ver Proyecto");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnNuevoProyecto);
        panelBotones.add(btnVerProyecto);

        add(new JScrollPane(tablaProyectos), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        try {
            Connection con = infraestructura.configuracion.ConexionBD.conectar();
            servicio = new ServicioProyecto(new infraestructura.mysql.RepositorioProyectosMySQL(con));
            servicioMaterial = new ServicioMaterial(new RepositorioMaterialesMySQL(con));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error conectando a base de datos");
        }

        btnNuevoProyecto.addActionListener(e -> new VentanaAgregarProyecto(servicio, this).setVisible(true));
        btnVerProyecto.addActionListener(e -> abrirDetalle());
        cargarProyectos();
    }

    public void cargarProyectos() {
        if (servicio == null)
            return;

        List<Proyecto> proyectos = servicio.listar();
        String[] columnas = { "ID", "Nombre", "Presupuesto" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Proyecto p : proyectos) {
            modelo.addRow(new Object[] { p.getId(), p.getNombre(), p.getPresupuesto() });
        }

        tablaProyectos.setModel(modelo);
    }

    private void abrirDetalle() {
        int fila = tablaProyectos.getSelectedRow();
        if (fila != -1) {
            int idProyecto = (int) tablaProyectos.getValueAt(fila, 0);
            String nombre = (String) tablaProyectos.getValueAt(fila, 1);
            new VentanaActividades(idProyecto, nombre).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un proyecto");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
