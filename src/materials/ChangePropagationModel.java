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

package materials;


import materials.ClassDependency;
import materials.ClassNode;
import valueobjects.RelationshipType;

import java.util.*;

public class ChangePropagationModel {

    private Set<ClassNode> _oldnodes;
    private Set<ClassDependency> _oldedges;
    private Map<ClassNode, Set<ClassDependency>> _oldnodesEdges;

    private Set<ClassNode> _nodes;
    private Set<ClassDependency> _edges;
    private Map<ClassNode, Set<ClassDependency>> _nodesEdges;


    public ChangePropagationModel() {
        _nodes = new HashSet<>();
        _nodesEdges = new HashMap<>();
        _edges = new HashSet<>();
    }

    public ChangePropagationModel(final Set<? extends ClassDependency> classdependencies) {
        _nodes = new HashSet<>();
        _nodesEdges = new HashMap<>();
        _edges = new HashSet<>();
        addAll(classdependencies);
    }

    /**
     * Makes a copy of the _nodes, _edges and _nodeEdges
     */
    private void saveNodes() {
        Iterator<ClassNode> nodesIterator = _nodes.iterator();
        while (nodesIterator.hasNext()) {
            _oldnodes.add(nodesIterator.next());
        }
        Iterator<ClassDependency> edgesIterator = _edges.iterator();
        while (edgesIterator.hasNext()) {
            _oldedges.add(edgesIterator.next());
        }
        for (Map.Entry<ClassNode, Set<ClassDependency>> entry : _nodesEdges.entrySet()) {
            _oldnodesEdges.put(entry.getKey(), entry.getValue());
        }
    }

    private void addNode(final ClassNode node) {
        boolean added = _nodes.add(node);
        if (added) {
            System.out.println("addNode - Added ClassNode : " + node);
        } else {
            System.out.println("addNode - ClassNode already in model : " + node);
        }
    }

    public void addEdge(final ClassDependency edge) {
        boolean added = _edges.add(edge);
        if (added) {
            System.out.println("addEdge - Added Edge : " + edge);
        } else {
            System.out.println("addEdge - Edge already in model : " + edge);
        }
        addNode(edge.getDependentClass());
        addNode(edge.get_independentClass());
        addNodeEdge(edge.getDependentClass(), edge);
        addNodeEdge(edge.get_independentClass(), edge);
    }

    public void addNodeEdge(final ClassNode node, final ClassDependency edge) {
        Set<ClassDependency> nodeEdges = _nodesEdges.get(node);
        if (nodeEdges == null) {
            nodeEdges = new HashSet<>();
            _nodesEdges.put(node, nodeEdges);
        }
        nodeEdges.add(edge);
    }

    /**
     * @param edges
     */
    public void addAll(final Set<? extends ClassDependency> edges) {
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
            System.out.println("removeNode - Removed node : " + node);
            removeNodeEdges(node);
            _nodesEdges.remove(node);
        } else {
            System.out.println("addNode - Node not in model : " + node);
        }
        return removed;
    }

    public boolean removeEdge(final ClassDependency edge) {
        boolean removed = _edges.remove(edge);
        if (removed) {
            System.out.println("removeEdge - Removed edge : " + edge);
        } else {
            System.out.println("removeEdge - Edge not in model : " + edge);
        }
        return removed;
    }

    public Set<ClassNode> getTopDependencies(final ClassNode classNode) {
        Set<ClassDependency> edgesForNode = _nodesEdges.get(classNode);
        Set<ClassNode> topDependencies = new HashSet();
        for (ClassDependency edge : edgesForNode) {
            if (edge.getRelationshipType() != RelationshipType.Traceability_Association) {
                if (!edge.get_independentClass().equals(classNode)) {
                    topDependencies.add(edge.get_independentClass());
                }
            }
        }
        return topDependencies;
    }

    public Set<ClassNode> getBottomDependencies(final ClassNode classNode) {
        Set<ClassNode> topDependencies = new HashSet();
        for (ClassDependency edge : _nodesEdges.get(classNode)) {
            if (edge.getRelationshipType() != RelationshipType.Traceability_Association) {
                if (!edge.getDependentClass().equals(classNode)) {
                    topDependencies.add(edge.getDependentClass());
                }
            }
        }
        return topDependencies;
    }

    public Set<ClassNode> getTopInconsistencies(final ClassNode classNode) {
        Set<ClassNode> bottomDependencies = new HashSet();
        for (ClassDependency edge : _nodesEdges.get(classNode)) {
            if (edge.getRelationshipType() == RelationshipType.InconsistentRelationship) {
                if (!edge.get_independentClass().equals(classNode)) {
                    bottomDependencies.add(edge.get_independentClass());
                }
            }
        }
        return bottomDependencies;
    }

    public Set<ClassNode> getBottomInconsistencies(final ClassNode classNode) {
        Set<ClassDependency> edgesForNode = _nodesEdges.get(classNode);
        Set<ClassNode> topDependencies = new HashSet();
        for (ClassDependency edge : edgesForNode) {
            if (edge.getRelationshipType() == RelationshipType.InconsistentRelationship) {
                if (!edge.getDependentClass().equals(classNode)) {
                    topDependencies.add(edge.getDependentClass());
                }
            }
        }
        return topDependencies;
    }

    public Set<ClassNode> getNeighbourhood(final ClassNode clazz) {
        Set<ClassNode> neighbourhood = new HashSet<>();
        neighbourhood.addAll(getTopDependencies(clazz));
        neighbourhood.addAll(getBottomDependencies(clazz));
        neighbourhood.addAll(getTopInconsistencies(clazz));
        neighbourhood.addAll(getBottomInconsistencies(clazz));
        return neighbourhood;
    }

    public Set<ClassNode> getNeighbourhoodWithoutIncomingInconsistencies(final ClassNode clazz) {
        Set<ClassNode> neighbourhood = new HashSet<>();
        neighbourhood.addAll(getTopDependencies(clazz));
        neighbourhood.addAll(getBottomDependencies(clazz));
        neighbourhood.addAll(getBottomInconsistencies(clazz));
        return neighbourhood;
    }

    public Set<ClassDependency> getDependenciesForNode(ClassNode node)
    {
        return _nodesEdges.get(node);
    }

    public Set<ClassNode> getNodes() {
        return _nodes;
    }

    /**
     *
     * @param changedNode
     */
    public void createInconsistencies(ClassNode changedNode)
    {
        assert changedNode != null : "ClassNode should not be null";
        assert _nodesEdges.containsKey(changedNode) : "ClassNode not contained in graph";

        Set<ClassDependency> dependencies = _nodesEdges.get(changedNode);
        Set<ClassDependency> newInconsistencies = new HashSet<>();
        Iterator<ClassDependency> it= dependencies.iterator();

        while(it.hasNext())
        {
            ClassDependency dependency = it.next();
            if(dependency.getRelationshipType() != RelationshipType.InconsistentRelationship)
            {
                boolean isReflexiv = dependency.getDependentClass().equals(changedNode) &&
                         dependency.get_independentClass().equals(changedNode);
                if(isReflexiv)
                {
                    ClassDependency reflexivDependency = new ClassDependency(dependency.getDependentClass(), dependency.get_independentClass(), RelationshipType.InconsistentRelationship);
                    newInconsistencies.add(reflexivDependency);
                    break;
                }
                if(dependency.get_independentClass().equals(changedNode) )
                {
                    if(!inconcistencyBetweenNodes(dependency.getDependentClass(), dependency.get_independentClass()))
                    {
                        newInconsistencies.add(new ClassDependency(changedNode, dependency.getDependentClass(), RelationshipType.InconsistentRelationship));
                    }

                }
                else
                {
                    if(!inconcistencyBetweenNodes(dependency.getDependentClass(), dependency.get_independentClass()))
                    {
                        newInconsistencies.add(new ClassDependency(dependency.getDependentClass(), dependency.get_independentClass(), RelationshipType.InconsistentRelationship));
                    }

                }

            }
        }
        addAll(newInconsistencies);
    }

    public Set<ClassDependency> getInconsistencies()
    {
        Set<ClassDependency> inconsistencies = new HashSet<>();
        for(ClassDependency dependency : _edges)
        {
            if(dependency.getRelationshipType() == RelationshipType.InconsistentRelationship)
            {
                inconsistencies.add(dependency);
            }
        }
        return inconsistencies;
    }

    private boolean inconcistencyBetweenNodes(ClassNode a, ClassNode b)
    {
        assert _nodesEdges.containsKey(a) : "ClassNode stored as a key";
        assert _nodesEdges.containsKey(b) : "ClassNode stored as a key";
        for(ClassDependency dependency : _edges)
        {
            boolean dependent = dependency.getDependentClass().equals(a) || dependency.getDependentClass().equals(b);
            boolean independent = dependency.get_independentClass().equals(a) || dependency.get_independentClass().equals(b);
            boolean isInconsistency = (dependency.getRelationshipType() == RelationshipType.InconsistentRelationship);
            boolean inconsistency = dependent && independent && isInconsistency;
            if(inconsistency)
                return true;
        }
        return false;
    }
}
