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

package material;

import org.junit.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

public class ChangePropagationModelMaterialTest {

    @Test
    public void getTopDependencies() {
        ChangePropagationModelMaterial modelMaterial = new ChangePropagationModelMaterial(getSimpleDependencyList());

        Set<ClassNodeMaterial> ftopdep = modelMaterial.getTopDependencies(new JavaClassNodeMaterial("f"));

        assertThat(ftopdep, hasItem(new JavaClassNodeMaterial("e")));
        assertThat(ftopdep, hasSize(1));

        Set<ClassNodeMaterial> dtopdep = modelMaterial.getTopDependencies(new JavaClassNodeMaterial("d"));

        assertThat(dtopdep,hasItem(new JavaClassNodeMaterial("e")));
        assertThat(dtopdep,hasItem(new JavaClassNodeMaterial("b")));
        assertThat(dtopdep,hasItem(new JavaClassNodeMaterial("c")));
        assertThat(dtopdep,hasSize(3));

//        assertThat(ftopdep,hasItem(new ClassNodeMaterial("e")));
//        assertThat(ftopdep, hasSize(1));
//
//        Set<ClassNodeMaterial> ctopdep = modelMaterial.getTopDependencies(new ClassNodeMaterial("c"));
//
//        assertThat(ctopdep,hasItem(new ClassNodeMaterial("a")));
//        assertThat(ctopdep,hasItem(new ClassNodeMaterial("b")));
//        assertThat(ctopdep,hasItem(new ClassNodeMaterial("d")));
//        assertThat(ctopdep,hasSize(3));
    }

    @Test
    public void getBottomDependencies() {
        ChangePropagationModelMaterial modelMaterial = new ChangePropagationModelMaterial(getSimpleDependencyList());

        Set<ClassNodeMaterial> ebotdep = modelMaterial.getBottomDependencies(new JavaClassNodeMaterial("e"));

        assertThat(ebotdep,hasItem(new JavaClassNodeMaterial("f")));
        assertThat(ebotdep, hasSize(1));

        Set<ClassNodeMaterial> dbotdep = modelMaterial.getBottomDependencies(new JavaClassNodeMaterial("d"));

        assertThat(dbotdep,hasItem(new JavaClassNodeMaterial("e")));
        assertThat(dbotdep,hasItem(new JavaClassNodeMaterial("b")));
        assertThat(dbotdep,hasItem(new JavaClassNodeMaterial("c")));
        assertThat(dbotdep,hasSize(3));

    }

    @Test
    public void getTopInconsistencies() {
        ChangePropagationModelMaterial modelMaterial = new ChangePropagationModelMaterial(getSimpleDependencyList());
        Set<ClassNodeMaterial> ctopdep = modelMaterial.getTopInconsistencies(new JavaClassNodeMaterial("c"));

        assertThat(ctopdep,hasItem(new JavaClassNodeMaterial("f")));
        assertThat(ctopdep, hasSize(1));

        Set<ClassNodeMaterial> ftopdep = modelMaterial.getTopInconsistencies(new JavaClassNodeMaterial("f"));

        assertThat(ftopdep,hasItem(new JavaClassNodeMaterial("d")));
        assertThat(ftopdep,hasItem(new JavaClassNodeMaterial("b")));
        assertThat(ftopdep,hasItem(new JavaClassNodeMaterial("c")));
        assertThat(ftopdep,hasSize(3));
    }

    @Test
    public void getBottomInconsistencies() {
        ChangePropagationModelMaterial modelMaterial = new ChangePropagationModelMaterial(getSimpleDependencyList());

        Set<ClassNodeMaterial> bbotdep = modelMaterial.getBottomInconsistencies(new JavaClassNodeMaterial("c"));
        assertThat(bbotdep,hasItem(new JavaClassNodeMaterial("b")));
        assertThat(bbotdep,hasItem(new JavaClassNodeMaterial("f")));
        assertThat(bbotdep, hasSize(2));

        Set<ClassNodeMaterial> dbotdep = modelMaterial.getBottomInconsistencies(new JavaClassNodeMaterial("d"));
        assertThat(dbotdep,hasItem(new JavaClassNodeMaterial("f")));
        assertThat(dbotdep,hasSize(1));
    }

    public static final List<DependencyIF> getSimpleDependencyList()
    {
        ClassNodeMaterial a = new JavaClassNodeMaterial("a");
        ClassNodeMaterial b = new JavaClassNodeMaterial("b");
        ClassNodeMaterial c = new JavaClassNodeMaterial("c");
        ClassNodeMaterial d = new JavaClassNodeMaterial("d");
        ClassNodeMaterial e = new JavaClassNodeMaterial("e");
        ClassNodeMaterial f = new JavaClassNodeMaterial("f");

        List<DependencyIF> dependencyFachwertList = new ArrayList<>();
        //consistent
        dependencyFachwertList.add(new ClassDependencyMaterial(b,a));
        dependencyFachwertList.add(new ClassDependencyMaterial(c,b));
        dependencyFachwertList.add(new ClassDependencyMaterial(c,d));
        dependencyFachwertList.add(new ClassDependencyMaterial(b,d));
        dependencyFachwertList.add(new ClassDependencyMaterial(e,d));
        dependencyFachwertList.add(new ClassDependencyMaterial(f,e));
        dependencyFachwertList.add(new ClassDependencyMaterial(c,a));
        dependencyFachwertList.add(new ClassDependencyMaterial(d,a));

        //inconsistent
        dependencyFachwertList.add(new InconsistentDependencyMaterial(new ClassDependencyMaterial(f,b)));
        dependencyFachwertList.add(new InconsistentDependencyMaterial(new ClassDependencyMaterial(f,c)));
        dependencyFachwertList.add(new InconsistentDependencyMaterial(new ClassDependencyMaterial(c,f)));
        dependencyFachwertList.add(new InconsistentDependencyMaterial(new ClassDependencyMaterial(f,d)));
        dependencyFachwertList.add(new InconsistentDependencyMaterial(new ClassDependencyMaterial(b,c)));

        return dependencyFachwertList;
    }
}
