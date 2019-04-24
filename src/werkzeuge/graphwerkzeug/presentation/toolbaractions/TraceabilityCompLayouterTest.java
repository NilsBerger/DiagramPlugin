package werkzeuge.graphwerkzeug.presentation.toolbaractions;

import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import materials.TraceLinkProgramEntityAssociation;
import org.junit.Test;
import valueobjects.Language;
import valueobjects.RelationshipType;
import werkzeuge.graphwerkzeug.presentation.TraceabilityCompLayouter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TraceabilityCompLayouterTest {


    @Test
    public void getOrderNodeListTest() {
        List<ProgramEntityRelationship> sortedList = new ArrayList<>();
        ProgramEntity javaProgramEntityMaterialA = new ProgramEntity("a", Language.Java);
        ProgramEntity swiftProgramEntityMaterialA = new ProgramEntity("a", Language.Swift);
        ProgramEntity javaProgramEntityMaterialB1 = new ProgramEntity("b", Language.Java);
        ProgramEntity swiftProgramEntityMaterialB1 = new ProgramEntity("b", Language.Swift);
        ProgramEntity javaProgramEntityMaterialB2 = new ProgramEntity("b", Language.Java);
        ProgramEntity swiftProgramEntityMaterialB2 = new ProgramEntity("b", Language.Swift);
        ProgramEntity javaProgramEntityMaterialC = new ProgramEntity("c", Language.Java);
        ProgramEntity swiftProgramEntityMaterialC = new ProgramEntity("c", Language.Swift);
        ProgramEntity javaProgramEntityMaterialZ = new ProgramEntity("z", Language.Java);

        ProgramEntityRelationship programEntityRelationship1 = new ProgramEntityRelationship(javaProgramEntityMaterialA, swiftProgramEntityMaterialA, RelationshipType.Dependency);
        ProgramEntityRelationship programEntityRelationship2 = new ProgramEntityRelationship(javaProgramEntityMaterialC, swiftProgramEntityMaterialC, RelationshipType.Dependency);

        TraceLinkProgramEntityAssociation traceLinkProgramEntityAssociation1 = new TraceLinkProgramEntityAssociation(javaProgramEntityMaterialB1, swiftProgramEntityMaterialB1, 1.0);
        TraceLinkProgramEntityAssociation traceLinkProgramEntityAssociation2 = new TraceLinkProgramEntityAssociation(javaProgramEntityMaterialB1, swiftProgramEntityMaterialA, 1.0);
        TraceLinkProgramEntityAssociation traceLinkProgramEntityAssociation3 = new TraceLinkProgramEntityAssociation(javaProgramEntityMaterialB2, swiftProgramEntityMaterialC, 1.0);
        TraceLinkProgramEntityAssociation traceLinkProgramEntityAssociation4 = new TraceLinkProgramEntityAssociation(javaProgramEntityMaterialA, swiftProgramEntityMaterialB2, 1.0);
        TraceLinkProgramEntityAssociation traceLinkProgramEntityAssociation5 = new TraceLinkProgramEntityAssociation(javaProgramEntityMaterialC, swiftProgramEntityMaterialB2, 1.0);
        TraceLinkProgramEntityAssociation traceLinkProgramEntityAssociation6 = new TraceLinkProgramEntityAssociation(javaProgramEntityMaterialZ, swiftProgramEntityMaterialB2, 1.0);

        ProgramEntityRelationship[] dependencies = {traceLinkProgramEntityAssociation6, programEntityRelationship1, programEntityRelationship2, traceLinkProgramEntityAssociation1, traceLinkProgramEntityAssociation2, traceLinkProgramEntityAssociation3, traceLinkProgramEntityAssociation4, traceLinkProgramEntityAssociation5};
        final List<ProgramEntityRelationship> dependencyList = Arrays.asList(dependencies);

        //final LinkedHashSet<ClassNode> orderedClassNodesJava = TraceabilityCompLayouter.getOrderedClassNodes(dependencyList, ClassLanguageType.Java);
        //final LinkedHashSet<ClassNode> orderedClassNodesSwift = TraceabilityCompLayouter.getOrderedClassNodes(dependencyList, ClassLanguageType.Swift);
    }

    @Test
    public void getVectorTest() {
        //XDestination: 200, NodePostion 100 --> Vector 100
        assertThat(100.0, is(TraceabilityCompLayouter.getVector(200.0, 100.0)));

        //XDestination: 200, NodePostion -100 --> Vector 300
        assertThat(300.0, is(TraceabilityCompLayouter.getVector(200.0, -100.0)));

        //XDestination: 200, NodePostion 0 --> Vector 200
        assertThat(200.0, is(TraceabilityCompLayouter.getVector(200.0, 0)));

        //XDestination: 200, NodePostion -200 --> Vector 0
        assertThat(0.0, is(TraceabilityCompLayouter.getVector(200.0, 200.0)));


        //_______

        //XDestination: 100, NodePostion 200 --> Vector -100
        assertThat(-100.0, is(TraceabilityCompLayouter.getVector(100.0, 200.0)));

        //XDestination: 100, NodePostion 200 --> Vector -100
        assertThat(-300.0, is(TraceabilityCompLayouter.getVector(-100.0, 200.0)));

        //XDestination: 100, NodePostion 200 --> Vector -100
        assertThat(-200.0, is(TraceabilityCompLayouter.getVector(0.0, 200.0)));

        //XDestination: 100, NodePostion 200 --> Vector -100
        assertThat(0.0, is(TraceabilityCompLayouter.getVector(200.0, 200.0)));
    }


}
