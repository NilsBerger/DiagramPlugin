package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.view.*;
import com.intellij.ui.JBColor;
import materials.ProgramEntityRelationship;
import materials.TraceLinkProgramEntityAssociation;
import org.jetbrains.annotations.NotNull;
import valueobjects.RelationshipType;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

public class ProgramEntityRelationshipEdgeRealizerFactory {

    private static final Color WHITE = new Color(255, 255, 255, 0);
    private static final Color LABEL_BG_COLOR = new Color(0x77FFFFFF, true);
    private static final String UML_ANGLE_ARROW_NAME = "angle";

    private ProgramEntityRelationshipEdgeRealizerFactory() {
    }


    public static EdgeRealizer getUMLEdgeRealizer(final ProgramEntityRelationship relationship) {
        GraphManager graphManager = GraphManager.getGraphManager();
        PolyLineEdgeRealizer edgeRealizer = graphManager.createPolyLineEdgeRealizer();
        configureEdge(relationship, edgeRealizer);
        return edgeRealizer;
    }

    /**
     * Configures the edge according to the RelationshipType of the ProgramEntityRelationship
     *
     * @param programEntityRelationship The ProgramEntity
     * @param edgeRealizer              The EdgeRealizer of the the PresentationModel
     */
    private static void configureEdge(final ProgramEntityRelationship programEntityRelationship, EdgeRealizer edgeRealizer) {
        RelationshipType relationshipType = programEntityRelationship.getRelationshipType();
        final GraphManager graphManager = GraphManager.getGraphManager();
        Arrow customArrow = getUMLAngleArrow();
        boolean directedEdge = false;
        switch (relationshipType) {
            case Extends:
                edgeRealizer.setSourceArrow(Arrow.WHITE_DELTA);
                directedEdge = true;
                break;
            case Implements:
                edgeRealizer.setLineType(LineType.DASHED_2);
                edgeRealizer.setSourceArrow((Arrow.WHITE_DELTA));
                directedEdge = true;
                break;
            case NEW_EXPRESSION:
                edgeRealizer.setLineType(LineType.DASHED_2);
                edgeRealizer.setTargetArrow(customArrow);
                EdgeLabel newEdgeLabel = graphManager.createEdgeLabel("<html>&laquo;create&raquo;</html>");
                newEdgeLabel.setModel(EdgeLabel.THREE_CENTER);
                newEdgeLabel.setPosition(EdgeLabel.CENTER);
                newEdgeLabel.setDistance(0);
                newEdgeLabel.setBackgroundColor(LABEL_BG_COLOR);
                edgeRealizer.addLabel(newEdgeLabel);
                break;
            case Dependency:
                edgeRealizer.setLineType(LineType.DASHED_2);
                edgeRealizer.setSourceArrow(customArrow);
                directedEdge = true;
                break;
            case Traceability_Association:
                if (programEntityRelationship instanceof TraceLinkProgramEntityAssociation) {
                    TraceLinkProgramEntityAssociation dependency = (TraceLinkProgramEntityAssociation) programEntityRelationship;
                    double traceLinkValue = dependency.getTracelinkValue();
                    edgeRealizer.setLineType(LineType.LINE_5);
                    edgeRealizer.setLineColor(JBColor.RED);
                    edgeRealizer.setLabelText(Double.toString(traceLinkValue));
                    directedEdge = true;
                }
                break;
            case InconsistentRelationship:
                break;
            case FIELD_TYPE_MANY:
                edgeRealizer.setSourceArrow(Arrow.DIAMOND);
                edgeRealizer.setTargetArrow(customArrow);
                EdgeLabel manyEdgeLabel = graphManager.createEdgeLabel("*");
                manyEdgeLabel.setModel(EdgeLabel.THREE_CENTER);
                manyEdgeLabel.setPosition(EdgeLabel.TCENTR);
                manyEdgeLabel.setDistance(0);
                manyEdgeLabel.setBackgroundColor(LABEL_BG_COLOR);
                edgeRealizer.addLabel(manyEdgeLabel);
                break;
            case FIELD_TYPE_ONE:
                edgeRealizer.setSourceArrow(Arrow.DIAMOND);
                edgeRealizer.setTargetArrow(customArrow);
                EdgeLabel oneEdgeLabel = graphManager.createEdgeLabel("1");
                oneEdgeLabel.setModel(EdgeLabel.THREE_CENTER);
                oneEdgeLabel.setPosition(EdgeLabel.TCENTR);
                oneEdgeLabel.setDistance(0);
                oneEdgeLabel.setBackgroundColor(LABEL_BG_COLOR);
                edgeRealizer.addLabel(oneEdgeLabel);
                break;
            case Aggregation:
                edgeRealizer.setSourceArrow(Arrow.WHITE_DELTA);
                edgeRealizer.setTargetArrow(customArrow);
                break;
            case Association:
                edgeRealizer.setSourceArrow(Arrow.NONE);
                edgeRealizer.setTargetArrow(Arrow.NONE);
                edgeRealizer.setLineType(LineType.LINE_2);
                break;
            case Composition:
                edgeRealizer.setSourceArrow(Arrow.DIAMOND);
                edgeRealizer.setTargetArrow(customArrow);
                break;
            default:
                edgeRealizer.setSourceArrow(Arrow.WHITE_DELTA);
                directedEdge = true;
                break;
        }
    }

    /**
     * Gets a UML angle arrow. If it does not exist already, it will be created and registered; otherwise
     * it will simply be returned.
     *
     * @return cached or created arrow
     */
    private @NotNull
    static Arrow getUMLAngleArrow() {
        Arrow customArrow = Arrow.Statics.getCustomArrow(UML_ANGLE_ARROW_NAME);
        if (customArrow == null) {
            Path2D.Float arrowShape = new GeneralPath();
            arrowShape.moveTo(-8, -5);
            arrowShape.lineTo(0, 0);
            arrowShape.lineTo(-8, 5);
            customArrow = Arrow.Statics.addCustomArrow(UML_ANGLE_ARROW_NAME, arrowShape, WHITE);
        }
        return customArrow;
    }
}

