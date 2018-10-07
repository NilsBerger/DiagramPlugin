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

package changepropagation;

import dotparser.ClassNodeMaterial;
import dotparser.DependencyFachwert;
import junit.framework.TestCase;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChangePropagationProcessTest{

    @Test
    public void getTopDependencies() {
        ChangePropagationProcess propagationProcess = new ChangePropagationProcess(ChangePropagationModelServiceTest.getSimpleDependencyList(), new RandomChangeAndFixStrategy());

        Set<ClassNodeMaterial> ftopdep = propagationProcess.getTopDependencies(new ClassNodeMaterial("f"));

        assertThat(ftopdep,hasItem(new ClassNodeMaterial("e")));
        assertThat(ftopdep, hasSize(1));

        Set<ClassNodeMaterial> ctopdep = propagationProcess.getTopDependencies(new ClassNodeMaterial("c"));

        assertThat(ctopdep,hasItem(new ClassNodeMaterial("a")));
        assertThat(ctopdep,hasItem(new ClassNodeMaterial("b")));
        assertThat(ctopdep,hasItem(new ClassNodeMaterial("d")));
        assertThat(ctopdep,hasSize(3));
    }

    @Test
    public void getBottomDependencies() {
        ChangePropagationProcess propagationProcess = new ChangePropagationProcess(ChangePropagationModelServiceTest.getSimpleDependencyList(), new RandomChangeAndFixStrategy());

        Set<ClassNodeMaterial> ebotdep = propagationProcess.getBottomDependencies(new ClassNodeMaterial("e"));

        assertThat(ebotdep,hasItem(new ClassNodeMaterial("f")));
        assertThat(ebotdep, hasSize(1));

        Set<ClassNodeMaterial> dbotdep = propagationProcess.getBottomDependencies(new ClassNodeMaterial("d"));

        TestCase.assertTrue(dbotdep.contains(new ClassNodeMaterial("e")));
        TestCase.assertTrue(dbotdep.contains(new ClassNodeMaterial("b")));
        TestCase.assertTrue(dbotdep.contains(new ClassNodeMaterial("c")));
        TestCase.assertTrue(dbotdep.size() == 3);

        assertThat(dbotdep,hasItem(new ClassNodeMaterial("e")));
        assertThat(dbotdep,hasItem(new ClassNodeMaterial("b")));
        assertThat(dbotdep,hasItem(new ClassNodeMaterial("c")));
        assertThat(dbotdep,hasSize(3));

    }

    @Test
    public void getTopInconsistencies() {
        ChangePropagationProcess propagationProcess = new ChangePropagationProcess(ChangePropagationModelServiceTest.getSimpleDependencyList(), new RandomChangeAndFixStrategy());
        Set<ClassNodeMaterial> ctopdep = propagationProcess.getTopInconsistencies(new ClassNodeMaterial("c"));

        assertThat(ctopdep,hasItem(new ClassNodeMaterial("f")));
        assertThat(ctopdep, hasSize(1));

        Set<ClassNodeMaterial> ftopdep = propagationProcess.getTopInconsistencies(new ClassNodeMaterial("f"));

        assertThat(ftopdep,hasItem(new ClassNodeMaterial("d")));
        assertThat(ftopdep,hasItem(new ClassNodeMaterial("b")));
        assertThat(ftopdep,hasItem(new ClassNodeMaterial("c")));
        assertThat(ftopdep,hasSize(3));
    }

    @Test
    public void getBottomInconsistencies() {
        ChangePropagationProcess propagationProcess = new ChangePropagationProcess(ChangePropagationModelServiceTest.getSimpleDependencyList(), new RandomChangeAndFixStrategy());

        Set<ClassNodeMaterial> bbotdep = propagationProcess.getBottomInconsistencies(new ClassNodeMaterial("c"));
        assertThat(bbotdep,hasItem(new ClassNodeMaterial("b")));
        assertThat(bbotdep,hasItem(new ClassNodeMaterial("f")));
        assertThat(bbotdep, hasSize(2));

        Set<ClassNodeMaterial> dbotdep = propagationProcess.getBottomInconsistencies(new ClassNodeMaterial("d"));
        assertThat(dbotdep,hasItem(new ClassNodeMaterial("f")));
        assertThat(dbotdep,hasSize(1));
    }

    @Test
    public void changeConsistentDependencyToInconsistentDependency()
    {
        ChangePropagationProcess propagationProcess = new ChangePropagationProcess(ChangePropagationModelServiceTest.getSimpleDependencyList(), new RandomChangeAndFixStrategy());

        Set<ClassNodeMaterial> nodeList = propagationProcess.getTopDependencies(new ClassNodeMaterial("f"));
        assertThat(nodeList,hasItem(new ClassNodeMaterial("e")));
        assertThat(nodeList, hasSize(1));

        Set<ClassNodeMaterial> nodeList2 = propagationProcess.getTopInconsistencies(new ClassNodeMaterial("f"));
        TestCase.assertTrue(nodeList2.size() == 3);
        TestCase.assertTrue(nodeList2.contains(new ClassNodeMaterial("d")));
        TestCase.assertTrue(nodeList2.contains(new ClassNodeMaterial("c")));
        TestCase.assertTrue(nodeList2.contains(new ClassNodeMaterial("b")));

        //Transform
        propagationProcess.changeConsistentDependencyToInconsistentDependency(new DependencyFachwert(new ClassNodeMaterial("f"), new ClassNodeMaterial("e")));

        Set<ClassNodeMaterial> nodeList3 = changePropagationModelService.getTopDependencies(new ClassNodeMaterial("f"));
        TestCase.assertTrue(nodeList3.size() == 0);
        TestCase.assertFalse(nodeList3.contains(new ClassNodeMaterial("e")));

        Set<ClassNodeMaterial> nodeList4 = changePropagationModelService.getTopInconsistencies(new ClassNodeMaterial("f"));
        TestCase.assertTrue(nodeList2.size() == 3);
        TestCase.assertTrue(nodeList4.contains(new ClassNodeMaterial("d")));
        TestCase.assertTrue(nodeList4.contains(new ClassNodeMaterial("c")));
        TestCase.assertTrue(nodeList4.contains(new ClassNodeMaterial("b")));
    }
}
