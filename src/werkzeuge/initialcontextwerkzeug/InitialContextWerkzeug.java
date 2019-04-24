package werkzeuge.initialcontextwerkzeug;

import javafx.collections.SetChangeListener;
import materials.ProgramEntity;
import service.functional.ChangePropagationProcess;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialContextWerkzeug{

    private InitialContextWerkzeugUI _ui;
    private ChangePropagationProcess _cpProcess;

    public InitialContextWerkzeug()
    {
        _ui = new InitialContextWerkzeugUI();
        _ui.setLabelText("Java: Suggested Impact Set");
        _cpProcess = ChangePropagationProcess.getInstance();
        registerUIActions();
        registerTextfieldListeners();
    }

    private void registerUIActions() {
        _cpProcess.getAffectedClassesByChange().addListener(new SetChangeListener<ProgramEntity>() {
           @Override
           public void onChanged(Change<? extends ProgramEntity> change) {
               if(change.wasAdded())
               {
                   ProgramEntity programEntity = change.getElementAdded();
                   if (programEntity != null && programEntity.isInitialClass()) {
                       addEntry(change.getElementAdded());
                   }
               }
           }
       });
    }

    public void registerTextfieldListeners()
    {
        _ui.getTextField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _ui.getContainsFilter().setFilterValue(_ui.getTextField().getText());
                _ui.getRowSorter().sort();
            }
        });
    }

    private void addEntry(final ProgramEntity initialProgramEntity)
    {
        _ui.getModel().addEntry(initialProgramEntity);

    }
    public JPanel getPanel()
    {
        return _ui.getPanel();
    }

}
