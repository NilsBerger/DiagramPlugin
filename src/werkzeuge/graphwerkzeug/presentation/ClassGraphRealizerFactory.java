package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.view.*;
import werkzeuge.graphwerkzeug.model.ClassGraphNode;

import java.awt.*;

public class ClassGraphRealizerFactory {

    public static NodeRealizer createDefaultNodeRealizer(final ClassGraphNode classGraphNode)
    {
        Graph2DNodeRealizer nodeRealizer = GraphManager.getGraphManager().createGraph2DNodeRealizer();

        String nameText = classGraphNode.getName();
        NodeLabel nameLabel = addNodeNameLabel(nameText, nodeRealizer);

        calculateAndSetNodeSize(nodeRealizer, nameLabel);

        setNodeColor(nodeRealizer, classGraphNode);
        setNodeShape(nodeRealizer);
        return nodeRealizer;
    }

    private static void setNodeOnSelectionListener(Graph2DNodeRealizer nodeRealizer) {
        //nodeRealizer.s

    }

    private static void setNodeShape(Graph2DNodeRealizer nodeRealizer) {
        nodeRealizer.setShapeType(ShapeNodeRealizer.RECT);
    }


    public static void setNodeColor(final Graph2DNodeRealizer nodeRealizer, ClassGraphNode classGraphNode)
    {
        ClassGraphNode.Type type = classGraphNode.getType();
        Color color = Color.WHITE;
//        switch(type)
//        {
//            case Java:
//                color = new Color(76, 131, 255);
//                break;
//            case Swift:
//                color = new Color(76, 255, 131);
//                break;
//        }
        nodeRealizer.setFillColor(color);
    }

    private static void calculateAndSetNodeSize(NodeRealizer nodeRealizer, NodeLabel nameLabel) {
        double width = 5 +  nameLabel.getWidth() + 5;
        double height = 3 + nameLabel.getHeight() + 3;
        nodeRealizer.setSize(width, height);
    }

    private static NodeLabel addNodeNameLabel(String nameText, NodeRealizer nodeRealizer) {
        NodeLabel nameLabel = GraphManager.getGraphManager().createNodeLabel();
        nameLabel.setText(nameText);
        nameLabel.setFontSize(20);
        nameLabel.setModel(NodeLabel.INTERNAL);
        nameLabel.setPosition(NodeLabel.CENTER);
        nodeRealizer.addLabel(nameLabel);
        return nameLabel;
    }

//    private static void onSelectOpenPopupMenu(NodeRealizer nodeRealizer, ClassGraphNode classGraphNode)
//    {
//        classGraphNode.
//    }



}
