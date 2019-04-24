package werkzeuge.traceabilitydisplaywerkzeug;

import com.intellij.ui.table.JBTable;
import javafx.collections.SetChangeListener;
import materials.ProgramEntityRelationship;
import materials.TraceLinkProgramEntityAssociation;
import service.functional.ChangePropagationProcess;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TraceabilityWerkzeug {

    private TraceabilityWerkzeugUI _ui;

    private ChangePropagationProcess _changePropagationProcess;

    public TraceabilityWerkzeug() {
        _ui = new TraceabilityWerkzeugUI();
        _ui.setText("Traceability Links");
        registerTextfieldListeners();
    }

    private void registerTraceLinkListener() {
        _changePropagationProcess.getTraceLinkDepenendecySet().addListener(new SetChangeListener<ProgramEntityRelationship>() {
            @Override
            public void onChanged(Change change) {
                if (change.wasAdded()) {
                    _ui.getModel().addEntry((TraceLinkProgramEntityAssociation) change.getElementAdded());
                }
            }
        });
    }

    public void registerTextfieldListeners() {
        _ui.getJBTextfield().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _ui.getContainsFilter().setFilterValue(_ui.getJBTextfield().getText());
                _ui.getRowSorter().sort();
            }
        });
    }

    public void setChangePropagationProcessService(final ChangePropagationProcess service) {
        _changePropagationProcess = service;
        registerTraceLinkListener();
    }

    public JPanel getPanel() {
        return _ui.getPanel();
    }

    public JBTable getTraceablilityTable() {
        return _ui.getTracebilityTable();
    }

    public TraceabilityTableModel getModel() {
        return _ui.getModel();
    }
}


