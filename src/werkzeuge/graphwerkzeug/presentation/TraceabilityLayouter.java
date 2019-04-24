package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.layout.*;
import com.intellij.openapi.graph.layout.orthogonal.DirectedOrthogonalLayouter;
import com.intellij.openapi.graph.layout.orthogonal.OrthogonalLayouter;
import com.intellij.openapi.graph.layout.router.EdgeGroupRouterStage;
import com.intellij.openapi.graph.layout.router.OrthogonalEdgeRouter;
import com.intellij.openapi.graph.layout.router.polyline.EdgeRouter;
import com.intellij.openapi.graph.view.BridgeCalculator;
import com.intellij.openapi.graph.view.DefaultGraph2DRenderer;
import materials.ProgramEntity;
import org.jetbrains.annotations.NotNull;
import valueobjects.Language;
import werkzeuge.graphwerkzeug.ImpactAnalysisGraph;
import werkzeuge.graphwerkzeug.presentation.toolbaractions.LanguageNodeSubgraphDataProvider;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;


/**
 * Custom Layouter for the a layoutGraph that contains "Java"- and "Swift"-ClassNodes. The Java-ClassNodes are displayed
 * on the left side of the view and the "Swift"-ClassNodes are displayed on the right side.
 */
public class TraceabilityLayouter implements Layouter {

    private static final double GAP = 6000.0;

    private ImpactAnalysisGraph _impactAnalysisGraph;


    private final DirectedOrthogonalLayouter _mainOrthogonalLayouter;
    private final SubgraphLayouter _subgraphLayouter;

    private GroupEdgeTargetDataProvider _groupEdgeTargetDataProvider;
    private DirectedEdgeDataProvider _directedEdgeDataProvider;

    private double _mostFarRightPosition = 0.0;
    private double _mostFarLeftPosition = 0.0;
    //private double _highestJavaNodePosition = 0.0;
    //private double _highestSwiftNodePosition = 0.0;

    public TraceabilityLayouter()
    {
        _mainOrthogonalLayouter = createJavaNodeLayouter();
        _subgraphLayouter = createSubgraphLayouter();
    }

    @Override
    public boolean canLayout(LayoutGraph layoutGraph) {
        return true;
    }

    public void doLayout(@NotNull LayoutGraph graph) {
        _directedEdgeDataProvider = new DirectedEdgeDataProvider(_impactAnalysisGraph, graph);
        graph.addDataProvider(DirectedOrthogonalLayouter.DIRECTED_EDGE_DPKEY, _directedEdgeDataProvider);
        _impactAnalysisGraph.getGraph().addDataProvider(DirectedOrthogonalLayouter.DIRECTED_EDGE_DPKEY, _directedEdgeDataProvider);

        _groupEdgeTargetDataProvider = new GroupEdgeTargetDataProvider(_impactAnalysisGraph, graph);
        graph.addDataProvider(PortConstraintKeys.TARGET_GROUPID_KEY, _groupEdgeTargetDataProvider);
        _impactAnalysisGraph.getGraph().addDataProvider(PortConstraintKeys.TARGET_GROUPID_KEY, _groupEdgeTargetDataProvider);

        final DefaultGraph2DRenderer graph2DRenderer = (DefaultGraph2DRenderer) _impactAnalysisGraph.getView().getGraph2DRenderer();
        final BridgeCalculator bridgeCalculator = GraphManager.getGraphManager().createBridgeCalculator();
        bridgeCalculator.setCrossingMode(BridgeCalculator.CROSSING_MODE_VERTICAL_CROSSES_HORIZONTAL);
        graph2DRenderer.setBridgeCalculator(bridgeCalculator);
        LanguageNodeSubgraphDataProvider swiftNodeSubgraphDataProvider = new LanguageNodeSubgraphDataProvider(_impactAnalysisGraph, Language.Swift);
        graph.addDataProvider(SubgraphLayouter.SELECTED_NODES, swiftNodeSubgraphDataProvider);


        run(_mainOrthogonalLayouter);
        _subgraphLayouter.doLayout(graph);
        calculateTracebilityLayout(graph);

        BufferedLayouter bufferedLayouter = GraphManager.getGraphManager().createBufferedLayouter(getEdgeGroupRouterStage(getEdgeRouter()));
        bufferedLayouter.doLayout(graph);
        getHighestNodeJavaPosition(graph.getNodeArray(), Language.Java);
        getHighestNodeJavaPosition(graph.getNodeArray(), Language.Swift);

        //getLowestNodeJavaPosition(graph.getNodeArray(), Language.Java);
        //getLowestNodeJavaPosition(graph.getNodeArray(), Language.Swift);
    }

    private void run(Layouter layouter) {
        _impactAnalysisGraph.getView().applyLayout(layouter);
    }


    /**
     * Generates a gap between the two types of nodes and aligns the two top "Java" and "Swift"-ClassNodes
     * @param graph The LayoutGraph that needs to be layouted
     */
    private void calculateTracebilityLayout(final LayoutGraph graph){
        if (!isCurrentGapBigEnough(graph)) {
            final double distance = _mostFarRightPosition + (GAP - _mostFarLeftPosition);
            moveSwiftNodesToRight(graph, distance);
        }
        double highestJavaNodePosition = getHighestNodeJavaPosition(graph.getNodeArray(), Language.Java);
        double highestSwiftNodePosition = getHighestNodeJavaPosition(graph.getNodeArray(), Language.Swift);
        // Highest Java Node ist higher than highest SwiftNode
        if (highestJavaNodePosition < highestSwiftNodePosition) {
            final double distance = highestSwiftNodePosition - highestJavaNodePosition;
            levelNodes(graph, distance);
        } else {
            final double distance = highestJavaNodePosition - highestSwiftNodePosition;
            levelNodes(graph, distance);
        }
    }



    /**
     * Returns if the current layout has a gap between "Java" and "Swift"-Nodes is big enough.
     * @param graph The LayoutGraph that needs to be layouted
     * @return True if big enough, False if the gap is too small
     */
    private boolean isCurrentGapBigEnough(LayoutGraph graph) {
        _mostFarRightPosition =  getMostFarRightJavaNodePosition(graph.getNodeArray());
        _mostFarLeftPosition = getMostFarLeftSwiftNodePosition(graph.getNodeArray());

        boolean gap_big_enough = _mostFarLeftPosition - _mostFarRightPosition > GAP;
        System.out.println("Gap big enough ?: " + gap_big_enough);
        return gap_big_enough;
    }

    /**
     * Sets the ClassGraph to provide information for the layout
     * @param impactAnalysisGraph The ClassGraph for the LayoutGraph
     */
    public void setClassGraph(ImpactAnalysisGraph impactAnalysisGraph) {
        _impactAnalysisGraph = impactAnalysisGraph;
    }

    /**
     * Moves all of the swift nodes on the x-axis for a given distance
     * @param graph The LayoutGraph that needs to be altered.
     * @param distance The distance that all of the swift node need to be moved on the X-Axis
     */
    private void moveSwiftNodesToRight(final LayoutGraph graph, final double distance)
    {

        final Node[] nodeArray = graph.getNodeArray();
        for(Node node : nodeArray)
        {
            ProgramEntity programEntity = _impactAnalysisGraph.getProgramEnitity(node);
            if (programEntity != null)
            {
                if (programEntity.getLanguage() == Language.Swift)
                {
                    //moveBy(Node node,double dx, double dy)

                    double xNode = graph.getX(node);
                    double neXNode = xNode +distance;
                    System.out.println("Moving node: " + programEntity.getSimpleName() + " by " + neXNode + " to the east");
                    graph.moveBy(node, neXNode, 0);
                }
            }
            else{
                System.out.println("Could not find node: '" + node.toString() +"' in GraphBuilder");
            }
        }
    }

    /**
     * Moves all the Swift-nodes for a given distance on the Y-Axis, so that both the highest java and swift nodes are level
     * @param graph The LayoutGraph that need to be changed
     * @param distance The distance that the swift node need to be moved
     */
    private void levelNodes(LayoutGraph graph, double distance) {

        final Node[] nodeArray = graph.getNodeArray();
        for(Node node : nodeArray)
        {
            ProgramEntity programEntity = _impactAnalysisGraph.getProgramEnitity(node);
            if (programEntity != null)
            {
                if (programEntity.getLanguage() == Language.Swift)
                {
                    //moveBy(Node node,double dx, double dy)

                    double xNode = graph.getX(node);
                    double yNode = graph.getY(node);
                    System.out.println("Moving node : " + programEntity.getSimpleName() + " from xPosition: '" + xNode + " by " + distance);
                    graph.moveBy(node, 0, -distance + 1000.0);
                }
            }
            else{
                System.out.println("Could not find node: '" + node.toString() +"' in GraphBuilder");
            }
        }

    }

    /**
     * Itarates over all nodes and calculates the highest(The most top) java node Y-Axis postition. T
     * @param nodeArray An Node-Array of nodes in the LayoutGraph
     * @return Returns the value of the highes java-node. The value can be negative
     */
    private double getHighestNodeJavaPosition(final Node[] nodeArray, Language language) {

        Comparator<Node> comparator = Comparator.comparingDouble(node -> _impactAnalysisGraph.getGraph().getY(node));

        final Stream<Node> nodeStream = Arrays.stream(nodeArray);
        final Node node1 = nodeStream.
                filter(node -> _impactAnalysisGraph.getProgramEnitity(node).getLanguage() == language)
                .min(comparator).get();
        final ProgramEntity programEnitity = _impactAnalysisGraph.getProgramEnitity(node1);
        System.out.println(language + ": " + programEnitity.getSimpleName() + " Position: " + _impactAnalysisGraph.getGraph().getY(node1));

        return _impactAnalysisGraph.getGraph().getY(node1);
    }

    /**
     * Itarates over all nodes and calculates the highest(The most top) java node Y-Axis postition. T
     * @param nodeArray An Node-Array of nodes in the LayoutGraph
     * @return Returns the value of the highes java-node. The value can be negative
     */
    private double getLowestNodeJavaPosition(final Node[] nodeArray, Language language) {

        Comparator<Node> comparator = (p1, p2) -> Double.compare(_impactAnalysisGraph.getGraph().getY(p1), _impactAnalysisGraph.getGraph().getY(p2));

        final Stream<Node> nodeStream = Arrays.stream(nodeArray);
        final Node node1 = nodeStream.
                filter(node -> _impactAnalysisGraph.getProgramEnitity(node).getLanguage() == language)
                .max(comparator).get();
        final ProgramEntity programEnitity = _impactAnalysisGraph.getProgramEnitity(node1);
        System.out.println(language + ": " + programEnitity.getSimpleName() + "Position: " + _impactAnalysisGraph.getGraph().getY(node1));

        return _impactAnalysisGraph.getGraph().getY(node1);
    }

    /**
     * Calculates the most right java-node position (x-Axis)
     * @param nodeArray the nodearry of the given LayoutGraph
     * @return Returns the double value of the most right java node (X-Value)
     */
    private double getMostFarRightJavaNodePosition(final Node[] nodeArray){
        double mostFarRightPosition = 0;
        for(Node node : nodeArray)
        {
            ProgramEntity programEntity = _impactAnalysisGraph.getProgramEnitity(node);
            if (programEntity != null && programEntity.getLanguage() == Language.Java)
            {
                double xNodePosition = _impactAnalysisGraph.getGraph().getX(node);
                if(xNodePosition > mostFarRightPosition)
                {
                    mostFarRightPosition = xNodePosition;
                }
            }
        }
        return mostFarRightPosition;
    }
    /**
     * Calculates the most left swift-node position (x-Axis)
     * @param nodeArray the nodearry of the given LayoutGraph
     * @return Returns the double value of the most left swift node (X-Value)
     */
    private double getMostFarLeftSwiftNodePosition(final Node[] nodeArray) {

        double mostFarLeftPosition = Double.MAX_VALUE;
        for(Node node : nodeArray)
        {
            ProgramEntity programEntity = _impactAnalysisGraph.getProgramEnitity(node);
            if (programEntity != null && programEntity.getLanguage() == Language.Swift)
            {
                double xNodePosition = _impactAnalysisGraph.getGraph().getX(node);
                if(xNodePosition < mostFarLeftPosition)
                {
                    mostFarLeftPosition = xNodePosition;
                }
            }
        }
        return mostFarLeftPosition;
    }

    private static SubgraphLayouter createSubgraphLayouter()
    {
        final SubgraphLayouter subgraphLayouter = GraphManager.getGraphManager().createSubgraphLayouter(GraphManager.getGraphManager().createBalloonLayouter());
        subgraphLayouter.setSubgraphNodesDpKey(Layouter.SELECTED_NODES);
//        partialLayouter.setComponentAssignmentStrategy(PartialLayouter.COMPONENT_ASSIGNMENT_STRATEGY_CONNECTED);
//        partialLayouter.setPositioningStrategy(PartialLayouter.SUBGRAPH_POSITIONING_STRATEGY_FROM_SKETCH);
//
//        partialLayouter.setEdgeRoutingStrategy(PartialLayouter.EDGE_ROUTING_STRATEGY_ORTHOGONAL);
//        partialLayouter.setMirroringAllowed(true);
//        partialLayouter.setConsiderNodeAlignment(true);
//        partialLayouter.setMinimalNodeDistance(3);
//        partialLayouter.setPackComponentsEnabled(false);


        return subgraphLayouter;
    }

    private static OrthogonalLayouter createSwiftNodeLayouter() {
        final OrthogonalLayouter orthogonalLayouter = GraphManager.getGraphManager().createOrthogonalLayouter();
        orthogonalLayouter.setGrid(20);
        orthogonalLayouter.setLayoutStyle(OrthogonalLayouter.UNIFORM_STYLE);
        orthogonalLayouter.setUseFaceMaximization(true);
        orthogonalLayouter.setNodeModel(OrthogonalLayouter.NODEMODEL_UNIFORM);
        orthogonalLayouter.setLayoutOrientation(LayoutOrientation.TOP_TO_BOTTOM);


        return orthogonalLayouter;
    }

    /**
     *
     * Creates an OrthogonalLayouter
     * @return an OrthogonalLayouter for the internal of the TraceabilityLayouter
     */
    private static DirectedOrthogonalLayouter createJavaNodeLayouter()
    {
        final DirectedOrthogonalLayouter directedOrthogonalLayouter = GraphManager.getGraphManager().createDirectedOrthogonalLayouter();
        directedOrthogonalLayouter.setGrid(20);
        directedOrthogonalLayouter.setLayoutOrientation(LayoutOrientation.TOP_TO_BOTTOM);

        return directedOrthogonalLayouter;
    }

    private static EdgeRouter getEdgeRouter() {
        final EdgeRouter orthogonalEdgeRouter = GraphManager.getGraphManager().createEdgeRouter(getOrthogonalEdgeRouter());
//        orthogonalEdgeRouter.setGridRoutingEnabled(true);
//        orthogonalEdgeRouter.setGridSpacing(10);
//        orthogonalEdgeRouter.setCenterToSpaceRatio(0.0);
//        orthogonalEdgeRouter.setCoupledDistances(false);
//        orthogonalEdgeRouter.setMinimumDistanceToNode(20);

        return orthogonalEdgeRouter;
    }

    private static OrthogonalEdgeRouter getOrthogonalEdgeRouter() {
        final OrthogonalEdgeRouter orthogonalEdgeRouter = GraphManager.getGraphManager().createOrthogonalEdgeRouter();
        orthogonalEdgeRouter.setGridRoutingEnabled(true);
        orthogonalEdgeRouter.setGridSpacing(10);
        orthogonalEdgeRouter.setCenterToSpaceRatio(0.0);
        orthogonalEdgeRouter.setCoupledDistances(false);
        orthogonalEdgeRouter.setMinimumDistanceToNode(20);

        return orthogonalEdgeRouter;
    }

    private static EdgeGroupRouterStage getEdgeGroupRouterStage(EdgeRouter layouter) {
        final GraphManager graphManager = GraphManager.getGraphManager();
        final EdgeGroupRouterStage edgeGroupRouterStage = graphManager.createEdgeGroupRouterStage(layouter);

        return edgeGroupRouterStage;
    }
}
