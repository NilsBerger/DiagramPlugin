package service;

import material.DependencyIF;
import material.JavaClassNodeMaterial;
import material.SwiftClassNodeMaterial;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DotFileParserServiceTest
{

    public final String _filepath = "/Users/nilsberger/Documents/Masterarbeit/clean2/DiagramPlugin/src/test/resources/android_graph.dot";


    @Test
    public void paserJavaDepenendicesFormDotFileTest()
    {

        List<DependencyIF> dependencies = DotFileParserService.parseJavaDependenciesFromDotFile(_filepath);

        assertThat(dependencies.size(), greaterThan(0));
        assertThat(dependencies.size(), is(21));

        for(DependencyIF dependency : dependencies)
        {
            assertThat(dependency.getDependentClass(), is(instanceOf(JavaClassNodeMaterial.class)));
            assertThat(dependency.getIndependentClass(), is(instanceOf(JavaClassNodeMaterial.class)));
        }
    }

    @Test
    public void paserSwiftDepenendicesFormDotFileTest()
    {

        List<DependencyIF> dependencies = DotFileParserService.parseSwiftDependenciesFromDotFile(_filepath);

        assertThat(dependencies.size(), greaterThan(0));
        assertThat(dependencies.size(), is(21));

        for(DependencyIF dependency : dependencies)
        {
            assertThat(dependency.getDependentClass(), is(instanceOf(SwiftClassNodeMaterial.class)));
            assertThat(dependency.getIndependentClass(), is(instanceOf(SwiftClassNodeMaterial.class)));
        }
    }
}