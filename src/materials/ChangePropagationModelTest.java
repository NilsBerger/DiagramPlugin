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

package materials;

import org.junit.Test;
import service.ChangePropagationProcessTest;
import valueobjects.RelationshipType;


import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

public class ChangePropagationModelTest {

    @Test
    public void getTopDependencies() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());

        JavaClassNode search = new JavaClassNode("Search");
        Set<ClassNode> searchTopDependencies = modelMaterial.getTopDependencies(search);

        assertThat(searchTopDependencies, hasSize(3));
        assertThat(searchTopDependencies, hasItem(new JavaClassNode("Input")));
        assertThat(searchTopDependencies, hasItem(new JavaClassNode("C")));
        assertThat(searchTopDependencies, hasItem(new JavaClassNode("Data")));

        JavaClassNode c = new JavaClassNode("C");
        Set<ClassNode> cTopDependencies = modelMaterial.getTopDependencies(c);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void getBottomDependencies() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());

        JavaClassNode data = new JavaClassNode("Data");
        Set<ClassNode> searchBottomDependencies = modelMaterial.getBottomDependencies(data);

        assertThat(searchBottomDependencies, hasSize(2));
        assertThat(searchBottomDependencies, hasItem(new JavaClassNode("Search")));
        assertThat(searchBottomDependencies, hasItem(new JavaClassNode("Init")));

        JavaClassNode main = new JavaClassNode("Main");
        Set<ClassNode> cTopDependencies = modelMaterial.getBottomDependencies(main);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void getTopInconsistencies() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaperWithInconsistencies());

        JavaClassNode search = new JavaClassNode("Search");
        Set<ClassNode> searchTopDependencies = modelMaterial.getTopInconsistencies(search);

        assertThat(searchTopDependencies, hasSize(3));
        assertThat(searchTopDependencies, hasItem(new JavaClassNode("Input")));
        assertThat(searchTopDependencies, hasItem(new JavaClassNode("C")));
        assertThat(searchTopDependencies, hasItem(new JavaClassNode("Data")));

        JavaClassNode c = new JavaClassNode("C");
        Set<ClassNode> cTopDependencies = modelMaterial.getTopInconsistencies(c);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void getBottomInconsistencies() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaperWithInconsistencies());

        JavaClassNode data = new JavaClassNode("Data");
        Set<ClassNode> searchTopDependencies = modelMaterial.getBottomInconsistencies(data);

        assertThat(searchTopDependencies, hasSize(2));
        assertThat(searchTopDependencies, hasItem(new JavaClassNode("Search")));
        assertThat(searchTopDependencies, hasItem(new JavaClassNode("Init")));

        JavaClassNode main = new JavaClassNode("Main");
        Set<ClassNode> cTopDependencies = modelMaterial.getBottomInconsistencies(main);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void createInconsistenciesTest() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());

        modelMaterial.createInconsistencies(new JavaClassNode("C"));
        ClassDependency cToSearch = new ClassDependency(new JavaClassNode("C"), new JavaClassNode("Search"), RelationshipType.InconsistentRealtionship);
        Set<ClassDependency> inconsistencies = modelMaterial.getInconsistencies();
        assertThat(inconsistencies, hasSize(1));
        assertThat(inconsistencies, hasItem(cToSearch));

        modelMaterial.createInconsistencies(new JavaClassNode("Search"));
        ClassDependency searchToInput = new ClassDependency(new JavaClassNode("Search"), new JavaClassNode("Input"), RelationshipType.InconsistentRealtionship);
        ClassDependency searchToData = new ClassDependency(new JavaClassNode("Search"), new JavaClassNode("Data"), RelationshipType.InconsistentRealtionship);
        ClassDependency searchToMain = new ClassDependency(new JavaClassNode("Search"), new JavaClassNode("Main"), RelationshipType.InconsistentRealtionship);
        //InconsistentDependency searchToC = new InconsistentDependency(new JavaClassNode("Search"), new JavaClassNode("C"));
        inconsistencies = modelMaterial.getInconsistencies();
        assertThat(inconsistencies, hasSize(4));
        assertThat(inconsistencies, hasItem(cToSearch));
        assertThat(inconsistencies, hasItem(searchToData));
        assertThat(inconsistencies, hasItem(searchToInput));
        assertThat(inconsistencies, hasItem(searchToMain));
        //assertThat(inconsistencies, hasItem(searchToC));


        modelMaterial.createInconsistencies(new JavaClassNode("Data"));
        ClassDependency dataToInit = new ClassDependency(new JavaClassNode("Data"), new JavaClassNode("Init"), RelationshipType.InconsistentRealtionship);
        inconsistencies = modelMaterial.getInconsistencies();
        assertThat(inconsistencies, hasSize(5));
        assertThat(inconsistencies, hasItem(dataToInit));

    }
}
