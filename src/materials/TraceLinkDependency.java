package materials;

import Utils.HashUtils;

/**
 * The tracelinkdependency class represents a dependency between a JavaClassNode and SwiftClassNode
 */
public class TraceLinkDependency extends ClassDependency {

    private double tracelinkvalue;

    /**
     *
     * @param javaClassNode
     * @param swiftClassNode
     * @param tracelinkvalue
     */
    public TraceLinkDependency(JavaClassNode javaClassNode, SwiftClassNode swiftClassNode, double tracelinkvalue)
    {
        super(javaClassNode, swiftClassNode);
        assert javaClassNode != null :"Precondition violated: JavaClassNode should not be null";
        assert swiftClassNode != null :"Precondition violated: SwiftClassNode should not be null";
        assert tracelinkvalue >=0 && tracelinkvalue <= 100.00 : "Precondition violated: Trace-Link not between 0.0 and 100.0";

        this.tracelinkvalue = tracelinkvalue;
    }

    /**
     * @return The Trace Link Value of the dependency
     */
    public double getTracelinkValue() {
        return tracelinkvalue;
    }

    /**
     * Sets
     * @param tracelinkvalue A value between 0.0 and 100.0.
     */
    public void setTracelinkvalue(double tracelinkvalue) {
        assert tracelinkvalue >=0 && tracelinkvalue <= 100.00 : "Precondition violated: Trace-Link not between 0.0 and 100.0";
        this.tracelinkvalue = tracelinkvalue;
    }

    /**
     *
     * @return The JavaClassNode of the dependency.
     */
    public JavaClassNode getJavaClassNode()
    {
        return (JavaClassNode) getDependentClass();
    }
    /**
     *
     * @return The SwiftClassNode of the dependency.
     */
    public SwiftClassNode getSwiftClassNodeMaterial()
    {
        return (SwiftClassNode) getIndependentClass();
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
        return "JavaClassNode '" + getJavaClassNode().getSimpleClassName() +"' has the Trace-Link-Value of " + tracelinkvalue+ " '" + "to the SwiftClassNode "+ getSwiftClassNodeMaterial().getSimpleClassName()+ "'.";
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = HashUtils.calcHashCode(hash, getJavaClassNode());
        hash = HashUtils.calcHashCode(hash, getSwiftClassNodeMaterial());
        hash = HashUtils.calcHashCode(hash, tracelinkvalue);
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
        final TraceLinkDependency traceLinkDependency = (TraceLinkDependency) obj;
        return  this.getJavaClassNode().equals(traceLinkDependency.getJavaClassNode()) &&
                this.getSwiftClassNodeMaterial().equals(traceLinkDependency.getSwiftClassNodeMaterial()) &&
                this.tracelinkvalue == traceLinkDependency.getTracelinkValue();
    }
}
