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

package service;

import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TypePointer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import material.*;
import valueobjects.Marking;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@XmlRootElement(namespace = "CPGraph")
public class ChangePropagationProcessService implements GraphChangeListener {

    private static final ChangePropagationProcessService _INSTANCE = new ChangePropagationProcessService();

    private ChangeAndFixStrategyIF _strategy;
    @XmlElement (name = "Graph")
    private ChangePropagationModelMaterial _model;
    @XmlElement (name = "AffectedClasses")
    private final ObservableSet<ClassNodeMaterial> _affectedClassesByChange  = FXCollections.observableSet(new HashSet<>());
    @XmlElement (name = "MarkedClasses")
    private final ObservableSet<ClassNodeMaterial> _markedClasses = FXCollections.observableSet(new HashSet<>());
    @XmlElement (name = "InitClasses")
    private final ObservableSet<ClassNodeMaterial> _initialChangedClasse = FXCollections.observableSet(new HashSet<>());

    private final ObservableSet<TraceLinkDependencyMaterial> _traceabilityLinks = FXCollections.observableSet(new HashSet<>());

    private Set<ChangePropagationChangeListener> _listeners;


    public static ChangePropagationProcessService getInstance()
    {
        return _INSTANCE;
    }

    public void initialize(final List<? extends DependencyIF> classdependencies, ChangeAndFixStrategyIF strategy)
    {
        clear();
         _model = new ChangePropagationModelMaterial(classdependencies);
         _listeners = new HashSet<>();
         init(strategy);
    }
    public void clear()
    {
        _affectedClassesByChange.clear();
        _markedClasses.clear();
        _initialChangedClasse.clear();
    }


    private void init(ChangeAndFixStrategyIF strategy) {
        this._strategy = strategy;
        addListenerToAffectedClasses();

    }

    public boolean change(final ClassNodeMaterial classNodeMaterial) {
        if (!_strategy.accept(_affectedClassesByChange)) {
            return false;
        }
        ClassNodeMaterial changedClass = getClassNodeMaterial(classNodeMaterial);
        changedClass.addGraphChangeListener(this);
        _initialChangedClasse.add(changedClass);
        changedClass.setMarking(Marking.CHANGED);
        _affectedClassesByChange.add(changedClass);
        _affectedClassesByChange.addAll(_model.getNeighbourhoodWithoutIncomingInconsistencies(changedClass));

        notifyChangeListener();
        return true;
    }


    public ClassNodeMaterial select(final ClassNodeMaterial classNodeMaterial) {
        if (!_markedClasses.contains(classNodeMaterial))
            System.out.println("Class '" + classNodeMaterial.getFullClassName()+ "' not found in affected Classes that need to be visited");
        try {
            return getClassToVisit(classNodeMaterial);
        } finally {
            _markedClasses.remove(classNodeMaterial);
        }

    }

    @Override
    public void update(ClassNodeMaterial changedMarking) {
        if (changedMarking.getMarking() == Marking.CHANGED || changedMarking.getMarking() == Marking.PROPAGATES) {
            _model.createInconsistencies(changedMarking);
            _affectedClassesByChange.addAll(_model.getNeighbourhoodWithoutIncomingInconsistencies(changedMarking));
        } else {
            //No new neighbours of changed class to calculate
        }
        notifyChangeListener();
    }

    private void addListenerToAffectedClasses() {
        _markedClasses.addListener(new SetChangeListener<ClassNodeMaterial>() {
            @Override
            public void onChanged(Change<? extends ClassNodeMaterial> change) {
                if (change.wasAdded()) {
                    change.getElementAdded().addGraphChangeListener(ChangePropagationProcessService.this);
                }
                if (change.wasRemoved()) {
                    //change.getElementAdded().removeChangeListener(ChangePropagationProcess.this);
                }
            }
        });
        _affectedClassesByChange.addListener(new SetChangeListener<ClassNodeMaterial>() {
            @Override
            public void onChanged(Change<? extends ClassNodeMaterial> change) {
                if (change.wasAdded()) {
                    ClassNodeMaterial classnode = change.getElementAdded();
                    if (classnode.getMarking() == Marking.BLANK) {
                        classnode.setMarking(Marking.NEXT);
                    }
                    _markedClasses.addAll(getMarkedNextClasses());

                }
                if (change.wasRemoved()) {
                    // change.getElementAdded().removeChangeListener(ChangePropagationProcess.this);
                }
            }
        });
    }

    /*private */ ClassNodeMaterial getClassToVisit(final ClassNodeMaterial classNodeMaterial) {
        for (ClassNodeMaterial node : _markedClasses) {
            if (node.equals(classNodeMaterial)) {
                return node;
            }
        }
        //Should not happen because no concurrency
        return null;
    }

    public Set<ClassNodeMaterial> getMarkedNextClasses() {
        return _affectedClassesByChange.stream().filter(classes -> classes.getMarking() == Marking.NEXT).collect(Collectors.toSet());
    }

    public ChangePropagationModelMaterial getModel()
    {
        return _model;
    }

    public ClassNodeMaterial getClassNodeMaterial(final ClassNodeMaterial classNodeMaterial)
    {
        if(!_model.getClassesInGraph().contains(classNodeMaterial))
            throw new IllegalArgumentException("Class '"+ classNodeMaterial.getFullClassName() + "' not found in dependency graph! Please extract dependencies again");
        Iterator<ClassNodeMaterial> it = _model.getClassesInGraph().iterator();
        while(it.hasNext())
        {
            ClassNodeMaterial tmp = it.next();
            if(tmp.equals(classNodeMaterial))
            {
                return tmp;
            }
        }
        return null;
    }
    public void addTraceabilityLinkJavaSource(final JavaClassNodeMaterial classNodeMaterial, final TraceabilityLink traceabilityLink)
    {

        TypePointer pointer = (TypePointer) traceabilityLink.getTarget();
        ClassNodeMaterial swiftClassNodeMaterial = getClassNodeMaterial(new SwiftClassNodeMaterial(pointer.getFullyQualifiedName()));
        swiftClassNodeMaterial.addGraphChangeListener(this);
        swiftClassNodeMaterial.setMarking(Marking.CHANGED);
        _affectedClassesByChange.add(swiftClassNodeMaterial);

        //Add Dependency
        TraceLinkDependencyMaterial dependencyMaterial = new TraceLinkDependencyMaterial((JavaClassNodeMaterial) classNodeMaterial, (SwiftClassNodeMaterial) swiftClassNodeMaterial, traceabilityLink.getProbability());
        _traceabilityLinks.add(dependencyMaterial);
        _model.getClassDependencyGraph().add(dependencyMaterial);
    }
    public void addTraceabilityLinkSwiftSource(final SwiftClassNodeMaterial swiftClassNodeMaterial, final TraceabilityLink traceabilityLink)
    {

        TypePointer pointer = (TypePointer) traceabilityLink.getTarget();
        ClassNodeMaterial javaClassNodeMaterial = getClassNodeMaterial(new JavaClassNodeMaterial(pointer.getFullyQualifiedName()));
        javaClassNodeMaterial.addGraphChangeListener(this);
        javaClassNodeMaterial.setMarking(Marking.CHANGED);
        _affectedClassesByChange.add(javaClassNodeMaterial);

        //Add Dependency
        TraceLinkDependencyMaterial dependencyMaterial = new TraceLinkDependencyMaterial((JavaClassNodeMaterial) javaClassNodeMaterial, (SwiftClassNodeMaterial) swiftClassNodeMaterial, traceabilityLink.getProbability());
        _traceabilityLinks.add(dependencyMaterial);
        _model.getClassDependencyGraph().add(dependencyMaterial);
    }

    public synchronized void addGraphChangeListener(final ChangePropagationChangeListener observer)
    {
        assert observer != null : "Precondition violated: !=null";
        _listeners.add(observer);
    }

    public synchronized void removeChangeListener(final GraphChangeListener observer)
    {
        assert observer != null : "Precondition violated: !=null";
        _listeners.remove(observer);
    }
    protected synchronized void notifyChangeListener()
    {
        final Iterator<ChangePropagationChangeListener> it = _listeners.iterator();
        while (it.hasNext())
        {
            final ChangePropagationChangeListener listener = it.next();

            listener.update();
        }
    }

    public ObservableSet<ClassNodeMaterial> getAffectedClassesByChange() {
        return _affectedClassesByChange;
    }

    public ObservableSet<ClassNodeMaterial> getMarkedClasses()
    {
        return _markedClasses;
    }

    public ObservableSet<ClassNodeMaterial> getInitalChangedClasses(){return _initialChangedClasse;}

    public ObservableSet<TraceLinkDependencyMaterial> getTraceLinkDepenendecySet(){return _traceabilityLinks;}
}
