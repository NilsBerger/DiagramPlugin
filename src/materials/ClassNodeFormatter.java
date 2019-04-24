package materials;

import Utils.StringUtils;

/**
 * Wrapper for a ClassNode to get the SimpleName of a ClassNode
 */
class ClassNodeFormatter {
    private final ProgramEntity _programEntity;

    ClassNodeFormatter(final ProgramEntity programEntity)
    {
        this._programEntity = programEntity;
    }

    @Override
    public String toString() {
        return StringUtils.sanitizeStringForSimpleName(_programEntity.getFullEntityName());
    }
}
