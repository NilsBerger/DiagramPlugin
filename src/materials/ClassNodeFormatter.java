package materials;

import Utils.StringUtils;

/**
 * Wrapper for a ClassNode to get the SimpleName of a ClassNode
 */
class ClassNodeFormatter {
    private final ClassNode _classNode;
    public ClassNodeFormatter(final ClassNode classNode)
    {
        this._classNode = classNode;
    }

    @Override
    public String toString() {
       return StringUtils.sanitizeStringForSimpleName(_classNode.getFullClassName());
    }
}
