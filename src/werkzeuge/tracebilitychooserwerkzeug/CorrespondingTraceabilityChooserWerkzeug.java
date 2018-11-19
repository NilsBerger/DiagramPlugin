package werkzeuge.tracebilitychooserwerkzeug;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TypePointer;
import materials.ClassNode;
import service.ChangePropagationProcess;
import service.TraceabilityClassNodeService;
import valueobjects.ClassNodeType;

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
        if(classNode.getType() == ClassNodeType.Java)
        {
            return traceabilityClassNodeService.getSwiftTracebiliityLinksForJavaClassNode(_classNode);
        }
        if(classNode.getType() == ClassNodeType.Swift)
        {
            return traceabilityClassNodeService.getJavaTracebiliityLinksForSwiftClassNode(classNode);
        }
        return Collections.emptyList();
    }

    public void show()
    {
        _ui.show();
    }
    private void registerListener()
    {
        _ui.getJBTable().addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
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
