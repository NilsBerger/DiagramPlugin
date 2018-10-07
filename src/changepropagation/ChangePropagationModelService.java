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

package changepropagation;

import dotparser.ClassNodeMaterial;
import dotparser.DependencyFachwert;
import dotparser.DependencyIF;
import dotparser.InconsistentDependencyFachwert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChangePropagationModelService implements GraphChangeListener{

    final private ObservableSet<DependencyIF> _classdependencygraph;
    private Set<ClassNodeMaterial> _classesInGraph;

    public ChangePropagationModelService(final List<DependencyIF> classdependencies)
    {
        this._classdependencygraph = FXCollections.observableSet((new HashSet<DependencyIF>(classdependencies)));
        this._classesInGraph = new HashSet<>();
        extractClassesAndRegisterObserver();
        observerDependenyGraphForChange();
    }

    /*private*/ void changeConsistentDependencyToInconsistentDependency(final DependencyFachwert concistentDependency)
    {
        if(!_classdependencygraph.contains(concistentDependency))
            return;

        InconsistentDependencyFachwert inconsistentDependencyFachwert = new InconsistentDependencyFachwert(concistentDependency);
        int oldsize = _classdependencygraph.size();
        //Delete old dependecy
        _classdependencygraph.remove(concistentDependency);
        //Add new dependency
        _classdependencygraph.add(inconsistentDependencyFachwert);

        if(oldsize != _classdependencygraph.size())
            throw new IllegalStateException("Graph should have the same size after a graph rewrite");
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

    private void observerDependenyGraphForChange()
    {
        _classdependencygraph.addListener(new SetChangeListener<DependencyIF>() {
            @Override
            public void onChanged(Change<? extends DependencyIF> change) {
                extractClassesAndRegisterObserver();
                System.out.println("Added: " + change.getElementAdded());
                System.out.println("Removed" + change.getElementRemoved());
            }
        });
    }

    public ObservableSet<DependencyIF> getClassDependencyGraph() {
        return _classdependencygraph;
    }
    private void registerToClassNode(ClassNodeMaterial node)
    {
        node.addGraphChangeListener(this);
    }

    @Override
    public void update(ClassNodeMaterial graphMaterial) {
        System.out.println("Pattern:  " + graphMaterial.getSimpleClassName());
    }
}
