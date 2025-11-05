package interfaces.vistas;

import aplicacion.*;
import dominio.Actividad;
import infraestructura.configuracion.ConexionBD;
import infraestructura.mysql.*;
import interfaces.theme.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class VentanaActividades extends JFrame {
    private JTable tablaActividades;
    private JLabel lblPresupuestoTotal, lblPresupuestoEjecutado, lblDiferencia, lblPorcentaje;
    private JButton btnNuevaActividad, btnVerDetalle, btnMateriales, btnEjecutar, btnManoObra, btnMaquinaria,
            btnVerEjecucion, btnCambiarEstado;
    private ServicioPresupuesto servicio;
    private ServicioMaterial servicioMaterial;
    private ServicioManoObra servicioManoObra;
    private ServicioMaquinaria servicioMaquinaria;
    private ServicioEjecucion servicioEjecucion;
    private RepositorioActividadesMySQL repoActividades;
    private RepositorioManoObraMySQL repoManoObra;
    private RepositorioMaquinariaMySQL repoMaquinaria;
    private int proyectoId;
    private String nombreProyecto;
    private JProgressBar progressBar;

    public VentanaActividades(int proyectoId, String nombreProyecto) {
        this.proyectoId = proyectoId;
        this.nombreProyecto = nombreProyecto;

        setTitle("Actividades del Proyecto");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con fondo celeste
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.CYAN_VERY_LIGHT);

        // Header con breadcrumb
        mainPanel.add(crearHeader(), BorderLayout.NORTH);

        // Contenido
        mainPanel.add(crearContenido(), BorderLayout.CENTER);

        add(mainPanel);

        try {
            Connection con = ConexionBD.conectar();
            repoActividades = new RepositorioActividadesMySQL(con);
            repoManoObra = new RepositorioManoObraMySQL(con);
            repoMaquinaria = new RepositorioMaquinariaMySQL(con);
            servicio = new ServicioPresupuesto(repoActividades);
            servicioMaterial = new ServicioMaterial(new RepositorioMaterialesMySQL(con));
            servicioManoObra = new ServicioManoObra(repoManoObra);
            servicioMaquinaria = new ServicioMaquinaria(repoMaquinaria);
            servicioEjecucion = new ServicioEjecucion(new RepositorioEjecucionesMySQL(con));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error conectando a base de datos");
        }

        btnNuevaActividad
                .addActionListener(e -> new VentanaAgregarActividad(servicio, proyectoId, this).setVisible(true));
        btnCambiarEstado.addActionListener(e -> cambiarEstadoActividad());
        btnVerDetalle.addActionListener(e -> abrirDetalle());
        btnMateriales.addActionListener(e -> abrirMateriales());
        btnEjecutar.addActionListener(e -> new VentanaEjecutarActividad(proyectoId, this).setVisible(true));
        btnManoObra.addActionListener(e -> abrirManoObra());
        btnMaquinaria.addActionListener(e -> abrirMaquinaria());
        btnVerEjecucion.addActionListener(e -> verEjecucionPresupuesto());

        cargarActividades();
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BLUE_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(16, 32, 16, 32));

        // Botón volver y breadcrumb
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftPanel.setOpaque(false);

        JButton btnVolver = new JButton("←");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnVolver.setForeground(Theme.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> dispose());

        JLabel breadcrumb = new JLabel("Inicio > Proyectos > " + nombreProyecto);
        breadcrumb.setFont(Theme.FONT_SMALL);
        breadcrumb.setForeground(Theme.WHITE);

        leftPanel.add(btnVolver);
        leftPanel.add(breadcrumb);

        header.add(leftPanel, BorderLayout.WEST);

        return header;
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout(0, 16));
        contenido.setBackground(Theme.CYAN_VERY_LIGHT);
        contenido.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        // Panel superior con stats y toolbar
        JPanel topPanel = new JPanel(new BorderLayout(0, 16));
        topPanel.setOpaque(false);

        topPanel.add(crearPanelEstadisticas(), BorderLayout.NORTH);
        topPanel.add(crearToolbar(), BorderLayout.CENTER);

        contenido.add(topPanel, BorderLayout.NORTH);
        contenido.add(crearPanelTabla(), BorderLayout.CENTER);

        return contenido;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 16, 0));
        panel.setOpaque(false);

        // Card 1: Presupuesto Total
        JPanel card1 = Theme.createCard();
        card1.setLayout(new BorderLayout(0, 10));

        JLabel title1 = new JLabel("💰 Presupuesto Total del Proyecto");
        title1.setFont(Theme.FONT_SMALL);
        title1.setForeground(Theme.GRAY_MUTED);

        lblPresupuestoTotal = new JLabel("Bs 0");
        lblPresupuestoTotal.setFont(Theme.FONT_LARGE);
        lblPresupuestoTotal.setForeground(Theme.BLUE_PRIMARY);

        card1.add(title1, BorderLayout.NORTH);
        card1.add(lblPresupuestoTotal, BorderLayout.CENTER);

        // Card 2: Total Ejecutado
        JPanel card2 = Theme.createCard();
        card2.setLayout(new BorderLayout(0, 10));

        JLabel title2 = new JLabel("📊 Total Ejecutado");
        title2.setFont(Theme.FONT_SMALL);
        title2.setForeground(Theme.GRAY_MUTED);

        JPanel ejecutadoPanel = new JPanel(new BorderLayout(0, 8));
        ejecutadoPanel.setOpaque(false);

        lblPresupuestoEjecutado = new JLabel("Bs 0");
        lblPresupuestoEjecutado.setFont(Theme.FONT_LARGE);
        lblPresupuestoEjecutado.setForeground(Theme.BLUE_SECONDARY);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setBackground(Theme.GRAY_BORDER);
        progressBar.setForeground(Theme.BLUE_SECONDARY);
        progressBar.setPreferredSize(new Dimension(200, 8));

        lblPorcentaje = new JLabel("0% ejecutado");
        lblPorcentaje.setFont(Theme.FONT_SMALL);
        lblPorcentaje.setForeground(Theme.GRAY_MUTED);

        ejecutadoPanel.add(lblPresupuestoEjecutado, BorderLayout.NORTH);
        ejecutadoPanel.add(progressBar, BorderLayout.CENTER);
        ejecutadoPanel.add(lblPorcentaje, BorderLayout.SOUTH);

        card2.add(title2, BorderLayout.NORTH);
        card2.add(ejecutadoPanel, BorderLayout.CENTER);

        // Card 3: Diferencia/Saldo
        JPanel card3 = Theme.createCard();
        card3.setLayout(new BorderLayout(0, 10));

        JLabel title3 = new JLabel("💵 Diferencia/Saldo");
        title3.setFont(Theme.FONT_SMALL);
        title3.setForeground(Theme.GRAY_MUTED);

        lblDiferencia = new JLabel("Bs 0");
        lblDiferencia.setFont(Theme.FONT_LARGE);
        lblDiferencia.setForeground(Theme.SUCCESS);

        card3.add(title3, BorderLayout.NORTH);
        card3.add(lblDiferencia, BorderLayout.CENTER);

        panel.add(card1);
        panel.add(card2);
        panel.add(card3);

        return panel;
    }

    private JPanel crearToolbar() {
        JPanel toolbar = Theme.createCard();
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));

        btnNuevaActividad = Theme.createPrimaryButton("➕ Agregar Actividad");
        btnCambiarEstado = Theme.createSecondaryButton("🔄 Cambiar Estado");
        btnEjecutar = Theme.createSuccessButton("▶️ Ejecutar Actividad");
        btnVerEjecucion = Theme.createSecondaryButton("📈 Ver Ejecución Presupuesto");
        btnVerEjecucion.setForeground(Theme.BLUE_SECONDARY);

        toolbar.add(btnNuevaActividad);
        toolbar.add(btnCambiarEstado);
        toolbar.add(btnEjecutar);
        toolbar.add(btnVerEjecucion);

        return toolbar;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = Theme.createCard();
        panel.setLayout(new BorderLayout());

        // Título
        JLabel titleLabel = new JLabel("Actividades del Proyecto");
        titleLabel.setFont(Theme.FONT_HEADING);
        titleLabel.setForeground(Theme.GRAY_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        // Crear tabla
        tablaActividades = new JTable();
        Theme.styleTable(tablaActividades);

        JScrollPane scrollPane = new JScrollPane(tablaActividades);
        scrollPane.setBorder(null);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones de gestión
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 12));
        botonesPanel.setBackground(Theme.WHITE);

        JLabel lblGestion = new JLabel("Gestión de actividad seleccionada:");
        lblGestion.setFont(Theme.FONT_SMALL);
        lblGestion.setForeground(Theme.GRAY_MUTED);

        btnMateriales = Theme.createSecondaryButton("📦 Materiales");
        btnManoObra = Theme.createSecondaryButton("👷 Mano de Obra");
        btnMaquinaria = Theme.createSecondaryButton("🚜 Maquinaria");
        btnVerDetalle = Theme.createSecondaryButton("👁️ Ver Detalle");

        botonesPanel.add(lblGestion);
        botonesPanel.add(btnMateriales);
        botonesPanel.add(btnManoObra);
        botonesPanel.add(btnMaquinaria);
        botonesPanel.add(btnVerDetalle);

        panel.add(botonesPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void cargarActividades() {
        if (servicio == null)
            return;

        List<Actividad> actividades = servicio.listarPorProyecto(proyectoId);
        String[] columnas = { "ID", "Descripción", "Estado", "Costo unitario (Bs)", "Ejecuciones",
                "Total Ejecutado (Bs)" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        double totalEjecutado = 0;
        for (Actividad a : actividades) {
            int ejecuciones = servicioEjecucion.contarEjecuciones(a.getId());
            // Calcular el costo real sumando materiales, mano de obra y maquinaria
            double costoMateriales = repoActividades.calcularCostoMateriales(a.getId());
            double costoManoObra = repoManoObra.calcularCostoManoObra(a.getId());
            double costoMaquinaria = repoMaquinaria.calcularCostoMaquinaria(a.getId());
            double costoTotal = costoMateriales + costoManoObra + costoMaquinaria;
            double total = ejecuciones * costoTotal;
            modelo.addRow(new Object[] {
                    a.getId(),
                    a.getDescripcion(),
                    a.getEstado() != null ? a.getEstado() : "Pendiente",
                    String.format("%.2f", costoTotal),
                    ejecuciones,
                    String.format("%.2f", total)
            });
            totalEjecutado += total;
        }

        tablaActividades.setModel(modelo);
        Theme.styleTable(tablaActividades);
        tablaActividades.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaActividades.getColumnModel().getColumn(2).setPreferredWidth(100); // Estado

        double presupuestoProyecto = obtenerPresupuestoProyecto();
        double diferencia = presupuestoProyecto - totalEjecutado;

        lblPresupuestoTotal.setText(String.format("Bs %.2f", presupuestoProyecto));
        lblPresupuestoEjecutado.setText(String.format("Bs %.2f", totalEjecutado));
        lblDiferencia.setText(String.format("Bs %.2f", diferencia));

        // Actualizar color de diferencia
        if (diferencia >= 0) {
            lblDiferencia.setForeground(Theme.SUCCESS);
        } else {
            lblDiferencia.setForeground(Theme.DANGER);
        }

        // Actualizar barra de progreso y porcentaje
        int porcentaje = 0;
        if (presupuestoProyecto > 0) {
            porcentaje = (int) ((totalEjecutado / presupuestoProyecto) * 100);
        }
        progressBar.setValue(porcentaje);
        lblPorcentaje.setText(porcentaje + "% ejecutado");
    }

    private void verEjecucionPresupuesto() {
        double presupuesto = obtenerPresupuestoProyecto();
        List<Actividad> actividades = servicio.listarPorProyecto(proyectoId);
        new VentanaEjecucionPresupuesto(actividades, presupuesto).setVisible(true);
    }

    private double obtenerPresupuestoProyecto() {
        try {
            Connection con = ConexionBD.conectar();
            var ps = con.prepareStatement("SELECT presupuesto FROM proyectos WHERE id = ?");
            ps.setInt(1, proyectoId);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("presupuesto");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void abrirDetalle() {
        int fila = tablaActividades.getSelectedRow();
        if (fila != -1) {
            try {
                int actividadId = Integer.parseInt(tablaActividades.getValueAt(fila, 0).toString());
                String descripcion = tablaActividades.getValueAt(fila, 1).toString();
                double costo = Double.parseDouble(tablaActividades.getValueAt(fila, 2).toString().replace(",", ""));
                new VentanaDetalleActividad(actividadId, descripcion, costo).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir detalle: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una actividad para ver el detalle");
        }
    }

    private void abrirMateriales() {
        int fila = tablaActividades.getSelectedRow();
        if (fila != -1) {
            try {
                int actividadId = Integer.parseInt(tablaActividades.getValueAt(fila, 0).toString());
                new VentanaMateriales(servicioMaterial, actividadId).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir materiales: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una actividad para ver o agregar materiales");
        }
    }

    private void abrirManoObra() {
        int fila = tablaActividades.getSelectedRow();
        if (fila != -1) {
            int actividadId = Integer.parseInt(tablaActividades.getValueAt(fila, 0).toString());
            new VentanaManoObraAsignar(servicioManoObra, actividadId).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una actividad para gestionar mano de obra");
        }
    }

    private void abrirMaquinaria() {
        int fila = tablaActividades.getSelectedRow();
        if (fila != -1) {
            int actividadId = Integer.parseInt(tablaActividades.getValueAt(fila, 0).toString());
            new VentanaMaquinariaAsignar(servicioMaquinaria, actividadId).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una actividad para gestionar maquinaria");
        }
    }

    private void cambiarEstadoActividad() {
        int fila = tablaActividades.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione una actividad de la tabla",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int actividadId = Integer.parseInt(tablaActividades.getValueAt(fila, 0).toString());
        String estadoActual = tablaActividades.getValueAt(fila, 2).toString();

        // Opciones de estado
        String[] estados = { "Pendiente", "En Progreso", "Completada" };

        // Mostrar diálogo de selección
        String nuevoEstado = (String) JOptionPane.showInputDialog(
                this,
                "Estado actual: " + estadoActual + "\n\nSeleccione el nuevo estado:",
                "Cambiar Estado de Actividad",
                JOptionPane.QUESTION_MESSAGE,
                null,
                estados,
                estadoActual);

        if (nuevoEstado != null && !nuevoEstado.equals(estadoActual)) {
            try {
                // Usar el servicio con validación
                servicio.cambiarEstadoActividad(actividadId, nuevoEstado);
                JOptionPane.showMessageDialog(this,
                        "✅ Estado actualizado a: " + nuevoEstado,
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarActividades(); // Recargar tabla
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "❌ Error al actualizar estado: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
