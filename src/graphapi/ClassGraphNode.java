package graphapi;

import Utils.HashUtils;
import materials.ClassNode;
import materials.JavaClassNode;
import materials.SwiftClassNode;

public class ClassGraphNode {
    private ClassNode _classNode;
    private Type _type;

    public enum Type
    {
       DEFAULT, Java, Swift
    }

    public ClassGraphNode(final ClassNode classNode)
    {
        _classNode = classNode;
        _type = calaculateType(classNode);
    }

    private Type calaculateType(final ClassNode classNode)
    {
        if(classNode instanceof JavaClassNode)
        {
            return Type.Java;
        }
        if(classNode instanceof SwiftClassNode)
        {
            return Type.Swift;
        }
            return Type.DEFAULT;
    }

    public ClassNode getClassNode() {
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
