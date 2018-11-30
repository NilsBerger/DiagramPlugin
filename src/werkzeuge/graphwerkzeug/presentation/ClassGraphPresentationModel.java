package werkzeuge.graphwerkzeug.presentation;

import colorspectrum.ColorUtils;
import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.base.*;
import com.intellij.openapi.graph.builder.components.BasicGraphPresentationModel;
import com.intellij.openapi.graph.builder.util.GraphViewUtil;
import com.intellij.openapi.graph.layout.PortConstraintKeys;
import com.intellij.openapi.graph.layout.orthogonal.DirectedOrthogonalLayouter;
import com.intellij.openapi.graph.view.*;
import materials.ClassDependency;
import materials.ClassNode;
import materials.TraceLinkClassDependency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import valueobjects.RelationshipType;
import werkzeuge.graphwerkzeug.ClassNodePopupMenu;
import werkzeuge.graphwerkzeug.model.GeneralClassGraphDataModel;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

public class ClassGraphPresentationModel extends BasicGraphPresentationModel<ClassNode, ClassDependency> {

    private ClassGraph _classGraph;
    private ClassNodeRenderer _nodeRenderer;
    private Graph2D _graph;

    private final EdgeMap _edgeMap;
    private final EdgeMap _directedEdgeMap;
    private final EdgeMap _groupEdgeMap;

    private static final Color WHITE = new Color (255, 255, 255, 0);
    private static final String UML_ANGLE_ARROW_NAME = "angle";
    private static final String RENDERER_NAME = "ClassGraph.NodeRenderer";

    public ClassGraphPresentationModel(final Graph graph)
    {
        super(graph);
        setShowEdgeLabels(false);
        _edgeMap         = graph.createEdgeMap ();
        _directedEdgeMap = graph.createEdgeMap ();
        _groupEdgeMap    = graph.createEdgeMap ();
        graph.addDataProvider (DataProviderKeys.DEPENDENCY_INFO_EDGE_KEY,      _edgeMap);
        graph.addDataProvider (DirectedOrthogonalLayouter.DIRECTED_EDGE_DPKEY, _directedEdgeMap);
        graph.addDataProvider (PortConstraintKeys.TARGET_GROUPID_KEY,          _groupEdgeMap);
    }

    public void setClassGraph(final ClassGraph classGraph)
    {
        _classGraph = classGraph;
        _graph = classGraph.getGraph();
        setGraph(classGraph.getGraph());
        setGraphBuilder(classGraph.getGraphBuilder());
    }

    private GeneralClassGraphDataModel getDataModel()
    {
        return _classGraph.getDataModel();
    }

    @Override
    public void customizeSettings(Graph2DView view, EditMode editMode) {

        ClassNodePopupMenu popupMode = new ClassNodePopupMenu(_classGraph);
        editMode.setPopupMode(popupMode);

        editMode.allowMoveSelection(false);
        editMode.allowMoving(true);
        editMode.allowNodeCreation(false);
        editMode.allowEdgeCreation(false);
    }

    @Override
    public String getNodeTooltip(@Nullable ClassNode node) {
        return node.getSimpleName();
    }

    @NotNull
    @Override
    public NodeRealizer getNodeRealizer(@Nullable ClassNode node) {
        if(_nodeRenderer == null )
        {
            _nodeRenderer = new ClassNodeRenderer(getGraphBuilder(), null);
        }
        return GraphViewUtil.createNodeRealizer(RENDERER_NAME, _nodeRenderer);
    }

    @NotNull
    @Override
    public EdgeRealizer getEdgeRealizer(@Nullable ClassDependency classDependency) {
        GraphManager graphManager = GraphManager.getGraphManager ();
        PolyLineEdgeRealizer edgeRealizer = graphManager.createPolyLineEdgeRealizer();
        Node sourceNode = _classGraph.getNode(classDependency.getIndependentClass());
        Node targetNode = _classGraph.getNode(classDependency.getDependentClass());
        configureEdge(classDependency, edgeRealizer);
//        if(sourceNode!= null && targetNode != null)
//        {
//            createEdge(sourceNode, targetNode, classDependency, edgeRealizer);
//        }
        return edgeRealizer;
    }

    private boolean configureEdge(final ClassDependency classDependency, EdgeRealizer edgeRealizer)
    {
        RelationshipType relationshipType = classDependency.getRelationshipType();
        Arrow customArrow = getUMLAngleArrow();
        boolean directedEdge = false;
        switch (relationshipType)
        {
            case Extends:
                edgeRealizer.setTargetArrow (Arrow.WHITE_DELTA);
                directedEdge = true;
                break;
            case Implements:
                edgeRealizer.setLineType (LineType.DASHED_1);
                edgeRealizer.setTargetArrow (Arrow.WHITE_DELTA);
                directedEdge = true;
                break;
//            case NEW_EXPRESSION:
//                edgeRealizer.setLineType (LineType.DASHED_1);
//                edgeRealizer.setTargetArrow (customArrow);
//                EdgeLabel newEdgeLabel = graphManager.createEdgeLabel ("<html>&laquo;create&raquo;</html>");
//                newEdgeLabel.setModel           (EdgeLabel.THREE_CENTER);
//                newEdgeLabel.setPosition        (EdgeLabel.CENTER);
//                newEdgeLabel.setDistance        (0);
//                newEdgeLabel.setBackgroundColor (LABEL_BG_COLOR);
//                edgeRealizer.addLabel (newEdgeLabel);
//                break;
            case Dependency:
                edgeRealizer.setLineType (LineType.DASHED_1);
                edgeRealizer.setTargetArrow (customArrow);
                directedEdge = true;
                break;
            case Traceability_Association:
                if(classDependency instanceof TraceLinkClassDependency)
                {
                    TraceLinkClassDependency dependency = (TraceLinkClassDependency) classDependency;
                    double traceLinkValue = dependency.getTracelinkValue();
                    edgeRealizer.setLineType (LineType.DASHED_1);
                    edgeRealizer.setTargetArrow (customArrow);
                    LineType lineType = LineType.LINE_1;
                    edgeRealizer.setLineType(lineType);
                    edgeRealizer.setLineColor(ColorUtils.test(traceLinkValue));
                    edgeRealizer.setLabelText(Double.toString(traceLinkValue));
                    directedEdge = true;
                }
                break;
            case InconsistentRelationship:
                break;
//            case FIELD_TYPE_MANY:
//                edgeRealizer.setSourceArrow (Arrow.DIAMOND);
//                edgeRealizer.setTargetArrow (customArrow);
//                EdgeLabel manyEdgeLabel = graphManager.createEdgeLabel ("*");
//                manyEdgeLabel.setModel           (EdgeLabel.THREE_CENTER);
//                manyEdgeLabel.setPosition        (EdgeLabel.TCENTR);
//                manyEdgeLabel.setDistance        (0);
//                manyEdgeLabel.setBackgroundColor (LABEL_BG_COLOR);
//                edgeRealizer.addLabel (manyEdgeLabel);
//                break;
//            case FIELD_TYPE_ONE:
//                edgeRealizer.setSourceArrow (Arrow.DIAMOND);
//                edgeRealizer.setTargetArrow (customArrow);
//                EdgeLabel oneEdgeLabel = graphManager.createEdgeLabel ("1");
//                oneEdgeLabel.setModel           (EdgeLabel.THREE_CENTER);
//                oneEdgeLabel.setPosition        (EdgeLabel.TCENTR);
//                oneEdgeLabel.setDistance        (0);
//                oneEdgeLabel.setBackgroundColor (LABEL_BG_COLOR);
//                edgeRealizer.addLabel (oneEdgeLabel);
//                break;
            default:
                edgeRealizer.setTargetArrow (Arrow.WHITE_DELTA);
                directedEdge = true;
                break;
        }
        return directedEdge;
    }

    /**
     * Gets a UML angle arrow. If it does not exist already, it will be created and registered; otherwise
     * it will simply be returned.
     * @return cached or created arrow
     */
    private @NotNull Arrow getUMLAngleArrow ()
    {
        Arrow customArrow = Arrow.Statics.getCustomArrow (UML_ANGLE_ARROW_NAME);
        if (customArrow == null)
        {
            Path2D.Float arrowShape = new GeneralPath();
            arrowShape.moveTo (-8, -5);
            arrowShape.lineTo (0, 0);
            arrowShape.lineTo (-8, 5);
            customArrow = Arrow.Statics.addCustomArrow (UML_ANGLE_ARROW_NAME, arrowShape, WHITE);
        }
        return customArrow;
    }

    /**
     * Creates a new edge.
     * @param sourceNode source node
     * @param targetNode target node
     */
    public void createEdge (@NotNull Node sourceNode, @NotNull Node targetNode, @NotNull ClassDependency dependency, EdgeRealizer edgeRealizer)
    {
        for (EdgeCursor edgeCursor = sourceNode.edges (); edgeCursor.ok (); edgeCursor.next ())
        {
            Edge existingEdge = edgeCursor.edge ();
            if (existingEdge.target () != targetNode)
            {
                RelationshipType edgeType = (RelationshipType) _edgeMap.get (existingEdge);
                // compare existing usage type with new usage type
                boolean newUsageTypeIsExtending = edgeType == RelationshipType.Extends ||
                        edgeType == RelationshipType.Implements;
                boolean oldUsageTypeIsExtending = edgeType == RelationshipType.Extends ||
                        edgeType == RelationshipType.Implements;
                if (newUsageTypeIsExtending)
                {
                    if (oldUsageTypeIsExtending)
                    {
                        // do not create edge, if new edge is extending edge and extending edge already exists
                        return;
                    }
                }
                else
                {
                    if (edgeType.compareTo (edgeType) > 0)
                    {
                        // remove old edge, because new edge is "more important"
                        existingEdge.getGraph ().removeEdge (existingEdge);
                    }
                    else if (!oldUsageTypeIsExtending)
                    {
                        // do not create edge, if old edge is not extending edge and new edge is "less important"
                        return;
                    }
                }
            }
        }
        Edge edge = _graph.createEdge (sourceNode, targetNode);
        // provide information for layouter to be able to group implements and extends edges
        RelationshipType relationshipType = dependency.getRelationshipType();
        _edgeMap.set (edge, relationshipType);
        if (relationshipType == RelationshipType.Extends || relationshipType == RelationshipType.Implements)
        {
            String groupID = _graph.getRealizer (targetNode).getLabelText ();
            _groupEdgeMap.set (edge, groupID);
        }
        boolean directedEdge = configureEdge (dependency, edgeRealizer);
        _graph.setRealizer (edge, edgeRealizer);
        _directedEdgeMap.set (edge, directedEdge);
    }
}
