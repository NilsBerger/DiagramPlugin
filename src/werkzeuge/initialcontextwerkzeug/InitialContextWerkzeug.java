/*
 * Copyright 1998-2018 Konstantin Bulenkov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package werkzeuge.initialcontextwerkzeug;

import service.ChangePropagationProcessService;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import materials.ClassNodeMaterial;
import materials.SwiftClassNodeMaterial;
import javafx.collections.SetChangeListener;

import javax.swing.*;

public class InitialContextWerkzeug {

    private InitialContextWerkzeugUI _ui;
    private ChangePropagationProcessService _cpProcess;
    private Project _project;

    public InitialContextWerkzeug()
    {
        _ui = new InitialContextWerkzeugUI();
        _ui.setLabelText("Initial Context");
        _ui.getPanel().add(createAndRegisterToolbar());
        _cpProcess = ChangePropagationProcessService.getInstance();
        registerUIActions();
    }

    private void registerUIActions() {
       _cpProcess.getInitalChangedClasses().addListener(new SetChangeListener<ClassNodeMaterial>() {
           @Override
           public void onChanged(Change<? extends ClassNodeMaterial> change) {
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
                _cpProcess.change(new SwiftClassNodeMaterial("C"));
            }
        }).disableUpAction().disableDownAction().createPanel();
    }

    private void addEntry(final ClassNodeMaterial initialClassNode)
    {
        _ui.getModel().addEntry(initialClassNode);

    }
    private void removeEntry(final ClassNodeMaterial initialClassNode)
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
