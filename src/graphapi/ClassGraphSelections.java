package graphapi;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.graph.base.DataProvider;
import com.intellij.openapi.graph.builder.actions.AbstractGraphToggleAction;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.openapi.graph.view.Graph2DSelectionEvent;
import com.intellij.openapi.graph.view.Selections;
import com.intellij.openapi.graph.view.Graph2DSelectionListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ClassGraphSelections extends AbstractGraphToggleAction {


    public ClassGraphSelections(Icon icon) {
        super(icon);
    }

    @Override
    protected boolean isSelected(Graph2D graph2D, Project project, AnActionEvent anActionEvent) {
        return false;
    }

    @Override
    protected void setSelected(Graph2D graph2D, boolean b, Project project, AnActionEvent anActionEvent) {

    }

    @Override
    protected String getText(@NotNull Graph2D graph2D) {
        return null;
    }
}
