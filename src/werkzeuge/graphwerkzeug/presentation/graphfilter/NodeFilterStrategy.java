package werkzeuge.graphwerkzeug.presentation.graphfilter;

import materials.ClassNode;
import valueobjects.ClassLanguageType;

/**
 * This FilterStrategy filters  Java-ClassNodes that are classes form libaries and ClassNodes that the user wants to hide.
 */
public final class NodeFilterStrategy implements FilterStrategy{
    @Override
    public boolean filterNode(ClassNode classNode) {
        if(classNode.getClassLanguageType() == ClassLanguageType.Java && !classNode.getFullClassName().contains("de.unihamburg"))
        {
           return true;
        }
        else if(classNode.isHidden())
        {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "NodeFilter";
    }
}
