package interfaces.theme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Clase centralizada para el tema visual de la aplicación
 * Basado en el diseño generado por Figma
 */
public class Theme {

    // ============= COLORES PRINCIPALES =============
    public static final Color BLUE_PRIMARY = new Color(30, 58, 138); // #1E3A8A
    public static final Color BLUE_SECONDARY = new Color(59, 130, 246); // #3B82F6
    public static final Color CYAN_LIGHT = new Color(147, 197, 253); // #93C5FD
    public static final Color CYAN_VERY_LIGHT = new Color(219, 234, 254); // #DBEAFE

    // ============= COLORES DE ESTADO =============
    public static final Color SUCCESS = new Color(16, 185, 129); // #10B981
    public static final Color WARNING = new Color(245, 158, 11); // #F59E0B
    public static final Color DANGER = new Color(239, 68, 68); // #EF4444

    // ============= COLORES NEUTROS =============
    public static final Color WHITE = Color.WHITE; // #FFFFFF
    public static final Color GRAY_TEXT = new Color(55, 65, 81); // #374151
    public static final Color GRAY_BORDER = new Color(229, 231, 235); // #E5E7EB
    public static final Color GRAY_MUTED = new Color(107, 114, 128); // #6B7280

    // ============= TIPOGRAFÍA =============
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_LARGE = new Font("Segoe UI", Font.BOLD, 32);

    // ============= DIMENSIONES Y ESPACIADO =============
    public static final int PADDING = 20;
    public static final int MARGIN = 16;
    public static final int BORDER_RADIUS = 8;

    // ============= MÉTODOS PARA CREAR COMPONENTES ESTILIZADOS =============

    /**
     * Crea un botón primario con estilo
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BLUE_PRIMARY);
        button.setForeground(WHITE);
        button.setFont(FONT_NORMAL);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BLUE_SECONDARY);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BLUE_PRIMARY);
            }
        });

        return button;
    }

    /**
     * Crea un botón secundario con estilo
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(WHITE);
        button.setForeground(GRAY_TEXT);
        button.setFont(FONT_NORMAL);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRAY_BORDER, 1),
                BorderFactory.createEmptyBorder(9, 19, 9, 19)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(CYAN_VERY_LIGHT);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(WHITE);
            }
        });

        return button;
    }

    /**
     * Crea un botón de acción (verde)
     */
    public static JButton createSuccessButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SUCCESS);
        button.setForeground(WHITE);
        button.setFont(FONT_NORMAL);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        return button;
    }

    /**
     * Crea un botón de peligro (rojo)
     */
    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(DANGER);
        button.setForeground(WHITE);
        button.setFont(FONT_NORMAL);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        return button;
    }

    /**
     * Crea un panel con estilo de tarjeta (Card)
     */
    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(GRAY_BORDER, 1, true),
                BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)));
        return card;
    }

    /**
     * Crea un panel de estadísticas (KPI Card)
     */
    public static JPanel createStatCard(String title, String value, Color valueColor) {
        JPanel card = createCard();
        card.setLayout(new BorderLayout(0, 10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_SMALL);
        titleLabel.setForeground(GRAY_MUTED);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(FONT_LARGE);
        valueLabel.setForeground(valueColor);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Crea un campo de texto estilizado
     */
    public static JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setFont(FONT_NORMAL);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRAY_BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        // Placeholder
        textField.setText(placeholder);
        textField.setForeground(GRAY_MUTED);

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(GRAY_TEXT);
                }
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BLUE_SECONDARY, 2),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(GRAY_MUTED);
                }
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GRAY_BORDER, 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            }
        });

        return textField;
    }

    /**
     * Aplica estilo a una tabla
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_NORMAL);
        table.setRowHeight(40);
        table.setGridColor(GRAY_BORDER);
        table.setSelectionBackground(CYAN_LIGHT);
        table.setSelectionForeground(GRAY_TEXT);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);

        // Header
        table.getTableHeader().setBackground(CYAN_VERY_LIGHT);
        table.getTableHeader().setForeground(GRAY_TEXT);
        table.getTableHeader().setFont(FONT_HEADING);
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(GRAY_BORDER));

        // Renderer para filas alternadas
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(WHITE);
                    } else {
                        c.setBackground(CYAN_VERY_LIGHT);
                    }
                }

                return c;
            }
        });
    }

    /**
     * Crea un botón de icono pequeño
     */
    public static JButton createIconButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setForeground(color);
        button.setFont(FONT_NORMAL);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setContentAreaFilled(true);
                button.setBackground(CYAN_VERY_LIGHT);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setContentAreaFilled(false);
            }
        });

        return button;
    }
}
