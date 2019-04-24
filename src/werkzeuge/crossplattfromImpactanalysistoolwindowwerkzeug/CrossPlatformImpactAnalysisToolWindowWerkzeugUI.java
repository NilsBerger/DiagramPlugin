package werkzeuge.crossplattfromImpactanalysistoolwindowwerkzeug;


import javax.swing.*;

public class CrossPlatformImpactAnalysisToolWindowWerkzeugUI {

    private final JComponent _graphWindowWerkzeugUI;
    private final JPanel _toolWindowWerkzeugUI;
    private final JSplitPane _pane;

    public CrossPlatformImpactAnalysisToolWindowWerkzeugUI(JComponent graphWindowWerkzeugUI, JPanel toolWindowWerkzeugUI) {
        _graphWindowWerkzeugUI = graphWindowWerkzeugUI;
        _toolWindowWerkzeugUI = toolWindowWerkzeugUI;
        _pane = new JSplitPane();

        setUpPanel();
    }

    private void setUpPanel() {
        _pane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        _pane.setTopComponent(_graphWindowWerkzeugUI);
        _pane.setBottomComponent(_toolWindowWerkzeugUI);

    }

    public JComponent getUI() {
        return _pane;
    }

}
