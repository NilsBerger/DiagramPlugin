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
     * @param horisontal orientation
     * @return created layouter
     */
    public static Layouter createLayouter(boolean horisontal) {
        HierarchicGroupLayouter layouter = GraphManager.getGraphManager().createHierarchicGroupLayouter();
        layouter.setLayoutOrientation(horisontal ? LayoutOrientation.LEFT_TO_RIGHT : LayoutOrientation.TOP_TO_BOTTOM);
        setLayoutOrientation(layouter, horisontal);
        return layouter;
    }

    public static void setLayoutOrientation(HierarchicLayouter layouter, boolean horisontal) {
        layouter.setLayoutOrientation(horisontal ? LayoutOrientation.LEFT_TO_RIGHT : LayoutOrientation.TOP_TO_BOTTOM);
    }
}
