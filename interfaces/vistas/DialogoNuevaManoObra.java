package interfaces.vistas;

import aplicacion.ServicioManoObra;
import interfaces.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class DialogoNuevaManoObra extends JDialog {
    private final ServicioManoObra servicioManoObra;
    private final JFrame ventanaPadre;

    public DialogoNuevaManoObra(ServicioManoObra servicioManoObra, JFrame ventanaPadre) {
        super(ventanaPadre, "Nueva Mano de Obra", true);
        this.servicioManoObra = servicioManoObra;
        this.ventanaPadre = ventanaPadre;

        setSize(520, 380);
        setLocationRelativeTo(ventanaPadre);
        setResizable(false);

        // Panel principal con fondo celeste
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Theme.CYAN_VERY_LIGHT);
        mainPanel.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Título
        JLabel titleLabel = new JLabel("👷 Agregar Mano de Obra al Catálogo");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.BLUE_PRIMARY);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Panel del formulario
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 0, 16));
        formPanel.setOpaque(false);

        // Campo Descripción
        JPanel descripcionPanel = new JPanel(new BorderLayout(0, 8));
        descripcionPanel.setOpaque(false);
        JLabel lblDescripcion = new JLabel("Descripción de la Mano de Obra");
        lblDescripcion.setFont(Theme.FONT_NORMAL);
        lblDescripcion.setForeground(Theme.GRAY_TEXT);
        JTextField txtDescripcion = new JTextField();
        txtDescripcion.setFont(Theme.FONT_NORMAL);
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));

        txtDescripcion.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BLUE_SECONDARY, 2),
                        new EmptyBorder(8, 12, 8, 12)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                        new EmptyBorder(8, 12, 8, 12)));
            }
        });

        descripcionPanel.add(lblDescripcion, BorderLayout.NORTH);
        descripcionPanel.add(txtDescripcion, BorderLayout.CENTER);

        // Campo Unidad
        JPanel unidadPanel = new JPanel(new BorderLayout(0, 8));
        unidadPanel.setOpaque(false);
        JLabel lblUnidad = new JLabel("Unidad de Medida (ej: hora, día, mes)");
        lblUnidad.setFont(Theme.FONT_NORMAL);
        lblUnidad.setForeground(Theme.GRAY_TEXT);
        JTextField txtUnidad = new JTextField();
        txtUnidad.setFont(Theme.FONT_NORMAL);
        txtUnidad.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));

        txtUnidad.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtUnidad.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BLUE_SECONDARY, 2),
                        new EmptyBorder(8, 12, 8, 12)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtUnidad.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                        new EmptyBorder(8, 12, 8, 12)));
            }
        });

        unidadPanel.add(lblUnidad, BorderLayout.NORTH);
        unidadPanel.add(txtUnidad, BorderLayout.CENTER);

        // Campo Precio
        JPanel precioPanel = new JPanel(new BorderLayout(0, 8));
        precioPanel.setOpaque(false);
        JLabel lblPrecio = new JLabel("Precio Unitario (Bs)");
        lblPrecio.setFont(Theme.FONT_NORMAL);
        lblPrecio.setForeground(Theme.GRAY_TEXT);
        JTextField txtPrecio = new JTextField();
        txtPrecio.setFont(Theme.FONT_NORMAL);
        txtPrecio.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));

        txtPrecio.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtPrecio.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BLUE_SECONDARY, 2),
                        new EmptyBorder(8, 12, 8, 12)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtPrecio.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                        new EmptyBorder(8, 12, 8, 12)));
            }
        });

        precioPanel.add(lblPrecio, BorderLayout.NORTH);
        precioPanel.add(txtPrecio, BorderLayout.CENTER);

        formPanel.add(descripcionPanel);
        formPanel.add(unidadPanel);
        formPanel.add(precioPanel);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnCancelar = Theme.createSecondaryButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JButton btnGuardar = Theme.createPrimaryButton("Guardar Mano de Obra");
        btnGuardar.addActionListener(e -> {
            String descripcion = txtDescripcion.getText().trim();
            String unidad = txtUnidad.getText().trim();
            String precioStr = txtPrecio.getText().trim();

            if (descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese la descripción", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (unidad.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese la unidad de medida", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese el precio", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double precio = Double.parseDouble(precioStr);

                if (precio <= 0) {
                    JOptionPane.showMessageDialog(this, "El precio debe ser mayor a 0", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                servicioManoObra.agregar(descripcion, unidad, precio);
                JOptionPane.showMessageDialog(this, "✓ Mano de obra añadida al catálogo exitosamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número válido", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnGuardar);

        // Ensamblar
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
