// 📦 interfaces/vistas/VentanaPrincipal.java
package interfaces.vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

import aplicacion.ServicioProyecto;
import aplicacion.ServicioMaterial;
import dominio.Proyecto;
import infraestructura.configuracion.ConexionBD;
import infraestructura.mysql.RepositorioProyectosMySQL;
import infraestructura.mysql.RepositorioMaterialesMySQL;
import interfaces.theme.Theme;

public class VentanaPrincipal extends JFrame {

    private JTable tablaProyectos;
    private JButton btnNuevoProyecto, btnVerProyecto;
    private JTextField txtBuscar;
    private ServicioProyecto servicio;
    private ServicioMaterial servicioMaterial;
    private JLabel lblTotalProyectos, lblPresupuestoTotal, lblProyectosEjecucion;

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión de Presupuestos");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con fondo celeste
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.CYAN_VERY_LIGHT);

        // Header
        mainPanel.add(crearHeader(), BorderLayout.NORTH);

        // Contenido
        mainPanel.add(crearContenido(), BorderLayout.CENTER);

        add(mainPanel);

        try {
            Connection con = infraestructura.configuracion.ConexionBD.conectar();
            servicio = new ServicioProyecto(new infraestructura.mysql.RepositorioProyectosMySQL(con));
            servicioMaterial = new ServicioMaterial(new RepositorioMaterialesMySQL(con));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error conectando a base de datos");
        }

        btnNuevoProyecto.addActionListener(e -> new VentanaAgregarProyecto(servicio, this).setVisible(true));
        btnVerProyecto.addActionListener(e -> abrirDetalle());

        cargarProyectos();
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BLUE_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(16, 32, 16, 32));

        // Logo y título
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftPanel.setOpaque(false);

        JLabel iconLabel = new JLabel("🏗️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        JLabel titleLabel = new JLabel("Gestión de Presupuestos de Obras");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.WHITE);

        leftPanel.add(iconLabel);
        leftPanel.add(titleLabel);

        // Botón de usuario
        JButton btnUser = new JButton("👤");
        btnUser.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        btnUser.setForeground(Theme.WHITE);
        btnUser.setFocusPainted(false);
        btnUser.setBorderPainted(false);
        btnUser.setContentAreaFilled(false);
        btnUser.setCursor(new Cursor(Cursor.HAND_CURSOR));

        header.add(leftPanel, BorderLayout.WEST);
        header.add(btnUser, BorderLayout.EAST);

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

        lblTotalProyectos = new JLabel("0");
        lblPresupuestoTotal = new JLabel("Bs 0");
        lblProyectosEjecucion = new JLabel("0");

        JPanel card1 = Theme.createStatCard("Total de Proyectos Activos", "0", Theme.BLUE_PRIMARY);
        JPanel card2 = Theme.createStatCard("Presupuesto Total Asignado", "Bs 0", Theme.BLUE_PRIMARY);
        JPanel card3 = Theme.createStatCard("Proyectos En Ejecución", "0", Theme.SUCCESS);

        // Guardar referencias para actualizar
        lblTotalProyectos = (JLabel) card1.getComponent(1);
        lblPresupuestoTotal = (JLabel) card2.getComponent(1);
        lblProyectosEjecucion = (JLabel) card3.getComponent(1);

        panel.add(card1);
        panel.add(card2);
        panel.add(card3);

        return panel;
    }

    private JPanel crearToolbar() {
        JPanel toolbar = Theme.createCard();
        toolbar.setLayout(new BorderLayout(16, 0));

        // Campo de búsqueda
        txtBuscar = Theme.createTextField("Buscar proyecto...");
        txtBuscar.setPreferredSize(new Dimension(300, 40));

        // Agregar listener para búsqueda en tiempo real
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarProyectos();
            }
        });

        // Botones
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botonesPanel.setOpaque(false);

        btnNuevoProyecto = Theme.createPrimaryButton("➕ Nuevo Proyecto");
        btnVerProyecto = Theme.createSecondaryButton("👁️ Ver Proyecto");

        botonesPanel.add(btnNuevoProyecto);
        botonesPanel.add(btnVerProyecto);

        toolbar.add(txtBuscar, BorderLayout.WEST);
        toolbar.add(botonesPanel, BorderLayout.EAST);

        return toolbar;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = Theme.createCard();
        panel.setLayout(new BorderLayout());

        // Crear tabla
        tablaProyectos = new JTable();
        tablaProyectos.setFont(Theme.FONT_NORMAL);
        Theme.styleTable(tablaProyectos);

        // Hacer que toda la fila sea clickeable
        tablaProyectos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    abrirDetalle();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaProyectos);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void filtrarProyectos() {
        String textoBusqueda = txtBuscar.getText().trim();

        if (textoBusqueda.equals("Buscar proyecto...") || textoBusqueda.isEmpty()) {
            cargarProyectos();
            return;
        }

        if (servicio == null)
            return;

        List<Proyecto> proyectos = servicio.listar();
        String[] columnas = { "ID", "Nombre del Proyecto", "Presupuesto" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Proyecto p : proyectos) {
            if (p.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                modelo.addRow(new Object[] {
                        p.getId(),
                        p.getNombre(),
                        "Bs " + String.format("%,.2f", p.getPresupuesto())
                });
            }
        }

        tablaProyectos.setModel(modelo);
        configurarColumnasTabla();
    }

    public void cargarProyectos() {
        if (servicio == null)
            return;

        List<Proyecto> proyectos = servicio.listar();
        String[] columnas = { "ID", "Nombre del Proyecto", "Presupuesto" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        double presupuestoTotal = 0;
        for (Proyecto p : proyectos) {
            modelo.addRow(new Object[] {
                    p.getId(),
                    p.getNombre(),
                    "Bs " + String.format("%,.2f", p.getPresupuesto())
            });
            presupuestoTotal += p.getPresupuesto();
        }

        tablaProyectos.setModel(modelo);
        configurarColumnasTabla();

        // Actualizar estadísticas
        lblTotalProyectos.setText(String.valueOf(proyectos.size()));
        lblPresupuestoTotal.setText("Bs " + String.format("%,.2f", presupuestoTotal));
        lblProyectosEjecucion.setText(String.valueOf(proyectos.size()));
    }

    private void configurarColumnasTabla() {
        // Configurar anchos de columnas
        tablaProyectos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaProyectos.getColumnModel().getColumn(0).setMaxWidth(50);
        tablaProyectos.getColumnModel().getColumn(1).setPreferredWidth(500);
        tablaProyectos.getColumnModel().getColumn(2).setPreferredWidth(200);
    }

    private void abrirDetalle() {
        int fila = tablaProyectos.getSelectedRow();
        if (fila != -1) {
            int idProyecto = (int) tablaProyectos.getValueAt(fila, 0);
            String nombre = (String) tablaProyectos.getValueAt(fila, 1);
            new VentanaActividades(idProyecto, nombre).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un proyecto", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Configurar Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
