package werkzeuge.graphwerkzeug.util;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.layout.LayoutOrientation;
import com.intellij.openapi.graph.layout.Layouter;
import com.intellij.openapi.graph.layout.hierarchic.HierarchicGroupLayouter;
import com.intellij.openapi.graph.layout.hierarchic.HierarchicLayouter;

public class GraphUtils {

    /**
     * Creates hierarchic layouter with specified orientation
     *
     * @param horizontal orientation
     * @return created layouter
     */
    public static Layouter createLayouter(boolean horizontal) {
        HierarchicGroupLayouter layouter = GraphManager.getGraphManager().createHierarchicGroupLayouter();
        layouter.setLayoutOrientation(LayoutOrientation.TOP_TO_BOTTOM);
        return layouter;
    }

    public static void setLayoutOrientationLeftToRight(HierarchicLayouter layouter, boolean horizontal) {
        layouter.setLayoutOrientation(horizontal ? LayoutOrientation.LEFT_TO_RIGHT : LayoutOrientation.TOP_TO_BOTTOM);
    }
    public static void setLayoutOrientationRight(HierarchicLayouter layouter, boolean horizontal) {
        layouter.setLayoutOrientation(horizontal ? LayoutOrientation.RIGHT_TO_LEFT : LayoutOrientation.TOP_TO_BOTTOM);
    }
}
