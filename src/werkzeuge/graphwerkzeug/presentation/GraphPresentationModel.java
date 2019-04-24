package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.builder.components.BasicGraphPresentationModel;
import com.intellij.openapi.graph.layout.PortConstraintKeys;
import com.intellij.openapi.graph.view.*;
import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import werkzeuge.graphwerkzeug.GraphPopupMenu;
import werkzeuge.graphwerkzeug.ImpactAnalysisGraph;

public class GraphPresentationModel extends BasicGraphPresentationModel<ProgramEntity, ProgramEntityRelationship> {

    private ImpactAnalysisGraph _impactAnalysisGraph;
    private Graph2D _graph;
    private GroupEdgeTargetDataProvider _groupEdgeTargetDataProvider;

    public GraphPresentationModel(final Graph2D graph) {
        super(graph);
        _graph = graph;
    }

    public void setClassGraph(final ImpactAnalysisGraph impactAnalysisGraph) {
        _impactAnalysisGraph = impactAnalysisGraph;
        setGraphBuilder(impactAnalysisGraph.getGraphBuilder());

        _groupEdgeTargetDataProvider = new GroupEdgeTargetDataProvider(_impactAnalysisGraph, _graph);
        _graph.addDataProvider(PortConstraintKeys.SOURCE_GROUPID_KEY, _groupEdgeTargetDataProvider);

        addBridgeCalculator(impactAnalysisGraph.getView());
    }

    @Override
    public void customizeSettings(Graph2DView view, EditMode editMode) {
        final GraphPopupMenu popupMode = new GraphPopupMenu(_impactAnalysisGraph);

        editMode.setPopupMode(popupMode);
        editMode.allowMoveSelection(false);
        editMode.allowMoving(false);
        editMode.allowNodeCreation(false);
        editMode.allowEdgeCreation(false);
        editMode.allowBendCreation(false);
    }

    @Override
    public String getNodeTooltip(@Nullable ProgramEntity node) {
        return node.getSimpleName();
    }

    @NotNull
    @Override
    public NodeRealizer getNodeRealizer(@Nullable ProgramEntity node) {
        final NodeRealizer defaultNodeRealizer = ProgramEntityNodeRealizerFactory.createDefaultNodeRealizer(node);
        final GraphManager graphManager = GraphManager.getGraphManager();
        return graphManager.createGenericNodeRealizer(defaultNodeRealizer);
    }

    @NotNull
    @Override
    public EdgeRealizer getEdgeRealizer(@Nullable ProgramEntityRelationship programEntityRelationship) {
        return ProgramEntityRelationshipEdgeRealizerFactory.getUMLEdgeRealizer(programEntityRelationship);
    }

    private void addBridgeCalculator(Graph2DView view) {
        final DefaultGraph2DRenderer graph2DRenderer = (DefaultGraph2DRenderer) view.getGraph2DRenderer();
        final BridgeCalculator bridgeCalculator = GraphManager.getGraphManager().createBridgeCalculator();
        bridgeCalculator.setCrossingMode(BridgeCalculator.CROSSING_MODE_VERTICAL_CROSSES_HORIZONTAL);
        graph2DRenderer.setBridgeCalculator(bridgeCalculator);
    }
}