
package werkzeuge.graphwerkzeug.presentation.toolbaractions;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.layout.LayoutGraph;
import com.intellij.openapi.graph.layout.Layouter;
import com.intellij.openapi.graph.layout.router.OrthogonalEdgeRouter;
import werkzeuge.graphwerkzeug.ImpactAnalysisGraph;

public class EvaluationLayouter implements Layouter {

    private ImpactAnalysisGraph _impactAnalysisGraph;
    private Layouter _currentLayouter;
    OrthogonalEdgeRouter _orthogonalEdgeRouter;

    @Override
    public boolean canLayout(LayoutGraph layoutGraph) {
        return false;
    }

    @Override
    public void doLayout(LayoutGraph layoutGraph) {
        final GraphManager graphManager = GraphManager.getGraphManager();
        setClassGraph(_impactAnalysisGraph);
        _orthogonalEdgeRouter = graphManager.createOrthogonalEdgeRouter();
        _orthogonalEdgeRouter.setMinimumDistance(10);
        _orthogonalEdgeRouter.doLayout(layoutGraph);
    }

    public void setClassGraph(ImpactAnalysisGraph impactAnalysisGraph) {
        _impactAnalysisGraph = impactAnalysisGraph;
    }

    public void setCurrentLayouter(Layouter layouter) {
        _currentLayouter = layouter;
    }
}
