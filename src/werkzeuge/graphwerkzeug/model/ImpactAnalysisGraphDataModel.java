package werkzeuge.graphwerkzeug.model;

import com.intellij.openapi.graph.builder.NodesGroup;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import service.functional.ChangePropagationProcess;
import service.functional.ClassDependencyChangeListener;
import service.functional.ClassNodeChangeListener;
import valueobjects.Language;
import valueobjects.RelationshipType;
import werkzeuge.graphwerkzeug.util.ClassGraphLogger;

import java.util.*;

public class ImpactAnalysisGraphDataModel extends com.intellij.openapi.graph.builder.GraphDataModel<ProgramEntity, ProgramEntityRelationship> implements ClassDependencyChangeListener, ClassNodeChangeListener {

    private Set<ProgramEntity> _nodes;
    private Map<ProgramEntity, Set<ProgramEntityRelationship>> _nodesEdges;
    private Set<ProgramEntityRelationship> _edges;
    private Language _languageType;
    protected ChangePropagationProcess _changePropagationProcess = ChangePropagationProcess.getInstance();

    public ImpactAnalysisGraphDataModel() {
        init();
    }

    public ImpactAnalysisGraphDataModel(Language language) {
        if (language != null) {
            _languageType = language;
        }
        init();
    }

    private void init() {
        _nodes = new HashSet<>();
        _nodesEdges = new HashMap<>();
        _edges = new HashSet<>();
        // addNodeChangeListener();
        _changePropagationProcess.addClassNodeChangeListener(this);
        _changePropagationProcess.addClassDependencyListener(this);
        final Set<ProgramEntityRelationship> affectedDependencies = _changePropagationProcess.getAffectedDependenciesByChange();
        ((ObservableSet<ProgramEntityRelationship>) affectedDependencies).addListener(new SetChangeListener<ProgramEntityRelationship>() {
            @Override
            public void onChanged(Change<? extends ProgramEntityRelationship> change) {
                addEdge(change.getElementAdded());
            }
        });

    }


    @Nullable
    @Override
    public NodesGroup getGroup(ProgramEntity programEntity) {
        return super.getGroup(programEntity);
    }

    @NotNull
    @Override
    public Collection<ProgramEntity> getNodes() {
        return _nodes;
    }

    @NotNull
    @Override
    public Collection<ProgramEntityRelationship> getEdges() {
        return _edges;
    }

    @NotNull
    @Override
    public ProgramEntity getSourceNode(final ProgramEntityRelationship programEntityRelationship) {
        return programEntityRelationship.getIndependentClass();
    }

    @NotNull
    @Override
    public ProgramEntity getTargetNode(final ProgramEntityRelationship programEntityRelationship) {
        return programEntityRelationship.getDependentClass();
    }

    @NotNull
    @Override
    public String getNodeName(final ProgramEntity programEntity) {
        return programEntity.getSimpleName();
    }

    @NotNull
    @Override
    public String getEdgeName(ProgramEntityRelationship programEntityRelationship) {
        return programEntityRelationship.getRelationshipType().name();
    }

    @Nullable
    @Override
    public ProgramEntityRelationship createEdge(@NotNull ProgramEntity classGraphNode, @NotNull ProgramEntity n1) {
        return null;
    }


    public void refreshDataModel(final ProgramEntity changedProgramEntity) {
        if (_languageType != null) {
            if (changedProgramEntity.getLanguage() == _languageType) {
                refreshNeighbourhood(changedProgramEntity);
            }
        } else {
            refreshNeighbourhood(changedProgramEntity);
        }
    }

    public void refreshNeighbourhood(final ProgramEntity programEntity) {
        addNode(programEntity);
        final ObservableMap<ProgramEntity, Set<ProgramEntityRelationship>> affectedNodeEdges = _changePropagationProcess.getAffectedNodeEdges();
        final Set<ProgramEntityRelationship> dependencies = affectedNodeEdges.get(programEntity);
        final Set<ProgramEntityRelationship> dependencies1 = _changePropagationProcess.getModel().getDependencies();
        for (ProgramEntityRelationship relationship : dependencies1) {
            if (_changePropagationProcess.getAffectedClassesByChange().contains(relationship.getIndependentClass()) && _changePropagationProcess.getAffectedClassesByChange().contains(relationship.getDependentClass())) {
                addEdge(relationship);
            }
        }
        addAll(dependencies);
    }

    public void dispose() {
        _nodes.clear();
        _edges.clear();
        _nodes = null;
        _edges = null;
    }

    public void addNode(final ProgramEntity node) {
        boolean added = _nodes.add(node);
        if (added) {
            ClassGraphLogger.debug("addNode - Added Node : " + node);
        } else {
            ClassGraphLogger.debug("addNode - Node already in model : " + node);
        }
    }

    public void addEdge(final ProgramEntityRelationship edge) {
        if (edge.getRelationshipType() == RelationshipType.InconsistentRelationship) {
            return;
        }
        if (rightNodes(edge)) {
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

    }

    private boolean rightNodes(ProgramEntityRelationship edge) {
        if (_languageType == null) {
            return true;
        }
        if (rightNode(edge.getDependentClass()) && rightNode(edge.getIndependentClass())) {
            return true;
        }
        return false;
    }

    private boolean rightNode(ProgramEntity node) {
        if (_languageType == null) {
            return true;
        }
        if (node.getLanguage() == _languageType) {
            return true;
        }
        return false;
    }

    public void addNodeEdge(final ProgramEntity node, final ProgramEntityRelationship edge) {
        Set<ProgramEntityRelationship> nodeEdges = _nodesEdges.get(node);
        if (nodeEdges == null) {
            nodeEdges = new HashSet<>();
            _nodesEdges.put(node, nodeEdges);
        }
        nodeEdges.add(edge);
    }

    public void addAll(final Set<ProgramEntityRelationship> edges) {
        if (edges != null) {
            for (ProgramEntityRelationship edge : edges) {
                addEdge(edge);
            }
        }

    }

    public void removeNodeEdges(final ProgramEntity node) {
        Set<ProgramEntityRelationship> nodeEdges = _nodesEdges.get(node);
        if (nodeEdges != null) {
            for (ProgramEntityRelationship nodeEdge : nodeEdges) {
                removeEdge(nodeEdge);
            }
        }
    }

    public Set<ProgramEntityRelationship> getNodeEdgesforNode(ProgramEntity node) {
        return _nodesEdges.get(node);
    }

    public boolean removeNode(final ProgramEntity node) {
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

    public boolean removeEdge(final ProgramEntityRelationship edge) {
        boolean removed = _edges.remove(edge);
        if (removed) {
            ClassGraphLogger.debug("removeEdge - Removed edge : " + edge);
        } else {
            ClassGraphLogger.debug("removeEdge - Edge not in model : " + edge);
        }
        return removed;
    }
//    public void updateData(Set<ClassNode> changedClassNodes)
//    {
//        d
//    }

    protected void clear() {
        _nodes.clear();
        _nodesEdges.clear();
        _edges.clear();
    }

    @Override
    public void updateDependencies(Set<ProgramEntityRelationship> changedDependencies) {
        clear();
        ObservableSet<ProgramEntity> affectedClassesByChange = _changePropagationProcess.getAffectedClassesByChange();
        for (ProgramEntity programEntity : affectedClassesByChange) {
            refreshDataModel(programEntity);
        }
    }

    @Override
    public void updateView() {
        clear();
        ObservableSet<ProgramEntity> affectedClassesByChange = _changePropagationProcess.getAffectedClassesByChange();
        for (ProgramEntity programEntity : affectedClassesByChange) {
            refreshDataModel(programEntity);
        }

////        }
//        for(ClassNode classNode : changedClassNodes)
//        {
//            //refreshDataModel(classNode);
//            final Set<ClassDependency> affectedDependencies = _changePropagationProcess.getAffectedDependencies(classNode);
//            addAll(affectedDependencies);
//
//            if(_languageType != null)
//            {
//                if(changedClassNode.getLanguage() == _languageType)
//                {
//                    refreshNeighbourhood(changedClassNode);
//                }
//            }
//            else
//            {
//                refreshNeighbourhood(changedClassNode);
//            }
//        }
    }
}
