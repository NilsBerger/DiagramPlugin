package materials;

import Utils.HashUtils;
import valueobjects.RelationshipType;

/**
 * Created by Nils-Pc on 06.08.2018.
 */
public class ClassDependency{
    private final ClassNode dependentClass;
    private final ClassNode independentClass;
    private RelationshipType _relationshipType;
    private double _tracelinkvalue = 0;


    public ClassDependency(ClassNode dependentClass, ClassNode independentClass, RelationshipType relationshipType)
    {
        this.dependentClass = dependentClass;
        this.independentClass = independentClass;
        this._relationshipType = relationshipType;
    }

    public ClassDependency(ClassNode dependentClass, ClassNode independentClass, RelationshipType relationshipType, double tracelinkValue)
    {
        this.dependentClass = dependentClass;
        this.independentClass = independentClass;
        this._relationshipType = relationshipType;
        this._tracelinkvalue = tracelinkValue;
    }
    public ClassDependency switchDependencies()
    {
        return new ClassDependency(this.independentClass, this.dependentClass, this._relationshipType, this._tracelinkvalue);
    }

    /**
     * Returns a ClassNode, that dependents on another class
     * @return ClassNode
     */

    public ClassNode getDependentClass() {
        return dependentClass;
    }

    /**
     * Returns a ClassNode that the depeendent ClassNode depends on.
     * @return
     */
    public ClassNode getIndependentClass() {
        return independentClass;
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
        return "Class '" + dependentClass.getSimpleClassName() +"' is dependent on Class '" + independentClass.getSimpleClassName()+ "'.";
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = HashUtils.calcHashCode(hash, independentClass);
        hash = HashUtils.calcHashCode(hash, dependentClass);
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
                this.getRelationshipType() == otherClassNodeFachwert.getRelationshipType() &&
                this._tracelinkvalue == otherClassNodeFachwert.getTracelinkValue();
    }
}
