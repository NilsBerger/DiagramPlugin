package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.view.Graph2DNodeRealizer;
import com.intellij.openapi.graph.view.NodeLabel;
import com.intellij.openapi.graph.view.NodeRealizer;
import com.intellij.openapi.graph.view.ShapeNodeRealizer;
import com.intellij.ui.JBColor;
import materials.ProgramEntity;
import valueobjects.Language;
import valueobjects.Marking;

import java.awt.*;

class ProgramEntityNodeRealizerFactory {

    private ProgramEntityNodeRealizerFactory() {
    }

    static NodeRealizer createDefaultNodeRealizer(final ProgramEntity programEntity) {
        Graph2DNodeRealizer nodeRealizer = GraphManager.getGraphManager().createGraph2DNodeRealizer();

        NodeLabel nameLabel = addClassNameLabel(programEntity.getSimpleName(), nodeRealizer);
        NodeLabel markingLabel = addMarkingLabel(programEntity.getMarking(), nodeRealizer);

        calculateAndSetNodeSize(nodeRealizer, nameLabel, markingLabel);

        setNodeColor(nodeRealizer, programEntity);
        setNodeShape(nodeRealizer);
        return nodeRealizer;
    }


    private static void setNodeShape(Graph2DNodeRealizer nodeRealizer) {
        nodeRealizer.setShapeType(ShapeNodeRealizer.RECT);
    }


    private static void setNodeColor(final Graph2DNodeRealizer nodeRealizer, ProgramEntity classGraphNode) {
        Language type = classGraphNode.getLanguage();
        Color color = JBColor.WHITE;
        switch (type) {
            case Java:
                color = new Color(135, 179, 255);
                break;
            case Swift:
                color = new Color(169, 30, 50);
                break;
        }
        nodeRealizer.setFillColor(JBColor.white);
        nodeRealizer.setLineColor(color);
    }

    private static void calculateAndSetNodeSize(NodeRealizer nodeRealizer, NodeLabel classNameLabel, NodeLabel statusLabel) {
        double width = 5 + Math.max(classNameLabel.getWidth(), statusLabel.getWidth()) + 5;
        double height = 3 + classNameLabel.getHeight() + 1 + statusLabel.getHeight() + 3;
        nodeRealizer.setSize(width, height);
    }

    private static NodeLabel addClassNameLabel(String className, NodeRealizer nodeRealizer) {
        NodeLabel nameLabel = GraphManager.getGraphManager().createNodeLabel();
        nameLabel.setText(className);
        nameLabel.setFontSize(14);
        nameLabel.setModel(NodeLabel.INTERNAL);
        nameLabel.setPosition(NodeLabel.TOP);

        nodeRealizer.addLabel(nameLabel);
        return nameLabel;

    }

    private static NodeLabel addMarkingLabel(Marking marking, NodeRealizer nodeRealizer) {
        NodeLabel typeLabel = GraphManager.getGraphManager().createNodeLabel();
        typeLabel.setText("<<" + marking + ">>");
        typeLabel.setFontSize(10);
        typeLabel.setFontStyle(Font.BOLD);
        typeLabel.setModel(NodeLabel.INTERNAL);
        typeLabel.setPosition(NodeLabel.BOTTOM);
        nodeRealizer.addLabel(typeLabel);
        return typeLabel;
    }
}
