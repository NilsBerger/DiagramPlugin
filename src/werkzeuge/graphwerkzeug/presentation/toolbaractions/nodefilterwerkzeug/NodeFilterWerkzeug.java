package werkzeuge.graphwerkzeug.presentation.toolbaractions.nodefilterwerkzeug;

import valueobjects.Marking;
import werkzeuge.graphwerkzeug.ImpactAnalysisGraph;
import werkzeuge.graphwerkzeug.presentation.graphfilter.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NodeFilterWerkzeug {

    private final NodeFilterWerkzeugUI _nodeFilterWerkzeugUI;
    private final ImpactAnalysisGraph _impactAnalysisGraph;
    private final ClassGraphFilterer _filter;

    public NodeFilterWerkzeug(ImpactAnalysisGraph impactAnalysisGraph) {
        _nodeFilterWerkzeugUI = new NodeFilterWerkzeugUI();
        _impactAnalysisGraph = impactAnalysisGraph;
        _filter = new ClassGraphFilterer(_impactAnalysisGraph);

        registerListener();
    }

    private void registerListener() {
        _nodeFilterWerkzeugUI.getDialog().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setFilter();
                System.out.println("Propagates: " + _nodeFilterWerkzeugUI.getPropagatesCheckbox().isSelected());
                System.out.println("Changed: " + _nodeFilterWerkzeugUI.getChangedCheckbox().isSelected());
            }
        });
    }

    private void setFilter() {
        if (_nodeFilterWerkzeugUI.getPropagatesCheckbox().isSelected()) {
            filter(new MarkingFilterStrategy(Marking.PROPAGATES));
        }

        if (_nodeFilterWerkzeugUI.getChangedCheckbox().isSelected()) {
            filter(new MarkingFilterStrategy(Marking.CHANGED));
        }

        if (_nodeFilterWerkzeugUI.getInspectedCheckbox().isSelected()) {
            filter(new MarkingFilterStrategy(Marking.INSPECTED));
        }

        if (_nodeFilterWerkzeugUI.getNextCheckbox().isSelected()) {
            filter(new MarkingFilterStrategy(Marking.NEXT));
        }
        if (_nodeFilterWerkzeugUI.getBlankCheckbox().isSelected()) {
            filter(new MarkingFilterStrategy(Marking.BLANK));
        }
        if (_nodeFilterWerkzeugUI.getApiNodeFilterCheckbox().isSelected()) {
            filter(new APINodeFilterStrategy());
        }
        if (_nodeFilterWerkzeugUI.getHiddenNodeFilterCheckbox().isSelected()) {
            filter(new NodeFilterStrategy());
        }

    }

    private void filter(FilterStrategy filterStrategy) {
        _filter.setFilterStrategy(filterStrategy);
        _filter.update(_impactAnalysisGraph.getGraph(), _impactAnalysisGraph.getView());
    }

    public void openDialog() {
        _nodeFilterWerkzeugUI.openDialog();
    }

}
