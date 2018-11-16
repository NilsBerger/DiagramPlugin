package werkzeuge.graphwerkzeug;

import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.view.PopupMode;
import com.intellij.openapi.ui.JBPopupMenu;
import materials.ClassNode;
import service.ChangePropagationProcess;
import valueobjects.Marking;
import werkzeuge.graphwerkzeug.presentation.ClassGraph;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ClassGraphPopupMenu extends PopupMode {
    private ClassGraph _classGraph;
    private ChangePropagationProcess _process = ChangePropagationProcess.getInstance();

    public ClassGraphPopupMenu(ClassGraph classGraph) {
        _classGraph = classGraph;

    }

    @Override
    public JPopupMenu getNodePopup(Node node) {

        JBPopupMenu menu = new JBPopupMenu();
        menu.add(new NodeChangeAction(node));
        menu.add(new NodePropagatesAction(node));
        menu.add(new NodeInspectedAction(node));

        return menu;
    }


    class NodeChangeAction extends AbstractAction {
        Node _node;

        public NodeChangeAction(Node node) {
            super("Changed");
            _node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ClassNode classNode = _classGraph.getGraphBuilder().getNodeObject(_node);
            _process.updateNeigbbourhood(classNode, Marking.CHANGED);
        }
    }
    class NodePropagatesAction extends AbstractAction {
        Node _node;

        public NodePropagatesAction(Node node) {
            super("Propagates");
            _node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ClassNode classNode = _classGraph.getGraphBuilder().getNodeObject(_node);
            _process.updateNeigbbourhood(classNode, Marking.PROPAGATES);
        }
    }
    class NodeInspectedAction extends AbstractAction {
        Node _node;

        public NodeInspectedAction(Node node) {
            super("Inspected");
            _node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ClassNode classNode = _classGraph.getGraphBuilder().getNodeObject(_node);
            _process.updateNeigbbourhood(classNode, Marking.INSPECTED);
        }
    }
}

