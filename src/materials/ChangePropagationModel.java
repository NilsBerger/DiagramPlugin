package materials;

import valueobjects.RelationshipType;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Def
 */
public class ChangePropagationModel {

    private Set<ProgramEntity> _nodes;
    private Set<ProgramEntityRelationship> _edges;
    private Map<ProgramEntity, Set<ProgramEntityRelationship>> _nodesEdges;

    public ChangePropagationModel(final Set<? extends ProgramEntityRelationship> classdependencies) {
        _nodes = new HashSet<>();
        _nodesEdges = new HashMap<>();
        _edges = new HashSet<>();
        addAll(classdependencies);
    }

    private void addNode(final ProgramEntity node) {
        _nodes.add(node);
    }

    public void addEdge(final ProgramEntityRelationship edge) {
        _edges.add(edge);
        addNode(edge.getDependentClass());
        addNode(edge.getIndependentClass());
        addNodeEdge(edge.getDependentClass(), edge);
        addNodeEdge(edge.getIndependentClass(), edge);
    }

    private void addNodeEdge(final ProgramEntity node, final ProgramEntityRelationship edge) {
        _nodesEdges.computeIfAbsent(node, empHashSet -> new HashSet<>());
        Set<ProgramEntityRelationship> nodeEdges = _nodesEdges.get(node);
        _nodesEdges.put(node, nodeEdges);
        nodeEdges.add(edge);
    }

    /**
     * @param edges
     */
    public void addAll(final Set<? extends ProgramEntityRelationship> edges) {
        edges.forEach(edge -> addEdge(edge));
    }

    Set<ProgramEntity> getBottomDependencies(final ProgramEntity programEntity) {
        final Set<ProgramEntityRelationship> programEntityRelationships = _nodesEdges.get(programEntity);
        return programEntityRelationships.stream()
                .parallel()
                .filter(not(ProgramEntityRelationship::isInconsistenntRelationship))
                .filter(relationship -> !relationship.getIndependentClass().equals(programEntity))
                .map(ProgramEntityRelationship::getIndependentClass)
                .collect(Collectors.toSet());
    }

    Set<ProgramEntity> getTopDependencies(final ProgramEntity programEntity) {
        final Set<ProgramEntityRelationship> programEntityRelationships = _nodesEdges.get(programEntity);
        return programEntityRelationships.stream()
                .parallel()
                .filter(not(ProgramEntityRelationship::isInconsistenntRelationship))
                .filter(relationship -> !relationship.getDependentClass().equals(programEntity))
                .map(ProgramEntityRelationship::getDependentClass)
                .collect(Collectors.toSet());
    }

    Set<ProgramEntity> getBottomInconsistencies(final ProgramEntity programEntity) {
        final Set<ProgramEntityRelationship> programEntityRelationships = _nodesEdges.get(programEntity);
        return programEntityRelationships.stream()
                .parallel()
                .filter(ProgramEntityRelationship::isInconsistenntRelationship)
                .filter(relationship -> !relationship.getIndependentClass().equals(programEntity))
                .map(ProgramEntityRelationship::getIndependentClass)
                .collect(Collectors.toSet());
    }


    Set<ProgramEntity> getTopInconsistencies(final ProgramEntity programEntity) {
        final Set<ProgramEntityRelationship> programEntityRelationships = _nodesEdges.get(programEntity);
        return programEntityRelationships.stream()
                .parallel()
                .filter(ProgramEntityRelationship::isInconsistenntRelationship)
                .filter(relationship -> !relationship.getDependentClass().equals(programEntity))
                .map(ProgramEntityRelationship::getDependentClass)
                .collect(Collectors.toSet());
    }

    public Set<ProgramEntity> getNeighbourhoodWithoutIncomingInconsistencies(final ProgramEntity clazz) {
        Set<ProgramEntity> neighbourhood = new HashSet<>();
        neighbourhood.addAll(getBottomDependencies(clazz));
        neighbourhood.addAll(getTopDependencies(clazz));
        neighbourhood.addAll(getTopInconsistencies(clazz));
        return neighbourhood;

    }

    public Set<ProgramEntityRelationship> getNeighbourhoodDependenciesWithoutIncomingInconsistencies(final ProgramEntity programEntity) {
        final Set dependencies = _nodesEdges.get(programEntity)
                .stream()
                .filter(not(ProgramEntityRelationship::isInconsistenntRelationship))
                .collect(Collectors.toSet());

        return Collections.unmodifiableSet(dependencies);
    }

    public Set<ProgramEntityRelationship> getDependenciesForNode(ProgramEntity node) {
        return _nodesEdges.get(node);
    }

    public Set<ProgramEntity> getNodes() {
        return _nodes;
    }

    /**
     * @param changedEntity
     */
    public void createInconsistencies(final ProgramEntity changedEntity) {
        assert changedEntity != null : "ClassNode should not be null";
        _nodesEdges.computeIfAbsent(changedEntity, empHashSet -> new HashSet<>());

        final Set<ProgramEntity> neighbours = new HashSet<>();

        neighbours.addAll(getBottomDependencies(changedEntity));
        neighbours.addAll(getTopDependencies(changedEntity));

//        for(ProgramEntity neighbourEntity : neighbours)
//        {
//            if(!inconcistencyBetweenNodes(neighbourEntity, changedEntity))
//            {
//                ProgramEntityRelationship inconsistency = new ProgramEntityRelationship(changedEntity, neighbourEntity, RelationshipType.InconsistentRelationship);
//                addEdge(inconsistency);
//            }
//        }
        neighbours.stream()
                .filter(neighbourEntity -> !inconcistencyBetweenNodes(neighbourEntity, changedEntity))
                .map(neighbourEntity -> new ProgramEntityRelationship(changedEntity, neighbourEntity, RelationshipType.InconsistentRelationship))
                .forEach(this::addEdge);
    }

    boolean inconcistencyBetweenNodes(ProgramEntity a, ProgramEntity b) {
        assert _nodesEdges.containsKey(a) : "ClassNode stored as a key";
        assert _nodesEdges.containsKey(b) : "ClassNode stored as a key";

        final ProgramEntityRelationship possibleRelationship1 = new ProgramEntityRelationship(a, b, RelationshipType.InconsistentRelationship);

        final ProgramEntityRelationship possibleRelationship2 = new ProgramEntityRelationship(b, a, RelationshipType.InconsistentRelationship);
        final Set<ProgramEntityRelationship> collect1 = _edges.stream()
                .filter(ProgramEntityRelationship::isInconsistenntRelationship)
                .filter(edge -> edge.equals(possibleRelationship1))
                .collect(Collectors.toSet());

        final Set<ProgramEntityRelationship> collect2 = _edges.stream()
                .filter(ProgramEntityRelationship::isInconsistenntRelationship)
                .filter(edge -> edge.equals(possibleRelationship2))
                .collect(Collectors.toSet());

        return collect1.size() > 0 || collect2.size() > 0;
    }

    public void updateDependency(ProgramEntityRelationship programEntityRelationship, RelationshipType relationshipType) {
        final ProgramEntityRelationship updatedRelationship = _edges.stream()
                .filter(edge -> edge.equals(programEntityRelationship))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        updatedRelationship.setRelationshipType(relationshipType);
    }

    public Set<ProgramEntityRelationship> getInconsistencies() {
        return _edges
                .stream()
                .filter(relationship -> relationship.getRelationshipType() == RelationshipType.InconsistentRelationship)
                .collect(Collectors.toSet());
    }

    public Set<ProgramEntityRelationship> getDependencies() {
        return Collections.unmodifiableSet(_edges);
    }

    public static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }
}
