package werkzeuge.graphwerkzeug.presentation.graphfilter;

import materials.ClassNode;
import valueobjects.Marking;

/**
 * Filters all ClassNodes that do not have the Marking "Changed" and "Propagates", so the diagram stays small.
 */
public final class MarkingFilterStrategy implements FilterStrategy{
    @Override
    public boolean filterNode(ClassNode classNode) {
        if(classNode.getMarking() == Marking.CHANGED || classNode.getMarking() == Marking.PROPAGATES)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MarkingFilterStrategy";
    }
}
