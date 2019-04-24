package service.functional;

import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityPointer;
import de.unihamburg.masterprojekt2016.traceability.TypePointer;
import de.unihamburg.masterprojekt2016.traceability.TypePointerClassification;
import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import valueobjects.Language;
import valueobjects.Marking;
import valueobjects.RelationshipType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ChangePropagationProcessTest {

    @Test
    public void test() {

        Set<ProgramEntityRelationship> verySmallGraph = new HashSet<>();

        ProgramEntity a = new ProgramEntity("a", Language.Default);
        ProgramEntity b = new ProgramEntity("b", Language.Default);
        ProgramEntity c = new ProgramEntity("c", Language.Default);

        ProgramEntityRelationship relationship1 = new ProgramEntityRelationship(a, b, RelationshipType.Dependency);
        ProgramEntityRelationship relationship2 = new ProgramEntityRelationship(a, c, RelationshipType.Dependency);
        ProgramEntityRelationship relationship3 = new ProgramEntityRelationship(a, b, RelationshipType.Dependency);

        ProgramEntityRelationship[] relationships = {relationship1, relationship2, relationship3};
        verySmallGraph.addAll(Arrays.asList(relationships));

        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(verySmallGraph, new RandomChangeAndFixStrategy());
        propgatatesProcess.changeInitial(a);

        Set<ProgramEntity> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ProgramEntity> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();
        Set<ProgramEntityRelationship> affectedDependencies = propgatatesProcess.getAffectedDependenciesByChange();

        //Tests
        assertThat(affectedClasses, hasSize(3));
        assertThat(affectedClasses, hasItem(a));
        assertThat(affectedClasses, hasItem(b));
        assertThat(affectedClasses, hasItem(c));

        assertThat(classesMarkedByNext, hasSize(2));
        assertThat(classesMarkedByNext, hasItem(b));
        assertThat(classesMarkedByNext, hasItem(c));

        //Test Relationship
        //assertThat(affectedDependencies, hasSize(3));
        assertThat(affectedDependencies, hasItem(relationship1));
        assertThat(affectedDependencies, hasItem(relationship2));
        assertThat(affectedDependencies, hasItem(relationship3));

    }

    @Test
    public void markingChange()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSimpleDependencyFromPaper(), new RandomChangeAndFixStrategy());
        propgatatesProcess.changeInitial(new ProgramEntity("C", Language.Java));

        Set<ProgramEntity> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ProgramEntity> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Search", Language.Java)));

        // "Search" --> Marking "Change"
        propgatatesProcess.update(new ProgramEntity("Search", Language.Java), Marking.CHANGED);
        affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasSize(5));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Main", Language.Java)));

        assertThat(classesMarkedByNext, hasSize(3));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Main", Language.Java)));

    }


    @Test
    public void markingPropagates()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSimpleDependencyFromPaper(), new RandomChangeAndFixStrategy());
        propgatatesProcess.changeInitial(new ProgramEntity("C", Language.Java));

        Set<ProgramEntity> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ProgramEntity> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Search", Language.Java)));

        // "Search" --> Marking "PROPAGATES"
        propgatatesProcess.update(new ProgramEntity("Search", Language.Java), Marking.PROPAGATES);
        affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasSize(5));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Main", Language.Java)));

        assertThat(classesMarkedByNext, hasSize(3));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Main", Language.Java)));
    }

    @Test
    public void markingNext()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSmallGraph(), new RandomChangeAndFixStrategy());
        propgatatesProcess.changeInitial(new ProgramEntity("a", Language.Java));

        Set<ProgramEntity> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ProgramEntity> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ProgramEntity("a", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("b", Language.Java)));


        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("b", Language.Java)));

        // Change if "b" has Marking "NEXT"
        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("b", Language.Java)));

        ProgramEntity b = classesMarkedByNext.stream().filter(item -> item.getSimpleName().equals("b")).findFirst().orElse(null);
        assertThat(b.getMarking(), is(Marking.NEXT));

        ProgramEntity c = classesMarkedByNext.stream().filter(item -> item.getSimpleName().equals("c")).findFirst().orElse(null);
        assertThat(c, IsNull.nullValue());

    }


    @Test
    public void markingInspected()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSmallGraph(), new RandomChangeAndFixStrategy());

        propgatatesProcess.changeInitial(new ProgramEntity("a", Language.Java));

        Set<ProgramEntity> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ProgramEntity> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ProgramEntity("a", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("b", Language.Java)));


        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("b", Language.Java)));

        // Change if "b" has Marking "NEXT"
        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("b", Language.Java)));

        ProgramEntity b = classesMarkedByNext.stream().filter(item -> item.getSimpleName().equals("b")).findFirst().orElse(null);
        assertThat(b.getMarking(), is(Marking.NEXT));
        b.setMarking(Marking.INSPECTED);

        ProgramEntity c = classesMarkedByNext.stream().filter(item -> item.getSimpleName().equals("c")).findFirst().orElse(null);
        assertThat(c, IsNull.nullValue());
    }


    @Test
    /**
     * Basic Model - Change-And-Fix-Process
     */
    public void basicCPProcess()
    {
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getJavaAndSwiftDependencyList(), new RandomChangeAndFixStrategy());
        propagationProcess.changeInitial(new ProgramEntity("C", Language.Java));

        Set<ProgramEntity> affectedClasses = propagationProcess.getAffectedClassesByChange();
        Set<ProgramEntity> markedClasses = propagationProcess.getNextMarkedClasses();


        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(affectedClasses, hasSize(2));
        assertThat(markedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(markedClasses, hasSize(1));
        Set<ProgramEntityRelationship> inconsistencies = propagationProcess.getModel().getInconsistencies();
        assertThat(inconsistencies, hasSize(1));

        //Selected by programmer
        ProgramEntity selectedSearchClass = propagationProcess.select(new ProgramEntity("Search", Language.Java));
        propagationProcess.update(selectedSearchClass,Marking.CHANGED);

        //Next changeInitial propagation
        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Main", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(affectedClasses, hasSize(5));
        assertThat(markedClasses, hasItem(new ProgramEntity("Main", Language.Java)));
        assertThat(markedClasses, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(markedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(markedClasses, hasSize(3));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Input" --> Inspected

        selectedSearchClass = propagationProcess.select(new ProgramEntity("Input", Language.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses, hasItem(new ProgramEntity("Main", Language.Java)));
        assertThat(markedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(markedClasses,hasSize(2));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Main" --> Inspected
        selectedSearchClass = propagationProcess.select(new ProgramEntity("Main", Language.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Data" --> Changed
        selectedSearchClass = propagationProcess.select(new ProgramEntity("Data", Language.Java));
        propagationProcess.update(selectedSearchClass, Marking.CHANGED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Main", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Init", Language.Java)));
        assertThat(affectedClasses, hasSize(6));

        assertThat(markedClasses, hasItem(new ProgramEntity("Init", Language.Java)));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Init" --> Changed
        selectedSearchClass = propagationProcess.select(new ProgramEntity("Init", Language.Java));
        propagationProcess.update(selectedSearchClass, Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Main", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Init", Language.Java)));
        assertThat(affectedClasses, hasSize(6));

        //assertThat(markedClasses, hasItem(new ClassNodeMaterial("Main")));
        assertThat(markedClasses, hasSize(0));
    }


    @Test
    public void basicCPProcessWithTraceLinks() {
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getJavaAndSwiftDependencyList(), new RandomChangeAndFixStrategy());
        propagationProcess.changeInitial(new ProgramEntity("C", Language.Java));

        Set<ProgramEntity> affectedClasses = propagationProcess.getAffectedClassesByChange();
        Set<ProgramEntity> markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(affectedClasses, hasSize(2));
        assertThat(markedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(markedClasses, hasSize(1));

        //Selected by programmer
        ProgramEntity selectedSearchClass = propagationProcess.select(new ProgramEntity("Search", Language.Java));
        propagationProcess.update(selectedSearchClass, Marking.PROPAGATES);

        //Next changeInitial propagation
        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Main", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(affectedClasses, hasSize(5));
        assertThat(markedClasses, hasItem(new ProgramEntity("Main", Language.Java)));
        assertThat(markedClasses, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(markedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(markedClasses, hasSize(3));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Input" --> Inspected

        selectedSearchClass = propagationProcess.select(new ProgramEntity("Input", Language.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses, hasItem(new ProgramEntity("Main", Language.Java)));
        assertThat(markedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(markedClasses, hasSize(2));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Main" --> Inspected
        selectedSearchClass = propagationProcess.select(new ProgramEntity("Main", Language.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Data" --> Changed
        selectedSearchClass = propagationProcess.select(new ProgramEntity("Data", Language.Java));
        propagationProcess.update(selectedSearchClass, Marking.CHANGED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasSize(6));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Main", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Init", Language.Java)));
        assertThat(affectedClasses, hasSize(6));

        assertThat(markedClasses, hasItem(new ProgramEntity("Init", Language.Java)));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Init" --> Changed
        selectedSearchClass = propagationProcess.select(new ProgramEntity("Init", Language.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Main", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Init", Language.Java)));
        assertThat(affectedClasses, hasSize(6));

        //assertThat(markedClasses, hasItem(new ClassNodeMaterial("Main")));
        assertThat(markedClasses, hasSize(0));

        //TraceLink


    }

    @Test
    public void propagationWithTraceLink()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSimpleDependencyFromPaperWithTraceLink(), new RandomChangeAndFixStrategy());
        propgatatesProcess.changeInitial(new ProgramEntity("C", Language.Java));

        Set<ProgramEntity> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ProgramEntity> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Search", Language.Java)));

        // "Search" --> Marking "Change"
        propgatatesProcess.update(new ProgramEntity("Search", Language.Java), Marking.CHANGED);
        affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();
        assertThat(affectedClasses, hasSize(6));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Main", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Swift)));

        assertThat(classesMarkedByNext, hasSize(4));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Data", Language.Java)));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Main", Language.Java)));

        //Neighbour through Trace Link
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Search", Language.Swift)));
    }


    @Test
    public void getAffectedDependencies()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSimpleDependeniesFromPaperWithoutTraceLink(), new RandomChangeAndFixStrategy());
        propgatatesProcess.changeInitial(new ProgramEntity("C", Language.Java));

        Set<ProgramEntity> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ProgramEntity> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Search", Language.Java)));

        //Add TraceLink between Java-ClassNode "C" and Swift-ClassNode "C"

        ProgramEntity javaC = new ProgramEntity("C", Language.Java);
        ProgramEntity swiftC = new ProgramEntity("C", Language.Swift);
        TraceabilityPointer javaPointer = new TypePointer("C", TypePointerClassification.CLASS,0);
        TraceabilityPointer swiftPointer = new TypePointer("C",TypePointerClassification.CLASS,0);

        TraceabilityLink tracelink = new TraceabilityLink(javaPointer, swiftPointer,1d);
        propgatatesProcess.addTraceabilityLinkJavaSource(javaC,tracelink);


        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();
        //assertThat(classesMarkedByNext, hasSize(2));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("C", Language.Swift)));

        //propgatatesProcess.getAffectedDependencies(javaC);

    }
    
    @Test
    public void createTracelinkTest()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSimpleDependeniesFromPaperWithoutTraceLink(), new RandomChangeAndFixStrategy());
        propgatatesProcess.changeInitial(new ProgramEntity("C", Language.Java));

        Set<ProgramEntity> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ProgramEntity> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(affectedClasses, hasItem(new ProgramEntity("C", Language.Java)));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Search", Language.Java)));

       //Add TraceLink between Java-ClassNode "C" and Swift-ClassNode "C"

        ProgramEntity javaC = new ProgramEntity("C", Language.Java);
        ProgramEntity swiftC = new ProgramEntity("C", Language.Swift);
        TraceabilityPointer javaPointer = new TypePointer("C", de.unihamburg.masterprojekt2016.traceability.TypePointerClassification.CLASS,0);
        TraceabilityPointer swiftPointer = new TypePointer("C",TypePointerClassification.CLASS,0);

        TraceabilityLink tracelink = new TraceabilityLink(javaPointer, swiftPointer,1d);
        propgatatesProcess.addTraceabilityLinkJavaSource(javaC,tracelink);


        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();
        assertThat(classesMarkedByNext, hasSize(2));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(classesMarkedByNext, hasItem(new ProgramEntity("C", Language.Swift)));

        //Set<ClassDependency> affectedDependencies = propgatatesProcess.getAffectedDependencies(javaC);
        //TraceLinkClassDependency dependency = new TraceLinkClassDependency();
        //assertThat(affectedClasses, ());

    }

    /**
     * Test usese two classes as the initial classes.
     */
//    @Test
//    public void twoInitalClassesTest()
//    {
//        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
//        propagationProcess.initialize(getSimpleDependencyList(), new RandomChangeAndFixStrategy());
//        assertThat(propagationProcess.changeInitial(new ClassNode("C", ClassLanguageType.Java)), is(true));
//        assertThat(propagationProcess.changeInitial(new ClassNode("Main", ClassLanguageType.Java)), is(false));
//    }

//------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void getAffectedClassesByChangeTest() {
        //GIVEN
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getSimpleDependencyList(), new StrictChangeAndFixStrategy());

        //WHEN
        propagationProcess.changeInitial(new ProgramEntity("C", Language.Java));

        //THEN
        assertThat(propagationProcess.getAffectedClassesByChange().size(), greaterThanOrEqualTo(2));

        assertThat(propagationProcess.getAffectedClassesByChange(), hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(propagationProcess.getAffectedClassesByChange(), hasItem(new ProgramEntity("Search", Language.Java)));
    }

    @Test
    public void getAffectedDependenciesByChangeTest() {
        //GIVEN
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getSimpleDependencyList(), new StrictChangeAndFixStrategy());

        //WHEN
        propagationProcess.changeInitial(new ProgramEntity("C", Language.Java));

        //THEN
        assertThat(propagationProcess.getAffectedDependenciesByChange().size(), greaterThanOrEqualTo(1));

        ProgramEntityRelationship dependency = new ProgramEntityRelationship(new ProgramEntity("C", Language.Java), new ProgramEntity("Search", Language.Java), RelationshipType.Dependency);
        assertThat(propagationProcess.getAffectedDependenciesByChange(), hasItem(dependency));
    }


    @Test
    public void getAffectedNodeEdgesTest() {
        //GIVEN
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getSimpleDependencyList(), new StrictChangeAndFixStrategy());

        //WHEN
        propagationProcess.changeInitial(new ProgramEntity("C", Language.Java));

        //THEN
        assertThat(propagationProcess.getAffectedNodeEdges().size(), greaterThanOrEqualTo(2));

        ProgramEntityRelationship dependency = new ProgramEntityRelationship(new ProgramEntity("C", Language.Java), new ProgramEntity("Search", Language.Java), RelationshipType.Dependency);
        assertThat(propagationProcess.getAffectedNodeEdges(), hasKey(new ProgramEntity("C", Language.Java)));
        assertThat(propagationProcess.getAffectedNodeEdges(), hasKey(new ProgramEntity("Search", Language.Java)));
    }


//    @Test
//    public void isClassDependencyAffectedTest()
//    {
//        //GIVEN
//        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
//        propagationProcess.initialize(getSimpleDependencyList(), new StrictChangeAndFixStrategy());
//
//        //WHEN
//        ClassNode c = new ClassNode("C", ClassLanguageType.Java);
//        c.setMarking(Marking.CHANGED);
//        ClassNode search = new ClassNode("Search", ClassLanguageType.Java);
//        search.setMarking(Marking.PROPAGATES);
//        ClassNode inspect = new ClassNode("Inspect", ClassLanguageType.Java);
//        inspect.setMarking(Marking.BLANK);
//
//        ClassDependency affectedClassDependency = new ClassDependency(c,search, RelationshipType.Dependency);
//        ClassDependency notAffectedClassDependency = new ClassDependency(c, inspect, RelationshipType.Dependency);
//
//        //THEN
//        assertThat(propagationProcess.isClassDependencyAffected(affectedClassDependency), is(true));
//        assertThat(propagationProcess.isClassDependencyAffected(notAffectedClassDependency), is(false));
//    }


    //------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void strategyStrict() {
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getSimpleDependencyList(), new StrictChangeAndFixStrategy());
        assertThat(propagationProcess.changeInitial(new ProgramEntity("C", Language.Java)), is(true));
        assertThat(propagationProcess.changeInitial(new ProgramEntity("Main", Language.Java)), is(false));
    }
    @Test
    public void strategyRandom()
    {
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getSimpleDependencyList(), new RandomChangeAndFixStrategy());
        propagationProcess.changeInitial(new ProgramEntity("C", Language.Java));

        assertThat(propagationProcess.getAffectedClassesByChange().size(), is(2));

        propagationProcess.changeInitial(new ProgramEntity("Main", Language.Java));

        assertThat(propagationProcess.getAffectedClassesByChange().size(), is(5));
    }

    public static final Set<ProgramEntityRelationship> getSimpleDependencyList()
    {
        ProgramEntity main = new ProgramEntity("Main", Language.Java);
        ProgramEntity search = new ProgramEntity("Search", Language.Java);
        ProgramEntity input = new ProgramEntity("Input", Language.Java);
        ProgramEntity c = new ProgramEntity("C", Language.Java);
        ProgramEntity data = new ProgramEntity("Data", Language.Java);
        ProgramEntity init = new ProgramEntity("Init", Language.Java);

        Set<ProgramEntityRelationship> dependencyFachwertList = new HashSet<>();
        //conistent
        dependencyFachwertList.add(new ProgramEntityRelationship(input, main, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(search, main, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(init, main, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(input, search, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(c, search, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(data, search, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(data, init, RelationshipType.Dependency));

        return dependencyFachwertList;
    }


    public static final Set<ProgramEntityRelationship> getSimpleDependencyFromPaperWithInconsistencies()
    {
        ProgramEntity main = new ProgramEntity("Main", Language.Java);
        ProgramEntity search = new ProgramEntity("Search", Language.Java);
        ProgramEntity input = new ProgramEntity("Input", Language.Java);
        ProgramEntity c = new ProgramEntity("C", Language.Java);
        ProgramEntity data = new ProgramEntity("Data", Language.Java);
        ProgramEntity init = new ProgramEntity("Init", Language.Java);

        Set<ProgramEntityRelationship> dependencyList = new HashSet<>();
        //conistent

        dependencyList.add(new ProgramEntityRelationship(main, input, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(main, search, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(main, init, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(search, input, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(search, c, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(search, data, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(init, data, RelationshipType.Dependency));

        dependencyList.add(new ProgramEntityRelationship(main, input, RelationshipType.InconsistentRelationship));
        dependencyList.add(new ProgramEntityRelationship(main, search, RelationshipType.InconsistentRelationship));
        dependencyList.add(new ProgramEntityRelationship(main, init, RelationshipType.InconsistentRelationship));
        dependencyList.add(new ProgramEntityRelationship(search, input, RelationshipType.InconsistentRelationship));
        dependencyList.add(new ProgramEntityRelationship(search, c, RelationshipType.InconsistentRelationship));
        dependencyList.add(new ProgramEntityRelationship(search, data, RelationshipType.InconsistentRelationship));
        dependencyList.add(new ProgramEntityRelationship(init, data, RelationshipType.InconsistentRelationship));

        return dependencyList;
    }

    public static final Set<ProgramEntityRelationship> getSimpleDependencyFromPaper()
    {
        ProgramEntity main = new ProgramEntity("Main", Language.Java);
        ProgramEntity search = new ProgramEntity("Search", Language.Java);
        ProgramEntity input = new ProgramEntity("Input", Language.Java);
        ProgramEntity c = new ProgramEntity("C", Language.Java);
        ProgramEntity data = new ProgramEntity("Data", Language.Java);
        ProgramEntity init = new ProgramEntity("Init", Language.Java);

        Set<ProgramEntityRelationship> dependencyList = new HashSet<>();
        //conistent


        dependencyList.add(new ProgramEntityRelationship(main, input, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(main, search, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(main, init, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(search, input, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(search, c, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(search, data, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(init, data, RelationshipType.Dependency));

        return dependencyList;
    }

    public static final Set<ProgramEntityRelationship> getSimpleDependencyFromPaperWithTraceLink()
    {
        ProgramEntity main = new ProgramEntity("Main", Language.Java);
        ProgramEntity search = new ProgramEntity("Search", Language.Java);
        ProgramEntity searchswift = new ProgramEntity("Search", Language.Swift);
        ProgramEntity input = new ProgramEntity("Input", Language.Java);
        ProgramEntity c = new ProgramEntity("C", Language.Java);
        ProgramEntity data = new ProgramEntity("Data", Language.Java);
        ProgramEntity init = new ProgramEntity("Init", Language.Java);

        Set<ProgramEntityRelationship> dependencyList = new HashSet<>();
        //conistent


        dependencyList.add(new ProgramEntityRelationship(main, input, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(main, search, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(main, init, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(search, input, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(search, c, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(search, data, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(search, searchswift, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(init, data, RelationshipType.Dependency));

        return dependencyList;
    }

    /**
     * Java and Swift dependency graph. Both sgraphs are not connected.
     * @return
     */
    public static final Set<ProgramEntityRelationship> getSimpleDependeniesFromPaperWithoutTraceLink()
    {
        ProgramEntity main = new ProgramEntity("Main", Language.Java);
        ProgramEntity search = new ProgramEntity("Search", Language.Java);
        ProgramEntity cSwift = new ProgramEntity("C", Language.Swift);
        ProgramEntity input = new ProgramEntity("Input", Language.Java);
        ProgramEntity c = new ProgramEntity("C", Language.Java);
        ProgramEntity data = new ProgramEntity("Data", Language.Java);
        ProgramEntity init = new ProgramEntity("Init", Language.Java);

        Set<ProgramEntityRelationship> dependencyList = new HashSet<>();
        //conistent


        dependencyList.add(new ProgramEntityRelationship(main, input, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(main, search, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(main, init, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(search, input, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(search, c, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(search, data, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(init, data, RelationshipType.Dependency));
        dependencyList.add(new ProgramEntityRelationship(cSwift, cSwift, RelationshipType.Dependency));

        return dependencyList;
    }


    public static final Set<ProgramEntityRelationship> getSmallGraph()
    {
        ProgramEntity a = new ProgramEntity("a", Language.Java);
        ProgramEntity b = new ProgramEntity("b", Language.Java);
        ProgramEntity c = new ProgramEntity("c", Language.Java);

        Set<ProgramEntityRelationship> dependencyFachwertList = new HashSet<>();
        //conistent
        dependencyFachwertList.add(new ProgramEntityRelationship(a, b, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(b, c, RelationshipType.Dependency));
        return dependencyFachwertList;
    }

    public static final Set<ProgramEntityRelationship> getJavaAndSwiftDependencyList()
    {
        ProgramEntity javaMain = new ProgramEntity("java.Main", Language.Java);
        ProgramEntity javaSearch = new ProgramEntity("java.Search", Language.Java);
        ProgramEntity javaInput = new ProgramEntity("java.Input", Language.Java);
        ProgramEntity javaC = new ProgramEntity("java.C", Language.Java);
        ProgramEntity javaData = new ProgramEntity("java.Data", Language.Java);
        ProgramEntity javaInit = new ProgramEntity("java.Init", Language.Java);

        Set<ProgramEntityRelationship> dependencyFachwertList = new HashSet<>();
        //conistent
        dependencyFachwertList.add(new ProgramEntityRelationship(javaInput, javaMain, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(javaSearch, javaMain, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(javaInit, javaMain, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(javaInput, javaSearch, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(javaC, javaSearch, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(javaData, javaSearch, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(javaData, javaInit, RelationshipType.Dependency));

        ProgramEntity swiftMain = new ProgramEntity("swift.Main", Language.Swift);
        ProgramEntity swiftSearch = new ProgramEntity("swift.Search", Language.Swift);
        ProgramEntity swiftInput = new ProgramEntity("swift.Input", Language.Swift);
        ProgramEntity swiftC = new ProgramEntity("swift.C", Language.Swift);
        ProgramEntity swiftData = new ProgramEntity("swift.Data", Language.Swift);
        ProgramEntity swiftInit = new ProgramEntity("swift.Init", Language.Swift);

        //conistent
        dependencyFachwertList.add(new ProgramEntityRelationship(swiftInput, swiftMain, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(swiftSearch, swiftMain, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(swiftInit, swiftMain, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(swiftInput, swiftSearch, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(swiftC, swiftSearch, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(swiftData, swiftSearch, RelationshipType.Dependency));
        dependencyFachwertList.add(new ProgramEntityRelationship(swiftData, swiftInit, RelationshipType.Dependency));

        //TraceLinks

        //dependencyFachwertList.add(new TraceLinkFachwertClass(javaMain, swiftMain,100));

        return dependencyFachwertList;
    }
}
