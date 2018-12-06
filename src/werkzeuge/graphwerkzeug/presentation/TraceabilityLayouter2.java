/*
 * Copyright 1998-2018 Konstantin Bulenkov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.layout.LayoutGraph;
import com.intellij.openapi.graph.layout.LayoutOrientation;
import com.intellij.openapi.graph.layout.Layouter;
import com.intellij.openapi.graph.layout.hierarchic.HierarchicLayouter;
import com.intellij.openapi.graph.layout.orthogonal.OrthogonalLayouter;
import com.intellij.openapi.graph.layout.partial.PartialLayouter;
import materials.ClassNode;
import org.jetbrains.annotations.NotNull;
import valueobjects.ClassLanguageType;

public class TraceabilityLayouter2 implements Layouter {

    private final ClassGraph _classGraph;
    private final OrthogonalLayouter _layouter;

    public TraceabilityLayouter2(final ClassGraph classGraph)
    {

        _layouter =  GraphManager.getGraphManager().createOrthogonalLayouter();
        _classGraph = classGraph;
        _layouter.setLayoutOrientation(LayoutOrientation.TOP_TO_BOTTOM);
        //configureLayouter(_layouter);
    }

    @Override
    public boolean canLayout(LayoutGraph layoutGraph) {
        return true;
    }

    public void doLayout(@NotNull LayoutGraph graph) {

        System.out.println("doLayout TraceabilitlyLayouter");
        _layouter.doLayout(graph);
        moveSwiftNodes(_classGraph.getGraph());

    }

    private void moveSwiftNodes(final LayoutGraph graph)
    {
        final Node[] nodeArray = graph.getNodeArray();
        final double mostfarRightPosition = 3000;
        for(Node node : nodeArray)
        {
            ClassNode classNode = _classGraph.getClassNode(node);
            if(classNode != null)
            {
                if(classNode.getSimpleName().length() > 10)
                {
                    //moveBy(Node node,double dx, double dy)

                    double xNode = graph.getX(node);
                    double yNode = graph.getY(node);
                    System.out.println("Moving node : "+ classNode.getSimpleName()+ " from xPosition: '"+ xNode + "' to xPosition: '" +(xNode + mostfarRightPosition) + "'   --> Y :' "+ yNode+ "'");
                    graph.moveBy(node, (xNode + mostfarRightPosition), yNode);
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

    private void configureLayouter(PartialLayouter layouter)
    {
        final Layouter orthogonalLayouter = getSwiftLayouter();
        layouter.setCoreLayouter(orthogonalLayouter);
        layouter.setComponentAssignmentStrategy(PartialLayouter.COMPONENT_ASSIGNMENT_STRATEGY_CONNECTED);
        layouter.setPositioningStrategy(PartialLayouter.SUBGRAPH_POSITIONING_STRATEGY_FROM_SKETCH);
        layouter.setLayoutOrientation(LayoutOrientation.TOP_TO_BOTTOM);

        layouter.setMirroringAllowed(true);
        layouter.setConsiderNodeAlignment(true);
        layouter.setMinimalNodeDistance(3);


    }
    private static Layouter getJavaLayouter()
    {
        HierarchicLayouter layouter = GraphManager.getGraphManager().createHierarchicGroupLayouter();
        layouter.setLayoutOrientation(LayoutOrientation.TOP_TO_BOTTOM);
        layouter.setMinimalNodeDistance(4);

        return layouter;
    }

    private static Layouter getSwiftLayouter()
    {
        HierarchicLayouter layouter = GraphManager.getGraphManager().createHierarchicGroupLayouter();
        layouter.setLayoutOrientation(LayoutOrientation.TOP_TO_BOTTOM);
        layouter.setRoutingStyle(HierarchicLayouter.ROUTE_ORTHOGONAL);
        layouter.setMinimalNodeDistance(4);
        return layouter;
    }


}
