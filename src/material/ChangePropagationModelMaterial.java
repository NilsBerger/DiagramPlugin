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

import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import service.GraphChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

import javax.xml.bind.annotation.XmlElement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChangePropagationModelMaterial implements GraphChangeListener {

    @XmlElement(name = "Graphelement")
    private ObservableSet<DependencyIF> _classdependencygraph;
    private Set<ClassNodeMaterial> _classesInGraph;
    private Set<InconsistentDependencyMaterial> _inconsistencies;

    public ChangePropagationModelMaterial()
    {}

    public ChangePropagationModelMaterial(final List<? extends DependencyIF> classdependencies)
    {
        this._classdependencygraph = FXCollections.observableSet((new HashSet<DependencyIF>(classdependencies)));
        this._classesInGraph = new HashSet<>();
        this._inconsistencies = new HashSet<>();
        extractClassesAndRegisterObserver();
        observerDependenyGraphForChange();
    }

    public Set<ClassNodeMaterial> getBottomDependencies(final ClassNodeMaterial classNode)
    {
        Set<ClassNodeMaterial> tmpList = new HashSet<>();
        for(DependencyIF dependency : _classdependencygraph)
        {
            if(classNode.equals(dependency.getDependentClass()) && !(dependency instanceof InconsistentDependencyMaterial)){
                tmpList.add(dependency.getIndependentClass());
            }
        }
        return tmpList;
    }
    public Set<ClassNodeMaterial> getTopDependencies(final ClassNodeMaterial classNode)
    {
        Set<ClassNodeMaterial> tmpList = new HashSet<>();
        for(DependencyIF dependency : _classdependencygraph)
        {
            if(classNode.equals(dependency.getIndependentClass()) && !(dependency instanceof InconsistentDependencyMaterial)){
                tmpList.add(dependency.getDependentClass());
            }
        }
        return tmpList;
    }
    public Set<ClassNodeMaterial> getBottomInconsistencies(final ClassNodeMaterial classNode)
    {
        Set<ClassNodeMaterial> tmpList = new HashSet<>();
        for(DependencyIF dependency : _classdependencygraph)
        {
            if(classNode.equals(dependency.getDependentClass()) && dependency instanceof InconsistentDependencyMaterial){
                tmpList.add(dependency.getIndependentClass());
            }
        }
        return tmpList;
    }
    public Set<ClassNodeMaterial> getTopInconsistencies(final ClassNodeMaterial classNode)
    {
        Set<ClassNodeMaterial> tmpList = new HashSet<>();
        for(DependencyIF dependency : _classdependencygraph)
        {
            if(classNode.equals(dependency.getIndependentClass()) && dependency instanceof InconsistentDependencyMaterial){
                tmpList.add(dependency.getDependentClass());
            }
        }
        return tmpList;
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

    private void extractClassesAndRegisterObserver() {
        for(DependencyIF dependency: _classdependencygraph)
        {
            addClass(dependency.getDependentClass());
            addClass(dependency.getIndependentClass());
        }
    }
    private void addClass(final ClassNodeMaterial classNodeMaterial)
    {
        registerToClassNode(classNodeMaterial);
        _classesInGraph.add(classNodeMaterial);
    }
    private void deleteClass(final ClassNodeMaterial classNodeMaterial)
    {
        classNodeMaterial.removeChangeListener(this);
        _classesInGraph.remove(classNodeMaterial);
    }



    public void createInconsistencies(final ClassNodeMaterial changedClass)
    {
        for(ClassNodeMaterial topDependency: getTopDependencies(changedClass))
        {
            _classdependencygraph.add(new InconsistentDependencyMaterial(new ClassDependencyMaterial(changedClass,topDependency)));
        }
        for(ClassNodeMaterial topDependency: getBottomDependencies(changedClass))
        {
            _classdependencygraph.add(new InconsistentDependencyMaterial(new ClassDependencyMaterial(changedClass,topDependency).switchDependencies()));
        }

    }

    public void addInconsistency(InconsistentDependencyMaterial inconsistency)
    {
        _classdependencygraph.add(inconsistency);
    }



    private void observerDependenyGraphForChange()
    {
        _classdependencygraph.addListener(new SetChangeListener<DependencyIF>() {
            @Override
            public void onChanged(Change<? extends DependencyIF> change) {
                extractClassesAndRegisterObserver();
            }
        });
    }

    public Set<ClassNodeMaterial> getClassesInGraph() {
        return _classesInGraph;
    }

    public ObservableSet<DependencyIF> getClassDependencyGraph() {
        return _classdependencygraph;
    }
    private void registerToClassNode(ClassNodeMaterial node)
    {
        node.addGraphChangeListener(this);
    }

    @Override
    public void update(ClassNodeMaterial changedClass) {

        System.out.println("Class:  '" + changedClass.getSimpleClassName() + "' changed from marking: '" + changedClass.getOldMarking() +"' to new marking: '" + changedClass.getMarking() + "'." );
    }
}
