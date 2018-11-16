/*
 * Copyright 1998-2018 Konstantin Bulenkov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package werkzeuge.graphwerkzeug.model;

import com.intellij.openapi.graph.builder.GraphDataModel;
import com.intellij.openapi.graph.builder.NodesGroup;
import javafx.collections.SetChangeListener;
import materials.ClassNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import service.ChangePropagationProcess;
import werkzeuge.graphwerkzeug.util.ClassGraphLogger;
import werkzeuge.graphwerkzeug.presentation.RelationshipType;

import java.util.*;

public class GerneralClassGraphDataModel extends GraphDataModel<ClassGraphNode, ClassGraphEdge>{

    private Set<ClassGraphNode> _nodes;
    private Map<ClassGraphNode, Set<ClassGraphEdge>> _nodesEdges;
    private Set<ClassGraphEdge> _edges;
    protected ChangePropagationProcess _changePropagationProcess = ChangePropagationProcess.getInstance();

    public GerneralClassGraphDataModel() {
        _nodes = new HashSet<>();
        _nodesEdges = new HashMap<>();
        _edges = new HashSet<>();
        addNodeChangeListener();
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
            addNode(new ClassGraphNode(changedClassNode));


            Set<ClassNode> topDependencies = _changePropagationProcess.getModel().getTopDependencies(changedClassNode);
            Set<ClassNode> bottomDependencies = _changePropagationProcess.getModel().getBottomDependencies(changedClassNode);

            addNeighbourhoodForClass(changedClassNode, topDependencies);
            addNeighbourhoodForClass(changedClassNode, bottomDependencies);

    }

    protected void addNeighbourhoodForClass(final ClassNode classNode, final Set<ClassNode> dependencies) {

        for (ClassNode topdependency : dependencies) {
           if (_changePropagationProcess.getAffectedClassesByChange().contains(topdependency)) {

                ClassGraphNode dependentNode = new ClassGraphNode(classNode);
                ClassGraphNode independentNode = new ClassGraphNode(topdependency);
                RelationshipType type = RelationshipType.DirectedRelationship;
                ClassGraphEdge edge = new ClassGraphEdge(dependentNode, independentNode, type);

                addEdge(edge);
           }
        }
    }

    public void dispose() {
        _nodes.clear();
        _edges.clear();
        _nodes = null;
        _edges = null;
    }

    public void addNode(final ClassGraphNode node) {
        boolean added = _nodes.add(node);
        if (added) {
            ClassGraphLogger.debug("addNode - Added Node : " + node);
        } else {
            ClassGraphLogger.debug("addNode - Node already in model : " + node);
        }
    }

    public void addEdge(final ClassGraphEdge edge) {
        boolean added = _edges.add(edge);
        if (added) {
            ClassGraphLogger.debug("addEdge - Added Edge : " + edge);
        } else {
            ClassGraphLogger.debug("addEdge - Edge already in model : " + edge);
        }
        addNode(edge.getIndependentNode());
        addNode(edge.getDependentNode());
        addNodeEdge(edge.getIndependentNode(), edge);
        addNodeEdge(edge.getDependentNode(), edge);
    }

    public void addNodeEdge(final ClassGraphNode node, final ClassGraphEdge edge) {
        Set<ClassGraphEdge> nodeEdges = _nodesEdges.get(node);
        if (nodeEdges == null) {
            nodeEdges = new HashSet<>();
            _nodesEdges.put(node, nodeEdges);
        }
        nodeEdges.add(edge);
    }

    public void addAll(final Set<ClassGraphEdge> edges) {
        for (ClassGraphEdge edge : edges) {
            addEdge(edge);
        }
    }

    public void removeNodeEdges(final ClassGraphNode node) {
        Set<ClassGraphEdge> nodeEdges = _nodesEdges.get(node);
        if (nodeEdges != null) {
            for (ClassGraphEdge nodeEdge : nodeEdges) {
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

    public boolean removeEdge(final ClassGraphEdge edge) {
        boolean removed = _edges.remove(edge);
        if (removed) {
            ClassGraphLogger.debug("removeEdge - Removed edge : " + edge);
        } else {
            ClassGraphLogger.debug("removeEdge - Edge not in model : " + edge);
        }
        return removed;
    }
}
