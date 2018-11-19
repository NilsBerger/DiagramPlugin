package werkzeuge;


import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.UIUtil;
import materials.ClassNode;
import valueobjects.Marking;

import javax.swing.*;
import java.awt.*;



public class ClassNodeCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        final ClassNode classnode = (ClassNode) value;
        final String classname = classnode.getSimpleClassName();
        Marking marking = classnode.getMarking();

        JPanel panel = new JBPanel<>();
        panel.setLayout(new BorderLayout());
        JLabel text = new JLabel();
        text.setText("<html><center><b>"+ classname +"</b><br>"  + marking +"</center></html>" );
        text.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(new JLabel(StatusIcons.getIconForMarking(marking), JLabel.LEFT), BorderLayout.WEST);
        panel.add(text, BorderLayout.CENTER);
        panel.setBackground(UIUtil.getListBackground(cellHasFocus));
        //add(panel);
        //setText("<html><center><b>"+ classname +"</b><br>"  + marking +"</center></html>" );

        return panel;
    }
}
