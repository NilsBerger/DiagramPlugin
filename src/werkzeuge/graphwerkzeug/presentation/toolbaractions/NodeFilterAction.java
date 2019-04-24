package werkzeuge.graphwerkzeug.presentation.toolbaractions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.graph.builder.actions.AbstractGraphAction;
import com.intellij.openapi.graph.view.Graph2D;
import werkzeuge.graphwerkzeug.ImpactAnalysisGraph;
import werkzeuge.graphwerkzeug.presentation.graphfilter.APINodeFilterStrategy;
import werkzeuge.graphwerkzeug.presentation.graphfilter.ClassGraphFilterer;

public class NodeFilterAction extends AbstractGraphAction {
    private ImpactAnalysisGraph _impactAnalysisGraph;
    private ClassGraphFilterer _filter;

    public NodeFilterAction(ImpactAnalysisGraph impactAnalysisGraph)
    {
        super(impactAnalysisGraph.getGraph(), "Hide Nodes", AllIcons.Actions.Show);
        _impactAnalysisGraph = impactAnalysisGraph;
        _filter = new ClassGraphFilterer(_impactAnalysisGraph);

    }
    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Graph2D graph2D) {
        _filter.setFilterStrategy(new APINodeFilterStrategy());
        _filter.update(graph2D, _impactAnalysisGraph.getView());
    }
}
