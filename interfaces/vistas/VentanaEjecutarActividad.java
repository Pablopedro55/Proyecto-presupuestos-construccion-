package interfaces.vistas;

import aplicacion.ServicioEjecucion;
import aplicacion.ServicioPresupuesto;
import infraestructura.configuracion.ConexionBD;
import infraestructura.mysql.RepositorioActividadesMySQL;
import infraestructura.mysql.RepositorioEjecucionesMySQL;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class VentanaEjecutarActividad extends JFrame {
    private JComboBox<String> comboActividades;
    private JButton btnEjecutar;
    private ServicioPresupuesto servicioPresupuesto;
    private ServicioEjecucion servicioEjecucion;
    private int proyectoId;
    private VentanaActividades parent;

    public VentanaEjecutarActividad(int proyectoId, VentanaActividades parent) {
        this.proyectoId = proyectoId;
        this.parent = parent;

        setTitle("Ejecutar Actividad");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        comboActividades = new JComboBox<>();
        btnEjecutar = new JButton("Ejecutar");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(comboActividades, BorderLayout.CENTER);
        panel.add(btnEjecutar, BorderLayout.SOUTH);
        add(panel);

        try {
            Connection con = ConexionBD.conectar();
            servicioPresupuesto = new ServicioPresupuesto(new RepositorioActividadesMySQL(con));
            servicioEjecucion = new ServicioEjecucion(new RepositorioEjecucionesMySQL(con));

            List<dominio.Actividad> actividades = servicioPresupuesto.listarPorProyecto(proyectoId);
            for (dominio.Actividad a : actividades) {
                comboActividades.addItem(a.getId() + " - " + a.getDescripcion());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando actividades");
        }

        btnEjecutar.addActionListener(e -> ejecutarActividad());
    }

    private void ejecutarActividad() {
        String seleccion = (String) comboActividades.getSelectedItem();
        if (seleccion != null && seleccion.contains("-")) {
            int actividadId = Integer.parseInt(seleccion.split("-")[0].trim());
            // Recalcular el costo de la actividad usando los precios actuales del catálogo
            try {
                Connection con = ConexionBD.conectar();
                RepositorioActividadesMySQL repoActividades = new RepositorioActividadesMySQL(con);
                repoActividades.calcularCostoActividad(actividadId);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al recalcular el costo: " + ex.getMessage());
            }
            servicioEjecucion.registrar(actividadId);
            JOptionPane.showMessageDialog(this, "Actividad ejecutada");
            parent.cargarActividades();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una actividad válida");
        }
    }
}
