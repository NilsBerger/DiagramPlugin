package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.renderer.BasicGraphNodeRenderer;
import com.intellij.openapi.util.ModificationTracker;
import materials.ClassDependency;
import materials.ClassNode;
import werkzeuge.StatusIcons;

import javax.swing.*;

public class ClassNodeRenderer extends BasicGraphNodeRenderer<ClassNode, ClassDependency> {
    public ClassNodeRenderer(GraphBuilder<ClassNode, ClassDependency> builder, ModificationTracker tracker) {
        super(builder, tracker);
    }

    @Override
    protected Icon getIcon(ClassNode node) {
        return StatusIcons.getIconForMarking(node.getMarking());
    }

    @Override
    protected String getNodeName(ClassNode node) {
        return node.getSimpleClassName();
    }

    //    @Override
//    protected Icon getIcon(Object node) {
//        return super.getIcon(node);
//    }
//
//    @Override
//    protected String getNodeName(Object node) {
//        return super.getNodeName(node);
//    }
//
//    @Override
//    protected Color getSelectionColor() {
//        return new Color(199,99,199);
//    }
}
