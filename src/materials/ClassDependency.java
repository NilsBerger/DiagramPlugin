package materials;

import Utils.HashUtils;
import valueobjects.RelationshipType;

/**
 * Created by Nils-Pc on 06.08.2018.
 */
public class ClassDependency{
    private final ClassNode _dependentClass;
    private final ClassNode _independentClass;
    private RelationshipType _relationshipType;
    private double _tracelinkvalue = 0;


    public ClassDependency(ClassNode dependentClass, ClassNode independentClass, RelationshipType relationshipType)
    {
        this._dependentClass = dependentClass;
        this._independentClass = independentClass;
        this._relationshipType = relationshipType;
    }

    public ClassDependency(ClassNode dependentClass, ClassNode independentClass, RelationshipType relationshipType, double tracelinkValue)
    {
        this._dependentClass = dependentClass;
        this._independentClass = independentClass;
        this._relationshipType = relationshipType;
        this._tracelinkvalue = tracelinkValue;
    }
    public ClassDependency switchDependencies()
    {
        return new ClassDependency(this._independentClass, this._dependentClass, this._relationshipType, this._tracelinkvalue);
    }

    /**
     * Returns a ClassNode, that dependents on another class
     * @return ClassNode
     */

    public ClassNode getDependentClass() {
        return _dependentClass;
    }

    /**
     * Returns a ClassNode that the depeendent ClassNode depends on.
     * @return
     */
    public ClassNode get_independentClass() {
        return _independentClass;
    }

    public RelationshipType getRelationshipType() {
        return _relationshipType;
    }

    public void setRelationshipType(RelationshipType type){
        _relationshipType = type;
    }

    /**
     * @return The Trace Link Value of the dependency
     */
    public double getTracelinkValue() {
        return _tracelinkvalue;
    }

    /**
     * Sets
     * @param tracelinkvalue A value between 0.0 and 100.0.
     */
    public void setTracelinkvalue(double tracelinkvalue) {
        assert tracelinkvalue >=0 && tracelinkvalue <= 100.00 : "Precondition violated: Trace-Link not between 0.0 and 100.0";
        this._tracelinkvalue = tracelinkvalue;
    }

    @Override
    public String toString() {
        return "Class '" + _dependentClass.getSimpleClassName() +"' is dependent on Class '" + _independentClass.getSimpleClassName()+ "'.";
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = HashUtils.calcHashCode(hash, _independentClass);
        hash = HashUtils.calcHashCode(hash, _dependentClass);
        return hash;
    }

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
                this.get_independentClass().equals(otherClassNodeFachwert.get_independentClass()) &&
                this.getRelationshipType() == otherClassNodeFachwert.getRelationshipType() &&
                this._tracelinkvalue == otherClassNodeFachwert.getTracelinkValue();
    }
}
