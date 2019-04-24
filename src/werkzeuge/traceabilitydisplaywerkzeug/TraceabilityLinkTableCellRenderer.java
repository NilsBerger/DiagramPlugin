package werkzeuge.traceabilitydisplaywerkzeug;

import colorspectrum.ColorUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

public class TraceabilityLinkTableCellRenderer extends DefaultTableCellRenderer {

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        final double tracelinkValue = (double) value;
        final Color color = ColorUtils.test(tracelinkValue);
        setText(decimalFormat.format(value));
        setHorizontalAlignment(SwingConstants.CENTER);
        //setForeground(color);

        return this;
    }
}
