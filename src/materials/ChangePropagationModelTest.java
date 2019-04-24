package materials;

import org.junit.Test;
import service.functional.ChangePropagationProcessTest;
import valueobjects.Language;
import valueobjects.RelationshipType;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class ChangePropagationModelTest {

    @Test
    public void getBottomDependencies() {
        //GIVEN
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());

        //WHEN
        ProgramEntity search = new ProgramEntity("Search", Language.Java);
        Set<ProgramEntity> searchBottomDependencies = modelMaterial.getBottomDependencies(search);

        assertThat(searchBottomDependencies, hasSize(3));
        assertThat(searchBottomDependencies, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(searchBottomDependencies, hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(searchBottomDependencies, hasItem(new ProgramEntity("Data", Language.Java)));

        //WHEN
        ProgramEntity c = new ProgramEntity("C", Language.Java);
        Set<ProgramEntity> cBottomDependencies = modelMaterial.getBottomDependencies(c);

        //THEN
        assertThat(cBottomDependencies, hasSize(0));
    }

    @Test
    public void getTopDependencies() {
        //GIVEN
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());
        ProgramEntity data = new ProgramEntity("Data", Language.Java);

        //WHEN
        Set<ProgramEntity> searchTopDependencies = modelMaterial.getTopDependencies(data);

        //THEN
        assertThat(searchTopDependencies, hasSize(2));
        assertThat(searchTopDependencies, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(searchTopDependencies, hasItem(new ProgramEntity("Init", Language.Java)));

        //GIVEN
        ProgramEntity main = new ProgramEntity("Main", Language.Java);
        Set<ProgramEntity> cTopDependencies = modelMaterial.getTopDependencies(main);

        //THEN
        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void getBottomInconsistencies() {
        //GIVEN
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaperWithInconsistencies());

        //WHEN
        ProgramEntity search = new ProgramEntity("Search", Language.Java);
        Set<ProgramEntity> searchBottomDependencies = modelMaterial.getBottomInconsistencies(search);

        //THEN
        assertThat(searchBottomDependencies, hasSize(3));
        assertThat(searchBottomDependencies, hasItem(new ProgramEntity("Input", Language.Java)));
        assertThat(searchBottomDependencies, hasItem(new ProgramEntity("C", Language.Java)));
        assertThat(searchBottomDependencies, hasItem(new ProgramEntity("Data", Language.Java)));

        //WHEN
        ProgramEntity c = new ProgramEntity("C", Language.Java);
        Set<ProgramEntity> cBottomDependencies = modelMaterial.getBottomInconsistencies(c);

        //THEN
        assertThat(cBottomDependencies, hasSize(0));
    }

    @Test
    public void getTopInconsistencies() {
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaperWithInconsistencies());

        ProgramEntity data = new ProgramEntity("Data", Language.Java);
        Set<ProgramEntity> searchTopDependencies = modelMaterial.getTopInconsistencies(data);

        assertThat(searchTopDependencies, hasSize(2));
        assertThat(searchTopDependencies, hasItem(new ProgramEntity("Search", Language.Java)));
        assertThat(searchTopDependencies, hasItem(new ProgramEntity("Init", Language.Java)));

        ProgramEntity main = new ProgramEntity("Main", Language.Java);
        Set<ProgramEntity> cTopDependencies = modelMaterial.getTopInconsistencies(main);

        assertThat(cTopDependencies, hasSize(0));
    }

    @Test
    public void createInconsistenciesTest() {
        //GIVEN
        ChangePropagationModel modelMaterial = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());

        //WHEN
        modelMaterial.createInconsistencies(new ProgramEntity("C", Language.Java));
        ProgramEntityRelationship cToSearch = new ProgramEntityRelationship(new ProgramEntity("C", Language.Java), new ProgramEntity("Search", Language.Java), RelationshipType.InconsistentRelationship);

        //THEN
        Set<ProgramEntityRelationship> inconsistencies = modelMaterial.getInconsistencies();
        assertThat(inconsistencies, hasSize(1));
        assertThat(inconsistencies, hasItem(cToSearch));

        //WHEN
        modelMaterial.createInconsistencies(new ProgramEntity("Search", Language.Java));

        //THEN
        inconsistencies = modelMaterial.getInconsistencies();

        ProgramEntityRelationship searchToInput = new ProgramEntityRelationship(new ProgramEntity("Search", Language.Java), new ProgramEntity("Input", Language.Java), RelationshipType.InconsistentRelationship);
        ProgramEntityRelationship searchToData = new ProgramEntityRelationship(new ProgramEntity("Search", Language.Java), new ProgramEntity("Data", Language.Java), RelationshipType.InconsistentRelationship);
        ProgramEntityRelationship searchToMain = new ProgramEntityRelationship(new ProgramEntity("Search", Language.Java), new ProgramEntity("Main", Language.Java), RelationshipType.InconsistentRelationship);
        ProgramEntityRelationship searchToC = new ProgramEntityRelationship(new ProgramEntity("Search", Language.Java), new ProgramEntity("C", Language.Java), RelationshipType.InconsistentRelationship);

        assertThat(inconsistencies, hasSize(4));
        assertThat(inconsistencies, hasItem(cToSearch));
        assertThat(inconsistencies, hasItem(searchToData));
        assertThat(inconsistencies, hasItem(searchToInput));
        assertThat(inconsistencies, hasItem(searchToMain));
        assertThat(inconsistencies, not(hasItem(searchToC)));

        //WHEN
        modelMaterial.createInconsistencies(new ProgramEntity("Data", Language.Java));

        //THEN
        ProgramEntityRelationship dataToInit = new ProgramEntityRelationship(new ProgramEntity("Data", Language.Java), new ProgramEntity("Init", Language.Java), RelationshipType.InconsistentRelationship);
        inconsistencies = modelMaterial.getInconsistencies();

        assertThat(inconsistencies, hasSize(5));
        assertThat(inconsistencies, hasItem(dataToInit));
    }

    @Test
    public void inconcistencyBetweenNodesTest() {

        //GIVEN
        ChangePropagationModel model = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());

        //WHEN
        //Nothing inconsistency
        ProgramEntity c = new ProgramEntity("C", Language.Java);
        ProgramEntity search = new ProgramEntity("Search", Language.Java);

        //THEN
        assertThat(model.inconcistencyBetweenNodes(c, search), is(false));

        //WHEN
        model.createInconsistencies(c);

        //THEN
        assertThat(model.inconcistencyBetweenNodes(c, search), is(true));
    }

    @Test
    public void updateDependencyTest(){
        ChangePropagationModel model = new ChangePropagationModel(ChangePropagationProcessTest.getSimpleDependencyFromPaper());


        ProgramEntity main = new ProgramEntity("Main", Language.Java);
        ProgramEntity input = new ProgramEntity("Input", Language.Java);
        ProgramEntityRelationship dependency = new ProgramEntityRelationship(main, input, RelationshipType.Dependency);

        assertThat(model.getDependencies(), hasItem(dependency));
        //When
        RelationshipType newRelationshipType = RelationshipType.Extends;
        model.updateDependency(dependency, newRelationshipType);
        ProgramEntityRelationship newDependency = new ProgramEntityRelationship(main, input, newRelationshipType);

        assertThat(model.getDependencies(), hasItem(newDependency));
        assertThat(model.getDependencies(), not(hasItem(dependency)));
    }
}
