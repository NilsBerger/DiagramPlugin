package werkzeuge.graphwerkzeug.model;

import com.intellij.openapi.graph.builder.GraphDataModel;
import com.intellij.openapi.graph.builder.NodesGroup;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import materials.ClassDependency;
import materials.ClassNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import service.functional.ChangePropagationProcess;
import service.functional.GraphChangeListener;
import valueobjects.ClassLanguageType;
import werkzeuge.graphwerkzeug.util.ClassGraphLogger;
import valueobjects.RelationshipType;

import java.util.*;

public class ClassGraphDataModel extends GraphDataModel<ClassNode, ClassDependency> implements GraphChangeListener {

    private Set<ClassNode> _nodes;
    private Map<ClassNode, Set<ClassDependency>> _nodesEdges;
    private Set<ClassDependency> _edges;
    private  ClassLanguageType _languageType;
    protected ChangePropagationProcess _changePropagationProcess = ChangePropagationProcess.getInstance();

    public ClassGraphDataModel() {
        init();
    }
    public ClassGraphDataModel(ClassLanguageType classLanguageType) {
        if(classLanguageType != null)
        {
            _languageType = classLanguageType;
        }
        init();
    }

    private void init()
    {
        _nodes = new HashSet<>();
        _nodesEdges = new HashMap<>();
        _edges = new HashSet<>();
        addNodeChangeListener();
        _changePropagationProcess.addGraphChangeListener(this);
    }


    @Nullable
    @Override
    public NodesGroup getGroup(ClassNode classNode) {
        return super.getGroup(classNode);
    }

    @NotNull
    @Override
    public Collection<ClassNode> getNodes() {
        return _nodes;
    }

    @NotNull
    @Override
    public Collection<ClassDependency> getEdges() {
        return _edges;
    }

    @NotNull
    @Override
    public ClassNode getSourceNode(final ClassDependency classDependency) {
        return classDependency.getIndependentClass();
    }

    @NotNull
    @Override
    public ClassNode getTargetNode(final ClassDependency classDependency) {
        return classDependency.getDependentClass();
    }

    @NotNull
    @Override
    public String getNodeName(final ClassNode classNode) {
        return classNode.getSimpleName();
    }

    @NotNull
    @Override
    public String getEdgeName(ClassDependency classDependency) {
        return classDependency.getRelationshipType().name();
    }

    @Nullable
    @Override
    public ClassDependency createEdge(@NotNull ClassNode classGraphNode, @NotNull ClassNode n1) {
        return null;
    }

    private void addNodeChangeListener() {
        _changePropagationProcess.getAffectedClassesByChange().addListener(new SetChangeListener<ClassNode>() {
            @Override
            public void onChanged(Change<? extends ClassNode> change) {
                if(change.wasAdded())
                {
                    refreshDataModel(change.getElementAdded());
                }
            }
        });
    }

    public void refreshDataModel(final ClassNode changedClassNode) {
        if(_languageType != null)
        {
            if(changedClassNode.getClassLanguageType() == _languageType)
            {
                refreshNeighbourhood(changedClassNode);
            }
        }
        else
        {
            refreshNeighbourhood(changedClassNode);
        }
    }

    private void refreshNeighbourhood(final ClassNode classNode)
    {
        addNode(classNode);
        Set<ClassDependency> affectedDependencies = _changePropagationProcess.getAffectedDependencies(classNode);
        addAll(affectedDependencies);
    }

    public void dispose() {
        _nodes.clear();
        _edges.clear();
        _nodes = null;
        _edges = null;
    }

    public void addNode(final ClassNode node) {
        boolean added = _nodes.add(node);
        if (added) {
            ClassGraphLogger.debug("addNode - Added Node : " + node);
        } else {
            ClassGraphLogger.debug("addNode - Node already in model : " + node);
        }
    }

    public void addEdge(final ClassDependency edge) {
        if(edge.getRelationshipType() == RelationshipType.InconsistentRelationship)
        {
            return;
        }

        boolean added = _edges.add(edge);
        if (added) {
            ClassGraphLogger.debug("addEdge - Added Edge : " + edge);
        } else {
            ClassGraphLogger.debug("addEdge - Edge already in model : " + edge);
        }
        addNode(edge.getIndependentClass());
        addNode(edge.getDependentClass());
        addNodeEdge(edge.getIndependentClass(), edge);
        addNodeEdge(edge.getDependentClass(), edge);
    }

    public void addNodeEdge(final ClassNode node, final ClassDependency edge) {
        Set<ClassDependency> nodeEdges = _nodesEdges.get(node);
        if (nodeEdges == null) {
            nodeEdges = new HashSet<>();
            _nodesEdges.put(node, nodeEdges);
        }
        nodeEdges.add(edge);
    }

    public void addAll(final Set<ClassDependency> edges) {
        for (ClassDependency edge : edges) {
            addEdge(edge);
        }
    }

    public void removeNodeEdges(final ClassNode node) {
        Set<ClassDependency> nodeEdges = _nodesEdges.get(node);
        if (nodeEdges != null) {
            for (ClassDependency nodeEdge : nodeEdges) {
                removeEdge(nodeEdge);
            }
        }
    }

    public boolean removeNode(final ClassNode node) {
        boolean removed = _nodes.remove(node);
        if (removed) {
            ClassGraphLogger.debug("removeNode - Removed node : " + node);
            removeNodeEdges(node);
            _nodesEdges.remove(node);
        } else {
            ClassGraphLogger.debug("addNode - Node not in model : " + node);
        }
        return removed;
    }

    public boolean removeEdge(final ClassDependency edge) {
        boolean removed = _edges.remove(edge);
        if (removed) {
            ClassGraphLogger.debug("removeEdge - Removed edge : " + edge);
        } else {
            ClassGraphLogger.debug("removeEdge - Edge not in model : " + edge);
        }
        return removed;
    }
    private void clear()
    {
        _nodes.clear();
        _nodesEdges.clear();
        _edges.clear();
    }

    @Override
    public void updateView() {
        clear();
        ObservableSet<ClassNode> affectedClassesByChange = _changePropagationProcess.getAffectedClassesByChange();
        for(ClassNode classNode : affectedClassesByChange)
        {
            refreshDataModel(classNode);
        }
    }
}
