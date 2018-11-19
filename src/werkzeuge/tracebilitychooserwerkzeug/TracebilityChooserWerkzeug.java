package werkzeuge.tracebilitychooserwerkzeug;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityPointer;
import de.unihamburg.masterprojekt2016.traceability.TypePointer;
import materials.ClassNode;
import service.ChangePropagationProcess;
import service.TraceabilityClassNodeService;
import valueobjects.ClassNodeType;

import javax.swing.event.MouseInputListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

public class TracebilityChooserWerkzeug {

    private TracebilityChooserWerkzeugUI _ui;
    private TraceabilityClassNodeService _service;
    private Project _project;
    private final ClassNode _classNode;
    ChangePropagationProcess _propagationProcessService = ChangePropagationProcess.getInstance();

    public TracebilityChooserWerkzeug(final ClassNode classNode)
    {
        _project = ProjectManager.getInstance().getOpenProjects()[0];
        _classNode = classNode;
        _ui = new TracebilityChooserWerkzeugUI();
        _service = new TraceabilityClassNodeService(_project);
        List<TraceabilityLink> traceabilityLinks = getTraceabilityLinks(_classNode);
        _ui.setContent(traceabilityLinks);
        registerListener();
        show();
    }

    private List<TraceabilityLink> getTraceabilityLinks(final ClassNode classNode)
    {
        if(classNode.getType() == ClassNodeType.Java)
        {
            return _service.getJavaTracebiliityLinksForJavaClassNode(classNode);
        }
        if(classNode.getType() ==  ClassNodeType.Swift)
        {
            return _service.getSwiftTracebiliityLinksForSwiftClassNode(classNode);
        }
        return Collections.emptyList();
    }


    public void show()
    {
        _ui.show();
    }
    private void registerListener()
    {
        _ui.getShowCorrespondingButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TraceabilityLink link = _ui.getTracebilityTableModel().getTraceabilityLink(_ui.getJBTable().getSelectedRow());
                TraceabilityPointer target = link.getTarget();
                VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(target.getSourceFilePath());
                new OpenFileDescriptor(_project, virtualFile).navigate(true);
            }
        });
        _ui.getSelectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TraceabilityLink link = _ui.getTracebilityTableModel().getTraceabilityLink(_ui.getJBTable().getSelectedRow());
                link.setSource(new TypePointer());
                if(_classNode.getType() == ClassNodeType.Java)
                {
                    _propagationProcessService.addTraceabilityLinkJavaSource(_classNode, link);
                }
                if(_classNode.getType() == ClassNodeType.Swift)
                {
                    _propagationProcessService.addTraceabilityLinkSwiftSource(_classNode, link);
                }
            }
        });
    }
}
