package interfaces.vistas;

import aplicacion.*;
import dominio.Actividad;
import infraestructura.configuracion.ConexionBD;
import infraestructura.mysql.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class VentanaActividades extends JFrame {
    private JTable tablaActividades;
    private JLabel lblPresupuestoTotal, lblPresupuestoEjecutado, lblDiferencia;
    private JButton btnNuevaActividad, btnVerDetalle, btnMateriales, btnEjecutar, btnManoObra, btnMaquinaria;
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

    public VentanaActividades(int proyectoId, String nombreProyecto) {
        this.proyectoId = proyectoId;
        this.nombreProyecto = nombreProyecto;

        setTitle("Actividades del Proyecto: " + nombreProyecto);
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tablaActividades = new JTable();
        btnNuevaActividad = new JButton("Agregar Actividad");
        btnVerDetalle = new JButton("Ver Detalle");
        btnMateriales = new JButton("Materiales");
        btnEjecutar = new JButton("Ejecutar Actividad");
        btnManoObra = new JButton("Mano de Obra");
        btnMaquinaria = new JButton("Maquinaria");

        lblPresupuestoTotal = new JLabel();
        lblPresupuestoEjecutado = new JLabel();
        lblDiferencia = new JLabel();

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnNuevaActividad);
        panelBotones.add(btnMateriales);
        panelBotones.add(btnManoObra);
        panelBotones.add(btnMaquinaria);
        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnEjecutar);

        JPanel panelInfo = new JPanel(new GridLayout(1, 3));
        panelInfo.add(lblPresupuestoTotal);
        panelInfo.add(lblPresupuestoEjecutado);
        panelInfo.add(lblDiferencia);

        add(new JScrollPane(tablaActividades), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        add(panelInfo, BorderLayout.NORTH);

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
        btnVerDetalle.addActionListener(e -> abrirDetalle());
        btnMateriales.addActionListener(e -> abrirMateriales());
        btnEjecutar.addActionListener(e -> new VentanaEjecutarActividad(proyectoId, this).setVisible(true));
        btnManoObra.addActionListener(e -> abrirManoObra());
        btnMaquinaria.addActionListener(e -> abrirMaquinaria());

        cargarActividades();
    }

    public void cargarActividades() {
        if (servicio == null)
            return;

        List<Actividad> actividades = servicio.listarPorProyecto(proyectoId);
        String[] columnas = { "ID", "Descripción", "Costo unitario", "Ejecuciones", "Total Ejecutado" };
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
            modelo.addRow(new Object[] { a.getId(), a.getDescripcion(), costoTotal, ejecuciones, total });
            totalEjecutado += total;
        }

        tablaActividades.setModel(modelo);
        tablaActividades.getColumnModel().getColumn(0).setPreferredWidth(50);

        double presupuestoProyecto = obtenerPresupuestoProyecto();
        double diferencia = presupuestoProyecto - totalEjecutado;

        lblPresupuestoTotal.setText("Presupuesto Proyecto: Bs " + String.format("%.2f", presupuestoProyecto));
        lblPresupuestoEjecutado.setText("Total Ejecutado: Bs " + String.format("%.2f", totalEjecutado));
        lblDiferencia.setText("Diferencia: Bs " + String.format("%.2f", diferencia));
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
                double presupuesto = Double.parseDouble(JOptionPane.showInputDialog(
                        this, "Presupuesto del proyecto:", "Presupuesto", JOptionPane.PLAIN_MESSAGE));
                List<Actividad> actividades = servicio.listarPorProyecto(proyectoId);
                new VentanaEjecucionPresupuesto(actividades, presupuesto).setVisible(true);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un número válido.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una actividad para ver ejecución");
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
}
