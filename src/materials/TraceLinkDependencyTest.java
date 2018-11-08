package materials;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TraceLinkDependencyTest {

    @Test
    public void basicTest()
    {
        JavaClassNode javaClassNodeMaterial = new JavaClassNode("Java");
        SwiftClassNode swiftClassNodeMaterial = new SwiftClassNode("Swift");
        double traceLinkValue = 99.9;

        TraceLinkDependency traceLinkDependencyMaterial = new TraceLinkDependency(javaClassNodeMaterial,swiftClassNodeMaterial, traceLinkValue);

        assertThat(javaClassNodeMaterial, is(traceLinkDependencyMaterial.getJavaClassNode()));
        assertThat(javaClassNodeMaterial, is(traceLinkDependencyMaterial.getDependentClass()));
        assertThat(swiftClassNodeMaterial, is(traceLinkDependencyMaterial.getSwiftClassNodeMaterial()));
        assertThat(swiftClassNodeMaterial, is(traceLinkDependencyMaterial.getIndependentClass()));
        assertThat(traceLinkValue, is(traceLinkDependencyMaterial.getTracelinkValue()));
    }

    @Test
    public void setTraceLinkTest()
    {
        JavaClassNode javaClassNodeMaterial = new JavaClassNode("Java");
        SwiftClassNode swiftClassNodeMaterial = new SwiftClassNode("Swift");
        double traceLinkValue = 99.9;

        TraceLinkDependency traceLinkDependencyMaterial = new TraceLinkDependency(javaClassNodeMaterial,swiftClassNodeMaterial, traceLinkValue);

        double newTraceValue = 11.1;
        traceLinkDependencyMaterial.setTracelinkvalue(newTraceValue);
        assertThat(newTraceValue, is(traceLinkDependencyMaterial.getTracelinkValue()));
    }

    @Test
    public void equalsAndHashTest()
    {
        JavaClassNode javaClassNodeMaterial = new JavaClassNode("Java");
        SwiftClassNode swiftClassNodeMaterial = new SwiftClassNode("Swift");
        double traceLinkValue = 99.9;
        TraceLinkDependency traceLinkDependencyMaterial = new TraceLinkDependency(javaClassNodeMaterial,swiftClassNodeMaterial, traceLinkValue);

        JavaClassNode javaClassNodeMaterial1 = new JavaClassNode("Java");
        SwiftClassNode swiftClassNodeMaterial1 = new SwiftClassNode("Swift");
        double traceLinkValue1 = 99.9;
        TraceLinkDependency traceLinkDependencyMaterial1 = new TraceLinkDependency(javaClassNodeMaterial1,swiftClassNodeMaterial1, traceLinkValue1);

        assertThat(traceLinkDependencyMaterial, is(traceLinkDependencyMaterial1));


    }
}
