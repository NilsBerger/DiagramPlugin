package werkzeuge.graphwerkzeug;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.graph.builder.actions.*;
import com.intellij.openapi.graph.builder.actions.layout.ApplyCurrentLayoutAction;
import com.intellij.openapi.graph.builder.actions.printing.PrintGraphAction;
import com.intellij.openapi.graph.builder.actions.printing.PrintPreviewAction;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTabbedPane;
import org.jetbrains.annotations.NotNull;
import werkzeuge.ToolWindowWerkzeug;
import werkzeuge.graphwerkzeug.presentation.ClassGraph;
import werkzeuge.graphwerkzeug.presentation.toolbaractions.ImagePrinterAction;
import werkzeuge.graphwerkzeug.presentation.toolbaractions.NodeFilterAction;
import werkzeuge.graphwerkzeug.presentation.toolbaractions.MarkingFilterAction;
import werkzeuge.graphwerkzeug.presentation.toolbaractions.NodeUnfilterAction;

import javax.swing.*;
import java.awt.*;

public class ClassGraphWindowWerkzeugUI {
    private final static Icon _ICON = AllIcons.Nodes.Artifact;
    private final JComponent _myComponent;
    private ClassGraph _generalClassGraph;
    private ClassGraph _javaClassGraph;
    private ClassGraph _swiftClassGraph;

    private JTabbedPane _tabbedPane;


    /**
     * Creates the the user interface and defines a layout of the graph components
     * @param classGraph
     * @param javaClassGraph
     * @param swiftClassGraph
     * @param werkzeug
     */
    public ClassGraphWindowWerkzeugUI(@NotNull ClassGraph classGraph, ClassGraph javaClassGraph, ClassGraph swiftClassGraph, ToolWindowWerkzeug werkzeug) {
        _generalClassGraph = classGraph;
        _javaClassGraph = javaClassGraph;
        _swiftClassGraph = swiftClassGraph;

        _myComponent = new JBPanel(new BorderLayout());

        DefaultActionGroup generalActions = getToolbarActionGroup(_generalClassGraph);
        DefaultActionGroup javaActions = getToolbarActionGroup(_javaClassGraph);
        DefaultActionGroup swiftActions = getToolbarActionGroup(_swiftClassGraph);
        ActionToolbar generalActionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, generalActions, true);
        ActionToolbar javaActionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, javaActions, true);
        ActionToolbar swiftActionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, swiftActions, true);

        _tabbedPane = new JBTabbedPane();

        JBPanel generalPane = new JBPanel();
        generalPane.setLayout(new BorderLayout());
        generalPane.add(generalActionToolbar.getComponent(), BorderLayout.NORTH);
        generalPane.add(_generalClassGraph.getView().getComponent(), BorderLayout.CENTER);
        _tabbedPane.insertTab("Combined Class Graph", _ICON, generalPane, "", 0);

        JSplitPane splitPane = new JSplitPane();

        JBPanel javaPane = new JBPanel();
        javaPane.setLayout(new BorderLayout());
        javaPane.add(javaActionToolbar.getComponent(), BorderLayout.NORTH);
        javaPane.add(_javaClassGraph.getView().getComponent(), BorderLayout.CENTER);

        JBPanel swiftPane = new JBPanel();
        swiftPane.setLayout(new BorderLayout());
        swiftPane.add(swiftActionToolbar.getComponent(), BorderLayout.NORTH);
        swiftPane.add(_swiftClassGraph.getView().getComponent(), BorderLayout.CENTER);

        splitPane.setLeftComponent(javaPane);
        splitPane.setRightComponent(swiftPane);

        _tabbedPane.insertTab("Seperated Class Graphs", _ICON, splitPane, "",1);


        _myComponent.add(_tabbedPane, BorderLayout.CENTER);
        _myComponent.add(werkzeug.getPanel(), BorderLayout.SOUTH);

        _generalClassGraph.initialize();

    }

    /**
     * Creates the Toolbar for a given Classgraph
     * @param classgraph
     * @return
     */
    private DefaultActionGroup getToolbarActionGroup(final ClassGraph classgraph) {
        DefaultActionGroup actions = new DefaultActionGroup();
        actions.add(new ShowHideGridAction(classgraph.getGraph()));
        actions.add(new SnapToGridAction(classgraph.getGraph()));
        actions.addSeparator();

        actions.add(new ZoomInAction(classgraph.getGraph()));
        actions.add(new ZoomOutAction(classgraph.getGraph()));

        actions.add(new ActualZoomAction(classgraph.getGraph()));

        actions.add(new FitContentAction(classgraph.getGraph()));
        actions.addSeparator();

        actions.add(new ApplyCurrentLayoutAction(classgraph.getGraph()));
        //actions.add(new CustomLayouterAction(classgraph.getGraphBuilder(), _generalClassGraph.getGraphBuilder().getS));
        actions.addSeparator();

        //actions.add(new DeleteSelectionAction());
        actions.addSeparator();

        //actions.add(new PrintGraphAction(classgraph.getGraph()));
        actions.add(new ImagePrinterAction(classgraph));
        actions.add(new PrintPreviewAction(classgraph.getGraph()));
        actions.addSeparator();
        actions.add(new NodeFilterAction(classgraph));
        actions.add(new MarkingFilterAction(classgraph));
        actions.add(new NodeUnfilterAction(classgraph));

        return actions;
    }

    @NotNull
    public JComponent getComponent() {
        return _myComponent;
    }

    public JTabbedPane getTabbedPane()
    {
        return _tabbedPane;
    }

    public ClassGraph getGeneralClassGraph()
    {
        return _generalClassGraph;
    }
    public ClassGraph getJavaClassGraph()
    {
        return _javaClassGraph;
    }
    public ClassGraph getSwiftClassGraph()
    {
        return _swiftClassGraph;
    }

}



