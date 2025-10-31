// 📦 interfaces/vistas/VentanaDetalleActividad.java
package interfaces.vistas;

import interfaces.theme.Theme;
import infraestructura.configuracion.ConexionBD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VentanaDetalleActividad extends JFrame {
    public VentanaDetalleActividad(int actividadId, String descripcion, double costoTotal) {
        setTitle("Detalle de Actividad");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Obtener costos desglosados
        double costoMateriales = obtenerCostoMateriales(actividadId);
        double costoManoObra = obtenerCostoManoObra(actividadId);
        double costoMaquinaria = obtenerCostoMaquinaria(actividadId);

        // Calcular costo unitario (costo de una sola ejecución)
        double costoUnitario = costoMateriales + costoManoObra + costoMaquinaria;

        // Obtener número de ejecuciones
        int numEjecuciones = obtenerNumeroEjecuciones(actividadId);

        // Calcular costo total (unitario × ejecuciones)
        double costoTotalEjecutado = costoUnitario * numEjecuciones;

        // Debug
        System.out.println("=== DEBUG Detalle Actividad ===");
        System.out.println("Actividad ID: " + actividadId);
        System.out.println("Costo Materiales: " + costoMateriales);
        System.out.println("Costo Mano de Obra: " + costoManoObra);
        System.out.println("Costo Maquinaria: " + costoMaquinaria);
        System.out.println("Costo Unitario: " + costoUnitario);
        System.out.println("Número de Ejecuciones: " + numEjecuciones);
        System.out.println("Costo Total Ejecutado: " + costoTotalEjecutado);
        System.out.println("==============================");

        // Panel principal con fondo celeste
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Theme.CYAN_VERY_LIGHT);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BLUE_PRIMARY);
        header.setBorder(new EmptyBorder(16, 32, 16, 32));

        JLabel titleLabel = new JLabel("👁️ Detalle de la Actividad");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.WHITE);

        header.add(titleLabel, BorderLayout.WEST);

        // Contenido
        JPanel contentPanel = new JPanel(new BorderLayout(0, 16));
        contentPanel.setBackground(Theme.CYAN_VERY_LIGHT);
        contentPanel.setBorder(new EmptyBorder(24, 32, 24, 32));

        // Card con información
        JPanel infoCard = Theme.createCard();
        infoCard.setLayout(new BorderLayout(0, 16));
        infoCard.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Descripción
        JPanel descripcionPanel = new JPanel(new BorderLayout(0, 8));
        descripcionPanel.setOpaque(false);

        JLabel lblDescripcionTitle = new JLabel("📋 Descripción");
        lblDescripcionTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDescripcionTitle.setForeground(Theme.GRAY_MUTED);

        JTextArea txtDescripcion = new JTextArea(descripcion);
        txtDescripcion.setFont(Theme.FONT_NORMAL);
        txtDescripcion.setForeground(Theme.GRAY_TEXT);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setBorder(null);

        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setBorder(null);
        scrollDescripcion.setOpaque(false);
        scrollDescripcion.getViewport().setOpaque(false);
        scrollDescripcion.setPreferredSize(new Dimension(550, 80));

        descripcionPanel.add(lblDescripcionTitle, BorderLayout.NORTH);
        descripcionPanel.add(scrollDescripcion, BorderLayout.CENTER);

        // Panel con 3 stats cards (Costo Unitario, Ejecuciones, Costo Total)
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        statsPanel.setOpaque(false);

        // Card 1: Costo Unitario
        JPanel costUnitCard = new JPanel(new BorderLayout(0, 8));
        costUnitCard.setOpaque(false);

        JLabel lblCostoUnitTitle = new JLabel("� Costo Unitario");
        lblCostoUnitTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCostoUnitTitle.setForeground(Theme.GRAY_MUTED);

        JLabel lblCostoUnit = new JLabel(String.format("Bs %,.2f", costoUnitario));
        lblCostoUnit.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCostoUnit.setForeground(Theme.BLUE_SECONDARY);

        costUnitCard.add(lblCostoUnitTitle, BorderLayout.NORTH);
        costUnitCard.add(lblCostoUnit, BorderLayout.CENTER);

        // Card 2: Ejecuciones
        JPanel execCard = new JPanel(new BorderLayout(0, 8));
        execCard.setOpaque(false);

        JLabel lblExecTitle = new JLabel("🔄 Ejecuciones");
        lblExecTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblExecTitle.setForeground(Theme.GRAY_MUTED);

        JLabel lblExec = new JLabel(String.valueOf(numEjecuciones));
        lblExec.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblExec.setForeground(Theme.SUCCESS);

        execCard.add(lblExecTitle, BorderLayout.NORTH);
        execCard.add(lblExec, BorderLayout.CENTER);

        // Card 3: Costo Total
        JPanel costTotalCard = new JPanel(new BorderLayout(0, 8));
        costTotalCard.setOpaque(false);

        JLabel lblCostoTotalTitle = new JLabel("� Costo Total");
        lblCostoTotalTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCostoTotalTitle.setForeground(Theme.GRAY_MUTED);

        JLabel lblCostoTotal = new JLabel(String.format("Bs %,.2f", costoTotalEjecutado));
        lblCostoTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCostoTotal.setForeground(Theme.BLUE_PRIMARY);

        costTotalCard.add(lblCostoTotalTitle, BorderLayout.NORTH);
        costTotalCard.add(lblCostoTotal, BorderLayout.CENTER);

        statsPanel.add(costUnitCard);
        statsPanel.add(execCard);
        statsPanel.add(costTotalCard);

        // Panel de distribución de costos
        JPanel costBreakdownPanel = new JPanel(new BorderLayout(0, 12));
        costBreakdownPanel.setOpaque(false);

        JLabel lblBreakdownTitle = new JLabel("📊 Distribución de Costos");
        lblBreakdownTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBreakdownTitle.setForeground(Theme.GRAY_MUTED);

        // Gráfico de pie (simulado con barras horizontales)
        JPanel chartPanel = new JPanel();
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setOpaque(false);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1),
                new EmptyBorder(16, 16, 16, 16)));

        // Calcular el total real sumando los costos obtenidos de SQL
        double totalParaGrafico = costoMateriales + costoManoObra + costoMaquinaria;
        if (totalParaGrafico == 0)
            totalParaGrafico = 1; // Evitar división por cero

        System.out.println("Total para gráfico (suma de costos): " + totalParaGrafico);

        // SIEMPRE mostrar las 4 categorías de barras
        System.out.println("\n=== AGREGANDO BARRAS AL PANEL ===");

        // Barra Materiales
        JPanel materialRow = crearBarraCosto("Materiales", costoMateriales, totalParaGrafico, Theme.BLUE_SECONDARY);
        chartPanel.add(materialRow);
        chartPanel.add(Box.createVerticalStrut(12));
        System.out.println("✓ Barra Materiales agregada");

        // Barra Mano de Obra
        JPanel laborRow = crearBarraCosto("Mano de Obra", costoManoObra, totalParaGrafico, Theme.SUCCESS);
        chartPanel.add(laborRow);
        chartPanel.add(Box.createVerticalStrut(12));
        System.out.println("✓ Barra Mano de Obra agregada");

        // Barra Maquinaria
        JPanel machineryRow = crearBarraCosto("Maquinaria", costoMaquinaria, totalParaGrafico, Theme.WARNING);
        chartPanel.add(machineryRow);
        System.out.println("✓ Barra Maquinaria agregada");

        // Desglose de costos - Mostrar solo 3 categorías
        JPanel breakdownList = new JPanel(new GridLayout(3, 2, 8, 8));
        breakdownList.setOpaque(false);
        breakdownList.setBorder(new EmptyBorder(8, 0, 0, 0));

        // Materiales
        JLabel lblMateriales = new JLabel("💎 Materiales:");
        lblMateriales.setFont(Theme.FONT_NORMAL);
        lblMateriales.setForeground(Theme.GRAY_TEXT);
        JLabel lblMaterialesVal = new JLabel(String.format("Bs %,.2f", costoMateriales));
        lblMaterialesVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMaterialesVal.setForeground(Theme.BLUE_SECONDARY);
        lblMaterialesVal.setHorizontalAlignment(SwingConstants.RIGHT);

        // Mano de Obra
        JLabel lblManoObra = new JLabel("👷 Mano de Obra:");
        lblManoObra.setFont(Theme.FONT_NORMAL);
        lblManoObra.setForeground(Theme.GRAY_TEXT);
        JLabel lblManoObraVal = new JLabel(String.format("Bs %,.2f", costoManoObra));
        lblManoObraVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblManoObraVal.setForeground(Theme.SUCCESS);
        lblManoObraVal.setHorizontalAlignment(SwingConstants.RIGHT);

        // Maquinaria
        JLabel lblMaquinaria = new JLabel("🚜 Maquinaria:");
        lblMaquinaria.setFont(Theme.FONT_NORMAL);
        lblMaquinaria.setForeground(Theme.GRAY_TEXT);
        JLabel lblMaquinariaVal = new JLabel(String.format("Bs %,.2f", costoMaquinaria));
        lblMaquinariaVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMaquinariaVal.setForeground(Theme.WARNING);
        lblMaquinariaVal.setHorizontalAlignment(SwingConstants.RIGHT);

        breakdownList.add(lblMateriales);
        breakdownList.add(lblMaterialesVal);
        breakdownList.add(lblManoObra);
        breakdownList.add(lblManoObraVal);
        breakdownList.add(lblMaquinaria);
        breakdownList.add(lblMaquinariaVal);

        costBreakdownPanel.add(lblBreakdownTitle, BorderLayout.NORTH);

        JPanel chartContainer = new JPanel();
        chartContainer.setLayout(new BoxLayout(chartContainer, BoxLayout.Y_AXIS));
        chartContainer.setOpaque(false);
        chartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        breakdownList.setAlignmentX(Component.LEFT_ALIGNMENT);
        chartContainer.add(chartPanel);
        chartContainer.add(Box.createVerticalStrut(12));
        chartContainer.add(breakdownList);

        costBreakdownPanel.add(chartContainer, BorderLayout.CENTER);

        // Agregar a la card
        JPanel infoContainer = new JPanel();
        infoContainer.setLayout(new BoxLayout(infoContainer, BoxLayout.Y_AXIS));
        infoContainer.setOpaque(false);

        descripcionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        costBreakdownPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoContainer.add(descripcionPanel);
        infoContainer.add(Box.createVerticalStrut(16));
        infoContainer.add(statsPanel);
        infoContainer.add(Box.createVerticalStrut(16));
        infoContainer.add(costBreakdownPanel);

        infoCard.add(infoContainer, BorderLayout.CENTER);

        contentPanel.add(infoCard, BorderLayout.CENTER);

        // Botón cerrar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);

        JButton btnCerrar = Theme.createSecondaryButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        buttonPanel.add(btnCerrar);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Ensamblar
        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // Método para crear barra de costo visual
    private JPanel crearBarraCosto(String nombre, double valor, double total, Color color) {
        JPanel row = new JPanel(new BorderLayout(8, 4));
        row.setOpaque(false);

        // Calcular porcentaje
        double porcentaje = (total > 0) ? (valor / total * 100) : 0;

        // Debug
        System.out.println("  Barra: " + nombre + " | Valor: " + valor + " | Total: " + total + " | Porcentaje: "
                + porcentaje + "%");

        // Nombre y porcentaje
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setOpaque(false);

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(Theme.FONT_NORMAL);
        lblNombre.setForeground(Theme.GRAY_TEXT);

        JLabel lblPorcentaje = new JLabel(String.format("%.1f%%", porcentaje));
        lblPorcentaje.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPorcentaje.setForeground(color);

        labelPanel.add(lblNombre, BorderLayout.WEST);
        labelPanel.add(lblPorcentaje, BorderLayout.EAST);

        // Barra de progreso
        JProgressBar barra = new JProgressBar(0, 100);
        int valorBarra = (int) Math.round(porcentaje);
        barra.setValue(valorBarra);
        barra.setForeground(color);
        barra.setBackground(Theme.CYAN_VERY_LIGHT);
        barra.setBorder(BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1));
        barra.setPreferredSize(new Dimension(400, 20));
        barra.setStringPainted(false); // No mostrar texto dentro de la barra

        System.out.println("  -> setValue: " + valorBarra);

        row.add(labelPanel, BorderLayout.NORTH);
        row.add(barra, BorderLayout.CENTER);

        return row;
    }

    // Método para obtener costo de materiales
    private double obtenerCostoMateriales(int actividadId) {
        try {
            Connection con = ConexionBD.conectar();
            String sql = "SELECT COALESCE(SUM(m.cantidad * c.precio_unitario), 0) as total " +
                    "FROM materiales m " +
                    "JOIN catalogo_materiales c ON m.catalogo_id = c.id " +
                    "WHERE m.actividad_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, actividadId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double result = rs.getDouble("total");
                System.out.println("  > Costo Materiales SQL: " + result);
                return result;
            }
        } catch (Exception e) {
            System.err.println("  > ERROR en obtenerCostoMateriales: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Método para obtener costo de mano de obra
    private double obtenerCostoManoObra(int actividadId) {
        try {
            Connection con = ConexionBD.conectar();
            String sql = "SELECT COALESCE(SUM(ma.cantidad * c.precio_unitario), 0) as total " +
                    "FROM mano_obra_asignada ma " +
                    "JOIN catalogo_mano_obra c ON ma.catalogo_id = c.id " +
                    "WHERE ma.actividad_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, actividadId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double result = rs.getDouble("total");
                System.out.println("  > Costo Mano de Obra SQL: " + result);
                return result;
            }
        } catch (Exception e) {
            System.err.println("  > ERROR en obtenerCostoManoObra: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Método para obtener costo de maquinaria
    private double obtenerCostoMaquinaria(int actividadId) {
        try {
            Connection con = ConexionBD.conectar();
            String sql = "SELECT COALESCE(SUM(ma.cantidad * c.precio_unitario), 0) as total " +
                    "FROM maquinaria_asignada ma " +
                    "JOIN catalogo_maquinaria c ON ma.catalogo_id = c.id " +
                    "WHERE ma.actividad_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, actividadId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double result = rs.getDouble("total");
                System.out.println("  > Costo Maquinaria SQL: " + result);
                return result;
            }
        } catch (Exception e) {
            System.err.println("  > ERROR en obtenerCostoMaquinaria: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Método para obtener número de ejecuciones
    private int obtenerNumeroEjecuciones(int actividadId) {
        try {
            Connection con = ConexionBD.conectar();
            String sql = "SELECT COUNT(*) as total FROM ejecuciones WHERE actividad_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, actividadId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int result = rs.getInt("total");
                System.out.println("  > Número de Ejecuciones SQL: " + result);
                return result;
            }
        } catch (Exception e) {
            System.err.println("  > ERROR en obtenerNumeroEjecuciones: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}