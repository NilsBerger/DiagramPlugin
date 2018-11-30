package service;

import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityPointer;
import de.unihamburg.masterprojekt2016.traceability.TypePointer;
import de.unihamburg.masterprojekt2016.traceability.TypePointerClassification;
import materials.*;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import valueobjects.ClassLanguageType;
import valueobjects.ClassNodeType;
import valueobjects.Marking;
import valueobjects.RelationshipType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.HashSet;
import java.util.Set;

public class ChangePropagationProcessTest {

    @Test
    public void markingChange()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSimpleDependencyFromPaper(), new RandomChangeAndFixStrategy());
        propgatatesProcess.change(new ClassNode("C", ClassLanguageType.Java));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Search", ClassLanguageType.Java)));

        // "Search" --> Marking "Change"
        propgatatesProcess.update(new ClassNode("Search", ClassLanguageType.Java), Marking.CHANGED);
        affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasSize(5));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassLanguageType.Java)));

        assertThat(classesMarkedByNext, hasSize(3));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Main", ClassLanguageType.Java)));

    }


    @Test
    public void markingPropagates()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSimpleDependencyFromPaper(), new RandomChangeAndFixStrategy());
        propgatatesProcess.change(new ClassNode("C", ClassLanguageType.Java));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Search", ClassLanguageType.Java)));

        // "Search" --> Marking "PROPAGATES"
        propgatatesProcess.update(new ClassNode("Search", ClassLanguageType.Java), Marking.PROPAGATES);
        affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasSize(5));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassLanguageType.Java)));

        assertThat(classesMarkedByNext, hasSize(3));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Main", ClassLanguageType.Java)));
    }

    @Test
    public void markingNext()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSmallGraph(), new RandomChangeAndFixStrategy());
        propgatatesProcess.change(new ClassNode("a", ClassLanguageType.Java));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ClassNode("a", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("b", ClassLanguageType.Java)));


        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("b", ClassLanguageType.Java)));

        // Change if "b" has Marking "NEXT"
        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("b", ClassLanguageType.Java)));

        ClassNode b = classesMarkedByNext.stream().filter(item -> item.getSimpleName().equals("b")).findFirst().orElse(null);
        assertThat(b.getMarking(), is(Marking.NEXT));

        ClassNode c = classesMarkedByNext.stream().filter(item -> item.getSimpleName().equals("c")).findFirst().orElse(null);
        assertThat(c, IsNull.nullValue());

    }


    @Test
    public void markingInspected()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSmallGraph(), new RandomChangeAndFixStrategy());

        propgatatesProcess.change(new ClassNode("a", ClassLanguageType.Java));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ClassNode("a", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("b", ClassLanguageType.Java)));


        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("b", ClassLanguageType.Java)));

        // Change if "b" has Marking "NEXT"
        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("b", ClassLanguageType.Java)));

        ClassNode b = classesMarkedByNext.stream().filter(item -> item.getSimpleName().equals("b")).findFirst().orElse(null);
        assertThat(b.getMarking(), is(Marking.NEXT));
        b.setMarking(Marking.INSPECTED);

        ClassNode c = classesMarkedByNext.stream().filter(item -> item.getSimpleName().equals("c")).findFirst().orElse(null);
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
        propagationProcess.change(new ClassNode("C", ClassLanguageType.Java));

        Set<ClassNode> affectedClasses = propagationProcess.getAffectedClassesByChange();
        Set<ClassNode> markedClasses = propagationProcess.getNextMarkedClasses();


        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasSize(2));
        assertThat(markedClasses,hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(markedClasses, hasSize(1));
        Set<ClassDependency> inconsistencies = propagationProcess.getModel().getInconsistencies();
        assertThat(inconsistencies, hasSize(1));

        //Selected by programmer
        ClassNode selectedSearchClass = propagationProcess.select(new ClassNode("Search", ClassLanguageType.Java));
        propagationProcess.update(selectedSearchClass,Marking.CHANGED);

        //Next change propagation
        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasSize(5));
        assertThat(markedClasses, hasItem(new ClassNode("Main", ClassLanguageType.Java)));
        assertThat(markedClasses, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(markedClasses, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(markedClasses, hasSize(3));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Input" --> Inspected

        selectedSearchClass = propagationProcess.select(new ClassNode("Input", ClassLanguageType.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses,hasItem(new ClassNode("Main", ClassLanguageType.Java)));
        assertThat(markedClasses,hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(markedClasses,hasSize(2));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Main" --> Inspected
        selectedSearchClass = propagationProcess.select(new ClassNode("Main", ClassLanguageType.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses,hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Data" --> Changed
        selectedSearchClass = propagationProcess.select(new ClassNode("Data", ClassLanguageType.Java));
        propagationProcess.update(selectedSearchClass, Marking.CHANGED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses,hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Main", ClassLanguageType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("C", ClassLanguageType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Init", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasSize(6));

        assertThat(markedClasses,hasItem(new ClassNode("Init", ClassLanguageType.Java)));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Init" --> Changed
        selectedSearchClass = propagationProcess.select(new ClassNode("Init", ClassLanguageType.Java));
        propagationProcess.update(selectedSearchClass, Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses,hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Main", ClassLanguageType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("C", ClassLanguageType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Init", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasSize(6));

        //assertThat(markedClasses, hasItem(new ClassNodeMaterial("Main")));
        assertThat(markedClasses, hasSize(0));
    }


    @Test
    public void basicCPProcessWithTraceLinks() {
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getJavaAndSwiftDependencyList(), new RandomChangeAndFixStrategy());
        propagationProcess.change(new ClassNode("C", ClassLanguageType.Java));

        Set<ClassNode> affectedClasses = propagationProcess.getAffectedClassesByChange();
        Set<ClassNode> markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasSize(2));
        assertThat(markedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(markedClasses, hasSize(1));

        //Selected by programmer
        ClassNode selectedSearchClass = propagationProcess.select(new ClassNode("Search", ClassLanguageType.Java));
        propagationProcess.update(selectedSearchClass, Marking.PROPAGATES);

        //Next change propagation
        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasSize(5));
        assertThat(markedClasses, hasItem(new ClassNode("Main", ClassLanguageType.Java)));
        assertThat(markedClasses, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(markedClasses, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(markedClasses, hasSize(3));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Input" --> Inspected

        selectedSearchClass = propagationProcess.select(new ClassNode("Input", ClassLanguageType.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses, hasItem(new ClassNode("Main", ClassLanguageType.Java)));
        assertThat(markedClasses, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(markedClasses, hasSize(2));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Main" --> Inspected
        selectedSearchClass = propagationProcess.select(new ClassNode("Main", ClassLanguageType.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Data" --> Changed
        selectedSearchClass = propagationProcess.select(new ClassNode("Data", ClassLanguageType.Java));
        propagationProcess.update(selectedSearchClass, Marking.CHANGED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasSize(6));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Init", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasSize(6));

        assertThat(markedClasses, hasItem(new ClassNode("Init", ClassLanguageType.Java)));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Init" --> Changed
        selectedSearchClass = propagationProcess.select(new ClassNode("Init", ClassLanguageType.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Init", ClassLanguageType.Java)));
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
        propgatatesProcess.change(new ClassNode("C", ClassLanguageType.Java));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Search", ClassLanguageType.Java)));

        // "Search" --> Marking "Change"
        propgatatesProcess.update(new ClassNode("Search", ClassLanguageType.Java), Marking.CHANGED);
        affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();
        assertThat(affectedClasses, hasSize(6));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Swift)));

        assertThat(classesMarkedByNext, hasSize(4));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Data", ClassLanguageType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Main", ClassLanguageType.Java)));

        //Neighbour through Trace Link
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Search", ClassLanguageType.Swift)));
    }


    @Test
    public void getAffectedDependencies()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSimpleDependeniesFromPaperWithoutTraceLink(), new RandomChangeAndFixStrategy());
        propgatatesProcess.change(new ClassNode("C", ClassLanguageType.Java));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Search", ClassLanguageType.Java)));

        //Add TraceLink between Java-ClassNode "C" and Swift-ClassNode "C"

        ClassNode javaC = new ClassNode("C", ClassLanguageType.Java);
        ClassNode swiftC = new ClassNode("C", ClassLanguageType.Swift);
        TraceabilityPointer javaPointer = new TypePointer("C", TypePointerClassification.CLASS,0);
        TraceabilityPointer swiftPointer = new TypePointer("C",TypePointerClassification.CLASS,0);

        TraceabilityLink tracelink = new TraceabilityLink(javaPointer, swiftPointer,1d);
        propgatatesProcess.addTraceabilityLinkJavaSource(javaC,tracelink);


        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();
        assertThat(classesMarkedByNext, hasSize(2));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("C", ClassLanguageType.Swift)));

        propgatatesProcess.getAffectedDependencies(javaC);

    }
    
    @Test
    public void createTracelinkTest()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSimpleDependeniesFromPaperWithoutTraceLink(), new RandomChangeAndFixStrategy());
        propgatatesProcess.change(new ClassNode("C", ClassLanguageType.Java));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassLanguageType.Java)));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Search", ClassLanguageType.Java)));

       //Add TraceLink between Java-ClassNode "C" and Swift-ClassNode "C"

        ClassNode javaC = new ClassNode("C", ClassLanguageType.Java);
        ClassNode swiftC = new ClassNode("C", ClassLanguageType.Swift);
        TraceabilityPointer javaPointer = new TypePointer("C", de.unihamburg.masterprojekt2016.traceability.TypePointerClassification.CLASS,0);
        TraceabilityPointer swiftPointer = new TypePointer("C",TypePointerClassification.CLASS,0);

        TraceabilityLink tracelink = new TraceabilityLink(javaPointer, swiftPointer,1d);
        propgatatesProcess.addTraceabilityLinkJavaSource(javaC,tracelink);


        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();
        assertThat(classesMarkedByNext, hasSize(2));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("C", ClassLanguageType.Swift)));

        Set<ClassDependency> affectedDependencies = propgatatesProcess.getAffectedDependencies(javaC);
        //TraceLinkClassDependency dependency = new TraceLinkClassDependency();
        //assertThat(affectedClasses, ());

    }


    @Test
    public void strategyStrict()
    {
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getSimpleDependencyList(), new StrictChangeAndFixStrategy());
        assertThat(propagationProcess.change(new ClassNode("C", ClassLanguageType.Java)), is(true));
        assertThat(propagationProcess.change(new ClassNode("Main", ClassLanguageType.Java)), is(false));
    }
    @Test
    public void strategyRandom()
    {
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getSimpleDependencyList(), new RandomChangeAndFixStrategy());
        assertThat(propagationProcess.change(new ClassNode("C", ClassLanguageType.Java)), is(true));
        assertThat(propagationProcess.change(new ClassNode("Main", ClassLanguageType.Java)), is(true));
    }

    public static final  Set<ClassDependency> getSimpleDependencyList()
    {
        ClassNode main = new ClassNode("Main", ClassLanguageType.Java);
        ClassNode search = new ClassNode("Search", ClassLanguageType.Java);
        ClassNode input = new ClassNode("Input", ClassLanguageType.Java);
        ClassNode c = new ClassNode("C", ClassLanguageType.Java);
        ClassNode data = new ClassNode("Data", ClassLanguageType.Java);
        ClassNode init = new ClassNode("Init", ClassLanguageType.Java);

        Set<ClassDependency> dependencyFachwertList = new HashSet<>();
        //conistent
        dependencyFachwertList.add(new ClassDependency(input,main, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(search,main, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(init,main, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(input,search, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(c,search, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(data,search, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(data,init, RelationshipType.Dependency));

        return dependencyFachwertList;
    }


    public static final Set<ClassDependency> getSimpleDependencyFromPaperWithInconsistencies()
    {
        ClassNode main = new ClassNode("Main", ClassLanguageType.Java);
        ClassNode search = new ClassNode("Search", ClassLanguageType.Java);
        ClassNode input = new ClassNode("Input", ClassLanguageType.Java);
        ClassNode c = new ClassNode("C", ClassLanguageType.Java);
        ClassNode data = new ClassNode("Data", ClassLanguageType.Java);
        ClassNode init = new ClassNode("Init", ClassLanguageType.Java);

        Set<ClassDependency> dependencyList = new HashSet<>();
        //conistent

        dependencyList.add(new ClassDependency(main, input, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(main, search, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(main, init, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(search, input, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(search, c, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(search, data, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(init, data, RelationshipType.Dependency));

        dependencyList.add(new ClassDependency(main, input, RelationshipType.InconsistentRelationship));
        dependencyList.add(new ClassDependency(main, search, RelationshipType.InconsistentRelationship));
        dependencyList.add(new ClassDependency(main, init, RelationshipType.InconsistentRelationship));
        dependencyList.add(new ClassDependency(search, input, RelationshipType.InconsistentRelationship));
        dependencyList.add(new ClassDependency(search, c, RelationshipType.InconsistentRelationship));
        dependencyList.add(new ClassDependency(search, data, RelationshipType.InconsistentRelationship));
        dependencyList.add(new ClassDependency(init, data, RelationshipType.InconsistentRelationship));

        return dependencyList;
    }

    public static final Set<ClassDependency> getSimpleDependencyFromPaper()
    {
        ClassNode main = new ClassNode("Main", ClassLanguageType.Java);
        ClassNode search = new ClassNode("Search", ClassLanguageType.Java);
        ClassNode input = new ClassNode("Input", ClassLanguageType.Java);
        ClassNode c = new ClassNode("C", ClassLanguageType.Java);
        ClassNode data = new ClassNode("Data", ClassLanguageType.Java);
        ClassNode init = new ClassNode("Init", ClassLanguageType.Java);

        Set<ClassDependency> dependencyList = new HashSet<>();
        //conistent


        dependencyList.add(new ClassDependency(main,input, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(main, search, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(main, init, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(search, input, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(search, c, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(search, data, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(init, data, RelationshipType.Dependency));

        return dependencyList;
    }

    public static final Set<ClassDependency> getSimpleDependencyFromPaperWithTraceLink()
    {
        ClassNode main = new ClassNode("Main", ClassLanguageType.Java);
        ClassNode search = new ClassNode("Search", ClassLanguageType.Java);
        ClassNode searchswift = new ClassNode("Search", ClassLanguageType.Swift);
        ClassNode input = new ClassNode("Input", ClassLanguageType.Java);
        ClassNode c = new ClassNode("C", ClassLanguageType.Java);
        ClassNode data = new ClassNode("Data", ClassLanguageType.Java);
        ClassNode init = new ClassNode("Init", ClassLanguageType.Java);

        Set<ClassDependency> dependencyList = new HashSet<>();
        //conistent


        dependencyList.add(new ClassDependency(main,input, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(main, search, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(main, init, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(search, input, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(search, c, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(search, data, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(search, searchswift, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(init, data, RelationshipType.Dependency));

        return dependencyList;
    }

    /**
     * Java and Swift dependency graph. Both sgraphs are not connected.
     * @return
     */
    public static final Set<ClassDependency> getSimpleDependeniesFromPaperWithoutTraceLink()
    {
        ClassNode main = new ClassNode("Main", ClassLanguageType.Java);
        ClassNode search = new ClassNode("Search", ClassLanguageType.Java);
        ClassNode cSwift = new ClassNode("C", ClassLanguageType.Swift);
        ClassNode input = new ClassNode("Input", ClassLanguageType.Java);
        ClassNode c = new ClassNode("C", ClassLanguageType.Java);
        ClassNode data = new ClassNode("Data", ClassLanguageType.Java);
        ClassNode init = new ClassNode("Init", ClassLanguageType.Java);

        Set<ClassDependency> dependencyList = new HashSet<>();
        //conistent


        dependencyList.add(new ClassDependency(main,input, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(main, search, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(main, init, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(search, input, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(search, c, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(search, data, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(init, data, RelationshipType.Dependency));
        dependencyList.add(new ClassDependency(cSwift, cSwift, RelationshipType.Dependency));

        return dependencyList;
    }

    public static final Set<ClassDependency> getSmallGraph()
    {
        ClassNode a = new ClassNode("a", ClassLanguageType.Java);
        ClassNode b = new ClassNode("b", ClassLanguageType.Java);
        ClassNode c = new ClassNode("c", ClassLanguageType.Java);

        Set<ClassDependency> dependencyFachwertList = new HashSet<>();
        //conistent
        dependencyFachwertList.add(new ClassDependency(a,b, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(b,c, RelationshipType.Dependency));
        return dependencyFachwertList;
    }

    public static final Set<ClassDependency> getJavaAndSwiftDependencyList()
    {
        ClassNode javaMain = new ClassNode("java.Main", ClassLanguageType.Java);
        ClassNode javaSearch = new ClassNode("java.Search", ClassLanguageType.Java);
        ClassNode javaInput = new ClassNode("java.Input", ClassLanguageType.Java);
        ClassNode javaC = new ClassNode("java.C", ClassLanguageType.Java);
        ClassNode javaData = new ClassNode("java.Data", ClassLanguageType.Java);
        ClassNode javaInit = new ClassNode("java.Init", ClassLanguageType.Java);

        Set<ClassDependency> dependencyFachwertList = new HashSet<>();
        //conistent
        dependencyFachwertList.add(new ClassDependency(javaInput,javaMain, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(javaSearch,javaMain, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(javaInit,javaMain, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(javaInput,javaSearch, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(javaC,javaSearch, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(javaData,javaSearch, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(javaData,javaInit, RelationshipType.Dependency));

        ClassNode swiftMain = new ClassNode("swift.Main", ClassLanguageType.Swift);
        ClassNode swiftSearch = new ClassNode("swift.Search", ClassLanguageType.Swift);
        ClassNode swiftInput = new ClassNode("swift.Input", ClassLanguageType.Swift);
        ClassNode swiftC = new ClassNode("swift.C", ClassLanguageType.Swift);
        ClassNode swiftData = new ClassNode("swift.Data", ClassLanguageType.Swift);
        ClassNode swiftInit = new ClassNode("swift.Init", ClassLanguageType.Swift);

        //conistent
        dependencyFachwertList.add(new ClassDependency(swiftInput,swiftMain, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(swiftSearch,swiftMain, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(swiftInit,swiftMain, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(swiftInput,swiftSearch, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(swiftC,swiftSearch, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(swiftData,swiftSearch, RelationshipType.Dependency));
        dependencyFachwertList.add(new ClassDependency(swiftData,swiftInit, RelationshipType.Dependency));

        //TraceLinks

        //dependencyFachwertList.add(new TraceLinkFachwertClass(javaMain, swiftMain,100));

        return dependencyFachwertList;
    }
}
