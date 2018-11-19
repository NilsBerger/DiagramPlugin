package werkzeuge.graphwerkzeug.presentation;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.graph.builder.actions.AbstractGraphAction;
import com.intellij.openapi.graph.view.Graph2D;
import werkzeuge.graphwerkzeug.model.ClassGraphFilterer;

public class NodeUnfilterAction extends AbstractGraphAction {
    ClassGraph _classGraph;
    ClassGraphFilterer _filter;

    public NodeUnfilterAction(ClassGraph classGraph)
    {
        super(classGraph.getGraph(), "Unfilter", AllIcons.Actions.ShowHiddens);
        _classGraph = classGraph;
        _filter = new ClassGraphFilterer(_classGraph);

    }
    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Graph2D graph2D) {
        _filter.setFilterOn(false);
        _filter.update(graph2D, _classGraph.getView());
    }
}