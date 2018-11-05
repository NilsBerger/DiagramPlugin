package graphapi;

import com.intellij.openapi.graph.builder.GraphDataModel;
import com.intellij.openapi.graph.builder.NodesGroup;
import material.ClassNodeMaterial;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import service.ChangePropagationChangeListener;
import service.ChangePropagationProcessService;

import java.util.*;

public class GerneralClassGraphDataModel extends GraphDataModel<ClassGraphNode, ClassGraphEdge> implements ChangePropagationChangeListener {

    private Set<ClassGraphNode> _nodes;
    private Map<ClassGraphNode, Set<ClassGraphEdge>> _nodesEdges;
    private Set<ClassGraphEdge> _edges;
    protected ChangePropagationProcessService _changePropagationProcessService;

    public GerneralClassGraphDataModel()
    {
        _nodes = new HashSet<>();
        _nodesEdges = new HashMap<>();
        _edges = new HashSet<>();
    }

    public GerneralClassGraphDataModel(final Set<ClassGraphNode> nodes, final Set<ClassGraphEdge> edges)
    {
        this._nodes = nodes;
        this._edges = edges;
    }

    

    @Nullable
    @Override
    public NodesGroup getGroup(ClassGraphNode classGraphNode) {
        //TODO
        return super.getGroup(classGraphNode);
    }

    @NotNull
    @Override
    public Collection<ClassGraphNode> getNodes() {
        return _nodes;
    }

    @NotNull
    @Override
    public Collection<ClassGraphEdge> getEdges() {
        return _edges;
    }

    @NotNull
    @Override
    public ClassGraphNode getSourceNode(final ClassGraphEdge classGraphEdge) {
        return classGraphEdge.getIndependentNode();
    }

    @NotNull
    @Override
    public ClassGraphNode getTargetNode(final ClassGraphEdge classGraphEdge) {
        return classGraphEdge.getDependentNode();
    }

    @NotNull
    @Override
    public String getNodeName(final ClassGraphNode classGraphNode) {
        return classGraphNode.getName();
    }

    @NotNull
    @Override
    public String getEdgeName(ClassGraphEdge classGraphEdge) {
        return classGraphEdge.getRelationshipType().name();
    }

    @Nullable
    @Override
    public ClassGraphEdge createEdge(@NotNull ClassGraphNode classGraphNode, @NotNull ClassGraphNode n1) {
        return null;
    }

    public void setChangePropagationService(final ChangePropagationProcessService service)
    {
        this._changePropagationProcessService = service;
        service.addGraphChangeListener(this);
        refreshDataModel();
    }
    protected void refreshDataModel()
    {
                for (ClassNodeMaterial classNodeMaterial : _changePropagationProcessService.getAffectedClassesByChange()) {
                    addNode(new ClassGraphNode(classNodeMaterial));


                    Set<ClassNodeMaterial> topdependencies = _changePropagationProcessService.getModel().getTopDependencies(classNodeMaterial);
                    Set<ClassNodeMaterial> bottompdependencies = _changePropagationProcessService.getModel().getBottomDependencies(classNodeMaterial);
                    Set<ClassNodeMaterial> neighbourhood = new HashSet<>();
                    neighbourhood.addAll(topdependencies);
                    neighbourhood.addAll(bottompdependencies);

                   Set<ClassNodeMaterial> dependencies = new HashSet<>();
                   dependencies.addAll(topdependencies);
                   dependencies.addAll(bottompdependencies);

                   addNeighbourhoodForClass(classNodeMaterial, dependencies);

                }
    }
    protected void addNeighbourhoodForClass(final ClassNodeMaterial classNodeMaterial, final Set<ClassNodeMaterial> dependencies)
    {

        for (ClassNodeMaterial topdependency : dependencies) {
            if (_changePropagationProcessService.getAffectedClassesByChange().contains(topdependency)) {

                ClassGraphNode dependentNode = new ClassGraphNode(classNodeMaterial);
                ClassGraphNode independentNode = new ClassGraphNode(topdependency);
                RelationshipType type = RelationshipType.DirectedRelationship;
                ClassGraphEdge edge = new ClassGraphEdge(dependentNode,independentNode,type);

                addEdge(edge);
            }
        }
    }

    public void dispose()
    {
        _nodes.clear();
        _edges.clear();
        _nodes = null;
        _edges = null;
    }

    public void addNode(final ClassGraphNode node)
    {
        boolean added = _nodes.add(node);
        if(added)
        {
            ClassGraphLogger.debug("addNode - Added Node : " + node);
        }
        else {
            ClassGraphLogger.debug("addNode - Node already in model : " +  node);
        }
    }

    public void addEdge(final ClassGraphEdge edge )
    {
        boolean added = _edges.add(edge);
        if(added)
        {
            ClassGraphLogger.debug("addEdge - Added Edge : " + edge);
        }
        else {
            ClassGraphLogger.debug("addEdge - Edge already in model : " +  edge);
        }
        addNode(edge.getIndependentNode());
        addNode(edge.getDependentNode());
        addNodeEdge(edge.getIndependentNode(), edge);
        addNodeEdge(edge.getDependentNode(), edge);
    }

    public void addNodeEdge(final ClassGraphNode node, final ClassGraphEdge edge)
    {
        Set<ClassGraphEdge> nodeEdges = _nodesEdges.get(node);
        if(nodeEdges == null)
        {
            nodeEdges = new HashSet<>();
            _nodesEdges.put(node, nodeEdges);
        }
        nodeEdges.add(edge);
    }

    public void addAll(final Set<ClassGraphEdge> edges)
    {
        for(ClassGraphEdge edge : edges)
        {
            addEdge(edge);
        }
    }

    public void removeNodeEdges(final ClassGraphNode node)
    {
        Set<ClassGraphEdge> nodeEdges = _nodesEdges.get(node);
        if(nodeEdges != null)
        {
            for(ClassGraphEdge nodeEdge : nodeEdges)
            {
                removeEdge(nodeEdge);
            }
        }
    }

    public boolean removeNode(final ClassGraphNode node) {
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
    public boolean removeEdge(final ClassGraphEdge edge)
    {
        boolean removed = _edges.remove(edge);
        if(removed)
        {
            ClassGraphLogger.debug("removeEdge - Removed edge : " + edge);
        }
        else{
            ClassGraphLogger.debug("removeEdge - Edge not in model : " + edge);
        }
        return removed;
    }

    @Override
    public void update() {
        refreshDataModel();
    }
}
