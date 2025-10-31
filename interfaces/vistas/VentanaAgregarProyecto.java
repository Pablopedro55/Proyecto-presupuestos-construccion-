// 📦 interfaces/vistas/VentanaAgregarProyecto.java
package interfaces.vistas;

import aplicacion.ServicioProyecto;
import interfaces.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class VentanaAgregarProyecto extends JDialog {
    private JTextField txtNombre;
    private JTextField txtPresupuesto;
    private ServicioProyecto servicio;
    private VentanaPrincipal ventanaPrincipal;

    public VentanaAgregarProyecto(ServicioProyecto servicio, VentanaPrincipal ventanaPrincipal) {
        super(ventanaPrincipal, "Agregar Nuevo Proyecto", true);
        this.servicio = servicio;
        this.ventanaPrincipal = ventanaPrincipal;

        setSize(500, 300);
        setLocationRelativeTo(ventanaPrincipal);
        setResizable(false);

        // Panel principal con padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Título
        JLabel titleLabel = new JLabel("Agregar Nuevo Proyecto");
        titleLabel.setFont(Theme.FONT_SUBTITLE);
        titleLabel.setForeground(Theme.BLUE_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // Label Nombre
        JLabel lblNombre = new JLabel("Nombre del Proyecto:");
        lblNombre.setFont(Theme.FONT_NORMAL);
        lblNombre.setForeground(Theme.GRAY_TEXT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        formPanel.add(lblNombre, gbc);

        // TextField Nombre
        txtNombre = new JTextField();
        txtNombre.setFont(Theme.FONT_NORMAL);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        txtNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombre.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BLUE_SECONDARY, 2),
                        BorderFactory.createEmptyBorder(9, 11, 9, 11)));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombre.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            }
        });
        gbc.gridy = 1;
        formPanel.add(txtNombre, gbc);

        // Label Presupuesto
        JLabel lblPresupuesto = new JLabel("Presupuesto (Bs):");
        lblPresupuesto.setFont(Theme.FONT_NORMAL);
        lblPresupuesto.setForeground(Theme.GRAY_TEXT);
        gbc.gridy = 2;
        formPanel.add(lblPresupuesto, gbc);

        // TextField Presupuesto
        txtPresupuesto = new JTextField();
        txtPresupuesto.setFont(Theme.FONT_NORMAL);
        txtPresupuesto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        txtPresupuesto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPresupuesto.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BLUE_SECONDARY, 2),
                        BorderFactory.createEmptyBorder(9, 11, 9, 11)));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPresupuesto.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            }
        });
        gbc.gridy = 3;
        formPanel.add(txtPresupuesto, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(Theme.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnCancelar = Theme.createSecondaryButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JButton btnGuardar = Theme.createPrimaryButton("Guardar");
        btnGuardar.addActionListener(e -> guardarProyecto());

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnGuardar);

        // Agregar todo al panel principal
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Enter para guardar
        getRootPane().setDefaultButton(btnGuardar);
    }

    private void guardarProyecto() {
        String nombre = txtNombre.getText().trim();
        String presupuestoStr = txtPresupuesto.getText().trim();

        // Validaciones
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre del proyecto es obligatorio",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus();
            return;
        }

        if (presupuestoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El presupuesto es obligatorio",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            txtPresupuesto.requestFocus();
            return;
        }

        try {
            double presupuesto = Double.parseDouble(presupuestoStr);

            if (presupuesto <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El presupuesto debe ser mayor a 0",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                txtPresupuesto.requestFocus();
                return;
            }

            // Guardar proyecto
            servicio.agregarProyecto(nombre, presupuesto);
            ventanaPrincipal.cargarProyectos();

            // Mensaje de éxito
            JOptionPane.showMessageDialog(this,
                    "Proyecto guardado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El presupuesto debe ser un número válido",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            txtPresupuesto.requestFocus();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
