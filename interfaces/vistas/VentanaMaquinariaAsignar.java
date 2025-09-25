
// 📦 interfaces/vistas/VentanaMaquinariaAsignar.java
package interfaces.vistas;

import aplicacion.ServicioMaquinaria;
import dominio.Maquinaria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaMaquinariaAsignar extends JFrame {
    private ServicioMaquinaria servicio;
    private int actividadId;
    private JTable tabla;
    private JButton btnAsignar;

    public VentanaMaquinariaAsignar(ServicioMaquinaria servicio, int actividadId) {
        this.servicio = servicio;
        this.actividadId = actividadId;

        setTitle("Ver Maquinaria");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        tabla = new JTable();
        btnAsignar = new JButton("Cerrar");

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(btnAsignar, BorderLayout.SOUTH);

        btnAsignar.addActionListener(e -> dispose());

        cargarTabla();
    }

    private void cargarTabla() {
        List<Maquinaria> lista = servicio.listar();
        String[] columnas = { "ID", "Descripción", "Unidad", "Precio" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        for (Maquinaria m : lista) {
            modelo.addRow(new Object[] { m.getId(), m.getDescripcion(), m.getUnidad(), m.getPrecioUnitario() });
        }
        tabla.setModel(modelo);
    }
}
