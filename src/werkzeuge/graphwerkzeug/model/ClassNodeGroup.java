package werkzeuge.graphwerkzeug.model;

import com.intellij.openapi.graph.builder.NodesGroup;
import com.intellij.openapi.graph.builder.components.BasicNodesGroup;
import materials.ClassNode;
import org.jetbrains.annotations.NotNull;
import valueobjects.ClassLanguageType;
import valueobjects.ClassNodeType;

public final class ClassNodeGroup{
    private final static NodesGroup _javaNodesGroup= new BasicNodesGroup(ClassLanguageType.Java.name());
    private final static NodesGroup _swiftNodesGroup= new BasicNodesGroup(ClassLanguageType.Swift.name());

    public static NodesGroup getNodesGroup(final ClassNode classNode){
        if(classNode.getClassLanguageType() == ClassLanguageType.Java)
        {
            return _javaNodesGroup;
        }
        else if(classNode.getClassLanguageType() == ClassLanguageType.Swift)
        {
            return _swiftNodesGroup;
        }
        else
        {
            throw new IllegalArgumentException("Could not find ClassLanuageType for ClassNode: " + classNode.getFullClassName());
        }

    }
}
