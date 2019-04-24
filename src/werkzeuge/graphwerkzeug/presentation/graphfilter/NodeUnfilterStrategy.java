package werkzeuge.graphwerkzeug.presentation.graphfilter;

import materials.ProgramEntity;

public final class NodeUnfilterStrategy implements FilterStrategy{

    @Override
    public boolean filterNode(final ProgramEntity Node) {
        return false;
    }

    @Override
    public String toString() {
        return "NodeUnfilterStrategy";
    }
}
