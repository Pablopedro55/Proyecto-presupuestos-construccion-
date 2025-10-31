// 📦 interfaces/vistas/VentanaDetalleActividad.java
package interfaces.vistas;

import interfaces.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaDetalleActividad extends JFrame {
    public VentanaDetalleActividad(String descripcion, double costo) {
        setTitle("Detalle de Actividad");
        setSize(600, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

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
        infoCard.setLayout(new BorderLayout(0, 20));
        infoCard.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Descripción
        JPanel descripcionPanel = new JPanel(new BorderLayout(0, 8));
        descripcionPanel.setOpaque(false);

        JLabel lblDescripcionTitle = new JLabel("Descripción:");
        lblDescripcionTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDescripcionTitle.setForeground(Theme.GRAY_TEXT);

        JLabel lblDescripcion = new JLabel("<html><p style='width: 480px;'>" + descripcion + "</p></html>");
        lblDescripcion.setFont(Theme.FONT_NORMAL);
        lblDescripcion.setForeground(Theme.GRAY_TEXT);

        descripcionPanel.add(lblDescripcionTitle, BorderLayout.NORTH);
        descripcionPanel.add(lblDescripcion, BorderLayout.CENTER);

        // Costo
        JPanel costoPanel = new JPanel(new BorderLayout(0, 8));
        costoPanel.setOpaque(false);

        JLabel lblCostoTitle = new JLabel("Costo Total:");
        lblCostoTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCostoTitle.setForeground(Theme.GRAY_TEXT);

        JLabel lblCosto = new JLabel(String.format("Bs %.2f", costo));
        lblCosto.setFont(Theme.FONT_LARGE);
        lblCosto.setForeground(Theme.BLUE_PRIMARY);

        costoPanel.add(lblCostoTitle, BorderLayout.NORTH);
        costoPanel.add(lblCosto, BorderLayout.CENTER);

        // Agregar a la card
        JPanel infoContainer = new JPanel(new GridLayout(2, 1, 0, 16));
        infoContainer.setOpaque(false);
        infoContainer.add(descripcionPanel);
        infoContainer.add(costoPanel);

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
}