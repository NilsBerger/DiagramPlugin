package werkzeuge.graphwerkzeug.presentation.graphfilter;

import materials.ProgramEntity;

/**
 * This FilterStrategy filters  Java-ClassNodes that are classes form libaries and ClassNodes that the user wants to hide.
 */
public final class NodeFilterStrategy implements FilterStrategy {
    @Override
    public boolean filterNode(ProgramEntity programEntity) {
        return programEntity.isHidden();
    }

    @Override
    public String toString() {
        return "NodeFilter";
    }
}
