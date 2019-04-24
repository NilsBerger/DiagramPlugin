package service.functional;

import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TypePointer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import materials.ChangePropagationModel;
import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import materials.TraceLinkProgramEntityAssociation;
import valueobjects.Language;
import valueobjects.Marking;
import valueobjects.RelationshipType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * ChangePropagationProcess keeps track of inconsistencies and and the location where secondary changes need to be made
 */
public class ChangePropagationProcess {

    private static final ChangePropagationProcess _INSTANCE = new ChangePropagationProcess();

    private ChangeAndFixStrategyIF _strategy;
    private ChangePropagationModel _model;
    private boolean _isInitialized = false;

    private final ObservableSet<ProgramEntity> _affectedEntities = FXCollections.observableSet(new HashSet<>());
    private final ObservableSet<ProgramEntityRelationship> _affectedRelationships = FXCollections.observableSet(new HashSet<>());
    private final ObservableMap<ProgramEntity, Set<ProgramEntityRelationship>> _affectedEntityRelationships = FXCollections.observableHashMap();

    private final ObservableSet<TraceLinkProgramEntityAssociation> _traceabilityLinks = FXCollections.observableSet(new HashSet<>());

    private static Set<ClassNodeChangeListener> _classNodeChangedListeners = new HashSet<>();
    private static Set<ClassDependencyChangeListener> _classDependencyChangedListeners = new HashSet<>();


    public static ChangePropagationProcess getInstance() {
        return _INSTANCE;
    }

    /**
     * Initiates the propagation model with fresh data and clears the previous data
     *
     * @param classdependencies The classdependencies a a program
     * @param strategy
     */
    public void initialize(final Set<ProgramEntityRelationship> classdependencies, ChangeAndFixStrategyIF strategy) {
        clear();
        _isInitialized = true;
        _model = new ChangePropagationModel(classdependencies);
        init(strategy);
    }

    /**
     * Reloads the data of a previous analysis
     *
     * @param relationships The classdependencies of a previous analysis
     * @param strategy
     */
    public void reloadData(final Set<ProgramEntityRelationship> relationships, ChangeAndFixStrategyIF strategy) {

        initialize(relationships, strategy);
        for (ProgramEntityRelationship programEntityRelationship : relationships) {
            addAffectedDependency(programEntityRelationship);
            if (programEntityRelationship instanceof TraceLinkProgramEntityAssociation) {
                _traceabilityLinks.add((TraceLinkProgramEntityAssociation) programEntityRelationship);
            }
        }
        notifyProgramEntityChangeListener();
    }

    /**
     * Clears all data
     */
    public void clear() {
        _isInitialized = false;
        _affectedEntityRelationships.clear();
        _affectedEntities.clear();
        _affectedRelationships.clear();
        _model = null;
    }

    public boolean isInitialized() {
        return _isInitialized;
    }

    private void init(ChangeAndFixStrategyIF strategy) {
        this._strategy = strategy;
    }

    /**
     * Set the given ProgramEntity as the "Initial" ProgramEntity
     * and updates the affected Neighbourhood!
     *
     * @param programEntity
     * @return
     */
    public boolean changeInitial(final ProgramEntity programEntity) {
        if (!_strategy.accept(_affectedEntities)) {
            return false;
        }
        final ProgramEntity changedClass = findClassFromModel(programEntity);


        changedClass.setAsInitialClass();
        changedClass.setMarking(Marking.CHANGED);
        update(changedClass, Marking.CHANGED);
        return true;
    }

    /**
     * Set the given ClassNode as the "Initial" ClassNode and updates the affected Neighbourhood!
     *
     * @param programEntity
     * @return returns true if the strategy
     */
    public boolean change(ProgramEntity programEntity) {

        Objects.requireNonNull(programEntity, "programEntity should not be null");
        if (!_strategy.accept(_affectedEntities)) {
            return false;
        }
        final ProgramEntity changedClass = findClass(programEntity);
        update(changedClass, Marking.CHANGED);
        return true;


    }

    //TODO
    private ProgramEntity findClass(final ProgramEntity classFromIntelliJ) {
        for (ProgramEntity node : _affectedEntities) {
            if (node.equals(classFromIntelliJ)) {
                return node;
            }
        }
        if (_affectedEntities.size() == 0) {

        }
        throw new IllegalStateException("Could not find node: " + classFromIntelliJ.getSimpleName());
    }

    //Todo
    private ProgramEntity findClassFromModel(final ProgramEntity programEntity) {
        for (ProgramEntity node : _model.getNodes()) {
            if (node.equals(programEntity)) {
                return node;
            }
        }
        if (_affectedEntities.size() == 0) {

        }
        throw new IllegalStateException("Could not find node: " + programEntity.getSimpleName());
    }


    /**
     * @param programEntity
     * @return
     */
    public ProgramEntity select(ProgramEntity programEntity) {

        assert programEntity != null : "ProgramEntity should not be null";
        assert getNextMarkedClasses().contains(programEntity) : "ProgramEntity : " + programEntity.getSimpleName() + " should be in set of marked classes";

        if (!getNextMarkedClasses().contains(programEntity))
            System.err.println("Class '" + programEntity.getFullEntityName() + "' not found in affected entities that need to be visited");
        try {
            return getClassToVisit(programEntity);
        } finally {
        }
    }

    /**
     * @param changedClass
     * @param marking
     */
    public void update(ProgramEntity changedClass, Marking marking) {

        assert changedClass != null : "Changed Class should not be null";
        assert marking != null : "Changed Class should not be null";

        final ProgramEntity classFromModel = findClassFromModel(changedClass);
        classFromModel.setMarking(marking);
        addAffectedProgramEntity(classFromModel);
        if (marking == Marking.CHANGED || marking == Marking.PROPAGATES) {
            _model.createInconsistencies(classFromModel);
            propagateChange(classFromModel, classFromModel);
        } else {//No new neighbours of changed class to calculate
        }

    }

    /**
     * Propagates the changeInitial of a ProgramEntity to its neighbours.
     *
     * @param changedClass
     */
    public void propagateChange(final ProgramEntity changedClass, final ProgramEntity actuatorProgramEntity) {
        Objects.requireNonNull(changedClass, "ProgramEntity should not be null");
        assert changedClass.getMarking() == Marking.CHANGED || changedClass.getMarking() == Marking.PROPAGATES : "Can only propagate the changeInitial, if the ProgramEntity has the Marking CHANGED or PROPAGATES";

        final Set<ProgramEntityRelationship> neighbourhood = _model.getNeighbourhoodDependenciesWithoutIncomingInconsistencies(changedClass);
        addAffectedRelationship(neighbourhood);

        setActuatorForNodes(_model.getNeighbourhoodWithoutIncomingInconsistencies(actuatorProgramEntity), actuatorProgramEntity);
        setMarkingNext();
        //TODO
        //addAffectedNodes(affectedNodes);
        //calculateAffectedNeighbourhood(affectedDependencies);
        addMissingRelationships();
        notifyProgramEntityChangeListener();
    }


    private void addMissingRelationships() {

        final ObservableSet<ProgramEntity> affectedClassesByChange = getAffectedClassesByChange();

        for (ProgramEntity entity : affectedClassesByChange) {
            final Set<ProgramEntityRelationship> dependenciesForNode = _model.getDependenciesForNode(entity);
            for (ProgramEntityRelationship relationship : dependenciesForNode) {
                final ProgramEntity dependentClass = relationship.getDependentClass();
                final ProgramEntity independentClass = relationship.getIndependentClass();
                if (affectedClassesByChange.contains(dependentClass) && affectedClassesByChange.contains(independentClass)) {
                    addAffectedDependency(relationship);
                }

            }
        }
    }

    private void setMarkingNext() {
        Marking wrongMarking = Marking.BLANK;
        for (ProgramEntity programEntity : _affectedEntities) {
            if (programEntity.getMarking() == wrongMarking) {
                programEntity.setMarking(Marking.NEXT);
            }
        }
    }


    private ProgramEntity getClassToVisit(final ProgramEntity programEntity) {
        for (ProgramEntity node : getNextMarkedClasses()) {
            if (node.equals(programEntity)) {
                return node;
            }
        }
        //Should not happen because no concurrency
        assert true : "Should never be here";
        return null;
    }

    private void addAffectedProgramEntity(final ProgramEntity node) {
        boolean added = _affectedEntities.add(node);
        if (added) {
            System.out.println("addNode - Added AffectedProgramEntity : " + node);
        } else {
            System.out.println("addNode - AffectedProgramEntity already in model : " + node);
        }
    }

    private void addAffectedDependency(final ProgramEntityRelationship edge) {
        _affectedRelationships.add(edge);
        addAffectedProgramEntity(edge.getDependentClass());
        addAffectedProgramEntity(edge.getIndependentClass());
        addaffectedProgramEntityRelationships(edge.getDependentClass(), edge);
        addaffectedProgramEntityRelationships(edge.getIndependentClass(), edge);
    }

    private void addAffectedRelationship(final Set<ProgramEntityRelationship> classDependencies) {
        for (ProgramEntityRelationship dependency : classDependencies) {
            addAffectedDependency(dependency);
        }
    }

    public void addaffectedProgramEntityRelationships(final ProgramEntity node, final ProgramEntityRelationship edge) {
        _affectedEntityRelationships.computeIfAbsent(node, empHashSet -> new HashSet<>());
        Set<ProgramEntityRelationship> nodeEdges = _affectedEntityRelationships.get(node);
        _affectedEntityRelationships.put(node, nodeEdges);
        nodeEdges.add(edge);
    }


    public ChangePropagationModel getModel() {
        return _model;
    }


    public void addTraceabilityLinkJavaSource(final ProgramEntity programEntityMaterial, final TraceabilityLink traceabilityLink) {

        TypePointer pointer = (TypePointer) traceabilityLink.getTarget();
        ProgramEntity swiftProgramEntity = findClassFromModel(new ProgramEntity(pointer.getFullyQualifiedName(), Language.Swift));


        //Add Dependency
        TraceLinkProgramEntityAssociation traceDependency = new TraceLinkProgramEntityAssociation(programEntityMaterial, swiftProgramEntity, traceabilityLink.getProbability());
        _traceabilityLinks.add(traceDependency);
        _model.addEdge(traceDependency);
        _model.createInconsistencies(swiftProgramEntity);
        update(swiftProgramEntity, Marking.NEXT);

    }

    public void addTraceabilityLinkSwiftSource(final ProgramEntity swiftProgramEntity, final TraceabilityLink traceabilityLink) {

        TypePointer pointer = (TypePointer) traceabilityLink.getTarget();
        ProgramEntity javaProgramEntity = findClassFromModel(new ProgramEntity(pointer.getFullyQualifiedName(), Language.Java));


        //Add Dependency
        TraceLinkProgramEntityAssociation traceDependency = new TraceLinkProgramEntityAssociation(javaProgramEntity, swiftProgramEntity, traceabilityLink.getProbability());
        _traceabilityLinks.add(traceDependency);
        _model.addEdge(traceDependency);
        _model.createInconsistencies(javaProgramEntity);

        _model.addEdge(traceDependency);
        update(javaProgramEntity, Marking.NEXT);
    }

    public void updateDependency(ProgramEntityRelationship programEntityRelationship, RelationshipType relationshipType) {
        //final Set<ProgramEntityRelationship> classDependencies = _model.updateDependency(programEntityRelationship, relationshipType);
        //notifyClassDependencyChangeListener(classDependencies);
    }

    public void addClassNodeChangeListener(final ClassNodeChangeListener observer) {
        assert observer != null : "Precondition violated: != null";
        _classNodeChangedListeners.add(observer);
    }

    public void removeClassNodeChangeListener(final ClassNodeChangeListener observer) {
        assert observer != null : "Precondition violates: != null";
        _classNodeChangedListeners.remove(observer);
    }

    public void notifyProgramEntityChangeListener() {
        final Iterator<ClassNodeChangeListener> it = _classNodeChangedListeners.iterator();
        while (it.hasNext()) {
            final ClassNodeChangeListener listener = it.next();
            listener.updateView();
        }
        while (it.hasNext()) {
            final ClassNodeChangeListener listener = it.next();
            listener.updateView();
        }
    }

    public void addClassDependencyListener(final ClassDependencyChangeListener observer) {
        assert observer != null : "Precondition violates: != null";
        _classDependencyChangedListeners.add(observer);
    }

    public void removeClassDependencyChangeListener(final ClassDependencyChangeListener observer) {
        assert observer != null : "Precondition violates: != null";
        _classDependencyChangedListeners.remove(observer);
    }

    public void notifyClassDependencyChangeListener(Set<ProgramEntityRelationship> changedDependencies) {
        final Iterator<ClassDependencyChangeListener> it = _classDependencyChangedListeners.iterator();
        while (it.hasNext()) {
            final ClassDependencyChangeListener listener = it.next();
            listener.updateDependencies(changedDependencies);
        }
    }

    public void setActuatorForNodes(final Set<ProgramEntity> programEntities, final ProgramEntity actuator) {
        for (ProgramEntity programEntity : programEntities) {
            programEntity.setActuator(actuator);
        }
    }

    /**
     * Returns all ClassNodes that are marked with "NEXT"
     *
     * @return
     */
    public Set<ProgramEntity> getNextMarkedClasses() {
        return _affectedEntities.stream().filter(classes -> classes.getMarking() == Marking.NEXT).collect(Collectors.toSet());
    }

    public Set<ProgramEntityRelationship> getAffectedDependencies(ProgramEntity node) {
        Set<ProgramEntityRelationship> dependencies = _model.getDependenciesForNode(node);
        Set<ProgramEntityRelationship> affectedDepenendcies = new HashSet<>();
        for (ProgramEntityRelationship dependency : dependencies) {
            for (ProgramEntity affectedNode : _affectedEntities) {
                if (dependency.containsNodes(node, affectedNode)) {
                    affectedDepenendcies.add(dependency);
                }
            }

        }
        return affectedDepenendcies;
    }

    /**
     * Returns all the dependencies, except the inconsistent dep.
     *
     * @return
     */
    public Set<ProgramEntityRelationship> getDependenciesOfModel() {
        Set<ProgramEntityRelationship> importDepdenencies = new HashSet<>();
        for (ProgramEntityRelationship dependency : _model.getDependencies()) {
            if (dependency.getRelationshipType() != RelationshipType.InconsistentRelationship) {
                importDepdenencies.add(dependency);
            }
        }
        return importDepdenencies;
    }


    public ObservableSet<ProgramEntity> getAffectedClassesByChange() {
        return _affectedEntities;
    }

    /**
     * Returns the affected dependencies by the changeInitial
     *
     * @return
     */
    public ObservableSet<ProgramEntityRelationship> getAffectedDependenciesByChange() {
        return _affectedRelationships;
    }


    public ObservableMap<ProgramEntity, Set<ProgramEntityRelationship>> getAffectedNodeEdges() {
        return _affectedEntityRelationships;
    }

    public ObservableSet<TraceLinkProgramEntityAssociation> getTraceLinkDepenendecySet() {
        return _traceabilityLinks;
    }
}
