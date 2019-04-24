package materials;

import Utils.HashUtils;
import valueobjects.RelationshipType;

/**
 * Defines a relationship between two ClassNodes
 */
public class ProgramEntityRelationship {

    private final ProgramEntity _dependentClass;
    private final ProgramEntity _independentClass;
    private RelationshipType _relationshipType;

    public static final ProgramEntityRelationshipComparator COMPARATOR = new ProgramEntityRelationshipComparator();

    public ProgramEntityRelationship(ProgramEntity dependentClass, ProgramEntity independentClass, RelationshipType relationshipType) {
        this._dependentClass = dependentClass;
        this._independentClass = independentClass;
        this._relationshipType = relationshipType;
    }

    /**
     * Sets the new RelationshipType of the dependency
     */
    public void setRelationshipType(RelationshipType type) {
        _relationshipType = type;
    }


    /**
     * Returns true, if their is a dependency between the nodes.
     */
    public boolean containsNodes(ProgramEntity oneNode, ProgramEntity otherNode) {
        boolean independent = _independentClass.equals(oneNode) || _independentClass.equals(otherNode);
        boolean dependent = _dependentClass.equals(oneNode) || _dependentClass.equals(otherNode);
        return independent && dependent;
    }

    /**
     * Returns a ClassNode that dependents on another class
     *
     * @return ClassNode
     */

    public ProgramEntity getDependentClass() {
        return _dependentClass;
    }

    /**
     * Returns a ClassNode that the depeendent ClassNode depends on.
     *
     * @return the independent ClassNode
     */
    public ProgramEntity getIndependentClass() {
        return _independentClass;
    }

    /**
     * Returns the RelationshipType of the Dependency
     */
    public RelationshipType getRelationshipType() {
        return _relationshipType;
    }

    public int getSortOrder() {
        return 2;
    }


    /**
     * @return true is relationship is inconsistent
     */
    public boolean isInconsistenntRelationship() {
        return _relationshipType == RelationshipType.InconsistentRelationship;
    }

    @Override
    public String toString() {
        return "Class '" + _dependentClass.getSimpleName() + "' is dependent on Class '" + _independentClass.getSimpleName() + "'.";
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = HashUtils.calcHashCode(hash, _independentClass);
        hash = HashUtils.calcHashCode(hash, _dependentClass);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (this.getClass() != obj.getClass())
            return false;
        final ProgramEntityRelationship otherClassNodeFachwert = (ProgramEntityRelationship) obj;
        return this.getDependentClass().equals(otherClassNodeFachwert.getDependentClass()) &&
                this.getIndependentClass().equals(otherClassNodeFachwert.getIndependentClass()) &&
                this.getRelationshipType() == otherClassNodeFachwert.getRelationshipType();
    }

    public static ProgramEntityRelationship createInconsistentRelationship(final ProgramEntity dependentEntity, final ProgramEntity independentEntity) {
        return new ProgramEntityRelationship(dependentEntity, independentEntity, RelationshipType.InconsistentRelationship);
    }
}

