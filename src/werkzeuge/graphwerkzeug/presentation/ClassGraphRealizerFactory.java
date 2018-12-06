package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.view.*;
import materials.ClassNode;

import java.awt.*;

public class ClassGraphRealizerFactory {

    public static NodeRealizer createDefaultNodeRealizer(final ClassNode classGraphNode)
    {
        Graph2DNodeRealizer nodeRealizer = GraphManager.getGraphManager().createGraph2DNodeRealizer();

        String nameText = classGraphNode.getSimpleName();
        NodeLabel nameLabel = addNodeNameLabel(nameText, nodeRealizer);

        calculateAndSetNodeSize(nodeRealizer, nameLabel);

        setNodeColor(nodeRealizer, classGraphNode);
        setNodeShape(nodeRealizer);
        return nodeRealizer;
    }


    private static void setNodeShape(Graph2DNodeRealizer nodeRealizer) {
        nodeRealizer.setShapeType(ShapeNodeRealizer.RECT);
    }



    public static void setNodeColor(final Graph2DNodeRealizer nodeRealizer, ClassNode classGraphNode)
    {
        //ClassGraphNode.Type type = classGraphNode.getClassLanguageType();
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
}
