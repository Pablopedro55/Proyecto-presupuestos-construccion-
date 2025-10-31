package interfaces.vistas;

import aplicacion.ServicioEjecucion;
import aplicacion.ServicioPresupuesto;
import infraestructura.configuracion.ConexionBD;
import infraestructura.mysql.RepositorioActividadesMySQL;
import infraestructura.mysql.RepositorioEjecucionesMySQL;
import interfaces.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class VentanaEjecutarActividad extends JDialog {
    private JComboBox<String> comboActividades;
    private JButton btnEjecutar;
    private ServicioPresupuesto servicioPresupuesto;
    private ServicioEjecucion servicioEjecucion;
    private int proyectoId;
    private VentanaActividades parent;

    public VentanaEjecutarActividad(int proyectoId, VentanaActividades parent) {
        super((Frame) null, "Ejecutar Actividad", true);
        this.proyectoId = proyectoId;
        this.parent = parent;

        setSize(580, 360);
        setLocationRelativeTo(parent);
        setResizable(false);

        // Panel principal con fondo celeste
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Theme.CYAN_VERY_LIGHT);
        mainPanel.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Título
        JLabel titleLabel = new JLabel("▶️ Ejecutar Actividad");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.BLUE_PRIMARY);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Panel del formulario
        JPanel formPanel = new JPanel(new BorderLayout(0, 12));
        formPanel.setOpaque(false);

        JLabel lblActividad = new JLabel("Seleccione la actividad a ejecutar:");
        lblActividad.setFont(Theme.FONT_NORMAL);
        lblActividad.setForeground(Theme.GRAY_TEXT);

        comboActividades = new JComboBox<>();
        comboActividades.setFont(Theme.FONT_NORMAL);
        comboActividades.setBackground(Theme.WHITE);
        comboActividades.setForeground(Theme.GRAY_TEXT);

        // Crear un panel para el combo con mejor estilo
        JPanel comboPanel = new JPanel(new BorderLayout());
        comboPanel.setOpaque(false);
        comboPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        comboPanel.add(comboActividades, BorderLayout.CENTER);

        formPanel.add(lblActividad, BorderLayout.NORTH);
        formPanel.add(comboPanel, BorderLayout.CENTER);

        // Info adicional
        JLabel infoLabel = new JLabel(
                "<html><i>💡 Se registrará una ejecución y se recalculará el costo según el catálogo actual</i></html>");
        infoLabel.setFont(Theme.FONT_SMALL);
        infoLabel.setForeground(Theme.GRAY_MUTED);
        infoLabel.setBorder(new EmptyBorder(8, 0, 0, 0));
        formPanel.add(infoLabel, BorderLayout.SOUTH);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnCancelar = Theme.createSecondaryButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        btnEjecutar = Theme.createSuccessButton("✓ Ejecutar Actividad");
        btnEjecutar.addActionListener(e -> ejecutarActividad());

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnEjecutar);

        // Ensamblar
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Cargar actividades
        try {
            Connection con = ConexionBD.conectar();
            servicioPresupuesto = new ServicioPresupuesto(new RepositorioActividadesMySQL(con));
            servicioEjecucion = new ServicioEjecucion(new RepositorioEjecucionesMySQL(con));

            List<dominio.Actividad> actividades = servicioPresupuesto.listarPorProyecto(proyectoId);

            if (actividades.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay actividades disponibles para ejecutar", "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                return;
            }

            for (dominio.Actividad a : actividades) {
                comboActividades.addItem(a.getId() + " - " + a.getDescripcion());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando actividades: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void ejecutarActividad() {
        String seleccion = (String) comboActividades.getSelectedItem();
        if (seleccion != null && seleccion.contains("-")) {
            int actividadId = Integer.parseInt(seleccion.split("-")[0].trim());

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de ejecutar esta actividad?",
                    "Confirmar Ejecución",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            try {
                // Recalcular el costo de la actividad usando los precios actuales del catálogo
                Connection con = ConexionBD.conectar();
                RepositorioActividadesMySQL repoActividades = new RepositorioActividadesMySQL(con);
                repoActividades.calcularCostoActividad(actividadId);

                servicioEjecucion.registrar(actividadId);

                JOptionPane.showMessageDialog(this, "✓ Actividad ejecutada exitosamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                parent.cargarActividades();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al ejecutar: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una actividad válida", "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
