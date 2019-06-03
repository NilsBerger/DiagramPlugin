package werkzeuge.graphwerkzeug.presentation;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.actions.AbstractGraphAction;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.openapi.project.Project;
import werkzeuge.graphwerkzeug.ImpactAnalysisGraph;

public class TraceabilityLayouterAction extends AbstractGraphAction<Graph2D> {
    private final ImpactAnalysisGraph _impactAnalysisGraph;


    public TraceabilityLayouterAction(ImpactAnalysisGraph graph) {
        super(graph.getGraph(), "Set current layout", AllIcons.Graph.Layout);
        _impactAnalysisGraph = graph;
    }

    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Graph2D graph42D) {

        _impactAnalysisGraph.getGraphBuilder().getGraphPresentationModel().getSettings().getCurrentLayouter();
        Project project = getProject(anActionEvent);
        if (project != null) {
            GraphBuilder builder = _impactAnalysisGraph.getGraphBuilder();
            if (builder != null) {
                TraceabilityLayouter traceabilityLayouter = new TraceabilityLayouter();
                traceabilityLayouter.setClassGraph(_impactAnalysisGraph);
                traceabilityLayouter.doLayout(_impactAnalysisGraph.getGraph());

                builder.getGraphPresentationModel().getSettings().setCurrentLayouter(traceabilityLayouter);

                _impactAnalysisGraph.updateView();
            }
        }
    }

    @Override
    protected void update(AnActionEvent e, Graph2D graph) {
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(presentation != null && graph != null && graph.getNodeArray().length > 0 && canLayout(this.getBuilder(e)));
    }

    public static boolean canLayout(GraphBuilder graphBuilder) {
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