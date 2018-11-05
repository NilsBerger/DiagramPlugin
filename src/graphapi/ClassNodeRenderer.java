package graphapi;

import com.intellij.openapi.graph.builder.GraphBuilder;
import com.intellij.openapi.graph.builder.renderer.BasicGraphNodeRenderer;
import com.intellij.psi.PsiManager;

import javax.swing.*;
import java.awt.*;

public class ClassNodeRenderer extends BasicGraphNodeRenderer {
    public ClassNodeRenderer(GraphBuilder graphBuilder) {
        super(graphBuilder, PsiManager.getInstance(graphBuilder.getProject()).getModificationTracker());
    }

    @Override
    protected Icon getIcon(Object node) {
        return super.getIcon(node);
    }

    @Override
    protected String getNodeName(Object node) {
        return super.getNodeName(node);
    }

    @Override
    protected Color getSelectionColor() {
        return new Color(199,99,199);
    }
}
