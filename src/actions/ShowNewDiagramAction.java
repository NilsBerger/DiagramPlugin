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


package actions;
import com.intellij.diagram.DiagramProvider;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.uml.core.actions.ShowDiagram;

import java.util.Collections;

public class ShowNewDiagramAction extends ShowDiagram
{
    public static final String USAGE_KEY = "actions.show.diagram";
    public static final String DESCRIPTION = "Show Change Propagation Diagram";

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        if (project == null) return;

        final ClassDiagramProvider diagramProvider =
                (ClassDiagramProvider) DiagramProvider.findByID(ClassDiagramProvider.ID);

        final Runnable callback = show(new String("f"), diagramProvider,project,null, Collections.EMPTY_LIST);

        if (callback != null) {
            callback.run();
        }

    }

    @Override
    public void update(AnActionEvent e) {
        final Project project = e.getProject();

        e.getPresentation().setEnabledAndVisible(project != null);

        e.getPresentation().setText(DESCRIPTION);
        e.getPresentation().setDescription(DESCRIPTION);
    }
}
