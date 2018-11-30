package werkzeuge.graphwerkzeug.presentation.toolbaractions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.graph.builder.actions.AbstractGraphAction;
import com.intellij.openapi.graph.view.Graph2D;
import werkzeuge.graphwerkzeug.presentation.ClassGraph;
import werkzeuge.graphwerkzeug.presentation.graphfilter.ClassGraphFilterer;
import werkzeuge.graphwerkzeug.presentation.graphfilter.MarkingFilterStrategy;
import werkzeuge.graphwerkzeug.presentation.graphfilter.NodeUnfilterStrategy;

public class MarkingFilterAction extends AbstractGraphAction {

    ClassGraph _classGraph;
    ClassGraphFilterer _filter;

    public MarkingFilterAction(ClassGraph classGraph)
    {
        super(classGraph.getGraph(), "Only show changed Classes", AllIcons.Actions.ShowChangesOnly);
        _classGraph = classGraph;
        _filter = new ClassGraphFilterer(_classGraph);

    }

    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Graph2D graph2D) {
        _filter.setFilterStrategy(new MarkingFilterStrategy());
        _filter.update(graph2D, _classGraph.getView());
    }
}
