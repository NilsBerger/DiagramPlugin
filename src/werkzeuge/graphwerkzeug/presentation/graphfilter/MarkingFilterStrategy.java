package werkzeuge.graphwerkzeug.presentation.graphfilter;

import materials.ProgramEntity;
import valueobjects.Marking;

/**
 * Filters all ClassNodes that do not have the Marking "Changed" and "Propagates", so the diagram stays small.
 */
public final class MarkingFilterStrategy implements FilterStrategy{
    @Override
    public boolean filterNode(ProgramEntity programEntity) {
        return programEntity.getMarking() == Marking.CHANGED || programEntity.getMarking() == Marking.PROPAGATES;
    }

    @Override
    public String toString() {
        return "MarkingFilterStrategy";
    }
}
