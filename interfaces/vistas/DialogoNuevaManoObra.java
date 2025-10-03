package interfaces.vistas;

import aplicacion.ServicioManoObra;

import javax.swing.*;
import java.awt.*;

public class DialogoNuevaManoObra extends JDialog {
    private final ServicioManoObra servicioManoObra;
    private final JFrame ventanaPadre;

    public DialogoNuevaManoObra(ServicioManoObra servicioManoObra, JFrame ventanaPadre) {
        super(ventanaPadre, "Nueva Mano de Obra", true);
        this.servicioManoObra = servicioManoObra;
        this.ventanaPadre = ventanaPadre;

        setSize(400, 250);
        setLocationRelativeTo(ventanaPadre);
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtDescripcion = new JTextField();
        JTextField txtUnidad = new JTextField();
        JTextField txtPrecio = new JTextField();

        panelFormulario.add(new JLabel("Descripción:"));
        panelFormulario.add(txtDescripcion);

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
            String descripcion = txtDescripcion.getText().trim();
            String unidad = txtUnidad.getText().trim();
            String precioStr = txtPrecio.getText().trim();

            if (descripcion.isEmpty() || unidad.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            try {
                double precio = Double.parseDouble(precioStr);
                servicioManoObra.agregar(descripcion, unidad, precio);
                JOptionPane.showMessageDialog(this, "Mano de obra añadida al catálogo.");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio inválido.");
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }
}
