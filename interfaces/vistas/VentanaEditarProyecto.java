// 📦 interfaces/vistas/VentanaEditarProyecto.java
package interfaces.vistas;

import aplicacion.ServicioProyecto;
import dominio.Proyecto;
import interfaces.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VentanaEditarProyecto extends JDialog {
    private JTextField txtNombre;
    private JTextField txtPresupuesto;
    private JTextField txtCliente;
    private JTextField txtUbicacion;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JComboBox<String> cmbTipoObra;
    private JComboBox<String> cmbEstado;
    private ServicioProyecto servicio;
    private VentanaPrincipal ventanaPrincipal;
    private Proyecto proyectoActual;

    public VentanaEditarProyecto(ServicioProyecto servicio, VentanaPrincipal ventanaPrincipal, int proyectoId) {
        super(ventanaPrincipal, "Editar Proyecto", true);
        this.servicio = servicio;
        this.ventanaPrincipal = ventanaPrincipal;

        // Cargar proyecto actual
        this.proyectoActual = servicio.obtenerPorId(proyectoId);
        if (proyectoActual == null) {
            JOptionPane.showMessageDialog(this, "Error: No se encontró el proyecto", "Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setSize(550, 700);
        setLocationRelativeTo(ventanaPrincipal);
        setResizable(false);

        // Panel principal con padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Título
        JLabel titleLabel = new JLabel("Editar Proyecto: " + proyectoActual.getNombre());
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
        txtNombre.setText(proyectoActual.getNombre());
        gbc.gridy = row++;
        formPanel.add(txtNombre, gbc);

        // === CAMPO: Cliente ===
        gbc.gridy = row++;
        formPanel.add(crearLabel("Cliente / Contratante: *"), gbc);

        txtCliente = crearTextField();
        txtCliente.setText(proyectoActual.getCliente() != null ? proyectoActual.getCliente() : "");
        gbc.gridy = row++;
        formPanel.add(txtCliente, gbc);

        // === CAMPO: Ubicación ===
        gbc.gridy = row++;
        formPanel.add(crearLabel("Ubicación de la Obra: *"), gbc);

        txtUbicacion = crearTextField();
        txtUbicacion.setText(proyectoActual.getUbicacion() != null ? proyectoActual.getUbicacion() : "");
        gbc.gridy = row++;
        formPanel.add(txtUbicacion, gbc);

        // === CAMPO: Tipo de Obra ===
        gbc.gridy = row++;
        formPanel.add(crearLabel("Tipo de Obra: *"), gbc);

        String[] tiposObra = { "Edificación", "Vial", "Hidráulica", "Sanitaria", "Eléctrica", "Infraestructura",
                "Otra" };
        cmbTipoObra = new JComboBox<>(tiposObra);
        cmbTipoObra.setFont(Theme.FONT_NORMAL);
        cmbTipoObra.setPreferredSize(new Dimension(0, 40));
        if (proyectoActual.getTipoObra() != null) {
            cmbTipoObra.setSelectedItem(proyectoActual.getTipoObra());
        }
        gbc.gridy = row++;
        formPanel.add(cmbTipoObra, gbc);

        // === CAMPO: Estado ===
        gbc.gridy = row++;
        formPanel.add(crearLabel("Estado del Proyecto: *"), gbc);

        String[] estados = { "Planificación", "En Ejecución", "Finalizado", "Suspendido" };
        cmbEstado = new JComboBox<>(estados);
        cmbEstado.setFont(Theme.FONT_NORMAL);
        cmbEstado.setPreferredSize(new Dimension(0, 40));
        if (proyectoActual.getEstado() != null) {
            cmbEstado.setSelectedItem(proyectoActual.getEstado());
        }
        gbc.gridy = row++;
        formPanel.add(cmbEstado, gbc);

        // === CAMPO: Presupuesto ===
        gbc.gridy = row++;
        formPanel.add(crearLabel("Presupuesto Total (Bs): *"), gbc);

        txtPresupuesto = crearTextField();
        txtPresupuesto.setText(String.valueOf(proyectoActual.getPresupuesto()));
        gbc.gridy = row++;
        formPanel.add(txtPresupuesto, gbc);

        // === CAMPO: Fecha Inicio ===
        gbc.gridy = row++;
        formPanel.add(crearLabel("Fecha de Inicio (DD/MM/YYYY):"), gbc);

        txtFechaInicio = crearTextField();
        if (proyectoActual.getFechaInicio() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            txtFechaInicio.setText(proyectoActual.getFechaInicio().format(formatter));
        }
        gbc.gridy = row++;
        formPanel.add(txtFechaInicio, gbc);

        // === CAMPO: Fecha Fin ===
        gbc.gridy = row++;
        formPanel.add(crearLabel("Fecha de Finalización (DD/MM/YYYY):"), gbc);

        txtFechaFin = crearTextField();
        if (proyectoActual.getFechaFin() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            txtFechaFin.setText(proyectoActual.getFechaFin().format(formatter));
        }
        gbc.gridy = row++;
        formPanel.add(txtFechaFin, gbc);

        // Nota de ayuda
        JLabel lblAyuda = new JLabel("<html><i>💡 Los campos marcados con * son obligatorios</i></html>");
        lblAyuda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAyuda.setForeground(new Color(100, 116, 139));
        gbc.gridy = row++;
        gbc.insets = new Insets(16, 0, 8, 0);
        formPanel.add(lblAyuda, gbc);

        // Scroll pane para el formulario
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setBackground(Theme.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        JButton btnCancelar = Theme.createSecondaryButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JButton btnGuardar = Theme.createPrimaryButton("💾 Guardar Cambios");
        btnGuardar.addActionListener(e -> guardarCambios());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        // Agregar componentes al panel principal
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(panelBotones, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Theme.FONT_NORMAL);
        label.setForeground(new Color(30, 41, 59));
        return label;
    }

    private JTextField crearTextField() {
        return Theme.createTextField("");
    }

    private void guardarCambios() {
        // Validar campos obligatorios
        String nombre = txtNombre.getText().trim();
        String cliente = txtCliente.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        String tipoObra = (String) cmbTipoObra.getSelectedItem();
        String estado = (String) cmbEstado.getSelectedItem();
        String presupuestoStr = txtPresupuesto.getText().trim();
        String fechaInicioStr = txtFechaInicio.getText().trim();
        String fechaFinStr = txtFechaFin.getText().trim();

        // Validaciones
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
            return;
        }

        // Validar presupuesto
        double presupuesto;
        try {
            presupuesto = Double.parseDouble(presupuestoStr);
            if (presupuesto <= 0) {
                mostrarError("El presupuesto debe ser mayor a 0", txtPresupuesto);
                return;
            }
        } catch (NumberFormatException e) {
            mostrarError("El presupuesto debe ser un número válido", txtPresupuesto);
            return;
        }

        // Validar y parsear fechas
        LocalDate fechaInicio = null;
        LocalDate fechaFin = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (!fechaInicioStr.isEmpty()) {
            try {
                fechaInicio = LocalDate.parse(fechaInicioStr, formatter);
            } catch (DateTimeParseException e) {
                mostrarError("Formato de fecha inicio inválido. Use DD/MM/YYYY", txtFechaInicio);
                return;
            }
        }

        if (!fechaFinStr.isEmpty()) {
            try {
                fechaFin = LocalDate.parse(fechaFinStr, formatter);
            } catch (DateTimeParseException e) {
                mostrarError("Formato de fecha fin inválido. Use DD/MM/YYYY", txtFechaFin);
                return;
            }
        }

        // Validar que fecha fin sea posterior a fecha inicio
        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            mostrarError("La fecha de finalización debe ser posterior a la fecha de inicio", txtFechaFin);
            return;
        }

        // Actualizar el proyecto actual
        proyectoActual.setNombre(nombre);
        proyectoActual.setPresupuesto(presupuesto);
        proyectoActual.setCliente(cliente);
        proyectoActual.setUbicacion(ubicacion);
        proyectoActual.setFechaInicio(fechaInicio);
        proyectoActual.setFechaFin(fechaFin);
        proyectoActual.setTipoObra(tipoObra);
        proyectoActual.setEstado(estado);

        try {
            servicio.actualizarProyecto(proyectoActual);
            JOptionPane.showMessageDialog(this,
                    "✅ Proyecto actualizado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            ventanaPrincipal.cargarProyectos();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "❌ Error al actualizar el proyecto: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarError(String mensaje, JTextField campo) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de Validación", JOptionPane.ERROR_MESSAGE);
        if (campo != null) {
            campo.requestFocus();
            campo.selectAll();
        }
    }
}
