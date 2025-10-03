// 📦 interfaces/vistas/VentanaManoObraAsignar.java
package interfaces.vistas;

import aplicacion.ServicioManoObra;
import dominio.ManoObra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class VentanaManoObraAsignar extends JFrame {
    private final ServicioManoObra servicio;
    private final int actividadId;
    private JTable tablaCatalogo, tablaAsignados;

    public VentanaManoObraAsignar(ServicioManoObra servicio, int actividadId) {
        this.servicio = servicio;
        this.actividadId = actividadId;

        setTitle("Mano de Obra para Actividad #" + actividadId);
        setSize(850, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Catálogo", crearPanelCatalogo());
        tabs.addTab("Asignados", crearPanelAsignados());
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> dispose());

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(btnVolver);

        add(tabs, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void importarManoObraDesdeCSV() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                int count = 0, duplicados = 0, errores = 0;
                java.util.Set<String> existentes = new java.util.HashSet<>();
                for (var mo : servicio.listar()) {
                    existentes
                            .add(mo.getDescripcion().trim().toLowerCase() + ";" + mo.getUnidad().trim().toLowerCase());
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

    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextField txtBuscar = new JTextField();
        tablaCatalogo = new JTable();
        cargarCatalogo();

        // Panel superior con búsqueda y botones de acción
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(txtBuscar, BorderLayout.CENTER);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevo = new JButton("Nueva Mano de Obra");
        JButton btnImportar = new JButton("Importar Mano de Obra");
        panelBotones.add(btnNuevo);
        panelBotones.add(btnImportar);
        topPanel.add(panelBotones, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        btnNuevo.addActionListener(e -> {
            new DialogoNuevaManoObra(servicio, this).setVisible(true);
            cargarCatalogo();
        });
        btnImportar.addActionListener(e -> importarManoObraDesdeCSV());

        JScrollPane scroll = new JScrollPane(tablaCatalogo);
        panel.add(scroll, BorderLayout.CENTER);

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

    private JPanel crearPanelAsignados() {
        JPanel panel = new JPanel(new BorderLayout());
        tablaAsignados = new JTable();
        cargarAsignados();
        JScrollPane scroll = new JScrollPane(tablaAsignados);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    public void cargarCatalogo() {
        List<ManoObra> catalogo = servicio.listar();
        String[] columnas = { "ID", "Descripción", "Unidad", "Precio", "Acción" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        for (ManoObra m : catalogo) {
            modelo.addRow(new Object[] {
                    m.getId(),
                    m.getDescripcion(),
                    m.getUnidad(),
                    "$" + m.getPrecioUnitario(),
                    "Añadir"
            });
        }
        tablaCatalogo.setModel(modelo);
        tablaCatalogo.getColumn("Acción").setCellRenderer(new ButtonRenderer());
        tablaCatalogo.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox(), (fila) -> {
            int catalogoId = (int) tablaCatalogo.getValueAt(fila, 0);
            String nombre = (String) tablaCatalogo.getValueAt(fila, 1);
            String input = JOptionPane.showInputDialog(this, "Cantidad para: " + nombre);
            if (input != null && !input.isEmpty()) {
                try {
                    double cantidad = Double.parseDouble(input);
                    // Guardar en la base de datos la asignación de mano de obra a la actividad
                    servicio.agregarManoObraAsignada(actividadId, catalogoId, cantidad);
                    cargarAsignados();
                    JOptionPane.showMessageDialog(this, "Mano de obra añadida correctamente.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Cantidad inválida.");
                }
            }
        }));
    }

    private void cargarAsignados() {
        List<dominio.ManoObraAsignada> asignados = servicio.listarPorActividad(actividadId);
        String[] columnas = { "ID", "Descripción", "Cantidad", "Precio Unitario", "Subtotal" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        for (dominio.ManoObraAsignada m : asignados) {
            double subtotal = m.getCantidad() * m.getPrecioUnitario();
            modelo.addRow(new Object[] {
                    m.getId(),
                    m.getDescripcion(),
                    m.getCantidad(),
                    "$" + m.getPrecioUnitario(),
                    "$" + subtotal
            });
        }
        tablaAsignados.setModel(modelo);
    }
}
