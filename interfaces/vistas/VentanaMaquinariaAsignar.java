// 📦 interfaces/vistas/VentanaMaquinariaAsignar.java
package interfaces.vistas;

import aplicacion.ServicioMaquinaria;
import dominio.Maquinaria;

import javax.swing.*;
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

        setTitle("Maquinaria para Actividad #" + actividadId);
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

    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        JTextField txtBuscar = new JTextField();
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevo = new JButton("Nueva Maquinaria");
        JButton btnImportar = new JButton("Importar Maquinaria");
        panelBotones.add(btnNuevo);
        panelBotones.add(btnImportar);

        topPanel.add(txtBuscar, BorderLayout.CENTER);
        topPanel.add(panelBotones, BorderLayout.EAST);

        tablaCatalogo = new JTable();
        cargarCatalogo();

        JScrollPane scroll = new JScrollPane(tablaCatalogo);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

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
        JPanel panel = new JPanel(new BorderLayout());
        tablaAsignados = new JTable();
        cargarAsignados();
        JScrollPane scroll = new JScrollPane(tablaAsignados);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    public void cargarCatalogo() {
        List<Maquinaria> catalogo = servicio.listar();
        String[] columnas = { "ID", "Descripción", "Unidad", "Precio", "Acción" };
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
                    // Guardar en la base de datos la asignación de maquinaria a la actividad
                    servicio.agregarMaquinariaAsignada(actividadId, catalogoId, cantidad);
                    cargarAsignados();
                    JOptionPane.showMessageDialog(this, "Maquinaria añadida correctamente.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Cantidad inválida.");
                }
            }
        }));
    }

    private void cargarAsignados() {
        List<dominio.MaquinariaAsignada> asignados = servicio.listarPorActividad(actividadId);
        String[] columnas = { "ID", "Descripción", "Cantidad", "Precio Unitario", "Subtotal" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        for (dominio.MaquinariaAsignada m : asignados) {
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
