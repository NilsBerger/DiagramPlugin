package graphapi;

import material.ClassNodeMaterial;
import material.SwiftClassNodeMaterial;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SwiftClassGraphDataModel extends GerneralClassGraphDataModel {

    @Override
    protected void refreshDataModel() {
        Set<ClassNodeMaterial> getAffectedJavaClassesByChange = _changePropagationProcessService.getAffectedClassesByChange().stream().filter(node -> node instanceof SwiftClassNodeMaterial).collect(Collectors.toSet());

        for (ClassNodeMaterial classNodeMaterial : getAffectedJavaClassesByChange) {
            addNode(new ClassGraphNode(classNodeMaterial));


            Set<ClassNodeMaterial> javatopdependencies = _changePropagationProcessService.getModel().getTopDependencies(classNodeMaterial).stream().filter(node -> node instanceof SwiftClassNodeMaterial).collect(Collectors.toSet());
            Set<ClassNodeMaterial> javabottompdependencies = _changePropagationProcessService.getModel().getBottomDependencies(classNodeMaterial).stream().filter(node -> node instanceof SwiftClassNodeMaterial).collect(Collectors.toSet());
            Set<ClassNodeMaterial> neighbourhood = new HashSet<>();
            neighbourhood.addAll(javatopdependencies);
            neighbourhood.addAll(javabottompdependencies);

            Set<ClassNodeMaterial> dependencies = new HashSet<>();
            dependencies.addAll(javatopdependencies);
            dependencies.addAll(javabottompdependencies);

            addNeighbourhoodForClass(classNodeMaterial, dependencies);
        }
    }
}

