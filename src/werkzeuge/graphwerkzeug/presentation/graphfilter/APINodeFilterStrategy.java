package werkzeuge.graphwerkzeug.presentation.graphfilter;

import materials.ClassNode;
import valueobjects.ClassLanguageType;

public final class APINodeFilterStrategy implements FilterStrategy{
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
        return "APINodeFilter";
    }
}
