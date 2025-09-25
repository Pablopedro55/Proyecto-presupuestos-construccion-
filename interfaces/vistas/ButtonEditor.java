package interfaces.vistas;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private Consumer<Integer> onClick;
    private int row;

    public ButtonEditor(JCheckBox checkBox, Consumer<Integer> onClick) {
        super(checkBox);
        this.onClick = onClick;
        button = new JButton("Añadir");
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.row = row;
        label = (value == null) ? "Añadir" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            onClick.accept(row);
        }
        isPushed = false;
        return label;
    }
}
