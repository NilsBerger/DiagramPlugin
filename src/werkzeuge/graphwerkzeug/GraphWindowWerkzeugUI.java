package werkzeuge.graphwerkzeug;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.graph.builder.actions.*;
import com.intellij.openapi.graph.builder.actions.printing.PrintPreviewAction;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTabbedPane;
import org.jetbrains.annotations.NotNull;
import werkzeuge.graphwerkzeug.presentation.TraceabilityLayouterAction;
import werkzeuge.graphwerkzeug.presentation.toolbaractions.*;
import werkzeuge.graphwerkzeug.presentation.toolbaractions.nodefilterwerkzeug.NodeFilterDialogOpenAction;

import javax.swing.*;
import java.awt.*;

public class GraphWindowWerkzeugUI {

    public static final int CLASS_GRAPH_INDEX = 0;
    public static final int SEPERATED_CLASS_GRAPH_INDEX = 1;
    public static final int EVALUATION_CLASS_GRAPH_INDEX = 2;

    private static final String GENERAL_TOOLBAR_ID = "GENERALID";
    private static final String JAVA_TOOLBAR_ID = "JAVAID";
    private static final String SWIFT_TOOLBAR_ID = "SWIFTID";
    private static final String EVAL_TOOLBAR_ID = "EVALID";

    private final static Icon _ICON = AllIcons.Nodes.Artifact;
    private final JComponent _myComponent;
    private final ImpactAnalysisGraph _generalImpactAnalysisGraph;
    private final ImpactAnalysisGraph _javaImpactAnalysisGraph;
    private final ImpactAnalysisGraph _swiftImpactAnalysisGraph;
    private final ImpactAnalysisGraph _evalImpactAnalysisGraph;

    private JTabbedPane _tabbedPane;


    /**
     * Creates the the user interface and defines a layout of the graph components
     *
     * @param impactAnalysisGraph
     * @param javaImpactAnalysisGraph
     * @param swiftImpactAnalysisGraph
     */
    public GraphWindowWerkzeugUI(@NotNull ImpactAnalysisGraph impactAnalysisGraph, ImpactAnalysisGraph javaImpactAnalysisGraph, ImpactAnalysisGraph swiftImpactAnalysisGraph, ImpactAnalysisGraph evalImpactAnalysisGraph) {
        _generalImpactAnalysisGraph = impactAnalysisGraph;
        _javaImpactAnalysisGraph = javaImpactAnalysisGraph;
        _swiftImpactAnalysisGraph = swiftImpactAnalysisGraph;
        _evalImpactAnalysisGraph = evalImpactAnalysisGraph;

        _myComponent = new JBPanel(new BorderLayout());

        final DefaultActionGroup generalActions = getToolbarActionGroup(_generalImpactAnalysisGraph);
        final DefaultActionGroup javaActions = getToolbarActionGroup(_javaImpactAnalysisGraph);
        final DefaultActionGroup swiftActions = getToolbarActionGroup(_swiftImpactAnalysisGraph);
        final DefaultActionGroup evalActions = getToolbarActionGroup(_evalImpactAnalysisGraph);

        ActionToolbar generalActionToolbar = ActionManager.getInstance().createActionToolbar(GENERAL_TOOLBAR_ID, generalActions, true);
        ActionToolbar javaActionToolbar = ActionManager.getInstance().createActionToolbar(JAVA_TOOLBAR_ID, javaActions, true);
        ActionToolbar swiftActionToolbar = ActionManager.getInstance().createActionToolbar(SWIFT_TOOLBAR_ID, swiftActions, true);
        ActionToolbar evalActionToolbar = ActionManager.getInstance().createActionToolbar(EVAL_TOOLBAR_ID, evalActions, true);

        _tabbedPane = new JBTabbedPane();

        JBPanel generalPane = new JBPanel();
        generalPane.setLayout(new BorderLayout());
        generalPane.add(generalActionToolbar.getComponent(), BorderLayout.NORTH);
        generalPane.add(_generalImpactAnalysisGraph.getView().getComponent(), BorderLayout.CENTER);
        _tabbedPane.insertTab("Combined Class Graph", _ICON, generalPane, "", CLASS_GRAPH_INDEX);

        JSplitPane splitPane = new JSplitPane();

        JBPanel javaPane = new JBPanel();
        javaPane.setLayout(new BorderLayout());
        javaPane.add(javaActionToolbar.getComponent(), BorderLayout.NORTH);
        javaPane.add(_javaImpactAnalysisGraph.getView().getComponent(), BorderLayout.CENTER);

        JBPanel swiftPane = new JBPanel();
        swiftPane.setLayout(new BorderLayout());
        swiftPane.add(swiftActionToolbar.getComponent(), BorderLayout.NORTH);
        swiftPane.add(_swiftImpactAnalysisGraph.getView().getComponent(), BorderLayout.CENTER);

        splitPane.setLeftComponent(javaPane);
        splitPane.setRightComponent(swiftPane);

        _tabbedPane.insertTab("Seperated Class Graphs", _ICON, splitPane, "", SEPERATED_CLASS_GRAPH_INDEX);

        JBPanel evalPane = new JBPanel();
        evalPane.setLayout(new BorderLayout());
        evalPane.add(evalActionToolbar.getComponent(), BorderLayout.NORTH);
        evalPane.add(_evalImpactAnalysisGraph.getView().getComponent(), BorderLayout.CENTER);
        _tabbedPane.insertTab("Evaluation", _ICON, evalPane, "", EVALUATION_CLASS_GRAPH_INDEX);


        _myComponent.add(_tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Creates the Toolbar for a given Classgraph
     *
     * @param classgraph The Classgraph for the Toolbar
     * @return The Toolbar for the Swing-UI
     */
    private static DefaultActionGroup getToolbarActionGroup(final ImpactAnalysisGraph classgraph) {
        DefaultActionGroup actions = new DefaultActionGroup();
        actions.add(new ShowHideGridAction(classgraph.getGraph()));
        actions.add(new SnapToGridAction(classgraph.getGraph()));
        actions.addSeparator();

        actions.add(new ZoomInAction(classgraph.getGraph()));
        actions.add(new ZoomOutAction(classgraph.getGraph()));
        actions.add(new ActualZoomAction(classgraph.getGraph()));
        actions.add(new FitContentAction(classgraph.getGraph()));

        actions.addSeparator();
        actions.add(new UndoAction());
        actions.add(new RedoAction());


        actions.addSeparator();
        actions.add(new TraceabilityLayouterAction(classgraph));
        actions.add(new TraceabilityCompLayouterAction(classgraph));
        actions.addSeparator();

        //actions.add(new PrintGraphAction(classgraph.getGraph()));
        actions.add(new ImagePrinterAction(classgraph));
        actions.add(new PrintPreviewAction(classgraph.getGraph()));

        actions.addSeparator();
        actions.add(new NodeFilterDialogOpenAction(classgraph));
        //actions.add(new NodeFilterAction(classgraph));
        //actions.add(new MarkingFilterAction(classgraph));
        actions.add(new NodeUnfilterAction(classgraph));
        actions.addSeparator();

        return actions;
    }

    @NotNull
    public JComponent getComponent() {
        return _myComponent;
    }

    JTabbedPane getTabbedPane() {
        return _tabbedPane;
    }
}



