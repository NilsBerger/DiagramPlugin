package service;

import materials.ClassNode;
import materials.SwiftClassNode;

public class ClassNodeFilter {
    private ClassNodeFilter(){}

    public static boolean isClassNodeFromAPI(ClassNode classNode)
    {
        if(classNode instanceof SwiftClassNode)
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
