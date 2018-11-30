package materials;

import valueobjects.RelationshipType;

public class TraceLinkClassDependency extends ClassDependency{
    private final double _tracelinkValue;
    public TraceLinkClassDependency(ClassNode dependentClass, ClassNode independentClass, double tracelinkValue)
    {
        super(dependentClass,independentClass,RelationshipType.Traceability_Association);
        assert tracelinkValue >=0 && tracelinkValue <= 100.00 : "Precondition violated: Trace-Link not between 0.0 and 100.0";
        this._tracelinkValue = tracelinkValue;
    }

    @Override
    public void setRelationshipType(RelationshipType type) {
    }

    /**
     * Returns the probability value of the Trace-Link
     */
    public double getTracelinkValue() {
        return _tracelinkValue;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(this == obj)
            return true;
        if(this.getClass() != obj.getClass())
            return false;
        final TraceLinkClassDependency otherClassNodeFachwert = (TraceLinkClassDependency) obj;
        return  this.getDependentClass().equals(otherClassNodeFachwert.getDependentClass()) &&
                this.getIndependentClass().equals(otherClassNodeFachwert.getIndependentClass()) &&
                this.getRelationshipType() == otherClassNodeFachwert.getRelationshipType() &&
                this._tracelinkValue == otherClassNodeFachwert.getTracelinkValue();
    }
}
