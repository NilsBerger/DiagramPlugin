package service;

import materials.ClassNode;
import valueobjects.ClassLanguageType;

public class ClassNodeFilter {
    private ClassNodeFilter(){}

    public static boolean isClassNodeFromAPI(ClassNode classNode)
    {
        if(classNode.getType() == ClassLanguageType.Swift)
        {
            return false;
        }
        else{
            if(classNode.getFullClassName().contains("de.unihamburg"))
            {
                return false;
            }
        }
        return true;
    }
}
