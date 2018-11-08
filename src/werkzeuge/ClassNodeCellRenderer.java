package werkzeuge;


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

        setText("<html><center><b>"+ classname +"</b><br>"  + marking +"</center></html>" );

        setIcon(StatusIcons.getIconForMarking(marking));
        return this;
    }
}
