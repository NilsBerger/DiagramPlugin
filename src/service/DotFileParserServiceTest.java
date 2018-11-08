package service;

import materials.DependencyIF;
import materials.JavaClassNode;
import materials.SwiftClassNode;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DotFileParserServiceTest
{

    public final String _filepath = "/Users/nilsberger/Documents/Masterarbeit/clean2/DiagramPlugin/src/test/resources/android_graph.dot";


    @Test
    public void paserJavaDepenendicesFormDotFileTest()
    {

        Set<DependencyIF> dependencies = DotFileParserService.parseJavaDependenciesFromDotFile(_filepath);

        assertThat(dependencies.size(), greaterThan(0));
        assertThat(dependencies.size(), is(13));

        for(DependencyIF dependency : dependencies)
        {
            assertThat(dependency.getDependentClass(), is(instanceOf(JavaClassNode.class)));
            assertThat(dependency.getIndependentClass(), is(instanceOf(JavaClassNode.class)));
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
            assertThat(dependency.getDependentClass(), is(instanceOf(SwiftClassNode.class)));
            assertThat(dependency.getIndependentClass(), is(instanceOf(SwiftClassNode.class)));
        }
    }
}
