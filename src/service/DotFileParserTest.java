package service;

import materials.ClassDependency;
import org.junit.Test;
import valueobjects.ClassLanguageType;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DotFileParserTest
{

    public final String _filepath = "/Users/nilsberger/Documents/Masterarbeit/clean2/DiagramPlugin/src/test/resources/android_graph.dot";


    @Test
    public void paserJavaDepenendicesFormDotFileTest()
    {

        Set<ClassDependency> dependencies = DotFileParser.parseJavaDependenciesFromDotFile(_filepath);

        assertThat(dependencies.size(), greaterThan(0));
        assertThat(dependencies.size(), is(13));

        for(ClassDependency dependency : dependencies)
        {
            assertThat(dependency.getDependentClass().getType(), is(ClassLanguageType.Java));
            assertThat(dependency.get_independentClass().getType(), is(ClassLanguageType.Java));
        }
    }

    @Test
    public void paserSwiftDepenendicesFormDotFileTest()
    {

        List<ClassDependency> dependencies = DotFileParser.parseSwiftDependenciesFromDotFile(_filepath);

        assertThat(dependencies.size(), greaterThan(0));
        assertThat(dependencies.size(), is(21));

        for(ClassDependency dependency : dependencies)
        {
            assertThat(dependency.getDependentClass().getType(), is(ClassLanguageType.Swift));
            assertThat(dependency.get_independentClass().getType(), is(ClassLanguageType.Swift));
        }
    }
}
