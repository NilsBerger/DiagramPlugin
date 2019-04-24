package werkzeuge.tracebilitychooserwerkzeug;

import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class TraceabilityListCellRenderer extends DefaultTableCellRenderer {

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        JBPanel panel = new JBPanel();
        panel.setBackground(UIUtil.getTableBackground());
        panel.setBackground(UIUtil.getTableBackground(isSelected));
        if (value instanceof Number) {

            JBLabel label = new JBLabel();
            label.setText(decimalFormat.format(value));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            //label.setForeground(ColorUtils.test((double) value));
            //label.setBackground(ColorUtils.test((double) value));
            label.setBackground(UIUtil.getTableBackground(isSelected));
            //panel.setBackground(ColorUtils.test((double) value));
            if(isSelected)
            {
                panel.setBackground(UIUtil.getTableBackground(isSelected));
            }
            panel.add(label, BorderLayout.CENTER);
            return panel;
        }

        if (value instanceof String) {
            panel.setLayout(new BorderLayout());
            if (getTypes().contains(value)) {
                panel.add(new JBLabel(getIconForType((String) value)), BorderLayout.WEST);
            }
            JBLabel textLabel = new JBLabel((String) value);
            textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            textLabel.setBackground(UIUtil.getTableBackground());
            textLabel.setBackground(UIUtil.getTableBackground(isSelected));
            panel.add(textLabel, BorderLayout.CENTER);
            return panel;
        }
        return this;
    }

    public static Icon getIconForType(String type) {
        if (type.equals("CLASS")) {
            return AllIcons.Nodes.Class;
        }
        if (type.equals("ANONYMOUS_CLASS")) {
            return AllIcons.Nodes.AnonymousClass;
        }
        if (type.equals("ENUM")) {
            return AllIcons.Nodes.Enum;
        }
        if (type.equals("INTERFACE")) {
            return AllIcons.Nodes.Interface;
        }


        return AllIcons.Nodes.Class;
    }

    public static java.util.List<String> getTypes() {
        String[] types = new String[]{"CLASS", "ANONYMOUS_CLASS", "ENUM", "INTERFACE"};
        return new ArrayList<String>(Arrays.asList(types));
    }
}
