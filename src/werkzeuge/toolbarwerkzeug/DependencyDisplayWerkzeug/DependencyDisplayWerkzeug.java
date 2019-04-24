package werkzeuge.toolbarwerkzeug.DependencyDisplayWerkzeug;


import service.functional.ChangePropagationProcess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DependencyDisplayWerkzeug {

    private final DependencyDisplayWerkzeugUI _ui;
    private ChangePropagationProcess _cpProcess = ChangePropagationProcess.getInstance();

    public DependencyDisplayWerkzeug() {
        _ui = new DependencyDisplayWerkzeugUI();
        registerTextfieldListeners();
    }

    public void loadData() {
        _ui.getModel().setNewContent(_cpProcess.getDependenciesOfModel());
    }

    public void registerTextfieldListeners() {
        _ui.getTextField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _ui.getContainsFilter().setFilterValue(_ui.getTextField().getText());
                _ui.getRowSorter().sort();
            }
        });
    }

    public void show() {
        loadData();
        _ui.show();
    }
}
