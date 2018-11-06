package graphapi;

import materials.ClassNodeMaterial;
import materials.JavaClassNodeMaterial;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaClassGraphDataModel extends GerneralClassGraphDataModel{
    @Override
    protected void refreshDataModel() {
        Set<ClassNodeMaterial> getAffectedJavaClassesByChange = _changePropagationProcessService.getAffectedClassesByChange().stream().filter(node -> node instanceof JavaClassNodeMaterial).collect(Collectors.toSet());

        for (ClassNodeMaterial classNodeMaterial : getAffectedJavaClassesByChange) {
            addNode(new ClassGraphNode(classNodeMaterial));


            Set<ClassNodeMaterial> javatopdependencies = _changePropagationProcessService.getModel().getTopDependencies(classNodeMaterial).stream().filter(node -> node instanceof JavaClassNodeMaterial).collect(Collectors.toSet());
            Set<ClassNodeMaterial> javabottompdependencies = _changePropagationProcessService.getModel().getBottomDependencies(classNodeMaterial).stream().filter(node -> node instanceof JavaClassNodeMaterial).collect(Collectors.toSet());
            Set<ClassNodeMaterial> neighbourhood = new HashSet<>();
            neighbourhood.addAll(javatopdependencies);
            neighbourhood.addAll(javabottompdependencies);

            Set<ClassNodeMaterial> dependencies = new HashSet<>();
            dependencies.addAll(javatopdependencies);
            dependencies.addAll(javabottompdependencies);

            for (ClassNodeMaterial topdependency : dependencies) {
                if (_changePropagationProcessService.getAffectedClassesByChange().contains(topdependency)) {

                    ClassGraphNode dependentNode = new ClassGraphNode(classNodeMaterial);
                    ClassGraphNode independentNode = new ClassGraphNode(topdependency);
                    RelationshipType type = RelationshipType.DirectedRelationship;
                    ClassGraphEdge edge = new ClassGraphEdge(dependentNode,independentNode,type);

                    addNeighbourhoodForClass(classNodeMaterial, dependencies);
                }
            }
        }
    }
}
