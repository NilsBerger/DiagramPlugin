package werkzeuge.initialcontextwerkzeug;

import service.functional.ChangePropagationProcess;
import com.intellij.openapi.project.Project;
import materials.ClassNode;
import javafx.collections.SetChangeListener;
import service.functional.GraphChangeListener;

import javax.swing.*;

public class InitialContextWerkzeug{

    private InitialContextWerkzeugUI _ui;
    private ChangePropagationProcess _cpProcess;

    public InitialContextWerkzeug()
    {
        _ui = new InitialContextWerkzeugUI();
        _ui.setLabelText("Initial Context");
        _cpProcess = ChangePropagationProcess.getInstance();
        registerUIActions();
    }

    private void registerUIActions() {
       _cpProcess.getAffectedClassesByChange().addListener(new SetChangeListener<ClassNode>() {
           @Override
           public void onChanged(Change<? extends ClassNode> change) {
               if(change.wasAdded())
               {
                   ClassNode classNode = change.getElementAdded();
                   if(classNode != null && classNode.isInitialClass()){
                       addEntry(change.getElementAdded());
                   }
               }
           }
       });
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

}
