// 📦 interfaces/vistas/VentanaAgregarProyecto.java
package interfaces.vistas;

import aplicacion.ServicioProyecto;
import dominio.Proyecto;
import interfaces.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VentanaAgregarProyecto extends JDialog {
    private JTextField txtNombre;
    private JTextField txtPresupuesto;
    private JTextField txtCliente;
    private JTextField txtUbicacion;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JComboBox<String> cmbTipoObra;
    private ServicioProyecto servicio;
    private VentanaPrincipal ventanaPrincipal;

    public VentanaAgregarProyecto(ServicioProyecto servicio, VentanaPrincipal ventanaPrincipal) {
        super(ventanaPrincipal, "Agregar Nuevo Proyecto", true);
        this.servicio = servicio;
        this.ventanaPrincipal = ventanaPrincipal;

        setSize(550, 650);
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

        // Panel de formulario con scroll
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.weightx = 1.0;

        int row = 0;

        // === CAMPO: Nombre del Proyecto ===
        gbc.gridx = 0;
        gbc.gridy = row++;
        formPanel.add(crearLabel("Nombre del Proyecto: *"), gbc);

        txtNombre = crearTextField();
        gbc.gridy = row++;
        formPanel.add(txtNombre, gbc);

        // === CAMPO: Cliente ===
        gbc.gridy = row++;
        formPanel.add(crearLabel("Cliente / Contratante: *"), gbc);

        txtCliente = crearTextField();
        gbc.gridy = row++;
        formPanel.add(txtCliente, gbc);

        // === CAMPO: Ubicación ===
        gbc.gridy = row++;
        formPanel.add(crearLabel("Ubicación de la Obra: *"), gbc);

        txtUbicacion = crearTextField();
        gbc.gridy = row++;
        formPanel.add(txtUbicacion, gbc);

        // === CAMPO: Tipo de Obra ===
        gbc.gridy = row++;
        formPanel.add(crearLabel("Tipo de Obra: *"), gbc);

        String[] tiposObra = {
                "Seleccionar...",
                "Edificación",
                "Vial",
                "Hidráulica",
                "Sanitaria",
                "Eléctrica",
                "Infraestructura",
                "Otra"
        };
        cmbTipoObra = new JComboBox<>(tiposObra);
        cmbTipoObra.setFont(Theme.FONT_NORMAL);
        cmbTipoObra.setBackground(Theme.WHITE);
        cmbTipoObra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        gbc.gridy = row++;
        formPanel.add(cmbTipoObra, gbc);

        // === CAMPO: Presupuesto ===
        gbc.gridy = row++;
        formPanel.add(crearLabel("Presupuesto (Bs): *"), gbc);

        txtPresupuesto = crearTextField();
        gbc.gridy = row++;
        formPanel.add(txtPresupuesto, gbc);

        // === CAMPO: Fecha Inicio ===
        gbc.gridy = row++;
        JLabel lblFechaInicio = crearLabel("Fecha de Inicio (DD/MM/AAAA):");
        lblFechaInicio.setForeground(Theme.GRAY_MUTED);
        formPanel.add(lblFechaInicio, gbc);

        txtFechaInicio = crearTextField();
        txtFechaInicio.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        gbc.gridy = row++;
        formPanel.add(txtFechaInicio, gbc);

        // === CAMPO: Fecha Fin ===
        gbc.gridy = row++;
        JLabel lblFechaFin = crearLabel("Fecha de Fin Estimada (DD/MM/AAAA):");
        lblFechaFin.setForeground(Theme.GRAY_MUTED);
        formPanel.add(lblFechaFin, gbc);

        txtFechaFin = crearTextField();
        gbc.gridy = row++;
        formPanel.add(txtFechaFin, gbc);

        // Nota de campos obligatorios
        JLabel lblNota = new JLabel("* Campos obligatorios");
        lblNota.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNota.setForeground(Theme.GRAY_MUTED);
        gbc.gridy = row++;
        gbc.insets = new Insets(16, 0, 0, 0);
        formPanel.add(lblNota, gbc);

        // Scroll para el formulario
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(Theme.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnCancelar = Theme.createSecondaryButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JButton btnGuardar = Theme.createPrimaryButton("Guardar Proyecto");
        btnGuardar.addActionListener(e -> guardarProyecto());

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnGuardar);

        // Agregar todo al panel principal
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Enter para guardar
        getRootPane().setDefaultButton(btnGuardar);
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Theme.FONT_NORMAL);
        label.setForeground(Theme.GRAY_TEXT);
        return label;
    }

    private JTextField crearTextField() {
        JTextField textField = new JTextField();
        textField.setFont(Theme.FONT_NORMAL);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BLUE_SECONDARY, 2),
                        BorderFactory.createEmptyBorder(9, 11, 9, 11)));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            }
        });
        return textField;
    }

    private void guardarProyecto() {
        String nombre = txtNombre.getText().trim();
        String cliente = txtCliente.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        String tipoObra = (String) cmbTipoObra.getSelectedItem();
        String presupuestoStr = txtPresupuesto.getText().trim();
        String fechaInicioStr = txtFechaInicio.getText().trim();
        String fechaFinStr = txtFechaFin.getText().trim();

        // === VALIDACIONES DE CAMPOS OBLIGATORIOS ===
        if (nombre.isEmpty()) {
            mostrarError("El nombre del proyecto es obligatorio", txtNombre);
            return;
        }

        if (cliente.isEmpty()) {
            mostrarError("El cliente es obligatorio", txtCliente);
            return;
        }

        if (ubicacion.isEmpty()) {
            mostrarError("La ubicación es obligatoria", txtUbicacion);
            return;
        }

        if (tipoObra == null || tipoObra.equals("Seleccionar...")) {
            mostrarError("Debe seleccionar un tipo de obra", null);
            cmbTipoObra.requestFocus();
            return;
        }

        if (presupuestoStr.isEmpty()) {
            mostrarError("El presupuesto es obligatorio", txtPresupuesto);
            return;
        }

        try {
            // Validar presupuesto
            double presupuesto = Double.parseDouble(presupuestoStr);
            if (presupuesto <= 0) {
                mostrarError("El presupuesto debe ser mayor a 0", txtPresupuesto);
                return;
            }

            // Parsear fechas (opcionales)
            LocalDate fechaInicio = null;
            LocalDate fechaFin = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            if (!fechaInicioStr.isEmpty()) {
                try {
                    fechaInicio = LocalDate.parse(fechaInicioStr, formatter);
                } catch (DateTimeParseException e) {
                    mostrarError("Formato de fecha de inicio inválido. Use DD/MM/AAAA", txtFechaInicio);
                    return;
                }
            }

            if (!fechaFinStr.isEmpty()) {
                try {
                    fechaFin = LocalDate.parse(fechaFinStr, formatter);

                    // Validar que fecha fin sea posterior a fecha inicio
                    if (fechaInicio != null && fechaFin.isBefore(fechaInicio)) {
                        mostrarError("La fecha de fin debe ser posterior a la fecha de inicio", txtFechaFin);
                        return;
                    }
                } catch (DateTimeParseException e) {
                    mostrarError("Formato de fecha de fin inválido. Use DD/MM/AAAA", txtFechaFin);
                    return;
                }
            }

            // Crear objeto Proyecto con todos los datos
            Proyecto proyecto = new Proyecto(nombre, presupuesto, cliente, ubicacion,
                    fechaInicio, fechaFin, tipoObra);

            // Guardar en base de datos
            servicio.agregarProyecto(proyecto);
            ventanaPrincipal.cargarProyectos();

            // Mensaje de éxito
            JOptionPane.showMessageDialog(this,
                    "Proyecto guardado exitosamente\n\n" +
                            "Cliente: " + cliente + "\n" +
                            "Ubicación: " + ubicacion + "\n" +
                            "Tipo: " + tipoObra,
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (NumberFormatException ex) {
            mostrarError("El presupuesto debe ser un número válido", txtPresupuesto);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void mostrarError(String mensaje, JTextField campo) {
        JOptionPane.showMessageDialog(this,
                mensaje,
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
        if (campo != null) {
            campo.requestFocus();
        }
    }
}
