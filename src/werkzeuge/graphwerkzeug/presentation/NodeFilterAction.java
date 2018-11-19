package werkzeuge.graphwerkzeug.presentation;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.graph.builder.actions.AbstractGraphAction;
import com.intellij.openapi.graph.view.Graph2D;
import werkzeuge.graphwerkzeug.model.ClassGraphFilterer;

public class NodeFilterAction extends AbstractGraphAction {
    ClassGraph _classGraph;
    ClassGraphFilterer _filter;

    public NodeFilterAction(ClassGraph classGraph)
    {
        super(classGraph.getGraph(), "Filter", AllIcons.Actions.Show);
        _classGraph = classGraph;
        _filter = new ClassGraphFilterer(_classGraph);

    }
    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Graph2D graph2D) {
        _filter.setFilterOn(true);
        _filter.update(graph2D, _classGraph.getView());
    }
}
