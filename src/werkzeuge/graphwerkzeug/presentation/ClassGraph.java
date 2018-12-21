package werkzeuge.graphwerkzeug.presentation;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.GraphBuilderFactory;
import com.intellij.openapi.graph.view.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.base.Node;

import materials.ClassDependency;
import materials.ClassNode;
import org.jetbrains.annotations.Nullable;
import service.functional.ChangePropagationProcess;
import service.functional.GraphChangeListener;
import valueobjects.ClassLanguageType;
import werkzeuge.graphwerkzeug.model.*;
import werkzeuge.graphwerkzeug.util.ClassGraphLogger;


import javax.swing.*;

public class ClassGraph implements Disposable, GraphChangeListener {

    private final GraphBuilder<ClassNode, ClassDependency> _graphBuilder;
    private static final ChangePropagationProcess _propagationProcessService = ChangePropagationProcess.getInstance();
    public final String _classGraphName;



   private ClassGraph(final Project project, final Graph2D graph, final Graph2DView view, final ClassGraphDataModel dataModel, final String name) {
        final ClassGraphPresentationModel presentationModel = new ClassGraphPresentationModel(graph);
        _graphBuilder = GraphBuilderFactory.getInstance(project).createGraphBuilder(graph, view, dataModel, presentationModel);
        presentationModel.setClassGraph(this);
        _propagationProcessService.addGraphChangeListener(this);
        _classGraphName = name;
    }


    public static ClassGraph createGraph(Project project, @Nullable ClassLanguageType classLanguageType, String name)
    {
        final Graph2D graph = GraphManager.getGraphManager().createGraph2D();
        final Graph2DView view = GraphManager.getGraphManager().createGraph2DView();
        view.setGraph2D(graph);

        final ClassGraphDataModel dataModel = new ClassGraphDataModel(classLanguageType);


        return new ClassGraph(project, graph, view, dataModel, name);
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
