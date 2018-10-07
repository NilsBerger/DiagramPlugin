package dotparser;

import Utils.HashUtils;

/**
 * Created by Nils-Pc on 06.08.2018.
 */
public class DependencyFachwert implements DependencyIF{
    private final ClassNodeMaterial dependentClass;
    private final ClassNodeMaterial independentClass;


    public DependencyFachwert(ClassNodeMaterial dependentClass, ClassNodeMaterial independentClass)
    {
        if((dependentClass) == null ||(independentClass) == null)
        {
            throw new IllegalArgumentException("ClassNode should not be null");
        }
        this.dependentClass = dependentClass;
        this.independentClass = independentClass;
    }

    public ClassNodeMaterial getDependentClass() {
        return dependentClass;
    }

    public ClassNodeMaterial getIndependentClass() {
        return independentClass;
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
        final DependencyFachwert otherClassNodeFachwert = (DependencyFachwert) obj;
        return  this.getDependentClass().equals(otherClassNodeFachwert.getDependentClass()) &&
                this.getIndependentClass().equals(otherClassNodeFachwert.getIndependentClass());
    }
}
