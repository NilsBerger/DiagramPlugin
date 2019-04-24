package werkzeuge.graphwerkzeug.presentation.graphfilter;

import materials.ProgramEntity;
import valueobjects.Language;

public final class APINodeFilterStrategy implements FilterStrategy{
    @Override
    public boolean filterNode(ProgramEntity programEntity) {
        if (programEntity.getLanguage() == Language.Java && !programEntity.getFullEntityName().contains("de.unihamburg")) {
            return true;
        }
        return programEntity.isHidden();
    }

    @Override
    public String toString() {
        return "APINodeFilter";
    }
}
