package werkzeuge.initialcontextwerkzeug;

import service.ChangePropagationProcess;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import materials.ClassNode;
import materials.SwiftClassNode;
import javafx.collections.SetChangeListener;

import javax.swing.*;

public class InitialContextWerkzeug {

    private InitialContextWerkzeugUI _ui;
    private ChangePropagationProcess _cpProcess;
    private Project _project;

    public InitialContextWerkzeug()
    {
        _ui = new InitialContextWerkzeugUI();
        _ui.setLabelText("Initial Context");
        _ui.getPanel().add(createAndRegisterToolbar());
        _cpProcess = ChangePropagationProcess.getInstance();
        registerUIActions();
    }

    private void registerUIActions() {
       _cpProcess.getInitalChangedClasses().addListener(new SetChangeListener<ClassNode>() {
           @Override
           public void onChanged(Change<? extends ClassNode> change) {
               if(change.wasAdded())
               {
                   addEntry(change.getElementAdded());
               }
               if(change.wasRemoved())
               {
                   //addEntry(change.getElementRemoved());
               }

           }
       });
    }

    private JPanel createAndRegisterToolbar()
    {
        return _ui.getToolbarDecorator().setAddAction(new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                _cpProcess.change(new SwiftClassNode("C"));
            }
        }).disableUpAction().disableDownAction().createPanel();
    }

    private void addEntry(final ClassNode initialClassNode)
    {
        _ui.getModel().addEntry(initialClassNode);

    }
    private void removeEntry(final ClassNode initialClassNode)
    {
        _ui.getModel().removeEntry(initialClassNode);
    }
    public JPanel getPanel()
    {
        return _ui.getPanel();
    }

    public void setProject(Project project) {

        _project = project;
    }
}
