package werkzeuge.crossplattfromImpactanalysistoolwindowwerkzeug;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;
import werkzeuge.ToolWindowWerkzeug;
import werkzeuge.graphwerkzeug.GraphWindowWerkzeug;

public class CrossPlatformImpactAnalysisToolWindowWerkzeug implements ProjectComponent {

    private final Project _project;
    private static final String CROSS_PLATFORM_IMPACT_ANALYSIS_WINDOW_ID = "Crossplatform Impact Analysis Tool";
    private final CrossPlatformImpactAnalysisToolWindowWerkzeugUI _ui;

    private final GraphWindowWerkzeug _graphWerkzeug;
    private final ToolWindowWerkzeug _toolWindowWerkzeug;

    public CrossPlatformImpactAnalysisToolWindowWerkzeug(Project project) {
        _project = project;
        _toolWindowWerkzeug = new ToolWindowWerkzeug();
        _graphWerkzeug = new GraphWindowWerkzeug(project, _toolWindowWerkzeug);

        _ui = new CrossPlatformImpactAnalysisToolWindowWerkzeugUI(_graphWerkzeug.getUI(), _toolWindowWerkzeug.getPanel());
    }

    public Project getProject() {
        return _project;
    }


    @NotNull
    public String getComponentName() {
        return "graphapi.ClassraphToolWindow";
    }

    public void projectOpened() {
        createWindow();
    }

    public void projectClosed() {
        destroyToolWindow();
    }

    private void createWindow() {
        ToolWindowManager toolWindowManager;
        toolWindowManager = ToolWindowManager.getInstance(_project);

        toolWindowManager.registerToolWindow(CROSS_PLATFORM_IMPACT_ANALYSIS_WINDOW_ID, false, ToolWindowAnchor.BOTTOM);
        final ToolWindow toolWindow = toolWindowManager.getToolWindow(CROSS_PLATFORM_IMPACT_ANALYSIS_WINDOW_ID);
        Content content = toolWindow.getContentManager().getFactory().createContent(_ui.getUI(), "", false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.activate(null);
    }

    private void destroyToolWindow() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(_project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow(CROSS_PLATFORM_IMPACT_ANALYSIS_WINDOW_ID);
        toolWindow.getContentManager().removeAllContents(true);
        toolWindowManager.unregisterToolWindow(CROSS_PLATFORM_IMPACT_ANALYSIS_WINDOW_ID);
    }

}
