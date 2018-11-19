package service;

import materials.*;
import org.hamcrest.core.IsNull;
import org.junit.Test;
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
        propgatatesProcess.change(new ClassNode("C", ClassNodeType.Java));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassNodeType.Java)));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Search", ClassNodeType.Java)));

        // "Search" --> Marking "Change"
        propgatatesProcess.updateNeigbbourhood(new ClassNode("Search", ClassNodeType.Java), Marking.CHANGED);
        affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasSize(5));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassNodeType.Java)));

        assertThat(classesMarkedByNext, hasSize(3));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Main", ClassNodeType.Java)));

    }


    @Test
    public void markingPropagates()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSimpleDependencyFromPaper(), new RandomChangeAndFixStrategy());
        propgatatesProcess.change(new ClassNode("C", ClassNodeType.Java));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassNodeType.Java)));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Search", ClassNodeType.Java)));

        // "Search" --> Marking "PROPAGATES"
        propgatatesProcess.updateNeigbbourhood(new ClassNode("Search", ClassNodeType.Java), Marking.PROPAGATES);
        affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasSize(5));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassNodeType.Java)));

        assertThat(classesMarkedByNext, hasSize(3));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Main", ClassNodeType.Java)));
    }

    @Test
    public void markingNext()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSmallGraph(), new RandomChangeAndFixStrategy());
        propgatatesProcess.change(new ClassNode("a", ClassNodeType.Java));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ClassNode("a", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("b", ClassNodeType.Java)));


        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("b", ClassNodeType.Java)));

        // Change if "b" has Marking "NEXT"
        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("b", ClassNodeType.Java)));

        ClassNode b = classesMarkedByNext.stream().filter(item -> item.getSimpleClassName().equals("b")).findFirst().orElse(null);
        assertThat(b.getMarking(), is(Marking.NEXT));

        ClassNode c = classesMarkedByNext.stream().filter(item -> item.getSimpleClassName().equals("c")).findFirst().orElse(null);
        assertThat(c, IsNull.nullValue());

    }


    @Test
    public void markingInspected()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSmallGraph(), new RandomChangeAndFixStrategy());

        propgatatesProcess.change(new ClassNode("a", ClassNodeType.Java));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ClassNode("a", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("b", ClassNodeType.Java)));


        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("b", ClassNodeType.Java)));

        // Change if "b" has Marking "NEXT"
        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("b", ClassNodeType.Java)));

        ClassNode b = classesMarkedByNext.stream().filter(item -> item.getSimpleClassName().equals("b")).findFirst().orElse(null);
        assertThat(b.getMarking(), is(Marking.NEXT));
        b.setMarking(Marking.INSPECTED);

        ClassNode c = classesMarkedByNext.stream().filter(item -> item.getSimpleClassName().equals("c")).findFirst().orElse(null);
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
        propagationProcess.change(new ClassNode("C", ClassNodeType.Java));

        Set<ClassNode> affectedClasses = propagationProcess.getAffectedClassesByChange();
        Set<ClassNode> markedClasses = propagationProcess.getNextMarkedClasses();


        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassNodeType.Java)));
        assertThat(affectedClasses, hasSize(2));
        assertThat(markedClasses,hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(markedClasses, hasSize(1));
        Set<ClassDependency> inconsistencies = propagationProcess.getModel().getInconsistencies();
        assertThat(inconsistencies, hasSize(1));

        //Selected by programmer
        ClassNode selectedSearchClass = propagationProcess.select(new ClassNode("Search", ClassNodeType.Java));
        propagationProcess.updateNeigbbourhood(selectedSearchClass,Marking.CHANGED);

        //Next change propagation
        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassNodeType.Java)));
        assertThat(affectedClasses, hasSize(5));
        assertThat(markedClasses, hasItem(new ClassNode("Main", ClassNodeType.Java)));
        assertThat(markedClasses, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(markedClasses, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(markedClasses, hasSize(3));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Input" --> Inspected

        selectedSearchClass = propagationProcess.select(new ClassNode("Input", ClassNodeType.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses,hasItem(new ClassNode("Main", ClassNodeType.Java)));
        assertThat(markedClasses,hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(markedClasses,hasSize(2));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Main" --> Inspected
        selectedSearchClass = propagationProcess.select(new ClassNode("Main", ClassNodeType.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses,hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Data" --> Changed
        selectedSearchClass = propagationProcess.select(new ClassNode("Data", ClassNodeType.Java));
        propagationProcess.updateNeigbbourhood(selectedSearchClass, Marking.CHANGED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses,hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Main", ClassNodeType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("C", ClassNodeType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Init", ClassNodeType.Java)));
        assertThat(affectedClasses, hasSize(6));

        assertThat(markedClasses,hasItem(new ClassNode("Init", ClassNodeType.Java)));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Init" --> Changed
        selectedSearchClass = propagationProcess.select(new ClassNode("Init", ClassNodeType.Java));
        propagationProcess.updateNeigbbourhood(selectedSearchClass, Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses,hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Main", ClassNodeType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("C", ClassNodeType.Java)));
        assertThat(affectedClasses,hasItem(new ClassNode("Init", ClassNodeType.Java)));
        assertThat(affectedClasses, hasSize(6));

        //assertThat(markedClasses, hasItem(new ClassNodeMaterial("Main")));
        assertThat(markedClasses, hasSize(0));
    }


    @Test
    public void basicCPProcessWithTraceLinks() {
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getJavaAndSwiftDependencyList(), new RandomChangeAndFixStrategy());
        propagationProcess.change(new ClassNode("C", ClassNodeType.Java));

        Set<ClassNode> affectedClasses = propagationProcess.getAffectedClassesByChange();
        Set<ClassNode> markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassNodeType.Java)));
        assertThat(affectedClasses, hasSize(2));
        assertThat(markedClasses, hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(markedClasses, hasSize(1));

        //Selected by programmer
        ClassNode selectedSearchClass = propagationProcess.select(new ClassNode("Search", ClassNodeType.Java));
        propagationProcess.updateNeigbbourhood(selectedSearchClass, Marking.PROPAGATES);

        //Next change propagation
        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassNodeType.Java)));
        assertThat(affectedClasses, hasSize(5));
        assertThat(markedClasses, hasItem(new ClassNode("Main", ClassNodeType.Java)));
        assertThat(markedClasses, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(markedClasses, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(markedClasses, hasSize(3));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Input" --> Inspected

        selectedSearchClass = propagationProcess.select(new ClassNode("Input", ClassNodeType.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses, hasItem(new ClassNode("Main", ClassNodeType.Java)));
        assertThat(markedClasses, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(markedClasses, hasSize(2));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Main" --> Inspected
        selectedSearchClass = propagationProcess.select(new ClassNode("Main", ClassNodeType.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Data" --> Changed
        selectedSearchClass = propagationProcess.select(new ClassNode("Data", ClassNodeType.Java));
        propagationProcess.updateNeigbbourhood(selectedSearchClass, Marking.CHANGED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasSize(6));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Init", ClassNodeType.Java)));
        assertThat(affectedClasses, hasSize(6));

        assertThat(markedClasses, hasItem(new ClassNode("Init", ClassNodeType.Java)));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Init" --> Changed
        selectedSearchClass = propagationProcess.select(new ClassNode("Init", ClassNodeType.Java));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Init", ClassNodeType.Java)));
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
        propgatatesProcess.change(new ClassNode("C", ClassNodeType.Java));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassNodeType.Java)));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Search", ClassNodeType.Java)));

        // "Search" --> Marking "Change"
        propgatatesProcess.updateNeigbbourhood(new ClassNode("Search", ClassNodeType.Java), Marking.CHANGED);
        affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();
        assertThat(affectedClasses, hasSize(6));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("C", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Main", ClassNodeType.Java)));
        assertThat(affectedClasses, hasItem(new ClassNode("Search", ClassNodeType.Swift)));

        assertThat(classesMarkedByNext, hasSize(4));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Data", ClassNodeType.Java)));
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Main", ClassNodeType.Java)));

        //Neighbour through Trace Link
        assertThat(classesMarkedByNext, hasItem(new ClassNode("Search", ClassNodeType.Swift)));


    }

    @Test
    public void strategyStrict()
    {
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getSimpleDependencyList(), new StrictChangeAndFixStrategy());
        assertThat(propagationProcess.change(new ClassNode("C", ClassNodeType.Java)), is(true));
        assertThat(propagationProcess.change(new ClassNode("Main", ClassNodeType.Java)), is(false));
    }
    @Test
    public void strategyRandom()
    {
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getSimpleDependencyList(), new RandomChangeAndFixStrategy());
        assertThat(propagationProcess.change(new ClassNode("C", ClassNodeType.Java)), is(true));
        assertThat(propagationProcess.change(new ClassNode("Main", ClassNodeType.Java)), is(true));
    }

    public static final  Set<ClassDependency> getSimpleDependencyList()
    {
        ClassNode main = new ClassNode("Main", ClassNodeType.Java);
        ClassNode search = new ClassNode("Search", ClassNodeType.Java);
        ClassNode input = new ClassNode("Input", ClassNodeType.Java);
        ClassNode c = new ClassNode("C", ClassNodeType.Java);
        ClassNode data = new ClassNode("Data", ClassNodeType.Java);
        ClassNode init = new ClassNode("Init", ClassNodeType.Java);

        Set<ClassDependency> dependencyFachwertList = new HashSet<>();
        //conistent
        dependencyFachwertList.add(new ClassDependency(input,main, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(search,main, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(init,main, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(input,search, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(c,search, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(data,search, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(data,init, RelationshipType.Directed_Association));

        return dependencyFachwertList;
    }


    public static final Set<ClassDependency> getSimpleDependencyFromPaperWithInconsistencies()
    {
        ClassNode main = new ClassNode("Main", ClassNodeType.Java);
        ClassNode search = new ClassNode("Search", ClassNodeType.Java);
        ClassNode input = new ClassNode("Input", ClassNodeType.Java);
        ClassNode c = new ClassNode("C", ClassNodeType.Java);
        ClassNode data = new ClassNode("Data", ClassNodeType.Java);
        ClassNode init = new ClassNode("Init", ClassNodeType.Java);

        Set<ClassDependency> dependencyList = new HashSet<>();
        //conistent

        dependencyList.add(new ClassDependency(main, input, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(main, search, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(main, init, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(search, input, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(search, c, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(search, data, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(init, data, RelationshipType.Directed_Association));

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
        ClassNode main = new ClassNode("Main", ClassNodeType.Java);
        ClassNode search = new ClassNode("Search", ClassNodeType.Java);
        ClassNode input = new ClassNode("Input", ClassNodeType.Java);
        ClassNode c = new ClassNode("C", ClassNodeType.Java);
        ClassNode data = new ClassNode("Data", ClassNodeType.Java);
        ClassNode init = new ClassNode("Init", ClassNodeType.Java);

        Set<ClassDependency> dependencyList = new HashSet<>();
        //conistent


        dependencyList.add(new ClassDependency(main,input, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(main, search, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(main, init, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(search, input, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(search, c, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(search, data, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(init, data, RelationshipType.Directed_Association));

        return dependencyList;
    }

    public static final Set<ClassDependency> getSimpleDependencyFromPaperWithTraceLink()
    {
        ClassNode main = new ClassNode("Main", ClassNodeType.Java);
        ClassNode search = new ClassNode("Search", ClassNodeType.Java);
        ClassNode searchswift = new ClassNode("Search", ClassNodeType.Swift);
        ClassNode input = new ClassNode("Input", ClassNodeType.Java);
        ClassNode c = new ClassNode("C", ClassNodeType.Java);
        ClassNode data = new ClassNode("Data", ClassNodeType.Java);
        ClassNode init = new ClassNode("Init", ClassNodeType.Java);

        Set<ClassDependency> dependencyList = new HashSet<>();
        //conistent


        dependencyList.add(new ClassDependency(main,input, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(main, search, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(main, init, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(search, input, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(search, c, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(search, data, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(search, searchswift, RelationshipType.Directed_Association));
        dependencyList.add(new ClassDependency(init, data, RelationshipType.Directed_Association));

        return dependencyList;
    }

    public static final Set<ClassDependency> getSmallGraph()
    {
        ClassNode a = new ClassNode("a", ClassNodeType.Java);
        ClassNode b = new ClassNode("b", ClassNodeType.Java);
        ClassNode c = new ClassNode("c", ClassNodeType.Java);

        Set<ClassDependency> dependencyFachwertList = new HashSet<>();
        //conistent
        dependencyFachwertList.add(new ClassDependency(a,b, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(b,c, RelationshipType.Directed_Association));
        return dependencyFachwertList;
    }

    public static final Set<ClassDependency> getJavaAndSwiftDependencyList()
    {
        ClassNode javaMain = new ClassNode("java.Main", ClassNodeType.Java);
        ClassNode javaSearch = new ClassNode("java.Search", ClassNodeType.Java);
        ClassNode javaInput = new ClassNode("java.Input", ClassNodeType.Java);
        ClassNode javaC = new ClassNode("java.C", ClassNodeType.Java);
        ClassNode javaData = new ClassNode("java.Data", ClassNodeType.Java);
        ClassNode javaInit = new ClassNode("java.Init", ClassNodeType.Java);

        Set<ClassDependency> dependencyFachwertList = new HashSet<>();
        //conistent
        dependencyFachwertList.add(new ClassDependency(javaInput,javaMain, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(javaSearch,javaMain, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(javaInit,javaMain, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(javaInput,javaSearch, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(javaC,javaSearch, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(javaData,javaSearch, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(javaData,javaInit, RelationshipType.Directed_Association));

        ClassNode swiftMain = new ClassNode("swift.Main", ClassNodeType.Swift);
        ClassNode swiftSearch = new ClassNode("swift.Search", ClassNodeType.Swift);
        ClassNode swiftInput = new ClassNode("swift.Input", ClassNodeType.Swift);
        ClassNode swiftC = new ClassNode("swift.C", ClassNodeType.Swift);
        ClassNode swiftData = new ClassNode("swift.Data", ClassNodeType.Swift);
        ClassNode swiftInit = new ClassNode("swift.Init", ClassNodeType.Swift);

        //conistent
        dependencyFachwertList.add(new ClassDependency(swiftInput,swiftMain, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(swiftSearch,swiftMain, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(swiftInit,swiftMain, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(swiftInput,swiftSearch, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(swiftC,swiftSearch, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(swiftData,swiftSearch, RelationshipType.Directed_Association));
        dependencyFachwertList.add(new ClassDependency(swiftData,swiftInit, RelationshipType.Directed_Association));

        //TraceLinks

        //dependencyFachwertList.add(new TraceLinkFachwertClass(javaMain, swiftMain,100));

        return dependencyFachwertList;
    }
}
