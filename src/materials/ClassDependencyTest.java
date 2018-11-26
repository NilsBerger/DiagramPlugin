package materials;

import org.junit.Test;
import valueobjects.ClassLanguageType;
import valueobjects.RelationshipType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ClassDependencyTest {

    @Test
    public void equalsAndHashCodeTest() {

        ClassDependency classDependency = new ClassDependency(new ClassNode("a", ClassLanguageType.Java), new ClassNode("b", ClassLanguageType.Java), RelationshipType.Dependency);
        ClassDependency otherclassDependency = new ClassDependency(new ClassNode("a", ClassLanguageType.Java), new ClassNode("b", ClassLanguageType.Java), RelationshipType.Dependency);

        assertThat(classDependency, is((classDependency)));
        assertThat(classDependency, is(otherclassDependency));

        assertThat(classDependency.hashCode(), is(not(otherclassDependency.hashCode()+1)));
        assertThat(classDependency.hashCode(), is(otherclassDependency.hashCode()));
    }

    @Test
    public void getDependenciesTest() {
        ClassNode javaClassNodeMaterial = new ClassNode("a", ClassLanguageType.Java);
        ClassNode swiftClassNodeMaterial = new ClassNode("b", ClassLanguageType.Swift);
        ClassDependency classDependency = new ClassDependency(javaClassNodeMaterial, swiftClassNodeMaterial, RelationshipType.Dependency);

        assertThat(javaClassNodeMaterial, is(classDependency.getDependentClass()));
        assertThat(swiftClassNodeMaterial, is(classDependency.get_independentClass()));

    }

    @Test
    public void getSwitchedDependenciesTest() {

        ClassNode javaClassNodeMaterial = new ClassNode("a", ClassLanguageType.Java);
        ClassNode swiftClassNodeMaterial = new ClassNode("b", ClassLanguageType.Swift);
        ClassDependency classDependency = new ClassDependency(javaClassNodeMaterial, swiftClassNodeMaterial, RelationshipType.Dependency);
        ClassDependency switchedClassDependency = new ClassDependency(swiftClassNodeMaterial, javaClassNodeMaterial, RelationshipType.Dependency);

        assertThat(classDependency, is(switchedClassDependency.switchDependencies()));
    }
}
