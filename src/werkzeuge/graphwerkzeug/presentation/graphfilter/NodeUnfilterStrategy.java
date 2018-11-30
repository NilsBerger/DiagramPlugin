package werkzeuge.graphwerkzeug.presentation.graphfilter;

import materials.ClassNode;

public final class NodeUnfilterStrategy implements FilterStrategy{

    @Override
    public boolean filterNode(final ClassNode Node) {
        return false;
    }

    @Override
    public String toString() {
        return "NodeUnfilterStrategy";
    }
}
