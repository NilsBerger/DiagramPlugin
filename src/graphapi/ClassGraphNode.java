package graphapi;

import Utils.HashUtils;
import materials.ClassNodeMaterial;
import materials.JavaClassNodeMaterial;
import materials.SwiftClassNodeMaterial;

public class ClassGraphNode {
    private ClassNodeMaterial _classNode;
    private Type _type;

    public enum Type
    {
       DEFAULT, Java, Swift
    }

    public ClassGraphNode(final ClassNodeMaterial classNodeMaterial)
    {
        _classNode = classNodeMaterial;
        _type = calaculateType(classNodeMaterial);
    }

    private Type calaculateType(final ClassNodeMaterial classNodeMaterial)
    {
        if(classNodeMaterial instanceof JavaClassNodeMaterial)
        {
            return Type.Java;
        }
        if(classNodeMaterial instanceof SwiftClassNodeMaterial)
        {
            return Type.Swift;
        }
            return Type.DEFAULT;
    }

    public ClassNodeMaterial getClassNode() {
        return _classNode;
    }

    public String getName()
    {
        return _classNode.getSimpleClassName();
    }

    public Type getType() {
        return _type;
    }

    @Override
    public int hashCode() {
        int hash = 37;
        hash = HashUtils.calcHashCode(hash, _classNode.hashCode());
        hash = HashUtils.calcHashCode(hash, _classNode.getClass());
        hash = HashUtils.calcHashCode(hash, _type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(this == obj)
            return true;
        if(this.getClass() != obj.getClass())
            return false;
        final ClassGraphNode otherClassGraphNode = (ClassGraphNode) obj;
        return  this._classNode.equals(otherClassGraphNode.getClassNode()) &&
                this._type == otherClassGraphNode.getType();
    }

    @Override
    public String toString() {
        return _classNode.getSimpleClassName();
    }
}
