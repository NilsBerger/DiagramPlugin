package werkzeuge.graphwerkzeug.presentation.toolbaractions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.graph.builder.actions.AbstractGraphAction;
import com.intellij.openapi.graph.view.Graph2D;
import werkzeuge.graphwerkzeug.ImpactAnalysisGraph;
import werkzeuge.graphwerkzeug.presentation.graphfilter.ClassGraphFilterer;
import werkzeuge.graphwerkzeug.presentation.graphfilter.MarkingFilterStrategy;

public class MarkingFilterAction extends AbstractGraphAction {

    ImpactAnalysisGraph _impactAnalysisGraph;
    ClassGraphFilterer _filter;

    public MarkingFilterAction(ImpactAnalysisGraph impactAnalysisGraph)
    {
        super(impactAnalysisGraph.getGraph(), "Only show changed Classes", AllIcons.Actions.ShowChangesOnly);
        _impactAnalysisGraph = impactAnalysisGraph;
        _filter = new ClassGraphFilterer(_impactAnalysisGraph);

    }

    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Graph2D graph2D) {
        _filter.setFilterStrategy(new MarkingFilterStrategy());
        _filter.update(graph2D, _impactAnalysisGraph.getView());
    }
}
