package graphapi;


import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.actions.AbstractGraphAction;
import com.intellij.openapi.graph.builder.actions.layout.AbstractLayoutAction;
import com.intellij.openapi.graph.impl.layout.hierarchic.HierarchicGroupLayouterImpl;
import com.intellij.openapi.graph.impl.layout.hierarchic.HierarchicLayouterImpl;
import com.intellij.openapi.graph.layout.Layouter;
import com.intellij.openapi.graph.layout.hierarchic.HierarchicGroupLayouter;
import com.intellij.openapi.graph.settings.GraphSettings;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.openapi.graph.view.Graph2DView;
import com.intellij.openapi.project.Project;

public class ApplyCustomLayoutAction extends AbstractGraphAction<Graph2D> {

    private static final String NAME = "ApplyCustumLayoutAction";//ActionsBundle.message("graphapi.ApplyCustomLayoutAction", new Object[0]);

    private Graph2D _classGraph;

    public ApplyCustomLayoutAction() {
        super(NAME, AllIcons.Actions.Checked_selected);
    }

    public ApplyCustomLayoutAction(Graph2D graph2D)
    {
        super(graph2D, NAME, AllIcons.Actions.Checked_selected);
        _classGraph = graph2D;
    }

    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Graph2D graph2D) {
        Graph2DView view = AbstractGraphAction.getGraph2DView(graph2D);
        Project project = getProject(anActionEvent);
        if (project != null) {
            GraphBuilder builder = this.getBuilder(anActionEvent);
            if (builder != null) {
                GraphSettings settings = builder.getGraphPresentationModel().getSettings();

                AbstractLayoutAction.doLayout(view,settings.getGroupLayouter(), project, settings);
            }
        }
    }

    public void update(AnActionEvent e, Graph2D graph) {
        Project project = getProject(e);
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(project != null && graph != null && graph.getNodeArray().length > 0 && canLayout(this.getBuilder(e), project));
    }

    private static boolean canLayout(GraphBuilder graphBuilder, Project project) {
        if (graphBuilder == null) {
            return false;
        } else {
            try {
                return true;
                //return graphBuilder.getGraphPresentationModel().getSettings().getCurrentLayouter().canLayout(graphBuilder.getGraph());
            } catch (Exception var3) {
                return false;
            }
        }
    }
}
