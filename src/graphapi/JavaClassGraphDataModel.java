package graphapi;

import materials.ClassNode;
import materials.JavaClassNode;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaClassGraphDataModel extends GerneralClassGraphDataModel{

    @Override
    public void refreshDataModel(final ClassNode changedClassNode)
    {
        if(changedClassNode instanceof JavaClassNode)
        {
            addNode(new ClassGraphNode(changedClassNode));

            Set<ClassNode> topDependencies = _changePropagationProcess.getModel().getTopDependencies(changedClassNode).stream().filter(node -> node instanceof JavaClassNode).collect(Collectors.toSet());
            Set<ClassNode> bottomDependencies = _changePropagationProcess.getModel().getBottomDependencies(changedClassNode).stream().filter(node -> node instanceof JavaClassNode).collect(Collectors.toSet());

            addNeighbourhoodForClass(changedClassNode, bottomDependencies);
            addNeighbourhoodForClass(changedClassNode, topDependencies);

        }
    }

}
