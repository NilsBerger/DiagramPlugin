package werkzeuge.traceabilitywerkzeug;

import com.intellij.ui.components.JBList;
import javafx.collections.SetChangeListener;
import materials.TraceLinkDependency;
import service.ChangePropagationProcess;

import javax.swing.*;

public class TraceabilityWerkzeug
{

    private TraceabilityWerkzeugUI _ui;
    private ChangePropagationProcess _changePropagationProcess;

    public TraceabilityWerkzeug()
    {
        _ui = new TraceabilityWerkzeugUI();
        _ui.setText("Traceability Links");
    }

    private void registerTraceLinkListener()
    {
        _changePropagationProcess.getTraceLinkDepenendecySet().addListener(new SetChangeListener<TraceLinkDependency>() {
            @Override
            public void onChanged(Change<? extends TraceLinkDependency> change) {
                if(change.wasAdded())
                {
                    _ui.getModel().addEntry(change.getElementAdded());
                }
                if(change.wasRemoved())
                {
                    _ui.getModel().removeEntry(change.getElementRemoved());
                }
            }
        });
    }
    public void setChangePropagationProcessService(final ChangePropagationProcess service)
    {
        _changePropagationProcess = service;
        registerTraceLinkListener();
    }


    public JPanel getPanel()
    {
        return _ui.getPanel();
    }
    public JBList<TraceLinkDependency> getTraceablilityList()
    {
        return  _ui.getTracebilityList();
    }
}


