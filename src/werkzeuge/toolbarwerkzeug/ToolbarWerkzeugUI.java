package werkzeuge.toolbarwerkzeug;

import javax.swing.*;

public class ToolbarWerkzeugUI {

    private JToolBar _toolbar;
    private JButton _dependencyPopupButton;

    public ToolbarWerkzeugUI() {
        _toolbar = new JToolBar();
        _dependencyPopupButton = new JButton();
        _toolbar.add(_dependencyPopupButton);
        _toolbar.setFloatable(false);
    }

    public JToolBar getToolbar() {
        return _toolbar;
    }

    public JButton getDependencyPopupButton() {
        return _dependencyPopupButton;
    }
}
