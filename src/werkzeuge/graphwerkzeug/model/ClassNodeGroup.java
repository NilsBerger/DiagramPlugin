package werkzeuge.graphwerkzeug.model;

import com.intellij.openapi.graph.builder.NodesGroup;
import com.intellij.openapi.graph.builder.components.BasicNodesGroup;
import materials.ProgramEntity;
import valueobjects.Language;

public final class ClassNodeGroup{
    private final static NodesGroup _javaNodesGroup = new BasicNodesGroup(Language.Java.name());
    private final static NodesGroup _swiftNodesGroup = new BasicNodesGroup(Language.Swift.name());

    public static NodesGroup getNodesGroup(final ProgramEntity programEntity) {
        if (programEntity.getLanguage() == Language.Java)
        {
            return _javaNodesGroup;
        } else if (programEntity.getLanguage() == Language.Swift)
        {
            return _swiftNodesGroup;
        }
        else
        {
            throw new IllegalArgumentException("Could not find ClassLanuageType for ClassNode: " + programEntity.getFullEntityName());
        }

    }
}
