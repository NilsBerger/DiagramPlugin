package service;

import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TypePointer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import materials.*;
import valueobjects.Marking;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

@XmlRootElement(namespace = "CPGraph")
/**
 * ChangePropagation is a SINGLETON
 */
public class ChangePropagationProcess{

    private static final ChangePropagationProcess _INSTANCE = new ChangePropagationProcess();

    private ChangeAndFixStrategyIF _strategy;
    @XmlElement (name = "Graph")
    private ChangePropagationModel _model;

    private final ObservableSet<DependencyIF> _affectedEdges  = FXCollections.observableSet(new HashSet<>());
    @XmlElement (name = "InitClasses")
    private final ObservableSet<ClassNode> _initialChangedClasse = FXCollections.observableSet(new HashSet<>());

    @XmlElement (name = "AffectedClasses")
    private final ObservableSet<ClassNode> _affectedNodes = FXCollections.observableSet(new HashSet<>());

    private final ObservableMap<ClassNode, Set<DependencyIF>> _affectedNodeEdges = FXCollections.observableHashMap();

    private final ObservableSet<TraceLinkDependency> _traceabilityLinks = FXCollections.observableSet(new HashSet<>());

    private static Set<GraphChangeListener> _listeners = new HashSet<>();


    public static ChangePropagationProcess getInstance(){
        return _INSTANCE;
    }

    public void initialize(final Set<? extends DependencyIF> classdependencies, ChangeAndFixStrategyIF strategy)
    {
         clear();
         _model = new ChangePropagationModel(classdependencies);
         init(strategy);
    }
    public void clear()
    {
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
        updateNeigbbourhood(changedClass, Marking.CHANGED);
        return true;
    }

    public ClassNode select(final ClassNode classNode) {

        assert classNode != null : "ClassNode should not be null";
        assert getNextMarkedClasses().contains(classNode) : "ClassNode : " + classNode.getSimpleClassName() + " should be in set of marked classes";

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
    public void updateNeigbbourhood(ClassNode changedClass, Marking marking) {

        assert changedClass != null : "Changed Class should not be null";
        assert marking != null : "Changed Class should not be null";

        ClassNode changedNode = getClassNodeMaterial(changedClass);
        changedNode.setMarking(marking);
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

//    private void addAffectedEdgesBetweenAffectedNodes() {
//        _affectedNodes.addListener(new SetChangeListener<ClassNode>() {
//            @Override
//            public void onChanged(Change<? extends ClassNode> change) {
//                if(change.wasAdded())
//                {
//                    ClassNode addedNode = change.getElementAdded();
//                    Set<ClassNode> topDependencies = _model.getTopDependencies(addedNode);
//                    Set<ClassNode> bottomDependencies = _model.getBottomDependencies(addedNode);
//
//                    for(Set<ClassNode> topDependencies)
//                }
//            }
//        });
//    }

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

    private void addEdge(final DependencyIF edge)
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

    public void addNodeEdge(final ClassNode node, final DependencyIF edge)
    {
        Set<DependencyIF> nodeEdges = _affectedNodeEdges.get(node);
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
    public void addTraceabilityLinkJavaSource(final JavaClassNode classNodeMaterial, final TraceabilityLink traceabilityLink)
    {

        TypePointer pointer = (TypePointer) traceabilityLink.getTarget();
        ClassNode swiftClassNode = getClassNodeMaterial(new SwiftClassNode(pointer.getFullyQualifiedName()));
        updateNeigbbourhood(swiftClassNode, Marking.CHANGED);

        //Add Dependency
        TraceLinkDependency dependencyMaterial = new TraceLinkDependency((JavaClassNode) classNodeMaterial, (SwiftClassNode) swiftClassNode, traceabilityLink.getProbability());
        _traceabilityLinks.add(dependencyMaterial);
        _model.addEdge(dependencyMaterial);
    }
    public void addTraceabilityLinkSwiftSource(final SwiftClassNode swiftClassNodeMaterial, final TraceabilityLink traceabilityLink)
    {

        TypePointer pointer = (TypePointer) traceabilityLink.getTarget();
        ClassNode javaClassNode = getClassNodeMaterial(new JavaClassNode(pointer.getFullyQualifiedName()));
        updateNeigbbourhood(javaClassNode, Marking.CHANGED);

        //Add Dependency
        TraceLinkDependency dependencyMaterial = new TraceLinkDependency((JavaClassNode) javaClassNode, (SwiftClassNode) swiftClassNodeMaterial, traceabilityLink.getProbability());
        _traceabilityLinks.add(dependencyMaterial);
        _model.addEdge(dependencyMaterial);
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

    public ObservableSet<ClassNode> getAffectedClassesByChange() {
        return _affectedNodes;
    }

    public ObservableSet<ClassNode> getInitalChangedClasses(){return _initialChangedClasse;}

    public ObservableMap<ClassNode, Set<DependencyIF>> getAffectedNodeEdges(){return _affectedNodeEdges;}

    public ObservableSet<TraceLinkDependency> getTraceLinkDepenendecySet(){return _traceabilityLinks;}
}