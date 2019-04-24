package service.technical;

import materials.ProgramEntityRelationship;
import org.junit.Test;
import valueobjects.Language;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class DotFileParserTest
{

    public final String _filepath = "/Users/nilsberger/Documents/Masterarbeit/clean2/DiagramPlugin/src/test/resources/android_graph.dot";


    @Test
    public void paserJavaDepenendicesFormDotFileTest()
    {

        Set<ProgramEntityRelationship> dependencies = DotFileParser.parseJavaDependenciesFromDotFile(_filepath);

        assertThat(dependencies.size(), greaterThan(0));
        //assertThat(dependencies.size(), is(13));

        for (ProgramEntityRelationship dependency : dependencies)
        {
            assertThat(dependency.getDependentClass().getLanguage(), is(Language.Java));
            assertThat(dependency.getIndependentClass().getLanguage(), is(Language.Java));
        }
    }

    @Test
    public void paserSwiftDepenendicesFormDotFileTest()
    {

        List<ProgramEntityRelationship> dependencies = DotFileParser.parseSwiftDependenciesFromDotFile(_filepath);

        assertThat(dependencies.size(), greaterThan(0));
        //assertThat(dependencies.size(), is(26));

        for (ProgramEntityRelationship dependency : dependencies)
        {
            assertThat(dependency.getDependentClass().getLanguage(), is(Language.Swift));
            assertThat(dependency.getIndependentClass().getLanguage(), is(Language.Swift));
        }
    }
}
