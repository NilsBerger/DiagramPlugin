package service;

import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TypePointer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import materials.*;
import valueobjects.ClassLanguageType;
import valueobjects.Marking;
import valueobjects.RelationshipType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * ChangePropagation is a SINGLETON
 */
public class ChangePropagationProcess{

    private static final ChangePropagationProcess _INSTANCE = new ChangePropagationProcess();

    private ChangeAndFixStrategyIF _strategy;
    private ChangePropagationModel _model;
    private final ObservableSet<ClassDependency> _affectedEdges  = FXCollections.observableSet(new HashSet<>());
    private final ObservableSet<ClassNode> _initialChangedClasse = FXCollections.observableSet(new HashSet<>());
    private final ObservableSet<ClassNode> _affectedNodes = FXCollections.observableSet(new HashSet<>());
    private final ObservableMap<ClassNode, Set<ClassDependency>> _affectedNodeEdges = FXCollections.observableHashMap();
    private final ObservableSet<TraceLinkClassDependency> _traceabilityLinks = FXCollections.observableSet(new HashSet<>());

    private static Set<GraphChangeListener> _listeners = new HashSet<>();


    public static ChangePropagationProcess getInstance(){
        return _INSTANCE;
    }

    public void initialize(final Set<ClassDependency> classdependencies, ChangeAndFixStrategyIF strategy)
    {
         clear();
         _model = new ChangePropagationModel(classdependencies);
         init(strategy);
    }
    public void clear()
    {
        //Model clear
        _affectedNodes.clear();
        _affectedEdges.clear();
        _initialChangedClasse.clear();
    }

    private void init(ChangeAndFixStrategyIF strategy) {
        this._strategy = strategy;
    }

    public boolean change(final ClassNode classNode) {

        assert classNode != null : "ClassNode should not be null";

        if (!_strategy.accept(_affectedNodes)) {
            return false;
        }
        final ClassNode changedClass = getClassNodeMaterial(classNode);
        _initialChangedClasse.add(classNode);
        update(changedClass, Marking.CHANGED);
        return true;
    }

    public ClassNode select(final ClassNode classNode) {

        assert classNode != null : "ClassNode should not be null";
        assert getNextMarkedClasses().contains(classNode) : "ClassNode : " + classNode.getSimpleName() + " should be in set of marked classes";

        if (!getNextMarkedClasses().contains(classNode))
            System.err.println("Class '" + classNode.getFullClassName()+ "' not found in affected Classes that need to be visited");
        try {
            return getClassToVisit(classNode);
        } finally {
        }
    }

    /**
     *
     * @param changedClass
     * @param marking
     */
    public void update(ClassNode changedClass, Marking marking) {

        assert changedClass != null : "Changed Class should not be null";
        assert marking != null : "Changed Class should not be null";

        ClassNode changedNode = getClassNodeMaterial(changedClass);
        changedNode.setMarking(marking);
        addAffectedNode(changedNode);
        if (marking == Marking.CHANGED || marking == Marking.PROPAGATES) {
            _model.createInconsistencies(changedNode);
            propagateChange(changedNode);
        } else {//No new neighbours of changed class to calculate
        }
        notifyGraphChangeListener();
    }

    public void propagateChange(ClassNode changedClass)
    {
        assert changedClass != null : "ClassNode should not be null";
        assert changedClass.getMarking() == Marking.CHANGED || changedClass.getMarking() == Marking.PROPAGATES : "Can only propagate the change, if the ClassNode has the Marking CHANGED or PROPAGATES";

        Set<ClassNode> affectedNodes = new HashSet<>();
        affectedNodes.addAll(_model.getNeighbourhoodWithoutIncomingInconsistencies(changedClass));
        for(ClassNode classNode :affectedNodes)
        {
            if(classNode.getMarking() == Marking.BLANK)
            {
                classNode.setMarking(Marking.NEXT);
            }
        }
        addAffectedNode(changedClass);
        addAffectedNodes(affectedNodes);
       // addAffectedEdgesBetweenAffectedNodes();
    }


    private ClassNode getClassToVisit(final ClassNode classNode) {
        for (ClassNode node : getNextMarkedClasses()) {
            if (node.equals(classNode)) {
                return node;
            }
        }
        //Should not happen because no concurrency
        assert true : "Should never be here";
        return null;
    }

    private void addAffectedNode(final ClassNode node)
    {
        boolean added = _affectedNodes.add(node);
        if(added)
        {
            System.out.println("addNode - Added AffectedClassNode : " + node);
        }
        else {
            System.out.println("addNode - AffectedClassNode already in model : " +  node);
        }
    }
    private void addAffectedNodes(Set<ClassNode> affectedNodes)
    {
        for(ClassNode node : affectedNodes)
        {
            addAffectedNode(node);
        }
    }

    private void addEdge(final ClassDependency edge)
    {
        boolean added = _affectedEdges.add(edge);
        if(added)
        {
            System.out.println("addEdge - Added AffectedEdge : " + edge);
        }
        else {
            System.out.println("addEdge - AffectedEdge already in model : " +  edge);
        }
        addAffectedNode(edge.getDependentClass());
        addAffectedNode(edge.getIndependentClass());
        addNodeEdge(edge.getDependentClass(), edge);
        addNodeEdge(edge.getIndependentClass(), edge);
    }

    public void addNodeEdge(final ClassNode node, final ClassDependency edge)
    {
        Set<ClassDependency> nodeEdges = _affectedNodeEdges.get(node);
        if(nodeEdges == null)
        {
            nodeEdges = new HashSet<>();
            _affectedNodeEdges.put(node, nodeEdges);
        }
        nodeEdges.add(edge);
    }

    public ChangePropagationModel getModel()
    {
        return _model;
    }

    public ClassNode getClassNodeMaterial(final ClassNode classNode)
    {
        assert classNode != null : "ClassNode should not be null";
        if(!_model.getNodes().contains(classNode))
            throw new IllegalArgumentException("Class '"+ classNode.getFullClassName() + "' not found in dependency graph! Please extract dependencies again");
        Iterator<ClassNode> it = _model.getNodes().iterator();
        while(it.hasNext())
        {
            ClassNode tmp = it.next();
            if(tmp.equals(classNode))
            {
                return tmp;
            }
        }
        return null;
    }
    public void addTraceabilityLinkJavaSource(final ClassNode classNodeMaterial, final TraceabilityLink traceabilityLink)
    {

        TypePointer pointer = (TypePointer) traceabilityLink.getTarget();
        ClassNode swiftClassNode = getClassNodeMaterial(new ClassNode(pointer.getFullyQualifiedName(), ClassLanguageType.Swift));


        //Add Dependency
        TraceLinkClassDependency traceDependency = new TraceLinkClassDependency(classNodeMaterial,swiftClassNode, traceabilityLink.getProbability());
        _traceabilityLinks.add(traceDependency);
        _model.addEdge(traceDependency);
        _model.createInconsistencies(swiftClassNode);
        _model.addEdge(traceDependency);
        update(swiftClassNode, Marking.NEXT);

    }
    public void addTraceabilityLinkSwiftSource(final ClassNode swiftClassNodeMaterial, final TraceabilityLink traceabilityLink)
    {

        TypePointer pointer = (TypePointer) traceabilityLink.getTarget();
        ClassNode javaClassNode = getClassNodeMaterial(new ClassNode(pointer.getFullyQualifiedName(), ClassLanguageType.Java));


        //Add Dependency
        TraceLinkClassDependency traceDependency = new TraceLinkClassDependency(javaClassNode, swiftClassNodeMaterial,  traceabilityLink.getProbability());
        _traceabilityLinks.add(traceDependency);
        _model.addEdge(traceDependency);
        _model.createInconsistencies(javaClassNode);

        _model.addEdge(traceDependency);
        update(javaClassNode, Marking.NEXT);
    }

    public void updateDependency(ClassDependency classDependency, RelationshipType relationshipType)
    {
        notifyGraphChangeListener();
        _model.updateDependency(classDependency, relationshipType);
    }

    public void addGraphChangeListener(final GraphChangeListener observer)
    {
        assert observer != null : "Precondition violated: != null";
        _listeners.add(observer);
    }

    public void removeChangeListener(final GraphChangeListener observer)
    {
        assert observer != null : "Precondition violates: != null";
        _listeners.remove(observer);
    }

    public void notifyGraphChangeListener()
    {
        final Iterator<GraphChangeListener> it  = _listeners.iterator();
        while(it.hasNext())
        {
            final GraphChangeListener listener = it.next();
            listener.updateView();
        }
    }

    /**
     * Returns all ClassNodes that are marked with "NEXT"
     * @return
     */
    public Set<ClassNode> getNextMarkedClasses() {
        return _affectedNodes.stream().filter(classes -> classes.getMarking() == Marking.NEXT).collect(Collectors.toSet());
    }

    public Set<ClassDependency> getAffectedDependencies(ClassNode node)
    {
        Set<ClassDependency> dependencies = _model.getDependenciesForNode(node);
        Set<ClassDependency> affectedDepenendcies = new HashSet<>();
        for(ClassDependency dependency : dependencies)
        {
            for(ClassNode affectedNode : _affectedNodes)
            {
                if(dependency.containsNodes(node, affectedNode))
                {
                    affectedDepenendcies.add(dependency);
                }
            }

        }
        return affectedDepenendcies;
    }

    public ObservableSet<ClassNode> getAffectedClassesByChange() {
        return _affectedNodes;
    }

    public ObservableSet<ClassNode> getInitalChangedClasses(){return _initialChangedClasse;}

    public ObservableMap<ClassNode, Set<ClassDependency>> getAffectedNodeEdges(){return _affectedNodeEdges;}

    public ObservableSet<TraceLinkClassDependency> getTraceLinkDepenendecySet(){return _traceabilityLinks;}
}
