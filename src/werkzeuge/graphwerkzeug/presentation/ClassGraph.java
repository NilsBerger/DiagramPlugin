package werkzeuge.graphwerkzeug.presentation;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.GraphBuilderFactory;
import com.intellij.openapi.graph.view.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.base.Node;
import com.intellij.util.containers.ContainerUtil;
import java.awt.geom.Point2D;

import materials.ClassDependency;
import materials.ClassNode;
import service.ChangePropagationProcess;
import service.GraphChangeListener;
import valueobjects.ClassNodeType;
import werkzeuge.graphwerkzeug.model.*;
import werkzeuge.graphwerkzeug.util.ClassGraphLogger;


import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassGraph implements Disposable, GraphChangeListener {

    private GraphBuilder<ClassNode, ClassDependency> _graphBuilder;
    private static final ChangePropagationProcess _propagationProcessService = ChangePropagationProcess.getInstance();


    public ClassGraph(Project project, Graph2D graph, Graph2DView view, GerneralClassGraphDataModel dataModel, ClassGraphPresentationModel presentationModel)
    {
        _graphBuilder = GraphBuilderFactory.getInstance(project).createGraphBuilder(graph, view, dataModel, presentationModel);
        presentationModel.setClassGraph(this);
        _propagationProcessService.addGraphChangeListener(this);
        graph.addGraph2DSelectionListener(new Graph2DSelectionListener() {
            @Override
            public void onGraph2DSelectionEvent(Graph2DSelectionEvent graph2DSelectionEvent) {
                if(graph2DSelectionEvent.isNodeSelection())
                {
                    Node node = (Node) graph2DSelectionEvent.getSubject();
                    ClassNode classNode = _graphBuilder.getNodeObject(node);
                }

            }
        });
    }

    public static ClassGraph createGeneralGraph(Project project)
    {
        Graph2D graph = GraphManager.getGraphManager().createGraph2D();
        Graph2DView view = GraphManager.getGraphManager().createGraph2DView();
        GerneralClassGraphDataModel dataModel = new GerneralClassGraphDataModel();
        ClassGraphPresentationModel presentationModel = new ClassGraphPresentationModel(graph);

        return new ClassGraph(project, graph, view,  dataModel, presentationModel);
    }
    public static ClassGraph createSwiftGraph(Project project)
    {
        Graph2D graph = GraphManager.getGraphManager().createGraph2D();
        Graph2DView view = GraphManager.getGraphManager().createGraph2DView();
        GerneralClassGraphDataModel dataModel = new SwiftClassGraphDataModel();
        ClassGraphPresentationModel presentationModel = new ClassGraphPresentationModel(graph);

        return new ClassGraph(project, graph, view,  dataModel, presentationModel);
    }
    public static ClassGraph createJavaGraph(Project project)
    {
        Graph2D graph = GraphManager.getGraphManager().createGraph2D();
        Graph2DView view = GraphManager.getGraphManager().createGraph2DView();
        GerneralClassGraphDataModel dataModel = new JavaClassGraphDataModel();
        ClassGraphPresentationModel presentationModel = new ClassGraphPresentationModel(graph);

        return new ClassGraph(project, graph, view,  dataModel, presentationModel);
    }


    public void zoomToNode(Node node)
    {
         final Graph2DView view = getView();
         double x = view.getGraph2D().getX(node);
         double y = view.getGraph2D().getY(node);
        getView().focusView(1.0, new Point2D.Double(x,y),true);
    }
    private void selectedNodes()
    {
        final List<ClassNode> toSelect = new ArrayList<>(Arrays.asList(new ClassNode("List", ClassNodeType.Java)));
        final Graph2D graph = _graphBuilder.getGraph();
        for(final ClassNode node : toSelect)
        {
            Node graphNode = _graphBuilder.getNode(node);
            graph.setSelected(graphNode, true);
        }
    }

    public List<ClassNode> getSelectedClassNodes()
    {
        final List<ClassNode> selected = new ArrayList<>();
        final Graph2D graph = _graphBuilder.getGraph();
        for(final Node node : graph.getNodeArray())
        {
            if(graph.isSelected(node)){
                final ClassNode nodeObject = _graphBuilder.getNodeObject(node);
                if(nodeObject != null)
                {
                    ContainerUtil.addIfNotNull(nodeObject, selected);
                }
            }
        }
        return selected;
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

    public GerneralClassGraphDataModel getDataModel()
    {
        return (GerneralClassGraphDataModel) _graphBuilder.getGraphDataModel();
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

    public ClassNode getClassGraphNode(Node node)
    {
        return  _graphBuilder.getNodeObject(node);
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
        //updateGraphView();
        fitContent();
    }
}
