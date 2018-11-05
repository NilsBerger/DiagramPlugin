/*
 * Copyright 1998-2018 Konstantin Bulenkov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package service;

import material.*;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import valueobjects.Marking;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChangePropagationProcessServiceTest {

    @Test
    public void markingChange()
    {
        ChangePropagationProcessService propgatatesProcess = ChangePropagationProcessService.getInstance();
        propgatatesProcess.initialize(getSmallGraph(), new RandomChangeAndFixStrategy());
        propgatatesProcess.change(new JavaClassNodeMaterial("a"));

        Set<ClassNodeMaterial> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNodeMaterial> classesMarkedByNext = propgatatesProcess.getMarkedNextClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("a")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("b")));


        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNodeMaterial("b")));
    }


    @Test
    public void markingPropagates()
    {
        ChangePropagationProcessService propgatatesProcess = ChangePropagationProcessService.getInstance();
        propgatatesProcess.initialize(getSmallGraph(), new RandomChangeAndFixStrategy());
        propgatatesProcess.change( new JavaClassNodeMaterial("a"));

        Set<ClassNodeMaterial> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNodeMaterial> classesMarkedByNext = propgatatesProcess.getMarkedNextClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("a")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("b")));


        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNodeMaterial("b")));

        // Change "b" to marking -->  "propagates"
        ClassNodeMaterial bselected = propgatatesProcess.select(new JavaClassNodeMaterial("b"));
        bselected.setMarking(Marking.PROPAGATES);
        classesMarkedByNext = propgatatesProcess.getMarkedNextClasses();

        //Tests

        assertThat(affectedClasses, hasSize(3));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("a")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("b")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("c")));


        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNodeMaterial("c")));
    }

    @Test
    public void markingNext()
    {
        ChangePropagationProcessService propgatatesProcess = ChangePropagationProcessService.getInstance();
        propgatatesProcess.initialize(getSmallGraph(), new RandomChangeAndFixStrategy());
        propgatatesProcess.change(new JavaClassNodeMaterial("a"));

        Set<ClassNodeMaterial> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNodeMaterial> classesMarkedByNext = propgatatesProcess.getMarkedNextClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("a")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("b")));


        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNodeMaterial("b")));

        // Change if "b" has Marking "NEXT"
        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNodeMaterial("b")));

        ClassNodeMaterial b = classesMarkedByNext.stream().filter(item -> item.getSimpleClassName().equals("b")).findFirst().orElse(null);
        assertThat(b.getMarking(), is(Marking.NEXT));

        ClassNodeMaterial c = classesMarkedByNext.stream().filter(item -> item.getSimpleClassName().equals("c")).findFirst().orElse(null);
        assertThat(c, IsNull.nullValue());

    }


    @Test
    public void markingInspected()
    {
        ChangePropagationProcessService propgatatesProcess = ChangePropagationProcessService.getInstance();
        propgatatesProcess.initialize(getSmallGraph(), new RandomChangeAndFixStrategy());

        propgatatesProcess.change(new JavaClassNodeMaterial("a"));

        Set<ClassNodeMaterial> affectedClasses = propgatatesProcess.getAffectedClassesByChange();
        Set<ClassNodeMaterial> classesMarkedByNext = propgatatesProcess.getMarkedNextClasses();

        //Tests
        assertThat(affectedClasses, hasSize(2));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("a")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("b")));


        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNodeMaterial("b")));

        // Change if "b" has Marking "NEXT"
        assertThat(classesMarkedByNext, hasSize(1));
        assertThat(classesMarkedByNext, hasItem(new JavaClassNodeMaterial("b")));

        ClassNodeMaterial b = classesMarkedByNext.stream().filter(item -> item.getSimpleClassName().equals("b")).findFirst().orElse(null);
        assertThat(b.getMarking(), is(Marking.NEXT));
        b.setMarking(Marking.INSPECTED);

        ClassNodeMaterial c = classesMarkedByNext.stream().filter(item -> item.getSimpleClassName().equals("c")).findFirst().orElse(null);
        assertThat(c, IsNull.nullValue());

    }


    @Test
    public void basicCPProcess(){
        ChangePropagationProcessService propagationProcess = ChangePropagationProcessService.getInstance();
        propagationProcess.initialize(getJavaAndSwiftDependencyList(), new RandomChangeAndFixStrategy());
        propagationProcess.change(new JavaClassNodeMaterial("C"));

        Set<ClassNodeMaterial> affectedClasses = propagationProcess.getAffectedClassesByChange();
        Set<ClassNodeMaterial> markedClasses = propagationProcess.getMarkedNextClasses();

        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("C")));
        assertThat(affectedClasses, hasSize(2));
        assertThat(markedClasses,hasItem(new JavaClassNodeMaterial("Search")));
        assertThat(markedClasses, hasSize(1));

        //Selected by programmer
        ClassNodeMaterial selectedSearchClass = propagationProcess.select(new JavaClassNodeMaterial("Search"));
        selectedSearchClass.setMarking(Marking.CHANGED);

        //Next change propagation
        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getMarkedNextClasses();

        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Search")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("Main")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("Input")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("Data")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("C")));
        assertThat(affectedClasses, hasSize(5));
        assertThat(markedClasses,hasItem(new JavaClassNodeMaterial("Main")));
        assertThat(markedClasses,hasItem(new JavaClassNodeMaterial("Input")));
        assertThat(markedClasses,hasItem(new JavaClassNodeMaterial("Data")));
        assertThat(markedClasses, hasSize(3));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Input" --> Inspected

        selectedSearchClass = propagationProcess.select(new JavaClassNodeMaterial("Input"));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getMarkedNextClasses();

        assertThat(markedClasses,hasItem(new JavaClassNodeMaterial("Main")));
        assertThat(markedClasses,hasItem(new JavaClassNodeMaterial("Data")));
        assertThat(markedClasses,hasSize(2));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Main" --> Inspected
        selectedSearchClass = propagationProcess.select(new JavaClassNodeMaterial("Main"));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getMarkedNextClasses();

        assertThat(markedClasses,hasItem(new JavaClassNodeMaterial("Data")));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Data" --> Changed
        selectedSearchClass = propagationProcess.select(new JavaClassNodeMaterial("Data"));
        selectedSearchClass.setMarking(Marking.CHANGED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getMarkedNextClasses();

        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("Search")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("Main")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("Input")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("Data")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("C")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("Init")));
        assertThat(affectedClasses, hasSize(6));

        assertThat(markedClasses,hasItem(new JavaClassNodeMaterial("Init")));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Init" --> Changed
        selectedSearchClass = propagationProcess.select(new JavaClassNodeMaterial("Init"));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getMarkedNextClasses();

        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("Search")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("Main")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("Input")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("Data")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("C")));
        assertThat(affectedClasses,hasItem(new JavaClassNodeMaterial("Init")));
        assertThat(affectedClasses, hasSize(6));

        //assertThat(markedClasses, hasItem(new ClassNodeMaterial("Main")));
        assertThat(markedClasses, hasSize(0));
    }


    @Test
    public void basicCPProcessWithTraceLinks() {
        ChangePropagationProcessService propagationProcess = ChangePropagationProcessService.getInstance();
        propagationProcess.initialize(getJavaAndSwiftDependencyList(), new RandomChangeAndFixStrategy());
        propagationProcess.change(new JavaClassNodeMaterial("C"));

        Set<ClassNodeMaterial> affectedClasses = propagationProcess.getAffectedClassesByChange();
        Set<ClassNodeMaterial> markedClasses = propagationProcess.getMarkedNextClasses();

        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("C")));
        assertThat(affectedClasses, hasSize(2));
        assertThat(markedClasses, hasItem(new JavaClassNodeMaterial("Search")));
        assertThat(markedClasses, hasSize(1));

        //Selected by programmer
        ClassNodeMaterial selectedSearchClass = propagationProcess.select(new JavaClassNodeMaterial("Search"));
        selectedSearchClass.setMarking(Marking.CHANGED);

        //Next change propagation
        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getMarkedNextClasses();

        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Search")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Main")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Input")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Data")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("C")));
        assertThat(affectedClasses, hasSize(5));
        assertThat(markedClasses, hasItem(new JavaClassNodeMaterial("Main")));
        assertThat(markedClasses, hasItem(new JavaClassNodeMaterial("Input")));
        assertThat(markedClasses, hasItem(new JavaClassNodeMaterial("Data")));
        assertThat(markedClasses, hasSize(3));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Input" --> Inspected

        selectedSearchClass = propagationProcess.select(new JavaClassNodeMaterial("Input"));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getMarkedNextClasses();

        assertThat(markedClasses, hasItem(new JavaClassNodeMaterial("Main")));
        assertThat(markedClasses, hasItem(new JavaClassNodeMaterial("Data")));
        assertThat(markedClasses, hasSize(2));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Main" --> Inspected
        selectedSearchClass = propagationProcess.select(new JavaClassNodeMaterial("Main"));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getMarkedNextClasses();

        assertThat(markedClasses, hasItem(new JavaClassNodeMaterial("Data")));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Data" --> Changed
        selectedSearchClass = propagationProcess.select(new JavaClassNodeMaterial("Data"));
        selectedSearchClass.setMarking(Marking.CHANGED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getMarkedNextClasses();

        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Search")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Main")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Input")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Data")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("C")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Init")));
        assertThat(affectedClasses, hasSize(6));

        assertThat(markedClasses, hasItem(new JavaClassNodeMaterial("Init")));
        assertThat(markedClasses, hasSize(1));

        //--------------------------------------------------------------------------------------------------------------
        //Visit "Init" --> Changed
        selectedSearchClass = propagationProcess.select(new JavaClassNodeMaterial("Init"));
        selectedSearchClass.setMarking(Marking.INSPECTED);

        affectedClasses = propagationProcess.getAffectedClassesByChange();
        markedClasses = propagationProcess.getMarkedNextClasses();

        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Search")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Main")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Input")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Data")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("C")));
        assertThat(affectedClasses, hasItem(new JavaClassNodeMaterial("Init")));
        assertThat(affectedClasses, hasSize(6));

        //assertThat(markedClasses, hasItem(new ClassNodeMaterial("Main")));
        assertThat(markedClasses, hasSize(0));

        //TraceLink


    }

    @Test
    public void propagationWithTraceLink()
    {
        ChangePropagationProcessService propagationProcess = ChangePropagationProcessService.getInstance();
        propagationProcess.initialize(getSmallGraphWithTrace(), new StrictChangeAndFixStrategy());

        propagationProcess.change(new JavaClassNodeMaterial("a"));
        assertThat(propagationProcess.getAffectedClassesByChange(), hasItem(new JavaClassNodeMaterial("a")));
        assertThat(propagationProcess.getAffectedClassesByChange(), hasItem(new JavaClassNodeMaterial("b")));
        assertThat(propagationProcess.getAffectedClassesByChange(), hasSize(2));

        //Neighbour through Trace Link

        ClassNodeMaterial changedClass = propagationProcess.select(new JavaClassNodeMaterial("b"));
        changedClass.setMarking(Marking.CHANGED);
        assertThat(propagationProcess.getAffectedClassesByChange(), hasItem(new JavaClassNodeMaterial("a")));
        assertThat(propagationProcess.getAffectedClassesByChange(), hasItem(new JavaClassNodeMaterial("b")));
        assertThat(propagationProcess.getAffectedClassesByChange(), hasItem(new SwiftClassNodeMaterial("c")));
        assertThat(propagationProcess.getAffectedClassesByChange(), hasSize(3));

    }

    @Test
    public void strategyStrict()
    {
        ChangePropagationProcessService propagationProcess = ChangePropagationProcessService.getInstance();
        propagationProcess.initialize(getSimpleDependencyList(), new StrictChangeAndFixStrategy());
        assertThat(propagationProcess.change(new JavaClassNodeMaterial("C")), is(true));
        assertThat(propagationProcess.change(new JavaClassNodeMaterial("Main")), is(false));
    }
    @Test
    public void strategyRandom()
    {
        ChangePropagationProcessService propagationProcess = ChangePropagationProcessService.getInstance();
        propagationProcess.initialize(getSimpleDependencyList(), new RandomChangeAndFixStrategy());
        assertThat(propagationProcess.change(new JavaClassNodeMaterial("C")), is(true));
        assertThat(propagationProcess.change(new JavaClassNodeMaterial("Main")), is(true));
    }

    public static final  List<DependencyIF> getSimpleDependencyList()
    {
        ClassNodeMaterial main = new JavaClassNodeMaterial("Main");
        ClassNodeMaterial search = new JavaClassNodeMaterial("Search");
        ClassNodeMaterial input = new JavaClassNodeMaterial("Input");
        ClassNodeMaterial c = new JavaClassNodeMaterial("C");
        ClassNodeMaterial data = new JavaClassNodeMaterial("Data");
        ClassNodeMaterial init = new JavaClassNodeMaterial("Init");

        List<DependencyIF> dependencyFachwertList = new ArrayList<>();
        //conistent
        dependencyFachwertList.add(new ClassDependencyMaterial(input,main));
        dependencyFachwertList.add(new ClassDependencyMaterial(search,main));
        dependencyFachwertList.add(new ClassDependencyMaterial(init,main));
        dependencyFachwertList.add(new ClassDependencyMaterial(input,search));
        dependencyFachwertList.add(new ClassDependencyMaterial(c,search));
        dependencyFachwertList.add(new ClassDependencyMaterial(data,search));
        dependencyFachwertList.add(new ClassDependencyMaterial(data,init));

        return dependencyFachwertList;
    }

    public static final List<DependencyIF> getSmallGraph()
    {
        ClassNodeMaterial a = new JavaClassNodeMaterial("a");
        ClassNodeMaterial b = new JavaClassNodeMaterial("b");
        ClassNodeMaterial c = new JavaClassNodeMaterial("c");

        List<DependencyIF> dependencyFachwertList = new ArrayList<>();
        //conistent
        dependencyFachwertList.add(new ClassDependencyMaterial(a,b));
        dependencyFachwertList.add(new ClassDependencyMaterial(b,c));
        return dependencyFachwertList;
    }

    public static final List<DependencyIF> getSmallGraphWithTrace()
    {
        ClassNodeMaterial a = new JavaClassNodeMaterial("a");
        JavaClassNodeMaterial b = new JavaClassNodeMaterial("b");
        SwiftClassNodeMaterial c = new SwiftClassNodeMaterial("c");

        List<DependencyIF> dependencyFachwertList = new ArrayList<>();
        //conistent
        dependencyFachwertList.add(new ClassDependencyMaterial(a,b));
        dependencyFachwertList.add(new TraceLinkDependencyMaterial(b,c,100));
        return dependencyFachwertList;
    }

    public static final List<DependencyIF> getJavaAndSwiftDependencyList()
    {
        ClassNodeMaterial javaMain = new JavaClassNodeMaterial("java.Main");
        ClassNodeMaterial javaSearch = new JavaClassNodeMaterial("java.Search");
        ClassNodeMaterial javaInput = new JavaClassNodeMaterial("java.Input");
        ClassNodeMaterial javaC = new JavaClassNodeMaterial("java.C");
        ClassNodeMaterial javaData = new JavaClassNodeMaterial("java.Data");
        ClassNodeMaterial javaInit = new JavaClassNodeMaterial("java.Init");

        List<DependencyIF> dependencyFachwertList = new ArrayList<>();
        //conistent
        dependencyFachwertList.add(new ClassDependencyMaterial(javaInput,javaMain));
        dependencyFachwertList.add(new ClassDependencyMaterial(javaSearch,javaMain));
        dependencyFachwertList.add(new ClassDependencyMaterial(javaInit,javaMain));
        dependencyFachwertList.add(new ClassDependencyMaterial(javaInput,javaSearch));
        dependencyFachwertList.add(new ClassDependencyMaterial(javaC,javaSearch));
        dependencyFachwertList.add(new ClassDependencyMaterial(javaData,javaSearch));
        dependencyFachwertList.add(new ClassDependencyMaterial(javaData,javaInit));

        ClassNodeMaterial swiftMain = new SwiftClassNodeMaterial("swift.Main");
        ClassNodeMaterial swiftSearch = new SwiftClassNodeMaterial("swift..Search");
        ClassNodeMaterial swiftInput = new SwiftClassNodeMaterial("swift.Input");
        ClassNodeMaterial swiftC = new SwiftClassNodeMaterial("swift.C");
        ClassNodeMaterial swiftData = new SwiftClassNodeMaterial("swift.Data");
        ClassNodeMaterial swiftInit = new SwiftClassNodeMaterial("swift.Init");

        //conistent
        dependencyFachwertList.add(new ClassDependencyMaterial(swiftInput,swiftMain));
        dependencyFachwertList.add(new ClassDependencyMaterial(swiftSearch,swiftMain));
        dependencyFachwertList.add(new ClassDependencyMaterial(swiftInit,swiftMain));
        dependencyFachwertList.add(new ClassDependencyMaterial(swiftInput,swiftSearch));
        dependencyFachwertList.add(new ClassDependencyMaterial(swiftC,swiftSearch));
        dependencyFachwertList.add(new ClassDependencyMaterial(swiftData,swiftSearch));
        dependencyFachwertList.add(new ClassDependencyMaterial(swiftData,swiftInit));

        //TraceLinks

        //dependencyFachwertList.add(new TraceLinkFachwertClass(javaMain, swiftMain,100));


        return dependencyFachwertList;
    }
}
