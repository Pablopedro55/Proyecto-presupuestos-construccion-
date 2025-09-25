// 📦 interfaces/vistas/VentanaAgregarProyecto.java
package interfaces.vistas;

import aplicacion.ServicioProyecto;

import javax.swing.*;
import java.awt.*;

public class VentanaAgregarProyecto extends JFrame {
    public VentanaAgregarProyecto(ServicioProyecto servicio, VentanaPrincipal ventanaPrincipal) {
        setTitle("Agregar Proyecto");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblNombre = new JLabel("Nombre del Proyecto:");
        JLabel lblPresupuesto = new JLabel("Presupuesto (Bs):");
        JTextField txtNombre = new JTextField(20);
        JTextField txtPresupuesto = new JTextField(10);
        JButton btnGuardar = new JButton("Guardar");

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblPresupuesto);
        panel.add(txtPresupuesto);
        panel.add(new JLabel());
        panel.add(btnGuardar);

        add(panel);

        btnGuardar.addActionListener(e -> {
            try {
                servicio.agregarProyecto(txtNombre.getText(), Double.parseDouble(txtPresupuesto.getText()));
                ventanaPrincipal.cargarProyectos();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
            }
        });
    }
}
