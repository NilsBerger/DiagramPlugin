package werkzeuge.graphwerkzeug.presentation;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.base.Graph;
import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.GraphBuilderFactory;
import com.intellij.openapi.graph.layout.LayoutGraph;
import com.intellij.openapi.graph.view.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.base.Node;

import materials.ClassDependency;
import materials.ClassNode;
import org.jetbrains.annotations.Nullable;
import service.ChangePropagationProcess;
import service.GraphChangeListener;
import valueobjects.ClassLanguageType;
import werkzeuge.graphwerkzeug.model.*;
import werkzeuge.graphwerkzeug.util.ClassGraphLogger;


import javax.swing.*;

public class ClassGraph implements Disposable, GraphChangeListener {

    private GraphBuilder<ClassNode, ClassDependency> _graphBuilder;
    private static final ChangePropagationProcess _propagationProcessService = ChangePropagationProcess.getInstance();
    //private final TraceabilityLayouter _layouter;
    private ClassGraphDataModel _dataModel;


    public ClassGraph(Project project, Graph2D graph, Graph2DView view, ClassGraphDataModel dataModel, ClassGraphPresentationModel presentationModel) {
        _graphBuilder = GraphBuilderFactory.getInstance(project).createGraphBuilder(graph, view, dataModel, presentationModel);


        _dataModel = dataModel;
        //_layouter = layouter;
        presentationModel.setClassGraph(this);
        _propagationProcessService.addGraphChangeListener(this);
        view.setGraph2D(graph);
        //_graphBuilder.getGraphPresentationModel().getSettings().setCurrentLayouter(layouter);
        //_layouter.setClassGraph(this);
    }


    public static ClassGraph createGraph(Project project, @Nullable ClassLanguageType classLanguageType)
    {
        Graph2D graph = GraphManager.getGraphManager().createGraph2D();
        Graph2DView view = GraphManager.getGraphManager().createGraph2DView();

        ClassGraphDataModel dataModel = new ClassGraphDataModel(classLanguageType);
        ClassGraphPresentationModel presentationModel = new ClassGraphPresentationModel(graph);
        //TraceabilityLayouter layouter = new TraceabilityLayouter();
        //LayoutGraph layoutGraph = GraphManager.getGraphManager().createDefaultLayoutGraph();


        //layouter.setLayoutGraph(layoutGraph);

        return new ClassGraph(project, graph, view,  dataModel, presentationModel);
    }

    public Project getProject()
    {
        return _graphBuilder.getProject();
    }

    public Graph2D getGraph()
    {
        return _graphBuilder.getGraph();
    }

    public Graph2DView getView()
    {
        return _graphBuilder.getView();
    }

    public TraceabilityLayouter getTraceabilityLayouter()
    {
        return null;
    }

    public ClassGraphDataModel getDataModel()
    {
        return (ClassGraphDataModel) _graphBuilder.getGraphDataModel();
    }

    public  ClassGraphPresentationModel getPresentationMode()
    {
        return (ClassGraphPresentationModel) _graphBuilder.getGraphPresentationModel();
    }


    public void initialize()
    {
        _graphBuilder.initialize();
    }

    public JComponent getJComponent()
    {
        return getView().getJComponent();
    }

    public void clear()
    {
        getGraph().clear();
    }

    public ClassNode getClassNode(Node node)
    {
        return _graphBuilder.getNodeObject(node);
    }
    public ClassDependency getClassGraphEdge(Edge edge)
    {
        return _graphBuilder.getEdgeObject(edge);
    }
    public Node getNode(ClassNode node)
    {
        return _graphBuilder.getNode(node);
    }

    public Edge getEdge(ClassDependency edge)
    {
        return _graphBuilder.getEdge(edge);
    }

    public void removeNode(ClassNode classGraphNode)
    {
        Node node = _graphBuilder.getNode(classGraphNode);
        getDataModel().getNodes().remove(classGraphNode);
        getGraph().removeNode(node);
    }

    public void removeNode(Node node)
    {
        getGraph().removeNode(node);
    }

    public GraphBuilder<ClassNode, ClassDependency> getGraphBuilder() {
        return _graphBuilder;
    }

    public void updateGraph()
    {
        long start = System.currentTimeMillis();
        _graphBuilder.updateGraph();
        long time = System.currentTimeMillis() - start;
        int modelNumOfNodes = getDataModel().getNodes().size();
        int modelNumOfEdges = getDataModel().getEdges().size();
        ClassGraphLogger.debug("Graph updated (DataModel: " + modelNumOfNodes + " nodes, " + modelNumOfEdges + " edges) , took " + time + "ms");
    }



    public void updateGraphView() {
        long start = System.currentTimeMillis();
        _graphBuilder.initialize();
        long time = System.currentTimeMillis() - start;
        ClassGraphLogger.debug("Graph view updated, took " + time + "ms");
    }

    public void fitContent()
    {
        getView().fitContent();
    }
    public void queueGraphUpdate() {
        _graphBuilder.queueUpdate();
    }



    @Override
    public void dispose() {
        _propagationProcessService.removeChangeListener(this);
    }

    @Override
    public void updateView() {
        updateGraph();
        updateGraphView();
        fitContent();
    }
}
