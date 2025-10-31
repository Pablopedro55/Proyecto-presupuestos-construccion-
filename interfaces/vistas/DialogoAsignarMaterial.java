// 📁 interfaces/vistas/DialogoAsignarMaterial.java
package interfaces.vistas;

import interfaces.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class DialogoAsignarMaterial extends JDialog {
    private JTextField txtCantidad, txtPrecio;
    private boolean confirmado = false;

    public DialogoAsignarMaterial(JFrame padre, String nombreMaterial) {
        super(padre, "Asignar Material", true);
        setSize(500, 380);
        setLocationRelativeTo(padre);
        setResizable(false);

        // Panel principal con fondo celeste
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Theme.CYAN_VERY_LIGHT);
        mainPanel.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Título
        JLabel titleLabel = new JLabel("📦 Asignar: " + nombreMaterial);
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.BLUE_PRIMARY);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Panel del formulario
        JPanel formPanel = new JPanel(new GridLayout(2, 1, 0, 16));
        formPanel.setOpaque(false);

        // Campo Cantidad
        JPanel cantidadPanel = new JPanel(new BorderLayout(0, 8));
        cantidadPanel.setOpaque(false);
        JLabel lblCantidad = new JLabel("Cantidad");
        lblCantidad.setFont(Theme.FONT_NORMAL);
        lblCantidad.setForeground(Theme.GRAY_TEXT);
        txtCantidad = new JTextField();
        txtCantidad.setFont(Theme.FONT_NORMAL);
        txtCantidad.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));

        txtCantidad.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtCantidad.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BLUE_SECONDARY, 2),
                        new EmptyBorder(8, 12, 8, 12)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtCantidad.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                        new EmptyBorder(8, 12, 8, 12)));
            }
        });

        cantidadPanel.add(lblCantidad, BorderLayout.NORTH);
        cantidadPanel.add(txtCantidad, BorderLayout.CENTER);

        // Campo Precio
        JPanel precioPanel = new JPanel(new BorderLayout(0, 8));
        precioPanel.setOpaque(false);
        JLabel lblPrecio = new JLabel("Precio Unitario (Bs)");
        lblPrecio.setFont(Theme.FONT_NORMAL);
        lblPrecio.setForeground(Theme.GRAY_TEXT);
        txtPrecio = new JTextField();
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

        formPanel.add(cantidadPanel);
        formPanel.add(precioPanel);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnCancelar = Theme.createSecondaryButton("Cancelar");
        btnCancelar.addActionListener(e -> {
            confirmado = false;
            setVisible(false);
        });

        JButton btnAceptar = Theme.createPrimaryButton("Aceptar");
        btnAceptar.addActionListener(e -> {
            String cantidadStr = txtCantidad.getText().trim();
            String precioStr = txtPrecio.getText().trim();

            if (cantidadStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese la cantidad", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese el precio", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double cantidad = Double.parseDouble(cantidadStr);
                double precio = Double.parseDouble(precioStr);

                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (precio <= 0) {
                    JOptionPane.showMessageDialog(this, "El precio debe ser mayor a 0", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                confirmado = true;
                setVisible(false);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Los valores deben ser números válidos", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnAceptar);

        // Ensamblar
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
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