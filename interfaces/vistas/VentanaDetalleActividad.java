// 📦 interfaces/vistas/VentanaDetalleActividad.java
package interfaces.vistas;

import javax.swing.*;
import java.awt.*;

public class VentanaDetalleActividad extends JFrame {
    public VentanaDetalleActividad(String descripcion, double costo) {
        setTitle("Detalle de Actividad");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblDescripcion = new JLabel("Descripción: " + descripcion);
        JLabel lblCosto = new JLabel("Costo Total: Bs " + costo);

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(lblDescripcion);
        panel.add(lblCosto);

        add(panel);
    }
}