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
import service.ChangePropagationProcessServiceTest;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

public class ChangePropagationModelMaterialTest {

    @Test
    public void getTopDependencies() {
        ChangePropagationModelMaterial modelMaterial = new ChangePropagationModelMaterial(ChangePropagationProcessServiceTest.getSimpleDependencyList());

        JavaClassNodeMaterial search = new JavaClassNodeMaterial("Search");
        Set<ClassNodeMaterial> searchTopDependencies = modelMaterial.getTopDependencies(search);

        assertThat(searchTopDependencies, hasSize(3));
        assertThat(searchTopDependencies, hasItem(new JavaClassNodeMaterial("Input")));
        assertThat(searchTopDependencies, hasItem(new JavaClassNodeMaterial("C")));
        assertThat(searchTopDependencies, hasItem(new JavaClassNodeMaterial("Data")));

        JavaClassNodeMaterial c = new JavaClassNodeMaterial("C");
        Set<ClassNodeMaterial> cTopDependencies = modelMaterial.getTopDependencies(c);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void getBottomDependencies() {
        ChangePropagationModelMaterial modelMaterial = new ChangePropagationModelMaterial(ChangePropagationProcessServiceTest.getSimpleDependencyList());

        JavaClassNodeMaterial data = new JavaClassNodeMaterial("Data");
        Set<ClassNodeMaterial> searchTopDependencies = modelMaterial.getBottomDependencies(data);

        assertThat(searchTopDependencies, hasSize(2));
        assertThat(searchTopDependencies, hasItem(new JavaClassNodeMaterial("Search")));
        assertThat(searchTopDependencies, hasItem(new JavaClassNodeMaterial("Init")));

        JavaClassNodeMaterial main = new JavaClassNodeMaterial("Main");
        Set<ClassNodeMaterial> cTopDependencies = modelMaterial.getBottomDependencies(main);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void getTopInconsistencies() {
        ChangePropagationModelMaterial modelMaterial = new ChangePropagationModelMaterial(ChangePropagationProcessServiceTest.getSimpleDependencyList());

        JavaClassNodeMaterial search = new JavaClassNodeMaterial("Search");
        Set<ClassNodeMaterial> searchTopDependencies = modelMaterial.getTopInconsistencies(search);

        assertThat(searchTopDependencies, hasSize(3));
        assertThat(searchTopDependencies, hasItem(new JavaClassNodeMaterial("Input")));
        assertThat(searchTopDependencies, hasItem(new JavaClassNodeMaterial("C")));
        assertThat(searchTopDependencies, hasItem(new JavaClassNodeMaterial("Data")));

        JavaClassNodeMaterial c = new JavaClassNodeMaterial("C");
        Set<ClassNodeMaterial> cTopDependencies = modelMaterial.getTopInconsistencies(c);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void getBottomInconsistencies() {
        ChangePropagationModelMaterial modelMaterial = new ChangePropagationModelMaterial(ChangePropagationProcessServiceTest.getSimpleDependencyList());

        JavaClassNodeMaterial data = new JavaClassNodeMaterial("Data");
        Set<ClassNodeMaterial> searchTopDependencies = modelMaterial.getBottomInconsistencies(data);

        assertThat(searchTopDependencies, hasSize(2));
        assertThat(searchTopDependencies, hasItem(new JavaClassNodeMaterial("Search")));
        assertThat(searchTopDependencies, hasItem(new JavaClassNodeMaterial("Init")));

        JavaClassNodeMaterial main = new JavaClassNodeMaterial("Main");
        Set<ClassNodeMaterial> cTopDependencies = modelMaterial.getBottomInconsistencies(main);

        assertThat(cTopDependencies, hasSize(0));
    }
}
