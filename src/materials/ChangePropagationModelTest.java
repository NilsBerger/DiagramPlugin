package materials;

import org.junit.Test;
import service.ChangePropagationProcessTest;
import valueobjects.ClassNodeType;
import valueobjects.RelationshipType;


import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

public class ChangePropagationModelTest {

    @Test
    public void getTopDependencies() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());

        ClassNode search = new ClassNode("Search", ClassNodeType.Java);
        Set<ClassNode> searchTopDependencies = modelMaterial.getTopDependencies(search);

        assertThat(searchTopDependencies, hasSize(3));
        assertThat(searchTopDependencies, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(searchTopDependencies, hasItem(new ClassNode("C", ClassNodeType.Java)));
        assertThat(searchTopDependencies, hasItem(new ClassNode("Data", ClassNodeType.Java)));

        ClassNode c = new ClassNode("C", ClassNodeType.Java);
        Set<ClassNode> cTopDependencies = modelMaterial.getTopDependencies(c);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void getBottomDependencies() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());
        ClassNode data = new ClassNode("Data", ClassNodeType.Java);
        Set<ClassNode> searchBottomDependencies = modelMaterial.getBottomDependencies(data);

        assertThat(searchBottomDependencies, hasSize(2));
        assertThat(searchBottomDependencies, hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(searchBottomDependencies, hasItem(new ClassNode("Init", ClassNodeType.Java)));

        ClassNode main = new ClassNode("Main", ClassNodeType.Java);
        Set<ClassNode> cTopDependencies = modelMaterial.getBottomDependencies(main);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void getTopInconsistencies() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaperWithInconsistencies());

        ClassNode search = new ClassNode("Search", ClassNodeType.Java);
        Set<ClassNode> searchTopDependencies = modelMaterial.getTopInconsistencies(search);

        assertThat(searchTopDependencies, hasSize(3));
        assertThat(searchTopDependencies, hasItem(new ClassNode("Input", ClassNodeType.Java)));
        assertThat(searchTopDependencies, hasItem(new ClassNode("C", ClassNodeType.Java)));
        assertThat(searchTopDependencies, hasItem(new ClassNode("Data", ClassNodeType.Java)));

        ClassNode c = new ClassNode("C", ClassNodeType.Java);
        Set<ClassNode> cTopDependencies = modelMaterial.getTopInconsistencies(c);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void getBottomInconsistencies() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaperWithInconsistencies());

        ClassNode data = new ClassNode("Data", ClassNodeType.Java);
        Set<ClassNode> searchTopDependencies = modelMaterial.getBottomInconsistencies(data);

        assertThat(searchTopDependencies, hasSize(2));
        assertThat(searchTopDependencies, hasItem(new ClassNode("Search", ClassNodeType.Java)));
        assertThat(searchTopDependencies, hasItem(new ClassNode("Init", ClassNodeType.Java)));

        ClassNode main = new ClassNode("Main", ClassNodeType.Java);
        Set<ClassNode> cTopDependencies = modelMaterial.getBottomInconsistencies(main);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void createInconsistenciesTest() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());

        modelMaterial.createInconsistencies(new ClassNode("C", ClassNodeType.Java));
        ClassDependency cToSearch = new ClassDependency(new ClassNode("C", ClassNodeType.Java), new ClassNode("Search", ClassNodeType.Java), RelationshipType.InconsistentRealtionship);
        Set<ClassDependency> inconsistencies = modelMaterial.getInconsistencies();
        assertThat(inconsistencies, hasSize(1));
        assertThat(inconsistencies, hasItem(cToSearch));

        modelMaterial.createInconsistencies(new ClassNode("Search", ClassNodeType.Java));
        ClassDependency searchToInput = new ClassDependency(new ClassNode("Search", ClassNodeType.Java), new ClassNode("Input", ClassNodeType.Java), RelationshipType.InconsistentRealtionship);
        ClassDependency searchToData = new ClassDependency(new ClassNode("Search", ClassNodeType.Java), new ClassNode("Data", ClassNodeType.Java), RelationshipType.InconsistentRealtionship);
        ClassDependency searchToMain = new ClassDependency(new ClassNode("Search", ClassNodeType.Java), new ClassNode("Main", ClassNodeType.Java), RelationshipType.InconsistentRealtionship);
        //InconsistentDependency searchToC = new InconsistentDependency(new JavaClassNode("Search"), new JavaClassNode("C"));
        inconsistencies = modelMaterial.getInconsistencies();
        assertThat(inconsistencies, hasSize(4));
        assertThat(inconsistencies, hasItem(cToSearch));
        assertThat(inconsistencies, hasItem(searchToData));
        assertThat(inconsistencies, hasItem(searchToInput));
        assertThat(inconsistencies, hasItem(searchToMain));
        //assertThat(inconsistencies, hasItem(searchToC));


        modelMaterial.createInconsistencies(new ClassNode("Data", ClassNodeType.Java));
        ClassDependency dataToInit = new ClassDependency(new ClassNode("Data", ClassNodeType.Java), new ClassNode("Init", ClassNodeType.Java), RelationshipType.InconsistentRealtionship);
        inconsistencies = modelMaterial.getInconsistencies();
        assertThat(inconsistencies, hasSize(5));
        assertThat(inconsistencies, hasItem(dataToInit));

    }
}
