package werkzeuge.graphwerkzeug;

import com.intellij.icons.AllIcons;
import com.intellij.icons.AllIcons.Graph;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.actions.*;
import com.intellij.openapi.graph.builder.actions.layout.AbstractLayoutAction;
import com.intellij.openapi.graph.builder.actions.layout.ApplyCurrentLayoutAction;
import com.intellij.openapi.graph.builder.actions.printing.PrintPreviewAction;
import com.intellij.openapi.graph.layout.Layouter;
import com.intellij.openapi.graph.settings.GraphSettings;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.openapi.graph.view.Graph2DView;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTabbedPane;
import org.jetbrains.annotations.NotNull;
import werkzeuge.ToolWindowWerkzeug;
import werkzeuge.graphwerkzeug.presentation.ClassGraph;
import werkzeuge.graphwerkzeug.presentation.TraceabilityLayouterAction;
import werkzeuge.graphwerkzeug.presentation.toolbaractions.ImagePrinterAction;
import werkzeuge.graphwerkzeug.presentation.toolbaractions.NodeFilterAction;
import werkzeuge.graphwerkzeug.presentation.toolbaractions.MarkingFilterAction;
import werkzeuge.graphwerkzeug.presentation.toolbaractions.NodeUnfilterAction;
import werkzeuge.graphwerkzeug.util.GraphUtils;

import javax.swing.*;
import java.awt.*;

public class ClassGraphWindowWerkzeugUI {

    public static final int CLASS_GRAPH_INDEX = 0;
    public static final int SEPERATED_CLASS_GRAPH_INDEX = 1;

    private final static Icon _ICON = AllIcons.Nodes.Artifact;
    private final JComponent _myComponent;
    private final ClassGraph _generalClassGraph;
    private final ClassGraph _javaClassGraph;
    private final ClassGraph _swiftClassGraph;

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

        final DefaultActionGroup generalActions = getToolbarActionGroup(_generalClassGraph);
        final DefaultActionGroup javaActions = getToolbarActionGroup(_javaClassGraph);
        final DefaultActionGroup swiftActions = getToolbarActionGroup(_swiftClassGraph);
        ActionToolbar generalActionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, generalActions, true);
        ActionToolbar javaActionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, javaActions, true);
        ActionToolbar swiftActionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, swiftActions, true);

        _tabbedPane = new JBTabbedPane();

        JBPanel generalPane = new JBPanel();
        generalPane.setLayout(new BorderLayout());
        generalPane.add(generalActionToolbar.getComponent(), BorderLayout.NORTH);
        generalPane.add(_generalClassGraph.getView().getComponent(), BorderLayout.CENTER);
        _tabbedPane.insertTab("Combined Class Graph", _ICON, generalPane, "", CLASS_GRAPH_INDEX);

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

        _tabbedPane.insertTab("Seperated Class Graphs", _ICON, splitPane, "",SEPERATED_CLASS_GRAPH_INDEX);


        _myComponent.add(_tabbedPane, BorderLayout.CENTER);
        _myComponent.add(werkzeug.getPanel(), BorderLayout.SOUTH);

        _generalClassGraph.initialize();

        //Set Layouter
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
        actions.add(new TraceabilityLayouterAction(classgraph));
        actions.addSeparator();

        //actions.add(new PrintGraphAction(classgraph.getGraph()));
        actions.add(new ImagePrinterAction(classgraph));
        actions.add(new PrintPreviewAction(classgraph.getGraph()));

        actions.addSeparator();
        actions.add(new NodeFilterAction(classgraph));
        actions.add(new MarkingFilterAction(classgraph));
        actions.add(new NodeUnfilterAction(classgraph));
        actions.addSeparator();

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



}



