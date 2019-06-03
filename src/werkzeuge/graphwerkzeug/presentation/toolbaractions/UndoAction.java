package werkzeuge.graphwerkzeug.presentation.toolbaractions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.graph.builder.actions.AbstractGraphAction;
import com.intellij.openapi.graph.view.Graph2D;

public class UndoAction extends AbstractGraphAction {

    public UndoAction() {
        super("Undo", AllIcons.Actions.Undo);
    }

    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Graph2D graph2D) {

    }
}
