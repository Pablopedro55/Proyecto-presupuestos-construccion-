// 📦 interfaces/vistas/VentanaEjecucionPresupuesto.java
package interfaces.vistas;

import dominio.Actividad;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaEjecucionPresupuesto extends JFrame {
    public VentanaEjecucionPresupuesto(List<Actividad> actividades, double presupuesto) {
        setTitle("Ejecución del Presupuesto");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Calcular total gastado y saldo
        double totalGastado = actividades.stream().mapToDouble(Actividad::getCosto).sum();
        double saldo = presupuesto - totalGastado;

        // Crear tabla con actividades
        String[] columnas = { "ID Actividad", "Descripción", "Costo Ejecutado (Bs)" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        for (Actividad a : actividades) {
            modelo.addRow(new Object[] { a.getId(), a.getDescripcion(), a.getCosto() });
        }

        JTable tabla = new JTable(modelo);
        JScrollPane scrollTabla = new JScrollPane(tabla);

        // Crear etiquetas con resumen
        JLabel lblPresupuesto = new JLabel("Presupuesto Total: Bs " + presupuesto);
        JLabel lblTotalGastado = new JLabel("Total Gastado: Bs " + totalGastado);
        JLabel lblSaldo = new JLabel("Saldo Disponible: Bs " + saldo);

        JPanel panelResumen = new JPanel(new GridLayout(3, 1, 5, 5));
        panelResumen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelResumen.add(lblPresupuesto);
        panelResumen.add(lblTotalGastado);
        panelResumen.add(lblSaldo);

        // Organizar interfaz
        setLayout(new BorderLayout());
        add(scrollTabla, BorderLayout.CENTER);
        add(panelResumen, BorderLayout.SOUTH);
    }
}
