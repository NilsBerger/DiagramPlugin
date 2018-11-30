package materials;

import Utils.HashUtils;
import valueobjects.RelationshipType;

/**
 * Defines a relationship between two ClassNodes
 */
public class ClassDependency{
    private final ClassNode _dependentClass;
    private final ClassNode _independentClass;
    private RelationshipType _relationshipType;


    public ClassDependency(ClassNode dependentClass, ClassNode independentClass, RelationshipType relationshipType)
    {
        this._dependentClass = dependentClass;
        this._independentClass = independentClass;
        this._relationshipType = relationshipType;
    }

    /**
     * Sets the new RelationshipType of the dependency
     */
    public void setRelationshipType(RelationshipType type){
        _relationshipType = type;
    }


    /**
     *Returns true, if their is a dependency between the nodes.
     */
    public boolean containsNodes(ClassNode oneNode, ClassNode otherNode)
    {
        boolean independent = _independentClass.equals(oneNode) || _independentClass.equals(otherNode);
        boolean dependent = _dependentClass.equals(oneNode) || _dependentClass.equals(otherNode);
        if(independent && dependent)
        {
            return true;
        }
        return false;
    }

    /**
     * Returns a ClassNode that dependents on another class
     * @return ClassNode
     */

    public ClassNode getDependentClass() {
        return _dependentClass;
    }

    /**
     * Returns a ClassNode that the depeendent ClassNode depends on.
     * @return
     */
    public ClassNode getIndependentClass() {
        return _independentClass;
    }

    /**
     * Returns the RelationshipType of the Dependency
     */
    public RelationshipType getRelationshipType() {
        return _relationshipType;
    }

    @Override
    public String toString() {
        return "Class '" + _dependentClass.getSimpleName() +"' is dependent on Class '" + _independentClass.getSimpleName()+ "'.";
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
        if(obj == null)
            return false;
        if(this == obj)
            return true;
        if(this.getClass() != obj.getClass())
            return false;
        final ClassDependency otherClassNodeFachwert = (ClassDependency) obj;
        return  this.getDependentClass().equals(otherClassNodeFachwert.getDependentClass()) &&
                this.getIndependentClass().equals(otherClassNodeFachwert.getIndependentClass()) &&
                this.getRelationshipType() == otherClassNodeFachwert.getRelationshipType();
    }
}
