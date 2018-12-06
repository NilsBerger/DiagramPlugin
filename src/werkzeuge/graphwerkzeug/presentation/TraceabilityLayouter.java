package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.layout.LayoutGraph;
import com.intellij.openapi.graph.layout.LayoutOrientation;
import com.intellij.openapi.graph.layout.Layouter;
import com.intellij.openapi.graph.layout.hierarchic.HierarchicLayouter;
import com.intellij.openapi.graph.layout.orthogonal.DirectedOrthogonalLayouter;
import com.intellij.openapi.graph.layout.partial.PartialLayouter;
import materials.ClassNode;
import org.jetbrains.annotations.NotNull;
import valueobjects.ClassLanguageType;

public class TraceabilityLayouter implements Layouter {

    private ClassGraph _classGraph;
    private final DirectedOrthogonalLayouter _layouter;

    public TraceabilityLayouter()
    {

        _layouter =  GraphManager.getGraphManager().createDirectedOrthogonalLayouter();
        //configureLayouter(_layouter);
    }

    @Override
    public boolean canLayout(LayoutGraph layoutGraph) {
        return true;
    }

    public void doLayout(@NotNull LayoutGraph graph) {

        System.out.println("doLayout TraceabilitlyLayouter");
        _layouter.doLayout(_classGraph.getGraph());
        moveSwiftNodes(_classGraph.getGraph());

    }

    public void setClassGraph(ClassGraph classGraph) {
        this._classGraph = classGraph;
    }

    private void moveSwiftNodes(final LayoutGraph graph)
    {

        final Node[] nodeArray = graph.getNodeArray();
        final double mostfarRightPosition = getMostfarRightPosition(nodeArray);
        System.out.println("most out far " + mostfarRightPosition);

        _classGraph.getView();
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
                    double neXNode = xNode +mostfarRightPosition;
                    System.out.println("Moving node : "+ classNode.getSimpleName()+ " from xPosition: '"+ xNode + "' to xPosition: '" +(xNode + mostfarRightPosition) + "'   --> Y :' "+ yNode+ "'");
                    graph.moveBy(node, neXNode, yNode);
                }
            }
        }
    }


    private double getMostfarRightPosition(Node[] nodearray){
        double mostFarRightPosition = 0;
        for(Node node : nodearray)
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

//    private void configureLayouter(PartialLayouter layouter)
//    {
//        final Layouter orthogonalLayouter = getSwiftLayouter();
//        layouter.setCoreLayouter(orthogonalLayouter);
//        layouter.setComponentAssignmentStrategy(PartialLayouter.COMPONENT_ASSIGNMENT_STRATEGY_CONNECTED);
//        layouter.setPositioningStrategy(PartialLayouter.SUBGRAPH_POSITIONING_STRATEGY_FROM_SKETCH);
//        layouter.setLayoutOrientation(LayoutOrientation.TOP_TO_BOTTOM);
//
//        layouter.setMirroringAllowed(true);
//        layouter.setConsiderNodeAlignment(true);
//        layouter.setMinimalNodeDistance(3);
//
//
//    }
//    private static Layouter getJavaLayouter()
//    {
//         HierarchicLayouter layouter = GraphManager.getGraphManager().createHierarchicGroupLayouter();
//         layouter.setLayoutOrientation(LayoutOrientation.TOP_TO_BOTTOM);
//         layouter.setMinimalNodeDistance(4);
//
//         return layouter;
//    }
//
//    private static Layouter getSwiftLayouter()
//    {
//        HierarchicLayouter layouter = GraphManager.getGraphManager().createHierarchicGroupLayouter();
//        layouter.setLayoutOrientation(LayoutOrientation.TOP_TO_BOTTOM);
//        layouter.setRoutingStyle(HierarchicLayouter.ROUTE_ORTHOGONAL);
//        layouter.setMinimalNodeDistance(4);
//        return layouter;
//    }


}
