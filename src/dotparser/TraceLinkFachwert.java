package dotparser;

public class TraceLinkFachwert extends DependencyFachwert{

    private double tracelinkvalue;
    public TraceLinkFachwert(ClassNodeMaterial dependentClass, ClassNodeMaterial independentClass, double tracelinkvalue) {
        super(dependentClass, independentClass);
        this.tracelinkvalue = tracelinkvalue;
    }
    public double getTracelinkvalue() {
        return tracelinkvalue;
    }


    public void setTracelinkvalue(double tracelinkvalue) {
        this.tracelinkvalue = tracelinkvalue;
    }

}
