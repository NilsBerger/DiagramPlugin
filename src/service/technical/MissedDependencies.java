package service.technical;

import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import valueobjects.Language;
import valueobjects.RelationshipType;

import java.util.HashSet;
import java.util.Set;

public class MissedDependencies {

    public static Set<ProgramEntityRelationship> getJavaMissedDependencies()
    {
        Set<ProgramEntityRelationship> dependencyList = new HashSet<>();

        ProgramEntity eventDetailModelNode = new ProgramEntity("EventDetailModel", Language.Java);
        ProgramEntity eventModelNode = new ProgramEntity("EventModel", Language.Java);
        ProgramEntity jsonKeysNode = new ProgramEntity("JSONKeys", Language.Java);
        ProgramEntity newsNode = new ProgramEntity("News", Language.Java);
        ProgramEntity imageNode = new ProgramEntity("Image", Language.Java);
        ProgramEntity instituteNode = new ProgramEntity("Institute", Language.Java);
        ProgramEntity eventNode = new ProgramEntity("Event", Language.Java);

        ProgramEntityRelationship dependency1 = new ProgramEntityRelationship(eventDetailModelNode, jsonKeysNode, RelationshipType.Dependency);
        ProgramEntityRelationship dependency2 = new ProgramEntityRelationship(eventDetailModelNode, newsNode, RelationshipType.Dependency);
        ProgramEntityRelationship dependency3 = new ProgramEntityRelationship(eventDetailModelNode, imageNode, RelationshipType.Dependency);
        ProgramEntityRelationship dependency4 = new ProgramEntityRelationship(eventDetailModelNode, instituteNode, RelationshipType.Dependency);
        ProgramEntityRelationship dependency5 = new ProgramEntityRelationship(eventDetailModelNode, eventNode, RelationshipType.Dependency);

        ProgramEntityRelationship dependency6 = new ProgramEntityRelationship(eventModelNode, jsonKeysNode, RelationshipType.Dependency);
        ProgramEntityRelationship dependency7 = new ProgramEntityRelationship(eventModelNode, newsNode, RelationshipType.Dependency);
        ProgramEntityRelationship dependency8 = new ProgramEntityRelationship(eventModelNode, imageNode, RelationshipType.Dependency);
        ProgramEntityRelationship dependency9 = new ProgramEntityRelationship(eventModelNode, instituteNode, RelationshipType.Dependency);
        ProgramEntityRelationship dependency10 = new ProgramEntityRelationship(eventModelNode, eventNode, RelationshipType.Dependency);

        dependencyList.add(dependency1);
        dependencyList.add(dependency2);
        dependencyList.add(dependency3);
        dependencyList.add(dependency4);
        dependencyList.add(dependency5);
        dependencyList.add(dependency6);
        dependencyList.add(dependency7);
        dependencyList.add(dependency8);
        dependencyList.add(dependency9);
        dependencyList.add(dependency10);

        return dependencyList;
    }

}
