package materials;

import org.junit.Test;
import valueobjects.ClassNodeType;
import valueobjects.RelationshipType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ClassDependencyTest {

    @Test
    public void equalsAndHashCodeTest() {

        ClassDependency classDependency = new ClassDependency(new ClassNode("a", ClassNodeType.Java), new ClassNode("b", ClassNodeType.Java), RelationshipType.Directed_Association);
        ClassDependency otherclassDependency = new ClassDependency(new ClassNode("a",ClassNodeType.Java), new ClassNode("b", ClassNodeType.Java), RelationshipType.Directed_Association);

        assertThat(classDependency, is((classDependency)));
        assertThat(classDependency, is(otherclassDependency));

        assertThat(classDependency.hashCode(), is(not(otherclassDependency.hashCode()+1)));
        assertThat(classDependency.hashCode(), is(otherclassDependency.hashCode()));
    }

    @Test
    public void getDependenciesTest() {
        ClassNode javaClassNodeMaterial = new ClassNode("a", ClassNodeType.Java);
        ClassNode swiftClassNodeMaterial = new ClassNode("b", ClassNodeType.Swift);
        ClassDependency classDependency = new ClassDependency(javaClassNodeMaterial, swiftClassNodeMaterial, RelationshipType.Directed_Association);

        assertThat(javaClassNodeMaterial, is(classDependency.getDependentClass()));
        assertThat(swiftClassNodeMaterial, is(classDependency.get_independentClass()));

    }

    @Test
    public void getSwitchedDependenciesTest() {

        ClassNode javaClassNodeMaterial = new ClassNode("a", ClassNodeType.Java);
        ClassNode swiftClassNodeMaterial = new ClassNode("b",ClassNodeType.Swift);
        ClassDependency classDependency = new ClassDependency(javaClassNodeMaterial, swiftClassNodeMaterial, RelationshipType.Directed_Association);
        ClassDependency switchedClassDependency = new ClassDependency(swiftClassNodeMaterial, javaClassNodeMaterial, RelationshipType.Directed_Association);

        assertThat(classDependency, is(switchedClassDependency.switchDependencies()));
    }
}
