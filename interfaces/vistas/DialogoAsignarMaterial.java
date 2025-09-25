// 📁 interfaces/vistas/DialogoAsignarMaterial.java
package interfaces.vistas;

import javax.swing.*;
import java.awt.*;

public class DialogoAsignarMaterial extends JDialog {
    private JTextField txtCantidad, txtPrecio;
    private boolean confirmado = false;

    public DialogoAsignarMaterial(JFrame padre, String nombreMaterial) {
        super(padre, "Asignar Material: " + nombreMaterial, true);
        setSize(300, 200);
        setLocationRelativeTo(padre);
        setLayout(new GridLayout(4, 2, 10, 10));

        txtCantidad = new JTextField();
        txtPrecio = new JTextField();

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> {
            if (!txtCantidad.getText().isEmpty() && !txtPrecio.getText().isEmpty()) {
                confirmado = true;
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.");
            }
        });

        btnCancelar.addActionListener(e -> setVisible(false));

        add(new JLabel("Cantidad:"));
        add(txtCantidad);
        add(new JLabel("Precio Unitario:"));
        add(txtPrecio);
        add(btnAceptar);
        add(btnCancelar);
    }

    public boolean fueConfirmado() {
        return confirmado;
    }

    public double getCantidad() {
        return Double.parseDouble(txtCantidad.getText());
    }

    public double getPrecio() {
        return Double.parseDouble(txtPrecio.getText());
    }
}