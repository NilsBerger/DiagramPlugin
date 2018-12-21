package service.technical;

import materials.ClassDependency;
import materials.ClassNode;
import valueobjects.ClassLanguageType;
import valueobjects.RelationshipType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MissedDependencies {

    public static Set<ClassDependency> getJavaMissedDependencies()
    {
        Set<ClassDependency> dependencyList = new HashSet<>();

        ClassNode eventDetailModelNode = new ClassNode("EventDetailModel", ClassLanguageType.Java);
        ClassNode jsonKeysNode = new ClassNode("JSONKeys", ClassLanguageType.Java);
        ClassNode newsNode = new ClassNode("News", ClassLanguageType.Java);
        ClassNode imageNode = new ClassNode("Image", ClassLanguageType.Java);
        ClassNode instituteNode = new ClassNode("Institute", ClassLanguageType.Java);
        ClassNode eventNode = new ClassNode("Event", ClassLanguageType.Java);

        ClassDependency dependency1 = new ClassDependency(eventDetailModelNode, jsonKeysNode, RelationshipType.Dependency);
        ClassDependency dependency2 = new ClassDependency(eventDetailModelNode, newsNode, RelationshipType.Dependency);
        ClassDependency dependency3 = new ClassDependency(eventDetailModelNode, imageNode, RelationshipType.Dependency);
        ClassDependency dependency4 = new ClassDependency(eventDetailModelNode, instituteNode, RelationshipType.Dependency);
        ClassDependency dependency5 = new ClassDependency(eventDetailModelNode, eventNode, RelationshipType.Dependency);

        dependencyList.add(dependency1);
        dependencyList.add(dependency2);
        dependencyList.add(dependency3);
        dependencyList.add(dependency4);
        dependencyList.add(dependency5);

        return dependencyList;
    }

}
