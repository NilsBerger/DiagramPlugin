package werkzeuge.graphwerkzeug.presentation;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.actions.AbstractGraphAction;
import com.intellij.openapi.graph.builder.actions.layout.AbstractLayoutAction;
import com.intellij.openapi.graph.settings.GraphSettings;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.openapi.graph.view.Graph2DView;
import com.intellij.openapi.project.Project;

public class TraceabilityLayouterAction extends AbstractGraphAction<Graph2D> {
    private final ClassGraph _classGraph;
    private GraphBuilder _builder;


    public TraceabilityLayouterAction(ClassGraph graph) {
        super(graph.getGraph(), "Set current layout", AllIcons.Graph.Layout);
        _classGraph = graph;
        _builder = graph.getGraphBuilder();
    }

    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Graph2D graph2D) {
        Graph2DView view = AbstractGraphAction.getGraph2DView(graph2D);
        Project project = getProject(anActionEvent);
        if (project != null) {
            GraphBuilder builder = this.getBuilder(anActionEvent);
            if (builder != null) {
                GraphSettings settings = builder.getGraphPresentationModel().getSettings();
                TraceabilityLayouter traceabilityLayouter = new TraceabilityLayouter();
                traceabilityLayouter.setClassGraph(_classGraph);
                AbstractLayoutAction.doLayout(view, traceabilityLayouter, project, settings);
            }
        }
    }

    @Override
    protected void update(AnActionEvent e, Graph2D graph) {
        Project project = getProject(e);
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(presentation != null && graph != null && graph.getNodeArray().length > 0 && canLayout(this.getBuilder(e), project));
    }

    public static boolean canLayout(GraphBuilder graphBuilder, Project project) {
        if(graphBuilder == null)
        {
            return false;
        }
        else{
            try{
                return graphBuilder.getGraphPresentationModel().getSettings().getCurrentLayouter().canLayout(graphBuilder.getGraph());
            }catch (Exception ex)
            {
                System.out.println("ERROR: Can not do Layout TraceabilityLayouterAction");
                return false;
            }
        }

    }

}