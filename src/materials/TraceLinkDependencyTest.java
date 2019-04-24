package materials;

import org.junit.Test;
import valueobjects.Language;
import valueobjects.RelationshipType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


public class TraceLinkDependencyTest {

    @Test
    public void equalsAndHashCodeTest() {

        ProgramEntityRelationship programEntityRelationship = new TraceLinkProgramEntityAssociation(new ProgramEntity("a", Language.Java), new ProgramEntity("b", Language.Java), 100);
        ProgramEntityRelationship otherclassDependency = new TraceLinkProgramEntityAssociation(new ProgramEntity("a", Language.Java), new ProgramEntity("b", Language.Java), 100);

        assertThat(programEntityRelationship, is((programEntityRelationship)));
        assertThat(programEntityRelationship, is(otherclassDependency));

        assertThat(programEntityRelationship.hashCode(), is(not(otherclassDependency.hashCode() + 1)));
        assertThat(programEntityRelationship.hashCode(), is(otherclassDependency.hashCode()));
    }

    @Test
    public void getDependenciesTest() {
        ProgramEntity javaProgramEntityMaterial = new ProgramEntity("a", Language.Java);
        ProgramEntity swiftProgramEntityMaterial = new ProgramEntity("b", Language.Swift);
        TraceLinkProgramEntityAssociation classDependency = new TraceLinkProgramEntityAssociation(javaProgramEntityMaterial, swiftProgramEntityMaterial, 100.0);

        assertThat(javaProgramEntityMaterial, is(classDependency.getDependentClass()));
        assertThat(swiftProgramEntityMaterial, is(classDependency.getIndependentClass()));
        assertThat(classDependency.getTracelinkValue(), is(100.0));
    }


    @Test
    public void getChangeRelationshipTypeTest() {
        ProgramEntity javaProgramEntityMaterial = new ProgramEntity("a", Language.Java);
        ProgramEntity swiftProgramEntityMaterial = new ProgramEntity("b", Language.Swift);
        TraceLinkProgramEntityAssociation classDependency = new TraceLinkProgramEntityAssociation(javaProgramEntityMaterial, swiftProgramEntityMaterial, 100.0);

        assertThat(classDependency.getRelationshipType(), is(RelationshipType.Traceability_Association));

        //Change
        RelationshipType newType = RelationshipType.Dependency;
        classDependency.setRelationshipType(newType);

        assertThat(classDependency.getRelationshipType(), is(not((newType))));
    }
}
