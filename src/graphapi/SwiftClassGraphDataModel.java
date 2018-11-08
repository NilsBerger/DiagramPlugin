package graphapi;

import materials.ClassNode;
import materials.SwiftClassNode;
import java.util.Set;
import java.util.stream.Collectors;

public class SwiftClassGraphDataModel extends GerneralClassGraphDataModel {

    @Override
   public void refreshDataModel(final ClassNode changedClassNode) {
        if(changedClassNode instanceof SwiftClassNode)
        {
            addNode(new ClassGraphNode(changedClassNode));

            Set<ClassNode> topDependencies = _changePropagationProcess.getModel().getTopDependencies(changedClassNode).stream().filter(node -> node instanceof SwiftClassNode).collect(Collectors.toSet());
            Set<ClassNode> bottompDependencies = _changePropagationProcess.getModel().getBottomDependencies(changedClassNode).stream().filter(node -> node instanceof SwiftClassNode).collect(Collectors.toSet());

            addNeighbourhoodForClass(changedClassNode, topDependencies);
            addNeighbourhoodForClass(changedClassNode, bottompDependencies);
        }
   }
}

