
package werkzeuge.graphwerkzeug;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.GraphBuilderFactory;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.openapi.graph.view.Graph2DView;
import com.intellij.openapi.project.Project;
import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import org.jetbrains.annotations.Nullable;
import service.functional.ChangePropagationProcess;
import service.functional.ClassDependencyChangeListener;
import service.functional.ClassNodeChangeListener;
import valueobjects.Language;
import werkzeuge.graphwerkzeug.model.EvalImpactAnalysisGraphDataModel;
import werkzeuge.graphwerkzeug.model.ImpactAnalysisGraphDataModel;
import werkzeuge.graphwerkzeug.presentation.GraphPresentationModel;
import werkzeuge.graphwerkzeug.util.ClassGraphLogger;

import javax.swing.*;
import java.util.Set;

public class ImpactAnalysisGraph implements Disposable, ClassNodeChangeListener, ClassDependencyChangeListener {

    private final GraphBuilder<ProgramEntity, ProgramEntityRelationship> _graphBuilder;
    private static final ChangePropagationProcess _propagationProcessService = ChangePropagationProcess.getInstance();


    private ImpactAnalysisGraph(final Project project, final Graph2D graph, final Graph2DView view, final com.intellij.openapi.graph.builder.GraphDataModel dataModel) {
        final GraphPresentationModel presentationModel = new GraphPresentationModel(graph);
        _graphBuilder = GraphBuilderFactory.getInstance(project).createGraphBuilder(graph, view, dataModel, presentationModel);
        presentationModel.setClassGraph(this);

        _propagationProcessService.addClassDependencyListener(this);
        _propagationProcessService.addClassNodeChangeListener(this);
    }


    public static ImpactAnalysisGraph createGraph(Project project, @Nullable Language language) {
        final Graph2D graph = GraphManager.getGraphManager().createGraph2D();
        final Graph2DView view = GraphManager.getGraphManager().createGraph2DView();
        view.setGraph2D(graph);

        final ImpactAnalysisGraphDataModel dataModel = new ImpactAnalysisGraphDataModel(language);


        return new ImpactAnalysisGraph(project, graph, view, dataModel);
    }

    public static ImpactAnalysisGraph createEvalGraph(Project project) {
        final Graph2D graph = GraphManager.getGraphManager().createGraph2D();
        final Graph2DView view = GraphManager.getGraphManager().createGraph2DView();
        view.setGraph2D(graph);

        final com.intellij.openapi.graph.builder.GraphDataModel dataModel = new EvalImpactAnalysisGraphDataModel();


        return new ImpactAnalysisGraph(project, graph, view, dataModel);
    }


    public Project getProject() {
        return _graphBuilder.getProject();
    }

    public Graph2D getGraph() {
        return _graphBuilder.getGraph();
    }

    public Graph2DView getView() {
        return _graphBuilder.getView();
    }


    public ImpactAnalysisGraphDataModel getDataModel() {
        return (ImpactAnalysisGraphDataModel) _graphBuilder.getGraphDataModel();
    }


    void initialize() {
        _graphBuilder.initialize();
    }

    public JComponent getJComponent() {
        return getView().getJComponent();
    }


    public ProgramEntity getProgramEnitity(Node node) {
        return _graphBuilder.getNodeObject(node);
    }

    public ProgramEntityRelationship getProgramEntityRelationship(Edge edge) {
        return _graphBuilder.getEdgeObject(edge);
    }

    public Node getNode(ProgramEntity node) {
        return _graphBuilder.getNode(node);
    }

    public Edge getEdge(ProgramEntityRelationship edge) {
        return _graphBuilder.getEdge(edge);
    }

    public GraphBuilder<ProgramEntity, ProgramEntityRelationship> getGraphBuilder() {
        return _graphBuilder;
    }

    private void updateGraph() {
        long start = System.currentTimeMillis();
        _graphBuilder.updateGraph();
        long time = System.currentTimeMillis() - start;
        int modelNumOfNodes = getDataModel().getNodes().size();
        int modelNumOfEdges = getDataModel().getEdges().size();
        ClassGraphLogger.debug("Graph updated (DataModel: " + modelNumOfNodes + " nodes, " + modelNumOfEdges + " edges) , took " + time + "ms");
    }


    private void updateGraphView() {
        long start = System.currentTimeMillis();
        _graphBuilder.initialize();
        long time = System.currentTimeMillis() - start;
        ClassGraphLogger.debug("Graph view updated, took " + time + "ms");
    }

    private void fitContent() {
        getView().fitContent();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void updateDependencies(Set<ProgramEntityRelationship> changedDependencies) {
        updateGraph();
        updateGraphView();
        fitContent();
    }

    @Override
    public void updateView() {
        updateGraph();
        updateGraphView();
        fitContent();
    }
}
