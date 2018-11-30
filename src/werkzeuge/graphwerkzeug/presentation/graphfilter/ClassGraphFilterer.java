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

package werkzeuge.graphwerkzeug.presentation.graphfilter;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.builder.CustomGraphUpdater;
import com.intellij.openapi.graph.util.GraphHider;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.openapi.graph.view.Graph2DView;
import materials.ClassDependency;
import materials.ClassNode;
import werkzeuge.graphwerkzeug.presentation.ClassGraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ClassGraphFilterer extends CustomGraphUpdater {

    private GraphHider _graphHider;
    private ClassGraph _classGraph;
    private Map<ClassNode, Boolean> _nodeShouldHideMap;
    private boolean _filterOn = false;
    private FilterStrategy _filterStrategy;

    public ClassGraphFilterer(ClassGraph classGraph) {
        _classGraph = classGraph;
        _nodeShouldHideMap = new HashMap<>();
    }

    /**
     * Updates the Graph for the given FilterStrategy. If no Stragey
     * @param graph2D
     * @param graph2DView
     */
    @Override
    public void update(Graph2D graph2D, Graph2DView graph2DView)
    {
        _graphHider = GraphManager.getGraphManager().createGraphHider(graph2D);
        _graphHider.setFireGraphEventsEnabled(true);
        Collection<ClassNode> nodes = _classGraph.getDataModel().getNodes();
        unfilterAll();
        if(_filterStrategy != null)
        {
            for (ClassNode node : nodes)
                if(_filterStrategy.filterNode(node))
                {
                    filter(node);
            }
        }
        else{
            //Log error
        }
    }

    /**
     * Sets the Filterstratgy for the ClassGraph. The FilterStrategy needs to be set before "update()" is called.
     * @param filterStrategy A filterstrategy for a ClassNode
     */
    public void setFilterStrategy(FilterStrategy filterStrategy)
    {
        _filterStrategy = filterStrategy;
    }

    public void unfilterAll() {
        // unhideAll(); not working
        //_graphHider.unhideAll();
        Collection<ClassNode> nodes = _classGraph.getDataModel().getNodes();
        for(ClassNode node : nodes)
        {
            unfilter(node);
        }

        Collection<ClassDependency> dependencies = _classGraph.getDataModel().getEdges();
        for(ClassDependency dependency : dependencies)
        {
            unfilter(dependency);
        }
    }

    public void filter(final Node node)
    {
        _graphHider.hide(node);
    }

    public void unfilter(final ClassDependency dependency)
    {
        Edge edge = _classGraph.getEdge(dependency);
        if(edge != null)
        {
            unfilter(edge);
        }
    }

    public void unfilter(final Edge edge)
    {
        _graphHider.unhideEdge(edge);
    }

   private void unfilter(final ClassNode classGraphNode)
    {
        Node node = _classGraph.getNode(classGraphNode);
        if(node != null)
        {
            unfilter(node);
        }
    }

    public void unfilter(final Node node)
    {
        _graphHider.unhideNode(node, true);
    }

    public void filter(final ClassNode classGraphNode)
    {
        Node node = _classGraph.getNode(classGraphNode);
        if(node != null)
        {
            filter(node);
        }
    }

    public void clear()
    {
        _nodeShouldHideMap.clear();
    }
}
