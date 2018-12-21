package werkzeuge.graphwerkzeug.presentation.toolbaractions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.graph.builder.actions.AbstractGraphAction;
import com.intellij.openapi.graph.view.Graph2D;
import werkzeuge.graphwerkzeug.presentation.ClassGraph;
import werkzeuge.graphwerkzeug.presentation.graphfilter.APINodeFilterStrategy;
import werkzeuge.graphwerkzeug.presentation.graphfilter.NodeFilterStrategy;
import werkzeuge.graphwerkzeug.presentation.graphfilter.ClassGraphFilterer;

public class NodeFilterAction extends AbstractGraphAction {
    ClassGraph _classGraph;
    ClassGraphFilterer _filter;

    public NodeFilterAction(ClassGraph classGraph)
    {
        super(classGraph.getGraph(), "Hide Nodes", AllIcons.Actions.Show);
        _classGraph = classGraph;
        _filter = new ClassGraphFilterer(_classGraph);

    }
    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Graph2D graph2D) {
        _filter.setFilterStrategy(new APINodeFilterStrategy());
        _filter.update(graph2D, _classGraph.getView());
    }
}
