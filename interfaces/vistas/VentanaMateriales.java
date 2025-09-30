package interfaces.vistas;

import aplicacion.ServicioMaterial;
import dominio.Material;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class VentanaMateriales extends JFrame {
    private final ServicioMaterial servicioMaterial;
    private final int actividadId;
    private JTable tablaCatalogo, tablaAsignados;

    public VentanaMateriales(ServicioMaterial servicioMaterial, int actividadId) {
        this.servicioMaterial = servicioMaterial;
        this.actividadId = actividadId;

        setTitle("Materiales para Actividad #" + actividadId);
        setSize(850, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Catálogo", crearPanelCatalogo());
        tabs.addTab("Materiales Asignados", crearPanelAsignados());

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> dispose());

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(btnVolver);

        add(tabs, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout());

        // Filtro + Botón Nuevo + Botón Importar
        JPanel topPanel = new JPanel(new BorderLayout());
        JTextField txtBuscar = new JTextField();
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevo = new JButton("Nuevo Material");
        JButton btnImportar = new JButton("Importar Materiales");
        panelBotones.add(btnNuevo);
        panelBotones.add(btnImportar);

        topPanel.add(txtBuscar, BorderLayout.CENTER);
        topPanel.add(panelBotones, BorderLayout.EAST);

        tablaCatalogo = new JTable();
        cargarCatalogo();

        JScrollPane scroll = new JScrollPane(tablaCatalogo);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        // Acción del botón Nuevo
        btnNuevo.addActionListener(e -> {
            new DialogoNuevoMaterial(servicioMaterial, this).setVisible(true);
            cargarCatalogo();
        });

        // Acción del botón Importar
        btnImportar.addActionListener(e -> importarMaterialesDesdeCSV());

        // Búsqueda dinámica
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            public void changedUpdate(DocumentEvent e) {
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

    private void importarMaterialesDesdeCSV() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                int count = 0, duplicados = 0, errores = 0;
                java.util.Set<String> existentes = new java.util.HashSet<>();
                // Cargar materiales existentes para evitar duplicados
                for (var mat : servicioMaterial.listarCatalogo()) {
                    existentes.add(mat.getNombre().trim().toLowerCase() + ";" + mat.getUnidad().trim().toLowerCase());
                }
                boolean primeraLinea = true;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty())
                        continue;
                    String[] parts = line.split(";");
                    if (primeraLinea) { // saltar encabezado
                        primeraLinea = false;
                        if (parts[1].toLowerCase().contains("descripcion"))
                            continue;
                    }
                    if (parts.length < 4) {
                        errores++;
                        continue;
                    }
                    String nombre = parts[1].trim();
                    String unidad = parts[2].trim();
                    String precioStr = parts[3].trim().replace(".", "").replace(",", ".");
                    double precio;
                    try {
                        precio = Double.parseDouble(precioStr);
                    } catch (NumberFormatException ex) {
                        errores++;
                        continue;
                    }
                    String clave = nombre.toLowerCase() + ";" + unidad.toLowerCase();
                    if (existentes.contains(clave)) {
                        duplicados++;
                        continue;
                    }
                    servicioMaterial.crearEnCatalogo(nombre, unidad, precio);
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
        cargarMaterialesAsignados();

        JScrollPane scroll = new JScrollPane(tablaAsignados);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    public void cargarCatalogo() {
        List<Material> catalogo = servicioMaterial.listarCatalogo();
        String[] columnas = { "ID", "Descripción", "Unidad", "Precio", "Acción" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        for (Material m : catalogo) {
            modelo.addRow(new Object[] {
                    m.getId(),
                    m.getNombre(),
                    "-", // Puedes actualizar si hay unidad
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
                    servicioMaterial.agregarMaterial(actividadId, catalogoId, cantidad);
                    cargarMaterialesAsignados();
                    JOptionPane.showMessageDialog(this, "Material añadido correctamente.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Cantidad inválida.");
                }
            }
        }));
    }

    private void cargarMaterialesAsignados() {
        List<Material> materiales = servicioMaterial.listarPorActividad(actividadId);
        String[] columnas = { "ID", "Descripción", "Cantidad", "Precio Unitario", "Subtotal" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Material m : materiales) {
            double subtotal = m.getCantidad() * m.getPrecioUnitario();
            modelo.addRow(new Object[] {
                    m.getId(),
                    m.getNombre(),
                    m.getCantidad(),
                    "$" + m.getPrecioUnitario(),
                    "$" + subtotal
            });
        }

        tablaAsignados.setModel(modelo);
    }
}
