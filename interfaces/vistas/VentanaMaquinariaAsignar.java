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
        JTextField txtBuscar = new JTextField();
        tablaCatalogo = new JTable();
        cargarCatalogo();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(txtBuscar, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);

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
