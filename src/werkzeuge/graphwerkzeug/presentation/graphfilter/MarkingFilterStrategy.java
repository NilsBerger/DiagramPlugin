package werkzeuge.graphwerkzeug.presentation.graphfilter;

import materials.ProgramEntity;
import valueobjects.Marking;

/**
 * Filters all ClassNodes that do not have the Marking "Changed" and "Propagates", so the diagram stays small.
 */
public final class MarkingFilterStrategy implements FilterStrategy{
    private final Marking _marking;

    public MarkingFilterStrategy(Marking marking) {
        _marking = marking;
    }

    @Override
    public boolean filterNode(ProgramEntity programEntity) {
        return programEntity.getMarking() == _marking;
    }

    @Override
    public String toString() {
        return "MarkingFilterStrategy";
    }
}
