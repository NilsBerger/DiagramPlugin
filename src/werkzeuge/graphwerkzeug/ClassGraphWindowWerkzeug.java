package werkzeuge.graphwerkzeug;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.view.Graph2DSelectionEvent;
import com.intellij.openapi.graph.view.Graph2DSelectionListener;
import com.intellij.openapi.graph.view.Graph2DView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import materials.ClassDependency;
import materials.ClassNode;
import materials.JavaClassNode;
import org.jetbrains.annotations.NotNull;
import service.ChangePropagationProcess;
import werkzeuge.ToolWindowWerkzeug;
import werkzeuge.graphwerkzeug.model.ClassGraphEdge;
import werkzeuge.graphwerkzeug.model.ClassGraphNode;
import werkzeuge.graphwerkzeug.presentation.ClassGraph;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

public class ClassGraphWindowWerkzeug implements ProjectComponent {

    private Project _project;
    private ClassGraph _generalClassGraph;
    private ClassGraph _javaClassGraph;
    private ClassGraph _swiftClassGraph;
    private ToolWindowWerkzeug _werkzeug;
    private ChangePropagationProcess _propagationProcess;
    private ClassGraphPopupMenu _popupMenu;

    public static final String TOOL_WINDOW_ID = "Class Graph";
    public static final Key<ClassGraph> GENERAL_GRAPH_KEY = Key.create("General Graph");
    public static final Key<ClassGraph> JAVA_GRAPH_KEY = Key.create("Java Graph");
    public static final Key<ClassGraph> SWIFT_GRAPH_KEY = Key.create("Swift Graph");

    public ClassGraphWindowWerkzeug(final Project project)
    {
        _project = project;
        _werkzeug = new ToolWindowWerkzeug();
        _propagationProcess = ChangePropagationProcess.getInstance();
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

        ToolWindow toolWindow = toolWindowManager.getToolWindow(ClassGraphWindowWerkzeug.TOOL_WINDOW_ID);
        JComponent _classGraphComponent = new ClassGraphWindowWerkzeugUI(_generalClassGraph,_javaClassGraph, _swiftClassGraph,  _werkzeug).getComponent();
        Content content = toolWindow.getContentManager().getFactory().createContent(_classGraphComponent, "", false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.activate(null);
        addFocusOnNode();
       // a();
    }

    private void destroyToolWindow() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(_project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow(ClassGraphWindowWerkzeug.TOOL_WINDOW_ID);
        toolWindow.getContentManager().removeAllContents(true);
        toolWindowManager.unregisterToolWindow(TOOL_WINDOW_ID);
    }

    private void addFocusOnNode()
    {
        _werkzeug.getTaceabilityWerkzeug().getTraceablilityList().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
               e.getSource();
               ClassDependency link = _werkzeug.getTaceabilityWerkzeug().getTraceablilityList().getSelectedValue();
               if(link.getIndependentClass() instanceof JavaClassNode)
               {
                   zoomToNode(_javaClassGraph, link.getIndependentClass());
               }
               else{
                   zoomToNode(_swiftClassGraph, link.getIndependentClass());
               }
                if(link.getDependentClass() instanceof JavaClassNode)
                {
                    zoomToNode(_javaClassGraph, link.getDependentClass());
                }
                else{
                    zoomToNode(_swiftClassGraph, link.getDependentClass());
                }
            }
        });
    }

    private void a()
    {
        _javaClassGraph.getGraph().addGraph2DSelectionListener(new Graph2DSelectionListener() {
            @Override
            public void onGraph2DSelectionEvent(Graph2DSelectionEvent graph2DSelectionEvent) {
                GraphBuilder<ClassGraphNode, ClassGraphEdge> graphBuilder = _javaClassGraph.getGraphBuilder();
                if(graph2DSelectionEvent.isNodeSelection())
                {
                    final Graph2DView view = _javaClassGraph.getView();
                    Node selectedNode = (Node)graph2DSelectionEvent.getSubject();
                    ClassGraphNode classGraphNode = graphBuilder.getNodeObject(selectedNode);
                    ClassNode classNode = classGraphNode.getClassNode();
                    int x = (int) view.getGraph2D().getX(selectedNode);
                    int y = (int) view.getGraph2D().getY(selectedNode);

                    JBPopupMenu popupMenu = new JBPopupMenu();
                    JBMenuItem changedMenuItem = new JBMenuItem("Changed");
                    changedMenuItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new ClassGraphPopupMenu(_javaClassGraph).getNodePopup(selectedNode).show(_javaClassGraph.getView().getJComponent(),x,y);
                        }
                    });
                    new ClassGraphPopupMenu(_javaClassGraph).getNodePopup(selectedNode).show(_javaClassGraph.getView().getJComponent(),x,y);


                }

                System.out.println(graph2DSelectionEvent.getGraph2D().selectedNodes().node());
            }
        });
    }


    private void zoomToNode(ClassGraph graph, ClassNode classNode)
    {
        final Graph2DView view = graph.getView();
        Node node = graph.getNode(new ClassGraphNode(classNode));
        double x = view.getGraph2D().getX(node);
        double y = view.getGraph2D().getY(node);
        graph.getView().focusView(1.0, new Point2D.Double(x,y),true);
    }
}
