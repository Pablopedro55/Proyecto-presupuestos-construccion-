// 📦 interfaces/vistas/VentanaAgregarActividad.java
package interfaces.vistas;

import aplicacion.ServicioPresupuesto;

import javax.swing.*;
import java.awt.*;

public class VentanaAgregarActividad extends JFrame {
    public VentanaAgregarActividad(ServicioPresupuesto servicio, int proyectoId, VentanaActividades padre) {
        setTitle("Agregar Actividad");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextField txtDescripcion = new JTextField(20);
        JTextField txtCosto = new JTextField(10);
        JButton btnGuardar = new JButton("Guardar");

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Descripción:"));
        panel.add(txtDescripcion);
        panel.add(new JLabel("Costo (Bs):"));
        panel.add(txtCosto);
        panel.add(new JLabel());
        panel.add(btnGuardar);

        add(panel);

        btnGuardar.addActionListener(e -> {
            try {
                double costo = Double.parseDouble(txtCosto.getText());
                servicio.agregarActividad(txtDescripcion.getText(), costo, proyectoId);
                padre.cargarActividades();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}