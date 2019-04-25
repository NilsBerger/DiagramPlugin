
package werkzeuge.graphwerkzeug;

import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.view.Graph2DView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import materials.ProgramEntity;
import materials.TraceLinkProgramEntityAssociation;
import valueobjects.Language;
import werkzeuge.ToolWindowWerkzeug;
import werkzeuge.finalcontextwerkzeug.FinalContextWerkzeug;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class GraphWindowWerkzeug {

    private final Project _project;
    private ImpactAnalysisGraph _generalImpactAnalysisGraph;
    private ImpactAnalysisGraph _javaImpactAnalysisGraph;
    private ImpactAnalysisGraph _swiftImpactAnalysisGraph;
    private ImpactAnalysisGraph _evalImpactAnalysisGraph;
    private final ToolWindowWerkzeug _toolWindowWerkzeug;
    private GraphWindowWerkzeugUI _ui;

    private static final Key<ImpactAnalysisGraph> GENERAL_GRAPH_KEY = Key.create("General Graph");
    private static final Key<ImpactAnalysisGraph> EVAL_GRAPH_KEY = Key.create("Eval Graph");
    private static final Key<ImpactAnalysisGraph> JAVA_GRAPH_KEY = Key.create("Java Graph");
    private static final Key<ImpactAnalysisGraph> SWIFT_GRAPH_KEY = Key.create("Swift Graph");

    public GraphWindowWerkzeug(final Project project, final ToolWindowWerkzeug toolWindowWerkzeug) {
        _project = project;
        initImpactAnalysisGraphs();
        _toolWindowWerkzeug = toolWindowWerkzeug;

        _ui = new GraphWindowWerkzeugUI(_generalImpactAnalysisGraph, _javaImpactAnalysisGraph, _swiftImpactAnalysisGraph, _evalImpactAnalysisGraph);
        addFocusOnNode();
    }


    private void initImpactAnalysisGraphs() {

        _generalImpactAnalysisGraph = ImpactAnalysisGraph.createGraph(_project, null);
        _javaImpactAnalysisGraph = ImpactAnalysisGraph.createGraph(_project, Language.Java);
        _swiftImpactAnalysisGraph = ImpactAnalysisGraph.createGraph(_project, Language.Swift);
        _evalImpactAnalysisGraph = ImpactAnalysisGraph.createEvalGraph(_project);

        _project.putUserData(GENERAL_GRAPH_KEY, _generalImpactAnalysisGraph);
        _project.putUserData(JAVA_GRAPH_KEY, _javaImpactAnalysisGraph);
        _project.putUserData(SWIFT_GRAPH_KEY, _swiftImpactAnalysisGraph);
        _project.putUserData(EVAL_GRAPH_KEY, _evalImpactAnalysisGraph);

        _generalImpactAnalysisGraph.initialize();
        _javaImpactAnalysisGraph.initialize();
        _swiftImpactAnalysisGraph.initialize();
        _evalImpactAnalysisGraph.initialize();

    }


    private void addFocusOnNode() {
        ;

        _toolWindowWerkzeug.getTraceabilityWerkzeug().getTraceablilityTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = _toolWindowWerkzeug.getTraceabilityWerkzeug().getTraceablilityTable().getSelectedRow();
                int convertRow = _toolWindowWerkzeug.getTraceabilityWerkzeug().getTraceablilityTable().convertRowIndexToModel(selectedRow);
                if (convertRow != -1) {
                    TraceLinkProgramEntityAssociation traceLink = _toolWindowWerkzeug.getTraceabilityWerkzeug().getModel().getTraceLinkforRow(convertRow);

                    if (traceLink.getIndependentClass().getLanguage() == Language.Java) {
                        zoomToNode(_javaImpactAnalysisGraph, traceLink.getIndependentClass());
                    } else {
                        zoomToNode(_swiftImpactAnalysisGraph, traceLink.getIndependentClass());
                    }
                    if (traceLink.getDependentClass().getLanguage() == Language.Java) {
                        zoomToNode(_javaImpactAnalysisGraph, traceLink.getDependentClass());
                    } else {
                        zoomToNode(_swiftImpactAnalysisGraph, traceLink.getDependentClass());
                    }
                }
            }
        });
        registerFocusOnNode(_toolWindowWerkzeug.getJavaContextWerkzeug());
        registerFocusOnNode(_toolWindowWerkzeug.getSwiftContextWerkzeug());


    }

    private void registerFocusOnNode(FinalContextWerkzeug werkzeug) {

        werkzeug.getJBTable().getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = werkzeug.getJBTable().getSelectedRow();
            int convertRow = werkzeug.getJBTable().convertRowIndexToModel(selectedRow);
            ProgramEntity selectedProgramEntity = werkzeug.getTableModel().getClassNodeFromRow(convertRow);
            if (selectedProgramEntity != null) {
                final int selectedIndex = _ui.getTabbedPane().getSelectedIndex();
                if (selectedIndex == GraphWindowWerkzeugUI.CLASS_GRAPH_INDEX) {
                    zoomToNode(_generalImpactAnalysisGraph, selectedProgramEntity);
                } else if (selectedIndex == GraphWindowWerkzeugUI.SEPERATED_CLASS_GRAPH_INDEX) {
                    final Language language = selectedProgramEntity.getLanguage();
                    if (language == Language.Java) {
                        zoomToNode(_javaImpactAnalysisGraph, selectedProgramEntity);
                    }
                    if (language == Language.Swift) {
                        zoomToNode(_swiftImpactAnalysisGraph, selectedProgramEntity);
                    }
                }
                if (selectedIndex == GraphWindowWerkzeugUI.EVALUATION_CLASS_GRAPH_INDEX) {
                    zoomToNode(_evalImpactAnalysisGraph, selectedProgramEntity);
                } else {
                    throw new IllegalArgumentException("Unkown index for TabbedPane: '" + selectedIndex + "'");
                }
            }
        });
    }


    private void zoomToNode(ImpactAnalysisGraph graph, ProgramEntity programEntity) {
        final Graph2DView view = graph.getView();
        Node node = graph.getNode(programEntity);
        if (node != null) {
            double x = view.getGraph2D().getX(node);
            double y = view.getGraph2D().getY(node);
            graph.getView().focusView(1.0, new Point2D.Double(x, y), true);
        }
    }

    public JComponent getUI() {
        return _ui.getComponent();
    }
}
