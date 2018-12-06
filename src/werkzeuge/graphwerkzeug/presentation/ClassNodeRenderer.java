package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.renderer.BasicGraphNodeRenderer;
import com.intellij.openapi.graph.view.Graph2DView;
import com.intellij.openapi.graph.view.NodeLabel;
import com.intellij.openapi.graph.view.NodeRealizer;
import com.intellij.openapi.util.ModificationTracker;
import materials.ClassDependency;
import materials.ClassNode;
import werkzeuge.StatusIcons;

import javax.swing.*;

public class ClassNodeRenderer extends BasicGraphNodeRenderer<ClassNode, ClassDependency> {
    private String _nodeName = "";
    public ClassNodeRenderer(GraphBuilder<ClassNode, ClassDependency> builder, ModificationTracker tracker) {
        super(builder, tracker);
    }

    @Override
    protected JComponent getLabelPanel(NodeRealizer nodeRealizer) {
//        NodeLabel nodeLabel = createNodeLabel(_nodeName,nodeRealizer);
////        double width = 5 + nodeLabel.getWidth() + 5;
////        double height = 3 + nodeLabel.getHeight() + 3;
////        nodeRealizer.setSize(width, height);
        JComponent labelPanel = super.getLabelPanel(nodeRealizer);
        return labelPanel;
    }


    @Override
    protected Icon getIcon(ClassNode node) {
        return StatusIcons.getIconForMarking(node.getMarking());
    }

    @Override
    protected String getNodeName(ClassNode node) {
        _nodeName = node.getSimpleName();
        return _nodeName;
    }

    private static NodeLabel createNodeLabel(String nameText, NodeRealizer nodeRealizer) {
        NodeLabel nameLabel = GraphManager.getGraphManager().createNodeLabel();
        nameLabel.setText(nameText);
        nameLabel.setFontSize(20);
        nameLabel.setModel(NodeLabel.INTERNAL);
        nameLabel.setPosition(NodeLabel.CENTER);
        nodeRealizer.addLabel(nameLabel);
        return nameLabel;
    }
}
