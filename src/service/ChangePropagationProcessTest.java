package service;

import materials.*;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import valueobjects.Marking;

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
        propgatatesProcess.change(new JavaClassNode("C"));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Search")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("C")));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("Search")));

        // "Search" --> Marking "Change"
        propgatatesProcess.updateNeigbbourhood(new JavaClassNode("Search"), Marking.CHANGED);
        affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasSize(5));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Search")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("C")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Input")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Data")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Main")));

        assertThat(classesMarkedByNext, hasSize(3));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("Input")));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("Data")));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("Main")));

    }


    @Test
    public void markingPropagates()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSimpleDependencyFromPaper(), new RandomChangeAndFixStrategy());
        propgatatesProcess.change(new JavaClassNode("C"));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Search")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("C")));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("Search")));

        // "Search" --> Marking "PROPAGATES"
        propgatatesProcess.updateNeigbbourhood(new JavaClassNode("Search"), Marking.PROPAGATES);
        affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasSize(5));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Search")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("C")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Input")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Data")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Main")));

        assertThat(classesMarkedByNext, hasSize(3));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("Input")));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("Data")));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("Main")));
    }

    @Test
    public void markingNext()
    {
        ChangePropagationProcess propgatatesProcess = ChangePropagationProcess.getInstance();
        propgatatesProcess.initialize(getSmallGraph(), new RandomChangeAndFixStrategy());
        propgatatesProcess.change(new JavaClassNode("a"));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new JavaClassNode("a")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("b")));


        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("b")));

        // Change if "b" has Marking "NEXT"
        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("b")));

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

        propgatatesProcess.change(new JavaClassNode("a"));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new JavaClassNode("a")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("b")));


        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("b")));

        // Change if "b" has Marking "NEXT"
        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("b")));

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
        propagationProcess.change(new JavaClassNode("C"));

        Set<ClassNode> affectedClasses = propagationProcess.getAffectedClassesByChange();
        Set<ClassNode> markedClasses = propagationProcess.getNextMarkedClasses();


        assertThat(affectedClasses, hasItem(new JavaClassNode("C")));
        assertThat(affectedClasses, hasSize(2));
        assertThat(markedClasses,hasItem(new JavaClassNode("Search")));
        assertThat(markedClasses, hasSize(1));
        Set<InconsistentDependency> inconsistencies = propagationProcess.getModel().getInconsistencies();
        assertThat(inconsistencies, hasSize(1));

        //Selected by programmer
        ClassNode selectedSearchClass = propagationProcess.select(new JavaClassNode("Search"));
        propagationProcess.updateNeigbbourhood(selectedSearchClass,Marking.CHANGED);

        //Next change propagation
        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new JavaClassNode("Search")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("Main")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("Input")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("Data")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("C")));
        assertThat(affectedClasses, hasSize(5));
        assertThat(markedClasses,hasItem(new JavaClassNode("Main")));
        assertThat(markedClasses,hasItem(new JavaClassNode("Input")));
        assertThat(markedClasses,hasItem(new JavaClassNode("Data")));
        assertThat(markedClasses, hasSize(3));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Input" --> Inspected

        selectedSearchClass = propagationProcess.select(new JavaClassNode("Input"));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses,hasItem(new JavaClassNode("Main")));
        assertThat(markedClasses,hasItem(new JavaClassNode("Data")));
        assertThat(markedClasses,hasSize(2));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Main" --> Inspected
        selectedSearchClass = propagationProcess.select(new JavaClassNode("Main"));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses,hasItem(new JavaClassNode("Data")));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Data" --> Changed
        selectedSearchClass = propagationProcess.select(new JavaClassNode("Data"));
        propagationProcess.updateNeigbbourhood(selectedSearchClass, Marking.CHANGED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses,hasItem(new JavaClassNode("Search")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("Main")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("Input")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("Data")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("C")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("Init")));
        assertThat(affectedClasses, hasSize(6));

        assertThat(markedClasses,hasItem(new JavaClassNode("Init")));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Init" --> Changed
        selectedSearchClass = propagationProcess.select(new JavaClassNode("Init"));
        propagationProcess.updateNeigbbourhood(selectedSearchClass, Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses,hasItem(new JavaClassNode("Search")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("Main")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("Input")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("Data")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("C")));
        assertThat(affectedClasses,hasItem(new JavaClassNode("Init")));
        assertThat(affectedClasses, hasSize(6));

        //assertThat(markedClasses, hasItem(new ClassNodeMaterial("Main")));
        assertThat(markedClasses, hasSize(0));
    }


    @Test
    public void basicCPProcessWithTraceLinks() {
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getJavaAndSwiftDependencyList(), new RandomChangeAndFixStrategy());
        propagationProcess.change(new JavaClassNode("C"));

        Set<ClassNode> affectedClasses = propagationProcess.getAffectedClassesByChange();
        Set<ClassNode> markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new JavaClassNode("C")));
        assertThat(affectedClasses, hasSize(2));
        assertThat(markedClasses, hasItem(new JavaClassNode("Search")));
        assertThat(markedClasses, hasSize(1));

        //Selected by programmer
        ClassNode selectedSearchClass = propagationProcess.select(new JavaClassNode("Search"));
        propagationProcess.updateNeigbbourhood(selectedSearchClass, Marking.PROPAGATES);

        //Next change propagation
        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new JavaClassNode("Search")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Main")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Input")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Data")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("C")));
        assertThat(affectedClasses, hasSize(5));
        assertThat(markedClasses, hasItem(new JavaClassNode("Main")));
        assertThat(markedClasses, hasItem(new JavaClassNode("Input")));
        assertThat(markedClasses, hasItem(new JavaClassNode("Data")));
        assertThat(markedClasses, hasSize(3));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Input" --> Inspected

        selectedSearchClass = propagationProcess.select(new JavaClassNode("Input"));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses, hasItem(new JavaClassNode("Main")));
        assertThat(markedClasses, hasItem(new JavaClassNode("Data")));
        assertThat(markedClasses, hasSize(2));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Main" --> Inspected
        selectedSearchClass = propagationProcess.select(new JavaClassNode("Main"));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(markedClasses, hasItem(new JavaClassNode("Data")));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Data" --> Changed
        selectedSearchClass = propagationProcess.select(new JavaClassNode("Data"));
        propagationProcess.updateNeigbbourhood(selectedSearchClass, Marking.CHANGED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasSize(6));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Search")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Main")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Input")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Data")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("C")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Init")));
        assertThat(affectedClasses, hasSize(6));

        assertThat(markedClasses, hasItem(new JavaClassNode("Init")));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Init" --> Changed
        selectedSearchClass = propagationProcess.select(new JavaClassNode("Init"));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getNextMarkedClasses();

        assertThat(affectedClasses, hasItem(new JavaClassNode("Search")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Main")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Input")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Data")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("C")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Init")));
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
        propgatatesProcess.change(new JavaClassNode("C"));

        Set<ClassNode> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNode> classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Search")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("C")));

        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("Search")));

        // "Search" --> Marking "Change"
        propgatatesProcess.updateNeigbbourhood(new JavaClassNode("Search"), Marking.CHANGED);
        affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        classesMarkedByNext = propgatatesProcess.getNextMarkedClasses();
        assertThat(affectedClasses, hasSize(6));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Search")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("C")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Input")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Data")));
        assertThat(affectedClasses, hasItem(new JavaClassNode("Main")));
        assertThat(affectedClasses, hasItem(new SwiftClassNode("Search")));

        assertThat(classesMarkedByNext, hasSize(4));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("Input")));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("Data")));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNode("Main")));

        //Neighbour through Trace Link
        assertThat(classesMarkedByNext, hasItem(new SwiftClassNode("Search")));


    }

    @Test
    public void strategyStrict()
    {
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getSimpleDependencyList(), new StrictChangeAndFixStrategy());
        assertThat(propagationProcess.change(new JavaClassNode("C")), is(true));
        assertThat(propagationProcess.change(new JavaClassNode("Main")), is(false));
    }
    @Test
    public void strategyRandom()
    {
        ChangePropagationProcess propagationProcess = ChangePropagationProcess.getInstance();
        propagationProcess.initialize(getSimpleDependencyList(), new RandomChangeAndFixStrategy());
        assertThat(propagationProcess.change(new JavaClassNode("C")), is(true));
        assertThat(propagationProcess.change(new JavaClassNode("Main")), is(true));
    }

    public static final  Set<DependencyIF> getSimpleDependencyList()
    {
        ClassNode main = new JavaClassNode("Main");
        ClassNode search = new JavaClassNode("Search");
        ClassNode input = new JavaClassNode("Input");
        ClassNode c = new JavaClassNode("C");
        ClassNode data = new JavaClassNode("Data");
        ClassNode init = new JavaClassNode("Init");

        Set<DependencyIF> dependencyFachwertList = new HashSet<>();
        //conistent
        dependencyFachwertList.add(new ClassDependency(input,main));
        dependencyFachwertList.add(new ClassDependency(search,main));
        dependencyFachwertList.add(new ClassDependency(init,main));
        dependencyFachwertList.add(new ClassDependency(input,search));
        dependencyFachwertList.add(new ClassDependency(c,search));
        dependencyFachwertList.add(new ClassDependency(data,search));
        dependencyFachwertList.add(new ClassDependency(data,init));

        return dependencyFachwertList;
    }


    public static final Set<DependencyIF> getSimpleDependencyFromPaperWithInconsistencies()
    {
        ClassNode main = new JavaClassNode("Main");
        ClassNode search = new JavaClassNode("Search");
        ClassNode input = new JavaClassNode("Input");
        ClassNode c = new JavaClassNode("C");
        ClassNode data = new JavaClassNode("Data");
        ClassNode init = new JavaClassNode("Init");

        Set<DependencyIF> dependencyList = new HashSet<>();
        //conistent

        dependencyList.add(new ClassDependency(main, input));
        dependencyList.add(new ClassDependency(main, search));
        dependencyList.add(new ClassDependency(main, init));
        dependencyList.add(new ClassDependency(search, input));
        dependencyList.add(new ClassDependency(search, c));
        dependencyList.add(new ClassDependency(search, data));
        dependencyList.add(new ClassDependency(init, data));

        dependencyList.add(new InconsistentDependency(new ClassDependency(main, input)));
        dependencyList.add(new InconsistentDependency(new ClassDependency(main, search)));
        dependencyList.add(new InconsistentDependency(new ClassDependency(main, init)));
        dependencyList.add(new InconsistentDependency(new ClassDependency(search, input)));
        dependencyList.add(new InconsistentDependency(new ClassDependency(search, c)));
        dependencyList.add(new InconsistentDependency(new ClassDependency(search, data)));
        dependencyList.add(new InconsistentDependency(new ClassDependency(init, data)));

        return dependencyList;
    }

    public static final Set<DependencyIF> getSimpleDependencyFromPaper()
    {
        ClassNode main = new JavaClassNode("Main");
        ClassNode search = new JavaClassNode("Search");
        ClassNode input = new JavaClassNode("Input");
        ClassNode c = new JavaClassNode("C");
        ClassNode data = new JavaClassNode("Data");
        ClassNode init = new JavaClassNode("Init");

        Set<DependencyIF> dependencyList = new HashSet<>();
        //conistent


        dependencyList.add(new ClassDependency(main,input));
        dependencyList.add(new ClassDependency(main, search));
        dependencyList.add(new ClassDependency(main, init));
        dependencyList.add(new ClassDependency(search, input));
        dependencyList.add(new ClassDependency(search, c));
        dependencyList.add(new ClassDependency(search, data));
        dependencyList.add(new ClassDependency(init, data));

        return dependencyList;
    }

    public static final Set<DependencyIF> getSimpleDependencyFromPaperWithTraceLink()
    {
        ClassNode main = new JavaClassNode("Main");
        ClassNode search = new JavaClassNode("Search");
        ClassNode searchswift = new SwiftClassNode("Search");
        ClassNode input = new JavaClassNode("Input");
        ClassNode c = new JavaClassNode("C");
        ClassNode data = new JavaClassNode("Data");
        ClassNode init = new JavaClassNode("Init");

        Set<DependencyIF> dependencyList = new HashSet<>();
        //conistent


        dependencyList.add(new ClassDependency(main,input));
        dependencyList.add(new ClassDependency(main, search));
        dependencyList.add(new ClassDependency(main, init));
        dependencyList.add(new ClassDependency(search, input));
        dependencyList.add(new ClassDependency(search, c));
        dependencyList.add(new ClassDependency(search, data));
        dependencyList.add(new ClassDependency(search, searchswift));
        dependencyList.add(new ClassDependency(init, data));

        return dependencyList;
    }

    public static final Set<DependencyIF> getSmallGraph()
    {
        ClassNode a = new JavaClassNode("a");
        ClassNode b = new JavaClassNode("b");
        ClassNode c = new JavaClassNode("c");

        Set<DependencyIF> dependencyFachwertList = new HashSet<>();
        //conistent
        dependencyFachwertList.add(new ClassDependency(a,b));
        dependencyFachwertList.add(new ClassDependency(b,c));
        return dependencyFachwertList;
    }

    public static final Set<DependencyIF> getJavaAndSwiftDependencyList()
    {
        ClassNode javaMain = new JavaClassNode("java.Main");
        ClassNode javaSearch = new JavaClassNode("java.Search");
        ClassNode javaInput = new JavaClassNode("java.Input");
        ClassNode javaC = new JavaClassNode("java.C");
        ClassNode javaData = new JavaClassNode("java.Data");
        ClassNode javaInit = new JavaClassNode("java.Init");

        Set<DependencyIF> dependencyFachwertList = new HashSet<>();
        //conistent
        dependencyFachwertList.add(new ClassDependency(javaInput,javaMain));
        dependencyFachwertList.add(new ClassDependency(javaSearch,javaMain));
        dependencyFachwertList.add(new ClassDependency(javaInit,javaMain));
        dependencyFachwertList.add(new ClassDependency(javaInput,javaSearch));
        dependencyFachwertList.add(new ClassDependency(javaC,javaSearch));
        dependencyFachwertList.add(new ClassDependency(javaData,javaSearch));
        dependencyFachwertList.add(new ClassDependency(javaData,javaInit));

        ClassNode swiftMain = new SwiftClassNode("swift.Main");
        ClassNode swiftSearch = new SwiftClassNode("swift..Search");
        ClassNode swiftInput = new SwiftClassNode("swift.Input");
        ClassNode swiftC = new SwiftClassNode("swift.C");
        ClassNode swiftData = new SwiftClassNode("swift.Data");
        ClassNode swiftInit = new SwiftClassNode("swift.Init");

        //conistent
        dependencyFachwertList.add(new ClassDependency(swiftInput,swiftMain));
        dependencyFachwertList.add(new ClassDependency(swiftSearch,swiftMain));
        dependencyFachwertList.add(new ClassDependency(swiftInit,swiftMain));
        dependencyFachwertList.add(new ClassDependency(swiftInput,swiftSearch));
        dependencyFachwertList.add(new ClassDependency(swiftC,swiftSearch));
        dependencyFachwertList.add(new ClassDependency(swiftData,swiftSearch));
        dependencyFachwertList.add(new ClassDependency(swiftData,swiftInit));

        //TraceLinks

        //dependencyFachwertList.add(new TraceLinkFachwertClass(javaMain, swiftMain,100));

        return dependencyFachwertList;
    }
}
