// 📦 interfaces/vistas/VentanaEjecucionPresupuesto.java
package interfaces.vistas;

import dominio.Actividad;
import interfaces.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaEjecucionPresupuesto extends JFrame {
    public VentanaEjecucionPresupuesto(List<Actividad> actividades, double presupuesto) {
        // Configuración ventana
        setTitle("Ejecución del Presupuesto");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Cálculos
        double totalGastado = actividades.stream().mapToDouble(Actividad::getCosto).sum();
        double saldo = presupuesto - totalGastado;

        // Layout principal
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Theme.CYAN_VERY_LIGHT);
        mainPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BLUE_PRIMARY);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitulo = new JLabel("📊 Ejecución del Presupuesto");
        lblTitulo.setFont(Theme.FONT_TITLE);
        lblTitulo.setForeground(Theme.WHITE);
        header.add(lblTitulo, BorderLayout.WEST);

        mainPanel.add(header, BorderLayout.NORTH);

        // PANEL CENTRAL
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(Theme.CYAN_VERY_LIGHT);
        centerPanel.setBorder(new EmptyBorder(20, 30, 0, 30));

        // STAT CARDS
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(Theme.CYAN_VERY_LIGHT);

        // Card 1: Presupuesto Total
        JPanel cardPresupuesto = Theme.createStatCard(
                "💼 Presupuesto Total",
                String.format("Bs %.2f", presupuesto),
                Theme.BLUE_PRIMARY);
        statsPanel.add(cardPresupuesto);

        // Card 2: Total Ejecutado
        JPanel cardGastado = Theme.createStatCard(
                "💸 Total Ejecutado",
                String.format("Bs %.2f", totalGastado),
                Theme.SUCCESS);
        statsPanel.add(cardGastado);

        // Card 3: Saldo Disponible
        Color colorSaldo = saldo >= 0 ? Theme.SUCCESS : Theme.DANGER;
        JPanel cardSaldo = Theme.createStatCard(
                "💰 Saldo Disponible",
                String.format("Bs %.2f", saldo),
                colorSaldo);
        statsPanel.add(cardSaldo);

        centerPanel.add(statsPanel, BorderLayout.NORTH);

        // TABLA DE ACTIVIDADES
        JPanel tableCard = Theme.createCard();
        tableCard.setLayout(new BorderLayout(0, 10));
        tableCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblActividades = new JLabel("📋 Actividades Ejecutadas");
        lblActividades.setFont(Theme.FONT_SUBTITLE);
        lblActividades.setForeground(Theme.BLUE_PRIMARY);
        tableCard.add(lblActividades, BorderLayout.NORTH);

        String[] columnas = { "ID Actividad", "Descripción", "Costo Ejecutado (Bs)" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Cargar actividades
        for (Actividad a : actividades) {
            modelo.addRow(new Object[] {
                    a.getId(),
                    a.getDescripcion(),
                    String.format("%.2f", a.getCosto())
            });
        }

        JTable tabla = new JTable(modelo);
        Theme.styleTable(tabla);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.GRAY_BORDER, 1));
        tableCard.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(tableCard, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // BOTÓN CERRAR
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottomPanel.setBackground(Theme.CYAN_VERY_LIGHT);
        bottomPanel.setBorder(new EmptyBorder(0, 30, 0, 30));

        JButton btnCerrar = Theme.createSecondaryButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        bottomPanel.add(btnCerrar);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }
}
