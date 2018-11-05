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

package werkzeuge.tracebilitychooserwerkzeug;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityPointer;
import material.ClassNodeMaterial;
import material.JavaClassNodeMaterial;
import material.SwiftClassNodeMaterial;
import service.TraceabilityClassNodeService;
import sun.text.resources.th.BreakIteratorInfo_th;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

public class TracebilityChooserWerkzeug {

    private TracebilityChooserWerkzeugUI _ui;
    private TraceabilityClassNodeService _service;
    private Project _project;
    private final ClassNodeMaterial _classNodeMaterial;

    public TracebilityChooserWerkzeug(final ClassNodeMaterial classNodeMaterial)
    {
        _project = ProjectManager.getInstance().getOpenProjects()[0];
        _classNodeMaterial = classNodeMaterial;
        _ui = new TracebilityChooserWerkzeugUI();
        _service = new TraceabilityClassNodeService(_project);
        List<TraceabilityLink> traceabilityLinks = getTraceabilityLinks(_classNodeMaterial);
        _ui.setContent(traceabilityLinks);
        registerListener();
        show();
    }

    private List<TraceabilityLink> getTraceabilityLinks(final ClassNodeMaterial classNodeMaterial)
    {
        if(classNodeMaterial instanceof JavaClassNodeMaterial)
        {
            return _service.getJavaTracebiliityLinksForJavaClassNode((JavaClassNodeMaterial) classNodeMaterial);
        }
        if(classNodeMaterial instanceof SwiftClassNodeMaterial)
        {
            return _service.getSwiftTracebiliityLinksForSwiftClassNode((SwiftClassNodeMaterial) classNodeMaterial);
        }
        return Collections.emptyList();
    }

    public void show()
    {
        _ui.show();
    }
    private void registerListener()
    {
        _ui.getJBList().addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TraceabilityLink link = _ui.getJBList().getSelectedValue();
                TraceabilityPointer target = link.getTarget();
                VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(target.getSourceFilePath());
                new OpenFileDescriptor(_project,virtualFile).navigate(true);

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }
}