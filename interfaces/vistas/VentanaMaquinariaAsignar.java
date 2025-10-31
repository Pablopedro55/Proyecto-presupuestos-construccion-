// 📦 interfaces/vistas/VentanaMaquinariaAsignar.java
package interfaces.vistas;

import aplicacion.ServicioMaquinaria;
import dominio.Maquinaria;
import interfaces.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class VentanaMaquinariaAsignar extends JFrame {
    private final ServicioMaquinaria servicio;
    private final int actividadId;
    private JTable tablaCatalogo, tablaAsignados;

    public VentanaMaquinariaAsignar(ServicioMaquinaria servicio, int actividadId) {
        this.servicio = servicio;
        this.actividadId = actividadId;

        setTitle("Maquinaria - Actividad #" + actividadId);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principal con fondo celeste
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Theme.CYAN_VERY_LIGHT);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BLUE_PRIMARY);
        header.setBorder(new EmptyBorder(16, 32, 16, 32));

        JLabel titleLabel = new JLabel("🚜 Gestión de Maquinaria - Actividad #" + actividadId);
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.WHITE);

        header.add(titleLabel, BorderLayout.WEST);

        // Tabs estilizados
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Theme.FONT_NORMAL);
        tabs.setBackground(Theme.CYAN_VERY_LIGHT);
        tabs.setBorder(new EmptyBorder(16, 16, 16, 16));

        tabs.addTab("📋 Catálogo de Maquinaria", crearPanelCatalogo());
        tabs.addTab("✓ Maquinaria Asignada", crearPanelAsignados());

        // Panel inferior con botón volver
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        panelInferior.setBackground(Theme.CYAN_VERY_LIGHT);

        JButton btnVolver = Theme.createSecondaryButton("← Volver");
        btnVolver.addActionListener(e -> dispose());
        panelInferior.add(btnVolver);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);
        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(Theme.CYAN_VERY_LIGHT);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Panel superior con búsqueda y botones
        JPanel topPanel = new JPanel(new BorderLayout(12, 0));
        topPanel.setOpaque(false);

        // Campo de búsqueda
        JTextField txtBuscar = new JTextField();
        txtBuscar.setFont(Theme.FONT_NORMAL);
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));
        txtBuscar.setPreferredSize(new Dimension(300, 40));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.add(txtBuscar, BorderLayout.CENTER);

        // Botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setOpaque(false);

        JButton btnNuevo = Theme.createPrimaryButton("➕ Nueva Maquinaria");
        JButton btnImportar = Theme.createSecondaryButton("📥 Importar CSV");

        panelBotones.add(btnNuevo);
        panelBotones.add(btnImportar);

        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(panelBotones, BorderLayout.EAST);

        // Tabla del catálogo
        JPanel tablePanel = Theme.createCard();
        tablePanel.setLayout(new BorderLayout());

        JLabel tableTitle = new JLabel("Catálogo de Maquinaria Disponible");
        tableTitle.setFont(Theme.FONT_HEADING);
        tableTitle.setForeground(Theme.GRAY_TEXT);
        tableTitle.setBorder(new EmptyBorder(0, 0, 12, 0));

        tablaCatalogo = new JTable();
        Theme.styleTable(tablaCatalogo);
        cargarCatalogo();

        JScrollPane scroll = new JScrollPane(tablaCatalogo);
        scroll.setBorder(null);

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scroll, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);

        btnNuevo.addActionListener(e -> {
            // Aquí podrías abrir un diálogo para agregar maquinaria manualmente
            cargarCatalogo();
        });

        btnImportar.addActionListener(e -> importarMaquinariaDesdeCSV());

        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            private void filtrar() {
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(tablaCatalogo.getModel());
                tablaCatalogo.setRowSorter(sorter);
                String texto = txtBuscar.getText().trim();
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
            }
        });

        return panel;
    }

    private void importarMaquinariaDesdeCSV() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                int count = 0, duplicados = 0, errores = 0;
                java.util.Set<String> existentes = new java.util.HashSet<>();
                for (var maq : servicio.listar()) {
                    existentes.add(
                            maq.getDescripcion().trim().toLowerCase() + ";" + maq.getUnidad().trim().toLowerCase());
                }
                boolean primeraLinea = true;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty())
                        continue;
                    String[] parts = line.split(";");
                    if (primeraLinea) {
                        primeraLinea = false;
                        if (parts[1].toLowerCase().contains("descripcion"))
                            continue;
                    }
                    if (parts.length < 3) {
                        errores++;
                        continue;
                    }
                    String descripcion = parts[1].trim();
                    String unidad = parts[2].trim();
                    String precioStr = parts.length > 3 ? parts[3].trim().replace(".", "").replace(",", ".") : "0";
                    double precio;
                    try {
                        precio = Double.parseDouble(precioStr);
                    } catch (NumberFormatException ex) {
                        errores++;
                        continue;
                    }
                    String clave = descripcion.toLowerCase() + ";" + unidad.toLowerCase();
                    if (existentes.contains(clave)) {
                        duplicados++;
                        continue;
                    }
                    servicio.agregar(descripcion, unidad, precio);
                    existentes.add(clave);
                    count++;
                }
                JOptionPane.showMessageDialog(this,
                        "Importados: " + count + " | Duplicados: " + duplicados + " | Errores: " + errores);
                cargarCatalogo();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al importar: " + ex.getMessage());
            }
        }

    }

    private JPanel crearPanelAsignados() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(Theme.CYAN_VERY_LIGHT);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Card con la tabla
        JPanel tablePanel = Theme.createCard();
        tablePanel.setLayout(new BorderLayout());

        JLabel tableTitle = new JLabel("Maquinaria Asignada a esta Actividad");
        tableTitle.setFont(Theme.FONT_HEADING);
        tableTitle.setForeground(Theme.GRAY_TEXT);
        tableTitle.setBorder(new EmptyBorder(0, 0, 12, 0));

        tablaAsignados = new JTable();
        Theme.styleTable(tablaAsignados);
        cargarAsignados();

        JScrollPane scroll = new JScrollPane(tablaAsignados);
        scroll.setBorder(null);

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scroll, BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    public void cargarCatalogo() {
        List<Maquinaria> catalogo = servicio.listar();
        String[] columnas = { "ID", "Descripción", "Unidad", "Precio (Bs)", "Acción" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        for (Maquinaria m : catalogo) {
            modelo.addRow(new Object[] {
                    m.getId(),
                    m.getDescripcion(),
                    m.getUnidad(),
                    String.format("%.2f", m.getPrecioUnitario()),
                    "Añadir"
            });
        }
        tablaCatalogo.setModel(modelo);
        Theme.styleTable(tablaCatalogo);

        tablaCatalogo.getColumn("Acción").setCellRenderer(new ButtonRenderer());
        tablaCatalogo.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox(), (fila) -> {
            int catalogoId = (int) tablaCatalogo.getValueAt(fila, 0);
            String nombre = (String) tablaCatalogo.getValueAt(fila, 1);
            String input = JOptionPane.showInputDialog(this, "Cantidad para: " + nombre);
            if (input != null && !input.isEmpty()) {
                try {
                    double cantidad = Double.parseDouble(input);
                    if (cantidad <= 0) {
                        JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    servicio.agregarMaquinariaAsignada(actividadId, catalogoId, cantidad);
                    cargarAsignados();
                    JOptionPane.showMessageDialog(this, "✓ Maquinaria añadida correctamente", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }));
    }

    private void cargarAsignados() {
        List<dominio.MaquinariaAsignada> asignados = servicio.listarPorActividad(actividadId);
        String[] columnas = { "ID", "Descripción", "Cantidad", "Precio Unitario (Bs)", "Subtotal (Bs)" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        double totalGeneral = 0;
        for (dominio.MaquinariaAsignada m : asignados) {
            double subtotal = m.getCantidad() * m.getPrecioUnitario();
            totalGeneral += subtotal;
            modelo.addRow(new Object[] {
                    m.getId(),
                    m.getDescripcion(),
                    String.format("%.2f", m.getCantidad()),
                    String.format("%.2f", m.getPrecioUnitario()),
                    String.format("%.2f", subtotal)
            });
        }
        tablaAsignados.setModel(modelo);
        Theme.styleTable(tablaAsignados);
    }
}
