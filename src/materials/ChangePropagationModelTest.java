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
import valueobjects.ClassLanguageType;
import valueobjects.RelationshipType;


import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

public class ChangePropagationModelTest {

    @Test
    public void getTopDependencies() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());

        ClassNode search = new ClassNode("Search", ClassLanguageType.Java);
        Set<ClassNode> searchTopDependencies = modelMaterial.getTopDependencies(search);

        assertThat(searchTopDependencies, hasSize(3));
        assertThat(searchTopDependencies, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(searchTopDependencies, hasItem(new ClassNode("C", ClassLanguageType.Java)));
        assertThat(searchTopDependencies, hasItem(new ClassNode("Data", ClassLanguageType.Java)));

        ClassNode c = new ClassNode("C", ClassLanguageType.Java);
        Set<ClassNode> cTopDependencies = modelMaterial.getTopDependencies(c);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void getBottomDependencies() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());
        ClassNode data = new ClassNode("Data", ClassLanguageType.Java);
        Set<ClassNode> searchBottomDependencies = modelMaterial.getBottomDependencies(data);

        assertThat(searchBottomDependencies, hasSize(2));
        assertThat(searchBottomDependencies, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(searchBottomDependencies, hasItem(new ClassNode("Init", ClassLanguageType.Java)));

        ClassNode main = new ClassNode("Main", ClassLanguageType.Java);
        Set<ClassNode> cTopDependencies = modelMaterial.getBottomDependencies(main);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void getTopInconsistencies() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaperWithInconsistencies());

        ClassNode search = new ClassNode("Search", ClassLanguageType.Java);
        Set<ClassNode> searchTopDependencies = modelMaterial.getTopInconsistencies(search);

        assertThat(searchTopDependencies, hasSize(3));
        assertThat(searchTopDependencies, hasItem(new ClassNode("Input", ClassLanguageType.Java)));
        assertThat(searchTopDependencies, hasItem(new ClassNode("C", ClassLanguageType.Java)));
        assertThat(searchTopDependencies, hasItem(new ClassNode("Data", ClassLanguageType.Java)));

        ClassNode c = new ClassNode("C", ClassLanguageType.Java);
        Set<ClassNode> cTopDependencies = modelMaterial.getTopInconsistencies(c);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void getBottomInconsistencies() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaperWithInconsistencies());

        ClassNode data = new ClassNode("Data", ClassLanguageType.Java);
        Set<ClassNode> searchTopDependencies = modelMaterial.getBottomInconsistencies(data);

        assertThat(searchTopDependencies, hasSize(2));
        assertThat(searchTopDependencies, hasItem(new ClassNode("Search", ClassLanguageType.Java)));
        assertThat(searchTopDependencies, hasItem(new ClassNode("Init", ClassLanguageType.Java)));

        ClassNode main = new ClassNode("Main", ClassLanguageType.Java);
        Set<ClassNode> cTopDependencies = modelMaterial.getBottomInconsistencies(main);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void createInconsistenciesTest() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());

        modelMaterial.createInconsistencies(new ClassNode("C", ClassLanguageType.Java));
        ClassDependency cToSearch = new ClassDependency(new ClassNode("C", ClassLanguageType.Java), new ClassNode("Search", ClassLanguageType.Java), RelationshipType.InconsistentRelationship);
        Set<ClassDependency> inconsistencies = modelMaterial.getInconsistencies();
        assertThat(inconsistencies, hasSize(1));
        assertThat(inconsistencies, hasItem(cToSearch));

        modelMaterial.createInconsistencies(new ClassNode("Search", ClassLanguageType.Java));
        ClassDependency searchToInput = new ClassDependency(new ClassNode("Search", ClassLanguageType.Java), new ClassNode("Input", ClassLanguageType.Java), RelationshipType.InconsistentRelationship);
        ClassDependency searchToData = new ClassDependency(new ClassNode("Search", ClassLanguageType.Java), new ClassNode("Data", ClassLanguageType.Java), RelationshipType.InconsistentRelationship);
        ClassDependency searchToMain = new ClassDependency(new ClassNode("Search", ClassLanguageType.Java), new ClassNode("Main", ClassLanguageType.Java), RelationshipType.InconsistentRelationship);
        //InconsistentDependency searchToC = new InconsistentDependency(new JavaClassNode("Search"), new JavaClassNode("C"));
        inconsistencies = modelMaterial.getInconsistencies();
        assertThat(inconsistencies, hasSize(4));
        assertThat(inconsistencies, hasItem(cToSearch));
        assertThat(inconsistencies, hasItem(searchToData));
        assertThat(inconsistencies, hasItem(searchToInput));
        assertThat(inconsistencies, hasItem(searchToMain));
        //assertThat(inconsistencies, hasItem(searchToC));


        modelMaterial.createInconsistencies(new ClassNode("Data", ClassLanguageType.Java));
        ClassDependency dataToInit = new ClassDependency(new ClassNode("Data", ClassLanguageType.Java), new ClassNode("Init", ClassLanguageType.Java), RelationshipType.InconsistentRelationship);
        inconsistencies = modelMaterial.getInconsistencies();
        assertThat(inconsistencies, hasSize(5));
        assertThat(inconsistencies, hasItem(dataToInit));

    }
}
