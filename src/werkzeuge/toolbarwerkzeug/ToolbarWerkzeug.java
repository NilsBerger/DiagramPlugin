package werkzeuge.toolbarwerkzeug;

import werkzeuge.toolbarwerkzeug.DependencyDisplayWerkzeug.DependencyDisplayWerkzeug;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolbarWerkzeug {

    private final DependencyDisplayWerkzeug _dependencyDisplayWerkzeug;

    private ToolbarWerkzeugUI _ui;

    public ToolbarWerkzeug() {
        _dependencyDisplayWerkzeug = new DependencyDisplayWerkzeug();

        this._ui = new ToolbarWerkzeugUI();
        _ui.getDependencyPopupButton().setText("Show Dependencies");

        registerDependencyButtonListerner();

    }

    private void registerDependencyButtonListerner() {
        _ui.getDependencyPopupButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _dependencyDisplayWerkzeug.show();
            }
        });
    }

    public JToolBar getToolbar() {
        return _ui.getToolbar();
    }
}
