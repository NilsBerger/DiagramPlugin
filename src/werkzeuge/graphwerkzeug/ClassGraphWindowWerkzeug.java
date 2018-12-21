package werkzeuge.graphwerkzeug;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.view.Graph2DView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import materials.ClassDependency;
import materials.ClassNode;
import org.jetbrains.annotations.NotNull;
import valueobjects.ClassLanguageType;
import werkzeuge.ToolWindowWerkzeug;
import werkzeuge.finalcontextwerkzeug.FinalContextWerkzeug;
import werkzeuge.graphwerkzeug.presentation.ClassGraph;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.geom.Point2D;

public class ClassGraphWindowWerkzeug implements ProjectComponent {

    private Project _project;
    private ClassGraph _generalClassGraph;
    private ClassGraph _javaClassGraph;
    private ClassGraph _swiftClassGraph;
    private ToolWindowWerkzeug _werkzeug;
    private ClassGraphWindowWerkzeugUI _ui;

    public static final String TOOL_WINDOW_ID = "Class Graph";
    public static final Key<ClassGraph> GENERAL_GRAPH_KEY = Key.create("General Graph");
    public static final Key<ClassGraph> JAVA_GRAPH_KEY = Key.create("Java Graph");
    public static final Key<ClassGraph> SWIFT_GRAPH_KEY = Key.create("Swift Graph");

    public ClassGraphWindowWerkzeug(final Project project)
    {
        _project = project;
        _werkzeug = new ToolWindowWerkzeug();
    }

    public Project getProject() {
        return _project;
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

        _generalClassGraph = ClassGraph.createGraph(_project, null, "General");
        _javaClassGraph = ClassGraph.createGraph(_project, ClassLanguageType.Java, "Java");
        _swiftClassGraph = ClassGraph.createGraph(_project, ClassLanguageType.Swift, "Swift");

        _project.putUserData(GENERAL_GRAPH_KEY, _generalClassGraph);
        _project.putUserData(JAVA_GRAPH_KEY, _javaClassGraph);
        _project.putUserData(SWIFT_GRAPH_KEY, _swiftClassGraph);

        _generalClassGraph.initialize();
        _javaClassGraph.initialize();
        _swiftClassGraph.initialize();

        ToolWindow toolWindow = toolWindowManager.getToolWindow(ClassGraphWindowWerkzeug.TOOL_WINDOW_ID);
        _ui = new ClassGraphWindowWerkzeugUI(_generalClassGraph, _javaClassGraph, _swiftClassGraph,  _werkzeug);
        Content content = toolWindow.getContentManager().getFactory().createContent(_ui.getComponent(), "", false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.activate(null);
        addFocusOnNode();
    }

    private void destroyToolWindow() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(_project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow(ClassGraphWindowWerkzeug.TOOL_WINDOW_ID);
        toolWindow.getContentManager().removeAllContents(true);
        toolWindowManager.unregisterToolWindow(TOOL_WINDOW_ID);
    }

    private void addFocusOnNode()
    {
        _werkzeug.getTraceabilityWerkzeug().getTraceablilityList().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
               e.getSource();
               ClassDependency link = _werkzeug.getTraceabilityWerkzeug().getTraceablilityList().getSelectedValue();
               if(link.getIndependentClass().getClassLanguageType() == ClassLanguageType.Java)
               {
                   zoomToNode(_javaClassGraph, link.getIndependentClass());
               }
               else{
                   zoomToNode(_swiftClassGraph, link.getIndependentClass());
               }
                if(link.getDependentClass().getClassLanguageType() == ClassLanguageType.Java)
                {
                    zoomToNode(_javaClassGraph, link.getDependentClass());
                }
                else{
                    zoomToNode(_swiftClassGraph, link.getDependentClass());
                }
            }
        });
        registerFocusOnNode(_werkzeug.getJavaContextWerkzeug());
        registerFocusOnNode(_werkzeug.getSwiftContextWerkzeug());

    }

    private void registerFocusOnNode(FinalContextWerkzeug werkzeug)
    {
        final JList jlist = werkzeug.getList();
        jlist.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Object listObject = jlist.getSelectedValue();
                if(listObject instanceof ClassNode)
                {
                    ClassNode selectedClassNode = (ClassNode) listObject;
                    final int selectedIndex = _ui.getTabbedPane().getSelectedIndex();
                    if(selectedIndex == ClassGraphWindowWerkzeugUI.CLASS_GRAPH_INDEX)
                    {
                        zoomToNode(_generalClassGraph, selectedClassNode);
                    }
                    else if(selectedIndex == ClassGraphWindowWerkzeugUI.SEPERATED_CLASS_GRAPH_INDEX)
                    {
                        final ClassLanguageType classLanguageType = selectedClassNode.getClassLanguageType();
                        if(classLanguageType == ClassLanguageType.Java)
                        {
                            zoomToNode(_javaClassGraph, selectedClassNode);
                        }
                        if(classLanguageType == ClassLanguageType.Swift)
                        {
                            zoomToNode(_swiftClassGraph, selectedClassNode);
                        }

                    }
                    else{
                        throw new IllegalArgumentException("Unkown index for TabbedPane: '" + selectedIndex +"'");
                    }
                }
            }
        });
    }
    private void zoomToNode(ClassGraph graph, ClassNode classNode)
    {
        final Graph2DView view = graph.getView();
        Node node = graph.getNode(classNode);
        if (node != null) {
            double x = view.getGraph2D().getX(node);
            double y = view.getGraph2D().getY(node);
            graph.getView().focusView(1.0, new Point2D.Double(x,y),true);
        }

    }
}
