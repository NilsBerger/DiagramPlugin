package werkzeuge;

import valueobjects.Marking;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ClassNodeTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        final Marking marking = (Marking) value;
        if (marking != null) {
            setIcon(StatusIcons.getIconForMarking(marking));
            table.setRowHeight(getIcon().getIconHeight() + 5);
        }
        return this;
    }
}
