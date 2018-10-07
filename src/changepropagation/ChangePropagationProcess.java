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

import dotparser.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ChangePropagationProcess {
    private final ChangeAndFixStrategyIF _strategy;
    private ChangePropagationModelService _model;
    private final Set<ClassNodeMaterial> _affectedClassesByChange;

    public ChangePropagationProcess(final List<DependencyIF> classdependenciesfinal, ChangeAndFixStrategyIF strategy)
    {
        this._model = new ChangePropagationModelService(classdependenciesfinal);
        this._affectedClassesByChange = new HashSet<>();
        this._strategy = strategy;
    }
    public void addClassAffectedByChange(final ClassNodeMaterial affectedClass){
        if(!_strategy.accept(_affectedClassesByChange))
                return;
        change(affectedClass);
    }
    public void change(final ClassNodeMaterial affectedClass)
    {
        affectedClass.setMarking(Marking.CHANGED);
        _affectedClassesByChange.add(affectedClass);
        _affectedClassesByChange.addAll(getNeighbourhoodWithoutIncomingInconsistencies(affectedClass));
    }

    public ClassNodeMaterial select(final String classname)
    {
        Iterator<ClassNodeMaterial> it = _affectedClassesByChange.iterator();
        while(it.hasNext())
        {
            ClassNodeMaterial tmp = it.next();
            if(tmp.getSimpleClassName().equals(classname))
                return tmp;
        }
        return new ClassNodeMaterial("FEHLER");
    }

    public Set<ClassNodeMaterial> getTopDependencies(final ClassNodeMaterial classNode)
    {
        Set<ClassNodeMaterial> tmpList = new HashSet<>();
        for(DependencyIF dependency : _model.getClassDependencyGraph())
        {
            if(classNode.equals(dependency.getDependentClass()) && dependency instanceof DependencyFachwert){
                tmpList.add(dependency.getIndependentClass());
            }
        }
        return tmpList;
    }
    public Set<ClassNodeMaterial> getBottomDependencies(final ClassNodeMaterial classNode)
    {
        Set<ClassNodeMaterial> tmpList = new HashSet<>();
        for(DependencyIF dependency : _model.getClassDependencyGraph())
        {
            if(classNode.equals(dependency.getIndependentClass()) && dependency instanceof DependencyFachwert){
                tmpList.add(dependency.getDependentClass());
            }
        }
        return tmpList;
    }
    public Set<ClassNodeMaterial> getTopInconsistencies(final ClassNodeMaterial classNode)
    {
        Set<ClassNodeMaterial> tmpList = new HashSet<>();
        for(DependencyIF dependency : _model.getClassDependencyGraph())
        {
            if(classNode.equals(dependency.getDependentClass()) && dependency instanceof InconsistentDependencyFachwert){
                tmpList.add(dependency.getIndependentClass());
            }
        }
        return tmpList;
    }
    public Set<ClassNodeMaterial> getBottomInconsistencies(final ClassNodeMaterial classNode)
    {
        Set<ClassNodeMaterial> tmpList = new HashSet<>();
        for(DependencyIF dependency : _model.getClassDependencyGraph())
        {
            if(classNode.equals(dependency.getIndependentClass()) && dependency instanceof InconsistentDependencyFachwert){
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
}
