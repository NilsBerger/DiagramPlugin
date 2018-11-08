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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TypePointer;
import materials.ClassNode;
import materials.JavaClassNode;
import materials.SwiftClassNode;
import service.ChangePropagationProcess;
import service.TraceabilityClassNodeService;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

public class CorrespondingTraceabilityChooserWerkzeug{

    private TracebilityChooserWerkzeugUI _ui;
    private TraceabilityClassNodeService traceabilityClassNodeService;
    private ChangePropagationProcess _propagationProcessService;
    private Project _project;
    private final ClassNode _classNode;

    public CorrespondingTraceabilityChooserWerkzeug(final ClassNode classNode)
    {
        _propagationProcessService = ChangePropagationProcess.getInstance();
        _project = ProjectManager.getInstance().getOpenProjects()[0];
        _classNode = classNode;
        _ui = new TracebilityChooserWerkzeugUI();
        traceabilityClassNodeService = new TraceabilityClassNodeService(_project);
        List<TraceabilityLink> traceabilityLinks = getTraceabilityLinks(_classNode);
        _ui.setContent(traceabilityLinks);
        registerListener();
        show();
    }

    private List<TraceabilityLink> getTraceabilityLinks(final ClassNode classNode)
    {
        if(classNode instanceof JavaClassNode)
        {
            return traceabilityClassNodeService.getSwiftTracebiliityLinksForJavaClassNode((JavaClassNode) _classNode);
        }
        if(classNode instanceof SwiftClassNode)
        {
            return traceabilityClassNodeService.getJavaTracebiliityLinksForSwiftClassNode((SwiftClassNode) classNode);
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
                link.setSource(new TypePointer());
                if(_classNode instanceof JavaClassNode)
                {
                    _propagationProcessService.addTraceabilityLinkJavaSource((JavaClassNode) _classNode, link);
                }
                if(_classNode instanceof SwiftClassNode)
                {
                    _propagationProcessService.addTraceabilityLinkSwiftSource((SwiftClassNode) _classNode, link);
                }

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
