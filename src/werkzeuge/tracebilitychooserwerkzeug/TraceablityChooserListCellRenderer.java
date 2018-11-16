package werkzeuge.tracebilitychooserwerkzeug;

import com.intellij.ui.components.JBLabel;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityPointer;
import werkzeuge.StatusIcons;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import com.intellij.util.ui.UIUtil;

public class TraceablityChooserListCellRenderer implements ListCellRenderer<TraceabilityLink> {

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public TraceablityChooserListCellRenderer(){
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends TraceabilityLink> list, TraceabilityLink value, int index, boolean isSelected, boolean cellHasFocus) {
        TraceabilityPointer pointer = value.getTarget();

        Double probability = value.getProbability();
        JBLabel probabilityLabel = new JBLabel(decimalFormat.format(probability));

        JBLabel linkLabel = new JBLabel(pointer.getDisplayName());
        linkLabel.setIcon(StatusIcons.getBlankIcon());

        JBLabel fileInfoLabel = new JBLabel(getPointerInfo(pointer));
        fileInfoLabel.setFontColor(UIUtil.FontColor.BRIGHTER);

        JPanel cellPanel = new JPanel(new BorderLayout(5, 5));
        cellPanel.add(probabilityLabel, BorderLayout.WEST);
        cellPanel.add(linkLabel, BorderLayout.CENTER);
        cellPanel.add(fileInfoLabel, BorderLayout.EAST);

        cellPanel.setBackground(UIUtil.getListBackground(cellHasFocus));

        return cellPanel;
    }

    private String getPointerInfo(TraceabilityPointer pointer) {

        String sourceFilePath = pointer.getSourceFilePath();
        String info = sourceFilePath.substring(sourceFilePath.lastIndexOf(File.separator) + 1, sourceFilePath.indexOf("."));
        return info;
    }
}
