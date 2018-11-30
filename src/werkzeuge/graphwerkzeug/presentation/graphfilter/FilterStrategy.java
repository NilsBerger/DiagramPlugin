package werkzeuge.graphwerkzeug.presentation.graphfilter;

import materials.ClassNode;

public interface FilterStrategy {
    boolean filterNode(final ClassNode Node);
}
