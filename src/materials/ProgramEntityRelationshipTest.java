package materials;

import org.junit.Test;
import valueobjects.Language;
import valueobjects.RelationshipType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ProgramEntityRelationshipTest {

    @Test
    public void equalsAndHashCodeTest() {

        ProgramEntityRelationship programEntityRelationship = new ProgramEntityRelationship(new ProgramEntity("a", Language.Java), new ProgramEntity("b", Language.Java), RelationshipType.Dependency);
        ProgramEntityRelationship otherclassDependency = new ProgramEntityRelationship(new ProgramEntity("a", Language.Java), new ProgramEntity("b", Language.Java), RelationshipType.Dependency);

        assertThat(programEntityRelationship, is((programEntityRelationship)));
        assertThat(programEntityRelationship, is(otherclassDependency));

        assertThat(programEntityRelationship.hashCode(), is(not(otherclassDependency.hashCode() + 1)));
        assertThat(programEntityRelationship.hashCode(), is(otherclassDependency.hashCode()));
    }

    @Test
    public void getDependenciesTest() {
        ProgramEntity javaProgramEntityMaterial = new ProgramEntity("a", Language.Java);
        ProgramEntity swiftProgramEntityMaterial = new ProgramEntity("b", Language.Swift);
        ProgramEntityRelationship programEntityRelationship = new ProgramEntityRelationship(javaProgramEntityMaterial, swiftProgramEntityMaterial, RelationshipType.Dependency);

        assertThat(javaProgramEntityMaterial, is(programEntityRelationship.getDependentClass()));
        assertThat(swiftProgramEntityMaterial, is(programEntityRelationship.getIndependentClass()));
    }

    /**
     * Tests if TraceLinkClassDependencies are sorted to the firstPosition
     */
    @Test
    public void compareToTest1() {
        List<ProgramEntityRelationship> sortedList = new ArrayList<>();
        ProgramEntity javaProgramEntityMaterial = new ProgramEntity("a", Language.Java);
        ProgramEntity swiftProgramEntityMaterial = new ProgramEntity("b", Language.Swift);
        ProgramEntityRelationship programEntityRelationship = new ProgramEntityRelationship(javaProgramEntityMaterial, swiftProgramEntityMaterial, RelationshipType.Dependency);
        TraceLinkProgramEntityAssociation traceLinkProgramEntityAssociation = new TraceLinkProgramEntityAssociation(javaProgramEntityMaterial, swiftProgramEntityMaterial, 1.0);

        sortedList.add(programEntityRelationship);
        sortedList.add(traceLinkProgramEntityAssociation);

        Collections.sort(sortedList, ProgramEntityRelationship.COMPARATOR);
        assertThat(sortedList.get(0), is(traceLinkProgramEntityAssociation));
    }

    /**
     * Tests if TraceLinkClassDependencies are sorted alphabetically by the "getSimpleName"
     */
//    @Test
//    public void compareToTest2() {
//        List<ClassDependency> sortedList = new ArrayList<>();
//        ClassNode javaClassNodeMaterialA = new ClassNode("a", ClassLanguageType.Java);
//        ClassNode swiftClassNodeMaterialA = new ClassNode("a", ClassLanguageType.Swift);
//        ClassNode javaClassNodeMaterialB1 = new ClassNode("b", ClassLanguageType.Java);
//        ClassNode swiftClassNodeMaterialB1 = new ClassNode("b", ClassLanguageType.Swift);
//        ClassNode javaClassNodeMaterialB2 = new ClassNode("b", ClassLanguageType.Java);
//        ClassNode swiftClassNodeMaterialB2 = new ClassNode("b", ClassLanguageType.Swift);
//        ClassNode javaClassNodeMaterialC = new ClassNode("c", ClassLanguageType.Java);
//        ClassNode swiftClassNodeMaterialC = new ClassNode("c", ClassLanguageType.Swift);
//
//        ClassDependency classDependency1 = new ClassDependency(javaClassNodeMaterialA, swiftClassNodeMaterialA, RelationshipType.Dependency);
//        ClassDependency classDependency2 = new ClassDependency(javaClassNodeMaterialC, swiftClassNodeMaterialC, RelationshipType.Dependency);
//
//        TraceLinkClassDependency traceLinkClassDependency1 = new TraceLinkClassDependency(javaClassNodeMaterialB1, swiftClassNodeMaterialB1, 1.0);
//        TraceLinkClassDependency traceLinkClassDependency2 = new TraceLinkClassDependency(javaClassNodeMaterialB1, swiftClassNodeMaterialA, 1.0);
//        TraceLinkClassDependency traceLinkClassDependency3 = new TraceLinkClassDependency(javaClassNodeMaterialB2, swiftClassNodeMaterialC, 1.0);
//        TraceLinkClassDependency traceLinkClassDependency4 = new TraceLinkClassDependency(javaClassNodeMaterialA, swiftClassNodeMaterialB2, 1.0);
//        TraceLinkClassDependency traceLinkClassDependency5 = new TraceLinkClassDependency(javaClassNodeMaterialC, swiftClassNodeMaterialB2, 1.0);
//
//        ClassDependency[] dependencies = {classDependency1, classDependency2, traceLinkClassDependency1, traceLinkClassDependency2, traceLinkClassDependency3, traceLinkClassDependency4, traceLinkClassDependency5};
//        sortedList.addAll(Arrays.asList(dependencies));
//
//        Collections.sort(sortedList);
//        assertThat(sortedList.get(0), is(traceLinkClassDependency1));
    //  }
}
