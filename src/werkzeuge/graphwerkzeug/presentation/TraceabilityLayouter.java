package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.base.NodeMap;
import com.intellij.openapi.graph.layout.LayoutGraph;
import com.intellij.openapi.graph.layout.LayoutOrientation;
import com.intellij.openapi.graph.layout.Layouter;
import com.intellij.openapi.graph.layout.hierarchic.HierarchicGroupLayouter;
import com.intellij.openapi.graph.layout.hierarchic.HierarchicLayouter;
import com.intellij.openapi.graph.layout.orthogonal.DirectedOrthogonalLayouter;
import com.intellij.openapi.graph.layout.orthogonal.OrthogonalLayouter;
import com.intellij.openapi.graph.layout.partial.PartialLayouter;
import com.intellij.openapi.graph.layout.router.OrthogonalEdgeRouter;
import com.intellij.openapi.graph.util.DataProviderAdapter;
import materials.ClassNode;
import org.jetbrains.annotations.NotNull;
import valueobjects.ClassLanguageType;


/**
 * Custom Layouter for the a layoutGraph that contains "Java"- and "Swift"-ClassNodes. The Java-ClassNodes are displayed
 * on the left side of the view and the "Swift"-ClassNodes are displayed on the right side.
 */
public class TraceabilityLayouter implements Layouter {

    private static final double GAP = 1000.0;

    private ClassGraph _classGraph;
    //private final PartialLayouter _layouter;
    private HierarchicGroupLayouter _directedOrthogonalLayouter;
    private final OrthogonalEdgeRouter _edgeRouter;
    private NodeMap _partialNodeMap;

    private double _mostFarRightPosition = 0.0;
    private double _mostFarLeftPosition = 0.0;
    private double _highestJavaNodePosition = 0.0;
    private double _highestSwiftNodePosition = 0.0;

    public TraceabilityLayouter()
    {

        //_layouter =  GraphManager.getGraphManager().createPartialLayouter();
        _directedOrthogonalLayouter = getLayouter();
        _edgeRouter = GraphManager.getGraphManager().createOrthogonalEdgeRouter();
        //_edgeRouter.setEnforceMonotonicPathRestrictions(true);
        //configureLayouter(_layouter);
    }

    @Override
    public boolean canLayout(LayoutGraph layoutGraph) {
        return true;
    }

    public void doLayout(@NotNull LayoutGraph graph) {

        //_layouter.doLayout(graph);
        _directedOrthogonalLayouter.doLayout(graph);
        _partialNodeMap = graph.createNodeMap();
        graph.addDataProvider(DirectedOrthogonalLayouter.NODE_ID_DPKEY, _partialNodeMap);
        markPartialNodes(graph);
        calculateTracebilityLayout(graph);
        _edgeRouter.doLayout(graph);

        //printNodeInfo(graph);
    }

    private void printNodeInfo(LayoutGraph graph) {
        for(Node node : graph.getNodeArray())
        {
            ClassNode classNode = _classGraph.getClassNode(node);
            if(classNode != null)
            {
                System.out.println(classNode.getSimpleName() + " from " + classNode.getClassLanguageType() + " X: " +  graph.getX(node) + " Y: " + graph.getY(node));
            }
        }
    }

    /**
     * Generates a gap between the two types of nodes and aligns the two top "Java" and "Swift"-ClassNodes
     * @param graph The LayoutGraph that needs to be layouted
     */
    private void calculateTracebilityLayout(final LayoutGraph graph){
        if(!isCurrentGapBigEnough(graph)){
            final double distance = _mostFarRightPosition + (GAP - _mostFarLeftPosition);
            moveSwiftNodesToRight(graph, distance);
        }
        if(!arePartialNodesLevel(graph))
        {
            // Highest Java Node ist higher than highest SwiftNode
            if(_highestJavaNodePosition < _highestSwiftNodePosition)
            {
                final double distance = _highestSwiftNodePosition - _highestJavaNodePosition;
                levelNodes(graph, -distance);
            }
            else
            {
                final double distance = _highestJavaNodePosition - _highestSwiftNodePosition;
                levelNodes(graph, distance);
            }
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
        return _mostFarLeftPosition - _mostFarRightPosition > GAP;
    }

    /**
     * Returns if both the highest java and swift node are level
     * @param graph The LayoutGraph that needs to be layouted
     * @return Turns true if both of the highest java and swift node are level
     */
    private boolean arePartialNodesLevel(LayoutGraph graph) {
        _highestJavaNodePosition =  getHighestNodeJavaPosition(graph.getNodeArray());
        _highestSwiftNodePosition = getHighestNodeSwiftPosition(graph.getNodeArray());
        return _highestJavaNodePosition == _highestSwiftNodePosition;
    }


    private void markPartialNodes(final LayoutGraph layoutGraph)
    {
        final Node[] nodeArray = layoutGraph.getNodeArray();
        for(Node node : nodeArray)
        {
            ClassNode classNode = _classGraph.getClassNode(node);
            if(classNode != null)
            {
                if(classNode.getClassLanguageType() == ClassLanguageType.Swift)
                {
                  _partialNodeMap.setBool(node, true);
                }
            }
            else{
                System.out.println("Could not mark node: '" + node.toString() +"' in GraphBuilder");
            }
        }
    }


    /**
     * Sets the ClassGraph to provide information for the layout
     * @param classGraph The ClassGraph for the LayoutGraph
     */
    public void setClassGraph(ClassGraph classGraph) {
        this._classGraph = classGraph;
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
            ClassNode classNode = _classGraph.getClassNode(node);
            if(classNode != null)
            {
                if(classNode.getClassLanguageType() == ClassLanguageType.Swift)
                {
                    //moveBy(Node node,double dx, double dy)

                    double xNode = graph.getX(node);
                    double neXNode = xNode +distance;
                    System.out.println("Moving node: " + classNode.getSimpleName() + " by " + neXNode);
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
            ClassNode classNode = _classGraph.getClassNode(node);
            if(classNode != null)
            {
                if(classNode.getClassLanguageType() == ClassLanguageType.Swift)
                {
                    //moveBy(Node node,double dx, double dy)

                    double xNode = graph.getX(node);
                    double yNode = graph.getY(node);
                    //System.out.println("Moving node : "+ classNode.getSimpleName()+ " from xPosition: '"+ xNode + "' to xPosition: '" + + "'   --> Y :' "+ yNode+ "'");
                    graph.moveBy(node, 0, distance);
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
    private double getHighestNodeJavaPosition(final Node[] nodeArray)
    {
        double mostHighestJavaPosition = Double.MAX_VALUE;
        for(Node node : nodeArray)
        {
            ClassNode classNode = _classGraph.getClassNode(node);
            if(classNode != null && classNode.getClassLanguageType() == ClassLanguageType.Java)
            {
                double yNodePosition = _classGraph.getGraph().getY(node);
                if(yNodePosition < mostHighestJavaPosition)
                {
                    mostHighestJavaPosition = yNodePosition;
                }
            }
        }
        return mostHighestJavaPosition;
    }

    /**
     * Itarates over all nodes and calculates the highest (the most top) swift node Y-Axis postition. T
     * @param nodeArray An Node-Array of nodes in the LayoutGraph
     * @return Returns the value of the highes swift-node. The value can be negative
     */
    private double getHighestNodeSwiftPosition(final Node[] nodeArray)
    {
        double mostHighestSwiftPosition = Double.MAX_VALUE;
        for(Node node : nodeArray)
        {
            ClassNode classNode = _classGraph.getClassNode(node);
            if(classNode != null && classNode.getClassLanguageType() == ClassLanguageType.Swift)
            {
                double yNodePosition = _classGraph.getGraph().getY(node);
                if(yNodePosition < mostHighestSwiftPosition)
                {
                    mostHighestSwiftPosition = yNodePosition;
                }
            }
        }
        return mostHighestSwiftPosition;
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
            ClassNode classNode = _classGraph.getClassNode(node);
            if(classNode != null && classNode.getClassLanguageType() == ClassLanguageType.Java)
            {
                double xNodePosition = _classGraph.getGraph().getX(node);
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
            ClassNode classNode = _classGraph.getClassNode(node);
            if(classNode != null && classNode.getClassLanguageType() == ClassLanguageType.Swift)
            {
                double xNodePosition = _classGraph.getGraph().getX(node);
                if(xNodePosition < mostFarLeftPosition)
                {
                    mostFarLeftPosition = xNodePosition;
                }
            }
        }
        return mostFarLeftPosition;
    }

    private void configureLayouter(PartialLayouter layouter)
    {
        Layouter orthogonalLayouter = _directedOrthogonalLayouter;
        layouter.setCoreLayouter(orthogonalLayouter);
        layouter.setComponentAssignmentStrategy(PartialLayouter.COMPONENT_ASSIGNMENT_STRATEGY_CONNECTED);
        layouter.setPositioningStrategy(PartialLayouter.SUBGRAPH_POSITIONING_STRATEGY_FROM_SKETCH);
        layouter.setLayoutOrientation(LayoutOrientation.RIGHT_TO_LEFT);

        layouter.setMirroringAllowed(true);
        layouter.setConsiderNodeAlignment(true);
        layouter.setMinimalNodeDistance(3);
    }

    /**
     * Creates an OrthogonalLayouter
     * @return an OrthogonalLayouter for the internal of the TraceabilityLayouter
     */
    private static HierarchicGroupLayouter getLayouter()
    {
        HierarchicGroupLayouter layouter = GraphManager.getGraphManager().createHierarchicGroupLayouter();
         layouter.setLayoutOrientation(LayoutOrientation.TOP_TO_BOTTOM);
         layouter.setLayoutStyle(HierarchicLayouter.LAYERING_HIERARCHICAL_TOPMOST);
         return layouter;
    }
}
