package werkzeuge.graphwerkzeug.model;

import materials.ClassDependency;
import materials.ClassNode;
import valueobjects.ClassLanguageType;

import java.util.Set;

public class JavaClassGraphDataModel extends GeneralClassGraphDataModel {

    @Override
    public void refreshDataModel(final ClassNode changedClassNode)
    {
        if(changedClassNode.getType() == ClassLanguageType.Java)
        {
            addNode(changedClassNode);
            Set<ClassDependency> affectedDependencies = _changePropagationProcess.getAffectedDependencies(changedClassNode);
            addAll(affectedDependencies);

//            Set<ClassNode> topDependencies = _changePropagationProcess.getModel().getTopDependencies(changedClassNode).stream().filter(node -> node.getType() == ClassLanguageType.Java).collect(Collectors.toSet());
//            Set<ClassNode> bottomDependencies = _changePropagationProcess.getModel().getBottomDependencies(changedClassNode).stream().filter(node -> node.getType() == ClassLanguageType.Java).collect(Collectors.toSet());
//
//            addNeighbourhoodForClass(changedClassNode, bottomDependencies);
//            addNeighbourhoodForClass(changedClassNode, topDependencies);

        }
    }

}
