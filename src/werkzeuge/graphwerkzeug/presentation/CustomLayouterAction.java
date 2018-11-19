package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.actions.AbstractGraphAction;
import com.intellij.openapi.graph.layout.Layouter;
import com.intellij.openapi.graph.view.Graph2D;
import werkzeuge.graphwerkzeug.util.GraphUtils;

public class CustomLayouterAction extends AbstractGraphAction {

    GraphBuilder _builder;

    public CustomLayouterAction(GraphBuilder builder)
    {
        _builder = builder;
    }
    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Graph2D graph2D) {
        Layouter layouter = GraphUtils.createLayouter(false);
        //_builder.ad
    }
}
