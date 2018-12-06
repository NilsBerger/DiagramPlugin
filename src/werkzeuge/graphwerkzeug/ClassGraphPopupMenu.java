package werkzeuge.graphwerkzeug;

import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.view.PopupMode;
import com.intellij.openapi.ui.JBPopupMenu;
import materials.ClassDependency;
import materials.ClassNode;
import service.ChangePropagationProcess;
import valueobjects.Marking;
import valueobjects.RelationshipType;
import werkzeuge.graphwerkzeug.presentation.graphfilter.NodeFilterStrategy;
import werkzeuge.graphwerkzeug.presentation.graphfilter.ClassGraphFilterer;
import werkzeuge.graphwerkzeug.presentation.ClassGraph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * Creates a PopupMenu on a selected ClassNode in a diagram.
 */
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

    /**
     * Create a PopupMenu that opens on a clicked edge.
     * @param edge
     * @return
     */
    @Override
    public JPopupMenu getEdgePopup(Edge edge) {
        final ClassDependency classGraphEdge = _classGraph.getClassGraphEdge(edge);
        if(classGraphEdge != null && classGraphEdge.getRelationshipType()!= RelationshipType.Traceability_Association)
        {
            return null;
        }
        JBPopupMenu menu = new JBPopupMenu();
        menu.add(new DependencyAction("Implements", edge, RelationshipType.Implements));
        menu.add(new DependencyAction("Extends", edge, RelationshipType.Extends));
        menu.add(new DependencyAction("Dependency", edge, RelationshipType.Dependency));
        menu.setVisible(true);
        menu.show();

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
            filter.setFilterStrategy(new NodeFilterStrategy());
            filter.update(_classGraph.getGraph(), _classGraph.getView());
        }

    }

    class DependencyAction extends AbstractAction{

        private Edge _edge;
        private RelationshipType _relationshipType;

        public DependencyAction(String name, Edge edge, RelationshipType relationship)
        {
            super(name);
            _edge = edge;
            _relationshipType = relationship;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final ClassDependency classDependency = _classGraph.getGraphBuilder().getEdgeObject(_edge);
            _classGraph.getDataModel().removeEdge(classDependency);
            classDependency.setRelationshipType(_relationshipType);
            _classGraph.getDataModel().addEdge(classDependency);
            _process.updateDependency(classDependency, _relationshipType );
        }

        @Override
        public void setEnabled(boolean newValue) {
            ClassDependency dependency = _classGraph.getClassGraphEdge(_edge);
            super.setEnabled(!(dependency.getRelationshipType() == _relationshipType));
        }
    }
}

