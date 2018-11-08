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


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChangePropagationModel {

    private Set<ClassNode> _oldnodes;
    private Set<DependencyIF> _oldedges;
    private Map<ClassNode, Set<DependencyIF>> _oldnodesEdges;

    private Set<ClassNode> _nodes;
    private Set<DependencyIF> _edges;
    private Map<ClassNode, Set<DependencyIF>> _nodesEdges;


    public ChangePropagationModel()
    {
        _nodes = new HashSet<>();
        _nodesEdges = new HashMap<>();
        _edges = new HashSet<>();
    }

    public ChangePropagationModel(final Set<? extends DependencyIF> classdependencies)
    {
        _nodes = new HashSet<>();
        _nodesEdges = new HashMap<>();
        _edges = new HashSet<>();
        addAll(classdependencies);
    }

    /**
     * Makes a copy of the _nodes, _edges and _nodeEdges
     */
    private void saveNodes()
    {
        Iterator<ClassNode> nodesIterator = _nodes.iterator();
        while(nodesIterator.hasNext())
        {
            _oldnodes.add(nodesIterator.next());
        }
        Iterator<DependencyIF> edgesIterator = _edges.iterator();
        while(edgesIterator.hasNext())
        {
            _oldedges.add(edgesIterator.next());
        }
        for(Map.Entry<ClassNode, Set<DependencyIF>> entry : _nodesEdges.entrySet())
        {
            _oldnodesEdges.put(entry.getKey(), entry.getValue());
        }
    }

    private void addNode(final ClassNode node)
    {
        boolean added = _nodes.add(node);
        if(added)
        {
            System.out.println("addNode - Added ClassNode : " + node);
        }
        else {
            System.out.println("addNode - ClassNode already in model : " +  node);
        }
    }

    public void addEdge(final DependencyIF edge)
    {
        boolean added = _edges.add(edge);
        if(added)
        {
            System.out.println("addEdge - Added Edge : " + edge);
        }
        else {
            System.out.println("addEdge - Edge already in model : " +  edge);
        }
        addNode(edge.getDependentClass());
        addNode(edge.getIndependentClass());
        addNodeEdge(edge.getDependentClass(), edge);
        addNodeEdge(edge.getIndependentClass(), edge);
    }

    public void addNodeEdge(final ClassNode node, final DependencyIF edge)
    {
        Set<DependencyIF> nodeEdges = _nodesEdges.get(node);
        if(nodeEdges == null)
        {
            nodeEdges = new HashSet<>();
            _nodesEdges.put(node, nodeEdges);
        }
        nodeEdges.add(edge);
    }

    /**
     *
     * @param edges
     */
    public void addAll(final Set<? extends DependencyIF> edges)
    {
        for(DependencyIF edge : edges)
        {
            addEdge(edge);
        }
    }

    public void removeNodeEdges(final ClassNode node)
    {
        Set<DependencyIF> nodeEdges = _nodesEdges.get(node);
        if(nodeEdges != null)
        {
            for(DependencyIF nodeEdge : nodeEdges)
            {
                removeEdge(nodeEdge);
            }
        }
    }

    public boolean removeNode(final ClassNode node){
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

    public boolean removeEdge(final DependencyIF edge)
    {
        boolean removed = _edges.remove(edge);
        if(removed)
        {
            System.out.println("removeEdge - Removed edge : " + edge);
        }
        else{
            System.out.println("removeEdge - Edge not in model : " + edge);
        }
        return removed;
    }

    public Set<ClassNode> getTopDependencies(final ClassNode classNode)
    {
        Set<DependencyIF> edgesForNode = _nodesEdges.get(classNode);
        Set<ClassNode> topDependencies = new HashSet();
        for(DependencyIF edge: edgesForNode)
        {
            if(!(edge instanceof InconsistentDependency))
            {
                if(!edge.getIndependentClass().equals(classNode))
                {
                    topDependencies.add(edge.getIndependentClass());
                }
            }
        }
        return topDependencies;
    }
    public Set<ClassNode> getBottomDependencies(final ClassNode classNode)
    {
        Set<ClassNode> topDependencies = new HashSet();
        for(DependencyIF edge: _nodesEdges.get(classNode))
        {
            if(!(edge instanceof InconsistentDependency))
            {
                if(!edge.getDependentClass().equals(classNode))
                {
                    topDependencies.add(edge.getDependentClass());
                }
            }
        }
        return topDependencies;
    }
    public Set<ClassNode> getTopInconsistencies(final ClassNode classNode)
    {
        Set<ClassNode> bottomDependencies = new HashSet();
        for(DependencyIF edge: _nodesEdges.get(classNode))
        {
            if(edge instanceof InconsistentDependency)
            {
                if(!edge.getIndependentClass().equals(classNode))
                {
                    bottomDependencies.add(edge.getIndependentClass());
                }
            }
        }
        return bottomDependencies;
    }
    public Set<ClassNode> getBottomInconsistencies(final ClassNode classNode)
    {
        Set<DependencyIF> edgesForNode = _nodesEdges.get(classNode);
        Set<ClassNode> topDependencies = new HashSet();
        for(DependencyIF edge: edgesForNode)
        {
            if(edge instanceof InconsistentDependency)
            {
                if(!edge.getDependentClass().equals(classNode))
                {
                    topDependencies.add(edge.getDependentClass());
                }
            }
        }
        return topDependencies;
    }
    public Set<ClassNode> getNeighbourhood(final ClassNode clazz)
    {
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

        Set<DependencyIF> dependencies = _nodesEdges.get(changedNode);
        Set<InconsistentDependency> newInconsistencies = new HashSet<>();
        Iterator<DependencyIF> it= dependencies.iterator();

        while(it.hasNext())
        {
            DependencyIF dependency = it.next();
            if(!(dependency instanceof InconsistentDependency) && dependency instanceof ClassDependency)
            {
                boolean isReflexiv = ((ClassDependency) dependency).dependentClass.equals(changedNode) &&
                        ((ClassDependency) dependency).independentClass.equals(dependency);
                if(isReflexiv)
                {
                    newInconsistencies.add(new InconsistentDependency((ClassDependency) dependency));
                    break;
                }
                if(dependency.getIndependentClass().equals(changedNode) )
                {
                    if(!inconcistencyBetweenNodes(dependency.getDependentClass(), dependency.getIndependentClass()))
                    {
                        newInconsistencies.add(new InconsistentDependency(changedNode, dependency.getDependentClass()));
                    }

                }
                else
                {
                    if(!inconcistencyBetweenNodes(dependency.getDependentClass(), dependency.getIndependentClass()))
                    {
                        newInconsistencies.add(new InconsistentDependency((ClassDependency) dependency));
                    }

                }

            }
        }
        addAll(newInconsistencies);
    }

    public Set<InconsistentDependency> getInconsistencies()
    {
        Set<InconsistentDependency> inconsistencies = new HashSet<>();
        for(DependencyIF dependency : _edges)
        {
            if(dependency instanceof InconsistentDependency)
            {
                inconsistencies.add((InconsistentDependency) dependency);
            }
        }
        return inconsistencies;
    }

    private boolean inconcistencyBetweenNodes(ClassNode a, ClassNode b)
    {
        assert _nodesEdges.containsKey(a) : "ClassNode stored as a key";
        assert _nodesEdges.containsKey(b) : "ClassNode stored as a key";
        for(DependencyIF dependency : _edges)
        {
            boolean dependent = dependency.getDependentClass().equals(a) || dependency.getDependentClass().equals(b);
            boolean independent = dependency.getIndependentClass().equals(a) || dependency.getIndependentClass().equals(b);
            boolean isInconsistency = (dependency instanceof InconsistentDependency);
            boolean inconsistency = dependent && independent && isInconsistency;
            if(inconsistency)
                return true;
        }
        return false;
    }
}
