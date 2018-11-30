package materials;

import org.junit.Test;
import valueobjects.ClassLanguageType;
import valueobjects.RelationshipType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


public class TraceLinkDependencyTest {

    @Test
    public void equalsAndHashCodeTest() {

        ClassDependency classDependency = new TraceLinkClassDependency(new ClassNode("a", ClassLanguageType.Java), new ClassNode("b", ClassLanguageType.Java), 100);
        ClassDependency otherclassDependency = new TraceLinkClassDependency(new ClassNode("a", ClassLanguageType.Java), new ClassNode("b", ClassLanguageType.Java), 100 );

        assertThat(classDependency, is((classDependency)));
        assertThat(classDependency, is(otherclassDependency));

        assertThat(classDependency.hashCode(), is(not(otherclassDependency.hashCode()+1)));
        assertThat(classDependency.hashCode(), is(otherclassDependency.hashCode()));
    }

    @Test
    public void getDependenciesTest() {
        ClassNode javaClassNodeMaterial = new ClassNode("a", ClassLanguageType.Java);
        ClassNode swiftClassNodeMaterial = new ClassNode("b", ClassLanguageType.Swift);
        TraceLinkClassDependency classDependency = new TraceLinkClassDependency(javaClassNodeMaterial, swiftClassNodeMaterial, 100.0);

        assertThat(javaClassNodeMaterial, is(classDependency.getDependentClass()));
        assertThat(swiftClassNodeMaterial, is(classDependency.getIndependentClass()));
        assertThat(classDependency.getTracelinkValue(), is(100.0));
    }


    @Test
    public void getChangeRelationshipTypeTest() {
        ClassNode javaClassNodeMaterial = new ClassNode("a", ClassLanguageType.Java);
        ClassNode swiftClassNodeMaterial = new ClassNode("b", ClassLanguageType.Swift);
        TraceLinkClassDependency classDependency = new TraceLinkClassDependency(javaClassNodeMaterial, swiftClassNodeMaterial, 100.0);

        assertThat(classDependency.getRelationshipType(), is(RelationshipType.Traceability_Association));

        //Change
        RelationshipType newType = RelationshipType.Dependency;
        classDependency.setRelationshipType(newType);

        assertThat(classDependency.getRelationshipType(), is(not((newType))));
    }
}
