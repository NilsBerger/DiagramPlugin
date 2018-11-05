package graphapi;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.view.Graph2DView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import material.ClassNodeMaterial;
import material.TraceLinkDependencyMaterial;
import org.jetbrains.annotations.NotNull;
import werkzeuge.ToolWindowWerkzeug;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.geom.Point2D;

public class ClassGraphToolWindow implements ProjectComponent {

    private Project _project;
    private ClassGraph _generalClassGraph;
    private ClassGraph _javaClassGraph;
    private ClassGraph _swiftClassGraph;
    private ToolWindowWerkzeug _werkzeug;
    private ClassGraphComponent _classGraphComponent;

    public static final String TOOL_WINDOW_ID = "Class Graph";
    public static final Key<ClassGraph> GENERAL_GRAPH_KEY = Key.create("General Graph");
    public static final Key<ClassGraph> JAVA_GRAPH_KEY = Key.create("Java Graph");
    public static final Key<ClassGraph> SWIFT_GRAPH_KEY = Key.create("Swift Graph");

    public ClassGraphToolWindow(final Project project)
    {
        _project = project;
        _werkzeug = new ToolWindowWerkzeug();
    }


    public ClassGraph getGeneralClassGraph() {
        return _generalClassGraph;
    }
    public ClassGraph getJavaClassGraph() { return _javaClassGraph;}
    public ClassGraph getSwiftClassGraph() {
        return _swiftClassGraph;
    }

    public Project getProject() {
        return _project;
    }

    public void initComponent() {

    }

    public void disposeComponent() {

    }

    @NotNull
    public String getComponentName() {
        return "graphapi.ClassraphToolWindow";
    }

    public void projectOpened() {
        createToolWindow();
    }

    public void projectClosed() {
        destroyToolWindow();
    }

    private void createToolWindow() {
        ToolWindowManager toolWindowManager;
        toolWindowManager = ToolWindowManager.getInstance(_project);

        toolWindowManager.registerToolWindow(TOOL_WINDOW_ID, false, ToolWindowAnchor.BOTTOM);

        _generalClassGraph = ClassGraph.createGeneralGraph(_project);
        _javaClassGraph = ClassGraph.createJavaGraph(_project);
        _swiftClassGraph = ClassGraph.createSwiftGraph(_project);

        _project.putUserData(GENERAL_GRAPH_KEY, _generalClassGraph);
        _project.putUserData(JAVA_GRAPH_KEY, _javaClassGraph);
        _project.putUserData(SWIFT_GRAPH_KEY, _swiftClassGraph);

        _generalClassGraph.initialize();
        _javaClassGraph.initialize();
        _swiftClassGraph.initialize();

        ToolWindow toolWindow = toolWindowManager.getToolWindow(ClassGraphToolWindow.TOOL_WINDOW_ID);
        JComponent _classGraphComponent = new ClassGraphComponent(_generalClassGraph,_javaClassGraph, _swiftClassGraph,  _werkzeug).getComponent();
        Content content = toolWindow.getContentManager().getFactory().createContent(_classGraphComponent, "", false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.activate(null);
        addFocusOnNode();
    }

    private void destroyToolWindow() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(_project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow(ClassGraphToolWindow.TOOL_WINDOW_ID);
        toolWindow.getContentManager().removeAllContents(true);
        toolWindowManager.unregisterToolWindow(TOOL_WINDOW_ID);
    }

    private void addFocusOnNode()
    {
        _werkzeug.getTaceabilityWerkzeug().getTraceablilityList().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
               e.getSource();
               TraceLinkDependencyMaterial link = _werkzeug.getTaceabilityWerkzeug().getTraceablilityList().getSelectedValue();

               zoomToNode(_javaClassGraph, link.getJavaClassNode());
               zoomToNode(_swiftClassGraph, link.getSwiftClassNodeMaterial());
            }
        });
    }

    private void zoomToNode(ClassGraph graph, ClassNodeMaterial classNodeMaterial)
    {
        final Graph2DView view = graph.getView();
        Node node = graph.getNode(new ClassGraphNode(classNodeMaterial));
        double x = view.getGraph2D().getX(node);
        double y = view.getGraph2D().getY(node);
        graph.getView().focusView(1.0, new Point2D.Double(x,y),true);
    }
}
