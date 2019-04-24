package werkzeuge.graphwerkzeug;

import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.view.PopupMode;
import com.intellij.openapi.ui.JBPopupMenu;
import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import service.functional.ChangePropagationProcess;
import valueobjects.Marking;
import valueobjects.RelationshipType;
import werkzeuge.graphwerkzeug.presentation.graphfilter.ClassGraphFilterer;
import werkzeuge.graphwerkzeug.presentation.graphfilter.NodeFilterStrategy;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Creates a PopupMenu on a selected ClassNode in a diagram.
 */
public class GraphPopupMenu extends PopupMode {
    private ImpactAnalysisGraph _impactAnalysisGraph;
    private ChangePropagationProcess _process = ChangePropagationProcess.getInstance();

    public GraphPopupMenu(ImpactAnalysisGraph impactAnalysisGraph) {
        _impactAnalysisGraph = impactAnalysisGraph;
    }

    @Override
    public JPopupMenu getNodePopup(Node node) {

        JBPopupMenu menu = new JBPopupMenu();
        menu.add(new NodeChangeAction(node));
        menu.add(new NodePropagatesAction(node));
        menu.add(new NodeInspectedAction(node));
        menu.add(new JSeparator());
        menu.add(new NodeHideAction(_impactAnalysisGraph, node));

        return menu;
    }

    /**
     * Create a PopupMenu that opens on a clicked edge.
     *
     * @param edge
     * @return
     */
    @Override
    public JPopupMenu getEdgePopup(Edge edge) {

        JBPopupMenu menu = new JBPopupMenu();
        menu.add(new DependencyAction("Implements", edge, RelationshipType.Implements));
        menu.add(new DependencyAction("Extends", edge, RelationshipType.Extends));
        menu.add(new DependencyAction("Dependency", edge, RelationshipType.Dependency));
        menu.add(new DependencyAction("Field Type Many", edge, RelationshipType.FIELD_TYPE_MANY));
        menu.add(new DependencyAction("Filed Type One", edge, RelationshipType.FIELD_TYPE_ONE));
        menu.add(new DependencyAction("Aggregation", edge, RelationshipType.Aggregation));
        menu.add(new DependencyAction("Associaton", edge, RelationshipType.Association));
        menu.add(new DependencyAction("Composition", edge, RelationshipType.Composition));
        menu.add(new DependencyAction("New Expression", edge, RelationshipType.NEW_EXPRESSION));
        menu.setVisible(true);
        menu.show(true);

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
            ProgramEntity programEntity = _impactAnalysisGraph.getGraphBuilder().getNodeObject(_node);
            _process.update(programEntity, Marking.CHANGED);
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
            ProgramEntity programEntity = _impactAnalysisGraph.getGraphBuilder().getNodeObject(_node);
            _process.update(programEntity, Marking.PROPAGATES);
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
            ProgramEntity programEntity = _impactAnalysisGraph.getGraphBuilder().getNodeObject(_node);
            _process.update(programEntity, Marking.INSPECTED);
        }
    }

    class NodeHideAction extends AbstractAction {

        private ImpactAnalysisGraph _impactAnalysisGraph;
        private Node _node;

        public NodeHideAction(ImpactAnalysisGraph impactAnalysisGraph, Node node) {
            super("Hide Node");
            _impactAnalysisGraph = impactAnalysisGraph;
            _node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ClassGraphFilterer filter = new ClassGraphFilterer(_impactAnalysisGraph);
            ProgramEntity programEntity = _impactAnalysisGraph.getGraphBuilder().getNodeObject(_node);
            programEntity.setHide(true);
            filter.setFilterStrategy(new NodeFilterStrategy());
            filter.update(_impactAnalysisGraph.getGraph(), _impactAnalysisGraph.getView());
        }

    }

    class DependencyAction extends AbstractAction {

        private Edge _edge;
        private RelationshipType _relationshipType;

        public DependencyAction(String name, Edge edge, RelationshipType relationship) {
            super(name);
            _edge = edge;
            _relationshipType = relationship;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final ProgramEntityRelationship programEntityRelationship = _impactAnalysisGraph.getGraphBuilder().getEdgeObject(_edge);
            _impactAnalysisGraph.getDataModel().removeEdge(programEntityRelationship);
            programEntityRelationship.setRelationshipType(_relationshipType);
            _impactAnalysisGraph.getDataModel().addEdge(programEntityRelationship);
            _process.updateDependency(programEntityRelationship, _relationshipType);
            _impactAnalysisGraph.updateView();

        }

        @Override
        public void setEnabled(boolean newValue) {
            ProgramEntityRelationship dependency = _impactAnalysisGraph.getProgramEntityRelationship(_edge);
            super.setEnabled(!(dependency.getRelationshipType() == _relationshipType));
        }
    }
}

