package werkzeuge.traceabilitywerkzeug;

import material.ClassNodeMaterial;
import material.JavaClassNodeMaterial;
import material.TraceLinkDependencyMaterial;

import javax.swing.*;
import java.awt.*;

public class TraceabilityListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        final TraceLinkDependencyMaterial traceLinkDependencyMaterial = (TraceLinkDependencyMaterial) value;
        ClassNodeMaterial classNode = traceLinkDependencyMaterial.getIndependentClass();
        ClassNodeMaterial otherClassNode = traceLinkDependencyMaterial.getDependentClass();
        double traceLinkValue = traceLinkDependencyMaterial.getTracelinkvalue();
        setText(createString(classNode, otherClassNode, traceLinkValue));
        return this;
    }

    /**
     * Return the String that gets displayed in the ListCell. The String of the JavaClassNodeMaterial should always be on the left side
     * @param classNode
     * @param otherclassNode
     * @param tracelinkValue
     * @return
     */
    private String createString(final ClassNodeMaterial classNode , final ClassNodeMaterial otherclassNode, double tracelinkValue)
    {
        String text = "";
        if(classNode instanceof JavaClassNodeMaterial)
        {
            text = "<html><center>" + classNode.getSimpleClassName() + " <"   +  String.format("%.2f", tracelinkValue) + "> " +otherclassNode.getSimpleClassName() + "</center></html>" ;
        }
        else
        {
            text =  "<html><center>" + otherclassNode.getSimpleClassName() + " <"   + String.format("%.2f", tracelinkValue) + "> " + classNode.getSimpleClassName() + "</center></html>" ;
        }
        return text;
    }
}
