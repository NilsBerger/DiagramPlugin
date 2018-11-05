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

package material;


import java.util.*;

public class ChangePropagationModelMaterial{

    private Set<ClassNodeMaterial> _nodes;
    private Set<DependencyIF> _edges;
    private Map<ClassNodeMaterial, Set<DependencyIF>> _nodesEdges;


    public ChangePropagationModelMaterial()
    {
        _nodes = new HashSet<>();
        _nodesEdges = new HashMap<>();
        _edges = new HashSet<>();
    }

    public ChangePropagationModelMaterial(final Set<? extends DependencyIF> classdependencies)
    {
        _nodes = new HashSet<>();
        _nodesEdges = new HashMap<>();
        _edges = new HashSet<>();
        addAll(classdependencies);
    }

    private void addNode(final ClassNodeMaterial node)
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

    public void addNodeEdge(final ClassNodeMaterial node, final DependencyIF edge)
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

    public void removeNodeEdges(final ClassNodeMaterial node)
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

    public boolean removeNode(final ClassNodeMaterial node){
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

    public Set<ClassNodeMaterial> getBottomDependencies(final ClassNodeMaterial classNode)
    {
        Set<DependencyIF> edgesForNode = _nodesEdges.get(classNode);
        Set<ClassNodeMaterial> bottomDependencies = new HashSet();
        for(DependencyIF edge: edgesForNode)
        {
            if(!(edge instanceof InconsistentDependencyMaterial))
            {
                if(!edge.getIndependentClass().equals(classNode))
                {
                    bottomDependencies.add(edge.getIndependentClass());
                }
            }
        }
        return bottomDependencies;
    }
    public Set<ClassNodeMaterial> getTopDependencies(final ClassNodeMaterial classNode)
    {
        Set<DependencyIF> edgesForNode = _nodesEdges.get(classNode);
        Set<ClassNodeMaterial> topDependencies = new HashSet();
        for(DependencyIF edge: edgesForNode)
        {
            if(!(edge instanceof InconsistentDependencyMaterial))
            {
                if(!edge.getDependentClass().equals(classNode))
                {
                    topDependencies.add(edge.getDependentClass());
                }
            }
        }
        return topDependencies;
    }
    public Set<ClassNodeMaterial> getBottomInconsistencies(final ClassNodeMaterial classNode)
    {
        Set<DependencyIF> edgesForNode = _nodesEdges.get(classNode);
        Set<ClassNodeMaterial> bottomDependencies = new HashSet();
        for(DependencyIF edge: edgesForNode)
        {
            if(edge instanceof InconsistentDependencyMaterial)
            {
                if(!edge.getIndependentClass().equals(classNode))
                {
                    bottomDependencies.add(edge.getIndependentClass());
                }
            }
        }
        return bottomDependencies;
    }
    public Set<ClassNodeMaterial> getTopInconsistencies(final ClassNodeMaterial classNode)
    {
        Set<DependencyIF> edgesForNode = _nodesEdges.get(classNode);
        Set<ClassNodeMaterial> topDependencies = new HashSet();
        for(DependencyIF edge: edgesForNode)
        {
            if(edge instanceof InconsistentDependencyMaterial)
            {
                if(!edge.getDependentClass().equals(classNode))
                {
                    topDependencies.add(edge.getDependentClass());
                }
            }
        }
        return topDependencies;
    }
    public Set<ClassNodeMaterial> getNeighbourhood(final ClassNodeMaterial clazz)
    {
        Set<ClassNodeMaterial> neighbourhood = new HashSet<>();
        neighbourhood.addAll(getTopDependencies(clazz));
        neighbourhood.addAll(getBottomDependencies(clazz));
        neighbourhood.addAll(getTopInconsistencies(clazz));
        neighbourhood.addAll(getBottomInconsistencies(clazz));
        return neighbourhood;
    }

    public Set<ClassNodeMaterial> getNeighbourhoodWithoutIncomingInconsistencies(final ClassNodeMaterial clazz) {
        Set<ClassNodeMaterial> neighbourhood = new HashSet<>();
        neighbourhood.addAll(getTopDependencies(clazz));
        neighbourhood.addAll(getBottomDependencies(clazz));
        neighbourhood.addAll(getBottomInconsistencies(clazz));
        return neighbourhood;
    }

    public Set<ClassNodeMaterial> getNodes() {
        return _nodes;
    }
}
