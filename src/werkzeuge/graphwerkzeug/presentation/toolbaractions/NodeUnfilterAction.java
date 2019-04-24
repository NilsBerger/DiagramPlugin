package werkzeuge.graphwerkzeug.presentation.toolbaractions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.graph.builder.actions.AbstractGraphAction;
import com.intellij.openapi.graph.view.Graph2D;
import werkzeuge.graphwerkzeug.ImpactAnalysisGraph;
import werkzeuge.graphwerkzeug.presentation.graphfilter.ClassGraphFilterer;
import werkzeuge.graphwerkzeug.presentation.graphfilter.NodeUnfilterStrategy;

public class NodeUnfilterAction extends AbstractGraphAction {
    private final ImpactAnalysisGraph _impactAnalysisGraph;
    private final ClassGraphFilterer _filter;

    public NodeUnfilterAction(ImpactAnalysisGraph impactAnalysisGraph)
    {
        super(impactAnalysisGraph.getGraph(), "Unfilter", AllIcons.Actions.ShowHiddens);
        _impactAnalysisGraph = impactAnalysisGraph;
        _filter = new ClassGraphFilterer(_impactAnalysisGraph);

    }
    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Graph2D graph2D) {
        _filter.setFilterStrategy(new NodeUnfilterStrategy());
        _filter.update(graph2D, _impactAnalysisGraph.getView());
    }
}