

package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.base.DataProvider;
import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.layout.LayoutGraph;
import materials.ProgramEntityRelationship;
import valueobjects.RelationshipType;
import werkzeuge.graphwerkzeug.ImpactAnalysisGraph;

public class DirectedEdgeDataProvider implements DataProvider {

    final LayoutGraph _graph;
    final ImpactAnalysisGraph _impactAnalysisGraph;

    public DirectedEdgeDataProvider(final ImpactAnalysisGraph impactAnalysisGraph, final LayoutGraph graph) {
        _impactAnalysisGraph = impactAnalysisGraph;
        _graph = graph;
    }

    @Override
    public Object get(Object o) {
        return null;
    }

    @Override
    public int getInt(Object o) {
        return 0;
    }

    @Override
    public double getDouble(Object o) {
        return 0;
    }

    @Override
    public boolean getBool(Object o) {
        if (o instanceof Edge) {
            Edge edge = (Edge) o;
            final ProgramEntityRelationship programEntityRelationship = _impactAnalysisGraph.getProgramEntityRelationship(edge);
            if (edge != null) {
                return programEntityRelationship.getRelationshipType() != RelationshipType.InconsistentRelationship;
            }
            throw new IllegalStateException("Could not find ProgramentityRelationship for edge:" + edge);

        }
        return false;
    }
}
