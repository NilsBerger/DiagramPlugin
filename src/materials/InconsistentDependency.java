package materials;

import Utils.HashUtils;

public class InconsistentDependency implements DependencyIF
{
    private final ClassNode dependentClass;
    private final ClassNode independentClass;

    public InconsistentDependency(final ClassDependency dependency)
    {
        this.dependentClass = dependency.getDependentClass();
        this.independentClass = dependency.getIndependentClass();
    }
    public InconsistentDependency(final ClassNode dependentClassNode, final ClassNode independetClassNode)
    {
        this.dependentClass = dependentClassNode;
        this.independentClass = independetClassNode;
    }

    @Override
    public ClassNode getDependentClass() {
        return dependentClass;
    }

    @Override
    public ClassNode getIndependentClass() {
        return independentClass;
    }
    @Override
    public String toString() {
        return "Inconsistency between Class '" + dependentClass.getSimpleClassName() +"' and Class '" + independentClass.getSimpleClassName()+ "'.";
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
        final InconsistentDependency otherClassNodeFachwert = (InconsistentDependency) obj;
        return  this.getDependentClass().equals(otherClassNodeFachwert.getDependentClass()) &&
                this.getIndependentClass().equals(otherClassNodeFachwert.getIndependentClass());
    }
}
