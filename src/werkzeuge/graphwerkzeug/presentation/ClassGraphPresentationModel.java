package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.base.Graph;
import com.intellij.openapi.graph.builder.components.BasicGraphPresentationModel;
import com.intellij.openapi.graph.view.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import werkzeuge.graphwerkzeug.ClassGraphPopupMenu;
import werkzeuge.graphwerkzeug.model.ClassGraphEdge;
import werkzeuge.graphwerkzeug.model.ClassGraphNode;
import werkzeuge.graphwerkzeug.model.GerneralClassGraphDataModel;

public class ClassGraphPresentationModel extends BasicGraphPresentationModel<ClassGraphNode, ClassGraphEdge> {
    private ClassGraph _classGraph;

    public ClassGraphPresentationModel(final Graph graph)
    {
        super(graph);
        setShowEdgeLabels(false);
    }

    public void setClassGraph(final ClassGraph graph)
    {
        this._classGraph = graph;
        setGraph(graph.getGraph());
        setGraphBuilder(graph.getGraphBuilder());

        new ClassNodeRenderer(_classGraph.getGraphBuilder());
    }

//    @Override
//    public DeleteProvider getDeleteProvider() {
//        return new DeleteProvider<ClassGraphNode, ClassGraphEdge>() {
//            @Override
//            public boolean canDeleteNode(@NotNull ClassGraphNode node) {
//                return false;
//            }
//
//            @Override
//            public boolean canDeleteEdge(@NotNull ClassGraphEdge edge) {
//                return false;
//            }
//
//            @Override
//            public boolean deleteNode(@NotNull ClassGraphNode node) {
//                boolean removed = getDataModel().removeNode(node);
//                _classGraph.queueGraphUpdate();
//                return removed;
//            }
//
//            @Override
//            public boolean deleteEdge(@NotNull ClassGraphEdge edge) {
//                boolean removed = getDataModel().removeEdge(edge);
//                _classGraph.queueGraphUpdate();
//                return removed;
//            }
//        };
   // }





    private GerneralClassGraphDataModel getDataModel()
    {
        return _classGraph.getDataModel();
    }

    @Override
    public void customizeSettings(Graph2DView view, EditMode editMode) {
        PopupMode defaultPopupMode = null;
        ViewMode vm = editMode.getPopupMode();
        if(vm instanceof PopupMode)
        {
            defaultPopupMode = (PopupMode) vm;
        }
        ClassGraphPopupMenu popupMode = new ClassGraphPopupMenu(_classGraph);
        editMode.setPopupMode(popupMode);

        editMode.allowMoveSelection(false);
        editMode.allowMoving(true);
        editMode.allowNodeCreation(false);
        editMode.allowEdgeCreation(false);
    }



    @Override
    public String getNodeTooltip(@Nullable ClassGraphNode node) {
        return node.getName();
    }

    @NotNull
    @Override
    public NodeRealizer getNodeRealizer(@Nullable ClassGraphNode node) {
        return ClassGraphRealizerFactory.createDefaultNodeRealizer(node);
    }
}
