package werkzeuge.graphwerkzeug.presentation.graphfilter;

import materials.ProgramEntity;

public interface FilterStrategy {
    boolean filterNode(final ProgramEntity Node);
}
