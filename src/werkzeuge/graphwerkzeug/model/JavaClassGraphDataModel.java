package werkzeuge.graphwerkzeug.model;

import materials.ClassDependency;
import materials.ClassNode;
import valueobjects.ClassNodeType;

import java.util.Set;
import java.util.stream.Collectors;

public class JavaClassGraphDataModel extends GerneralClassGraphDataModel {

    @Override
    public void refreshDataModel(final ClassNode changedClassNode)
    {
        if(changedClassNode.getType() == ClassNodeType.Java)
        {
            addNode(changedClassNode);
            Set<ClassDependency> affectedDependencies = _changePropagationProcess.getAffectedDependencies(changedClassNode);
            addAll(affectedDependencies);

//            Set<ClassNode> topDependencies = _changePropagationProcess.getModel().getTopDependencies(changedClassNode).stream().filter(node -> node.getType() == ClassNodeType.Java).collect(Collectors.toSet());
//            Set<ClassNode> bottomDependencies = _changePropagationProcess.getModel().getBottomDependencies(changedClassNode).stream().filter(node -> node.getType() == ClassNodeType.Java).collect(Collectors.toSet());
//
//            addNeighbourhoodForClass(changedClassNode, bottomDependencies);
//            addNeighbourhoodForClass(changedClassNode, topDependencies);

        }
    }

}
