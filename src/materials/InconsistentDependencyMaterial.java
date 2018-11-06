package materials;

import Utils.HashUtils;

public class InconsistentDependencyMaterial implements DependencyIF
{
    private final ClassNodeMaterial dependentClass;
    private final ClassNodeMaterial independentClass;

    public InconsistentDependencyMaterial(final ClassDependencyMaterial dependency)
    {
        this.dependentClass = dependency.getDependentClass();
        this.independentClass = dependency.getIndependentClass();
    }

    @Override
    public ClassNodeMaterial getDependentClass() {
        return dependentClass;
    }

    @Override
    public ClassNodeMaterial getIndependentClass() {
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
        final InconsistentDependencyMaterial otherClassNodeFachwert = (InconsistentDependencyMaterial) obj;
        return  this.getDependentClass().equals(otherClassNodeFachwert.getDependentClass()) &&
                this.getIndependentClass().equals(otherClassNodeFachwert.getIndependentClass());
    }
}
