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

import dotparser.*;


import java.util.ArrayList;
import java.util.List;

public class ChangePropagationModelServiceTest {


//    @Test
//    public void changeConsistentDependencyToInconsistentDependency()
//    {
//        ChangePropagationModelService changePropagationModelService = new ChangePropagationModelService(getSimpleDependencyList());
//
//        Set<ClassNodeMaterial> nodeList = changePropagationModelService.getTopDependencies(new ClassNodeMaterial("f"));
//        TestCase.assertTrue(nodeList.size() == 1);
//        TestCase.assertTrue(nodeList.contains(new ClassNodeMaterial("e")));
//
//        Set<ClassNodeMaterial> nodeList2 = changePropagationModelService.getTopInconsistencies(new ClassNodeMaterial("f"));
//        TestCase.assertTrue(nodeList2.size() == 3);
//        TestCase.assertTrue(nodeList2.contains(new ClassNodeMaterial("d")));
//        TestCase.assertTrue(nodeList2.contains(new ClassNodeMaterial("c")));
//        TestCase.assertTrue(nodeList2.contains(new ClassNodeMaterial("b")));
//
//        //Transform
//        changePropagationModelService.changeConsistentDependencyToInconsistentDependency(new DependencyFachwert(new ClassNodeMaterial("f"), new ClassNodeMaterial("e")));
//
//        Set<ClassNodeMaterial> nodeList3 = changePropagationModelService.getTopDependencies(new ClassNodeMaterial("f"));
//        TestCase.assertTrue(nodeList3.size() == 0);
//        TestCase.assertFalse(nodeList3.contains(new ClassNodeMaterial("e")));
//
//        Set<ClassNodeMaterial> nodeList4 = changePropagationModelService.getTopInconsistencies(new ClassNodeMaterial("f"));
//        TestCase.assertTrue(nodeList2.size() == 3);
//        TestCase.assertTrue(nodeList4.contains(new ClassNodeMaterial("d")));
//        TestCase.assertTrue(nodeList4.contains(new ClassNodeMaterial("c")));
//        TestCase.assertTrue(nodeList4.contains(new ClassNodeMaterial("b")));
//    }
//
//    @Test
//    public void getNeighbourhood()
//    {
//        ChangePropagationModelService changePropagationModelService = new ChangePropagationModelService(getSimpleDependencyList());
//        Set<ClassNodeMaterial> aNeighbourhood = changePropagationModelService.getNeighbourhood(new ClassNodeMaterial("a"));
//        TestCase.assertTrue(aNeighbourhood.size() == 3);
//
//        //Transform
//        int oldSize = changePropagationModelService.getNeighbourhood(new ClassNodeMaterial("a")).size();
//        DependencyFachwert dependencyFachwert = new DependencyFachwert(new ClassNodeMaterial("d"), new ClassNodeMaterial("a"));
//        changePropagationModelService.changeConsistentDependencyToInconsistentDependency(dependencyFachwert);
//
//        int newSize = changePropagationModelService.getNeighbourhood(new ClassNodeMaterial("a")).size();
//        TestCase.assertTrue(oldSize == newSize);
//        TestCase.assertFalse(changePropagationModelService.getNeighbourhood(new ClassNodeMaterial("a")).contains(dependencyFachwert));
//        Set<ClassNodeMaterial> testset = changePropagationModelService.getTopDependencies(new ClassNodeMaterial("b"));
//
//        for(ClassNodeMaterial c : testset)
//        {
//            c.setMarking(Marking.CHANGED);
//        }
//    }

    public static final List<DependencyIF> getSimpleDependencyList()
    {
        ClassNodeMaterial a = new ClassNodeMaterial("a");
        ClassNodeMaterial b = new ClassNodeMaterial("b");
        ClassNodeMaterial c = new ClassNodeMaterial("c");
        ClassNodeMaterial d = new ClassNodeMaterial("d");
        ClassNodeMaterial e = new ClassNodeMaterial("e");
        ClassNodeMaterial f = new ClassNodeMaterial("f");

        List<DependencyIF> dependencyFachwertList = new ArrayList<>();
        //conistent
        dependencyFachwertList.add(new DependencyFachwert(b,a));
        dependencyFachwertList.add(new DependencyFachwert(c,b));
        dependencyFachwertList.add(new DependencyFachwert(c,d));
        dependencyFachwertList.add(new DependencyFachwert(b,d));
        dependencyFachwertList.add(new DependencyFachwert(e,d));
        dependencyFachwertList.add(new DependencyFachwert(f,e));
        dependencyFachwertList.add(new DependencyFachwert(c,a));
        dependencyFachwertList.add(new DependencyFachwert(d,a));

        //inconsistent
        dependencyFachwertList.add(new InconsistentDependencyFachwert(new DependencyFachwert(f,b)));
        dependencyFachwertList.add(new InconsistentDependencyFachwert(new DependencyFachwert(f,c)));
        dependencyFachwertList.add(new InconsistentDependencyFachwert(new DependencyFachwert(c,f)));
        dependencyFachwertList.add(new InconsistentDependencyFachwert(new DependencyFachwert(f,d)));
        dependencyFachwertList.add(new InconsistentDependencyFachwert(new DependencyFachwert(b,c)));

        return dependencyFachwertList;
    }



}
