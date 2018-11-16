package werkzeuge.graphwerkzeug.model;


import Utils.HashUtils;
import werkzeuge.graphwerkzeug.presentation.RelationshipType;

public class ClassGraphEdge {
    private ClassGraphNode _dependentNode;
    private ClassGraphNode _independentNode;
    private RelationshipType _relationshipType;

    public ClassGraphEdge(final ClassGraphNode dependentNode, final ClassGraphNode independentNode, final RelationshipType relationshipType)
    {
        _dependentNode = dependentNode;
        _independentNode = independentNode;
        _relationshipType = relationshipType;
    }

    public ClassGraphNode getDependentNode() {
        return _dependentNode;
    }

    public ClassGraphNode getIndependentNode() {
        return _independentNode;
    }

    public RelationshipType getRelationshipType() {
        return _relationshipType;
    }

    @Override
    public int hashCode() {
        int hash = 37;
        hash = HashUtils.calcHashCode(hash, _independentNode.hashCode());
        hash = HashUtils.calcHashCode(hash, _dependentNode.hashCode());
        hash = HashUtils.calcHashCode(hash, _relationshipType);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if(obj == null)
            return false;
        if(this == obj)
            return true;
        if(this.getClass() != obj.getClass())
            return false;
        final ClassGraphEdge otherClassNodeFachwert = (ClassGraphEdge) obj;
        return  this._independentNode.equals(otherClassNodeFachwert.getIndependentNode()) &&
                this._dependentNode.equals(otherClassNodeFachwert.getDependentNode()) &&
                this._relationshipType == otherClassNodeFachwert.getRelationshipType();
    }

    @Override
    public String toString() {
        return _dependentNode.toString() + " --" + _relationshipType.toString() + "-> " + _independentNode.toString();
    }
}
