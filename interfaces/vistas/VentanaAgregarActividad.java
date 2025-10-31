// 📦 interfaces/vistas/VentanaAgregarActividad.java
package interfaces.vistas;

import aplicacion.ServicioPresupuesto;
import interfaces.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class VentanaAgregarActividad extends JDialog {
    public VentanaAgregarActividad(ServicioPresupuesto servicio, int proyectoId, VentanaActividades padre) {
        super((Frame) null, "Agregar Actividad", true);
        setSize(500, 380);
        setLocationRelativeTo(padre);
        setResizable(false);

        // Panel principal con fondo celeste
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Theme.CYAN_VERY_LIGHT);
        mainPanel.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Título
        JLabel titleLabel = new JLabel("➕ Nueva Actividad");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.BLUE_PRIMARY);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Panel del formulario
        JPanel formPanel = new JPanel(new GridLayout(2, 1, 0, 16));
        formPanel.setOpaque(false);

        // Campo Descripción
        JPanel descPanel = new JPanel(new BorderLayout(0, 8));
        descPanel.setOpaque(false);
        JLabel lblDesc = new JLabel("Descripción de la Actividad");
        lblDesc.setFont(Theme.FONT_NORMAL);
        lblDesc.setForeground(Theme.GRAY_TEXT);
        JTextField txtDescripcion = new JTextField(20);
        txtDescripcion.setFont(Theme.FONT_NORMAL);
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));

        // Focus effects
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

        descPanel.add(lblDesc, BorderLayout.NORTH);
        descPanel.add(txtDescripcion, BorderLayout.CENTER);

        // Campo Costo
        JPanel costoPanel = new JPanel(new BorderLayout(0, 8));
        costoPanel.setOpaque(false);
        JLabel lblCosto = new JLabel("Costo Unitario (Bs)");
        lblCosto.setFont(Theme.FONT_NORMAL);
        lblCosto.setForeground(Theme.GRAY_TEXT);
        JTextField txtCosto = new JTextField(10);
        txtCosto.setFont(Theme.FONT_NORMAL);
        txtCosto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));

        // Focus effects
        txtCosto.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtCosto.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BLUE_SECONDARY, 2),
                        new EmptyBorder(8, 12, 8, 12)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtCosto.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                        new EmptyBorder(8, 12, 8, 12)));
            }
        });

        costoPanel.add(lblCosto, BorderLayout.NORTH);
        costoPanel.add(txtCosto, BorderLayout.CENTER);

        formPanel.add(descPanel);
        formPanel.add(costoPanel);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnCancelar = Theme.createSecondaryButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JButton btnGuardar = Theme.createPrimaryButton("Guardar Actividad");
        btnGuardar.addActionListener(e -> {
            try {
                String descripcion = txtDescripcion.getText().trim();
                String costoText = txtCosto.getText().trim();

                if (descripcion.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor ingrese la descripción", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (costoText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor ingrese el costo", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double costo = Double.parseDouble(costoText);

                if (costo <= 0) {
                    JOptionPane.showMessageDialog(this, "El costo debe ser mayor a 0", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                servicio.agregarActividad(descripcion, costo, proyectoId);
                padre.cargarActividades();
                JOptionPane.showMessageDialog(this, "Actividad agregada exitosamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El costo debe ser un número válido", "Error",
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