package interfaces.vistas;

import aplicacion.ServicioMaterial;

import javax.swing.*;
import java.awt.*;

public class DialogoNuevoMaterial extends JDialog {
    private final ServicioMaterial servicioMaterial;
    private final JFrame ventanaPadre;

    public DialogoNuevoMaterial(ServicioMaterial servicioMaterial, JFrame ventanaPadre) {
        super(ventanaPadre, "Nuevo Material", true);
        this.servicioMaterial = servicioMaterial;
        this.ventanaPadre = ventanaPadre;

        setSize(400, 250);
        setLocationRelativeTo(ventanaPadre);
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtNombre = new JTextField();
        JTextField txtUnidad = new JTextField();
        JTextField txtPrecio = new JTextField();

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Unidad:"));
        panelFormulario.add(txtUnidad);

        panelFormulario.add(new JLabel("Precio Unitario:"));
        panelFormulario.add(txtPrecio);

        add(panelFormulario, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String unidad = txtUnidad.getText().trim();
            String precioStr = txtPrecio.getText().trim();

            if (nombre.isEmpty() || unidad.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            try {
                double precio = Double.parseDouble(precioStr);
                servicioMaterial.crearEnCatalogo(nombre, unidad, precio);
                JOptionPane.showMessageDialog(this, "Material añadido al catálogo.");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio inválido.");
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }
}
