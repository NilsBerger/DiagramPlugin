package werkzeuge.traceabilitywerkzeug;

import com.intellij.ui.components.JBList;
import javafx.collections.SetChangeListener;
import materials.TraceLinkDependencyMaterial;
import service.ChangePropagationProcessService;

import javax.swing.*;

public class TraceabilityWerkzeug
{

    private TraceabilityWerkzeugUI _ui;
    private ChangePropagationProcessService _changePropagationProcessService;

    public TraceabilityWerkzeug()
    {
        _ui = new TraceabilityWerkzeugUI();
        _ui.setText("Traceability Links");
    }

    private void registerTraceLinkListener()
    {
        _changePropagationProcessService.getTraceLinkDepenendecySet().addListener(new SetChangeListener<TraceLinkDependencyMaterial>() {
            @Override
            public void onChanged(Change<? extends TraceLinkDependencyMaterial> change) {
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
    public void setChangePropagationProcessService(final ChangePropagationProcessService service)
    {
        _changePropagationProcessService = service;
        registerTraceLinkListener();
    }


    public JPanel getPanel()
    {
        return _ui.getPanel();
    }
    public JBList<TraceLinkDependencyMaterial> getTraceablilityList()
    {
        return  _ui.getTracebilityList();
    }
}


