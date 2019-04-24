package werkzeuge.graphwerkzeug.presentation.graphfilter;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.builder.CustomGraphUpdater;
import com.intellij.openapi.graph.util.GraphHider;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.openapi.graph.view.Graph2DView;
import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import werkzeuge.graphwerkzeug.ImpactAnalysisGraph;

import java.util.Collection;

/**
 *
 */
public class ClassGraphFilterer extends CustomGraphUpdater {

    private GraphHider _graphHider;
    private ImpactAnalysisGraph _impactAnalysisGraph;
    private FilterStrategy _filterStrategy;

    public ClassGraphFilterer(ImpactAnalysisGraph impactAnalysisGraph) {
        _impactAnalysisGraph = impactAnalysisGraph;
    }

    /**
     * Updates the Graph for the given FilterStrategy. If no Stragey
     *
     * @param graph2D The graph2D
     * @param graph2DView The graph2DView
     */
    @Override
    public void update(Graph2D graph2D, Graph2DView graph2DView) {
        _graphHider = GraphManager.getGraphManager().createGraphHider(graph2D);
        _graphHider.setFireGraphEventsEnabled(true);
        Collection<ProgramEntity> nodes = _impactAnalysisGraph.getDataModel().getNodes();
        unfilterAll();
        if (_filterStrategy != null) {
            for (ProgramEntity node : nodes)
                if (_filterStrategy.filterNode(node)) {
                    filter(node);
                }
        }
    }

    /**
     * Sets the Filterstratgy for the ClassGraph. The FilterStrategy needs to be set before "update()" is called.
     *
     * @param filterStrategy A filterstrategy for a ClassNode
     */
    public void setFilterStrategy(FilterStrategy filterStrategy) {
        _filterStrategy = filterStrategy;
    }

    private void unfilterAll() {
        Collection<ProgramEntity> nodes = _impactAnalysisGraph.getDataModel().getNodes();
        for (ProgramEntity node : nodes) {
            unfilter(node);
        }

        Collection<ProgramEntityRelationship> dependencies = _impactAnalysisGraph.getDataModel().getEdges();
        for (ProgramEntityRelationship dependency : dependencies) {
            unfilter(dependency);
        }
    }

    private void filter(final Node node) {
        _graphHider.hide(node);
    }

    private void unfilter(final ProgramEntityRelationship dependency) {
        Edge edge = _impactAnalysisGraph.getEdge(dependency);
        if (edge != null) {
            unfilter(edge);
        }
    }

    private void unfilter(final Edge edge) {
        _graphHider.unhideEdge(edge);
    }

    private void unfilter(final ProgramEntity classGraphNode) {
        Node node = _impactAnalysisGraph.getNode(classGraphNode);
        if (node != null) {
            unfilter(node);
        }
    }

    private void unfilter(final Node node) {
        _graphHider.unhideNode(node, true);
    }

    private void filter(final ProgramEntity classGraphNode) {
        Node node = _impactAnalysisGraph.getNode(classGraphNode);
        if (node != null) {
            filter(node);
        }
    }
}
