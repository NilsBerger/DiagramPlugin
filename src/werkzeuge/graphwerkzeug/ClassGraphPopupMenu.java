package werkzeuge.graphwerkzeug;

import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.view.PopupMode;
import com.intellij.openapi.ui.JBPopupMenu;
import materials.ClassNode;
import service.ChangePropagationProcess;
import valueobjects.Marking;
import werkzeuge.graphwerkzeug.model.ClassGraphFilterer;
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
        menu.add(new JSeparator());
        menu.add(new NodeHideAction(_classGraph, node));

        return menu;
    }

    @Override
    public JPopupMenu getEdgePopup(Edge edge) {
        JBPopupMenu menu = new JBPopupMenu();
        menu.add(new EdgeChangeAction(edge ));


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
            _process.update(classNode, Marking.CHANGED);
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
            _process.update(classNode, Marking.PROPAGATES);
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
            _process.update(classNode, Marking.INSPECTED);
        }
    }

    class NodeHideAction extends AbstractAction{

        private ClassGraph _classGraph;
        private Node _node;

        public NodeHideAction(ClassGraph classGraph, Node node)
        {
            super("Hide Node");
            _classGraph = classGraph;
            _node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ClassGraphFilterer filter = new ClassGraphFilterer(_classGraph);
            ClassNode classNode = _classGraph.getGraphBuilder().getNodeObject(_node);
            classNode.setHide(true);
            filter.setFilterOn(true);
            filter.update(_classGraph.getGraph(), _classGraph.getView());
        }

    }

    class EdgeChangeAction extends AbstractAction{

        private Edge _edge;

        public EdgeChangeAction(Edge edge)
        {
            super("Change Edge");
            _edge = edge;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
}

