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
import org.apache.commons.io.FilenameUtils;
import service.functional.ChangePropagationProcess;
import service.functional.TraceabilityClassNodeService;
import valueobjects.ClassLanguageType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * A tool that
 */
public class TraceabilityChooserWerkzeug {

    private TracebilityChooserWerkzeugUI _ui;
    private TraceabilityClassNodeService _service;
    private Project _project;
    private final ClassNode _classNode;
    private final boolean _isCrossPlatform;
    ChangePropagationProcess _propagationProcessService = ChangePropagationProcess.getInstance();

    /**
     *
     * @param classNode The ClassNode containing the information
     * @param isCrossPlatform Set true for crossPlatform-Traceabilitylins, False for tracelinks of the given platform
     */
    public TraceabilityChooserWerkzeug(final ClassNode classNode, boolean isCrossPlatform)
    {
        _project = ProjectManager.getInstance().getOpenProjects()[0];
        _classNode = classNode;
        _isCrossPlatform = isCrossPlatform;

        _ui = new TracebilityChooserWerkzeugUI();
        _service = new TraceabilityClassNodeService(_project);
        if(!_isCrossPlatform){
            _ui.getSelectButton().setEnabled(false);
        }
        setContentAndShowUI();

    }

    /**
     * Sets the calculated traceabilitylinks in the UI as the Content and shows the
     */
    private void setContentAndShowUI(){
        List<TraceabilityLink> traceabilityLinks = getTraceLinks(_classNode);
        _ui.setContent(traceabilityLinks);
        registerListener();
        show();
    }


    /**
     * Returns the right type of TraceabilityLinks
     * @param classNode The given ClassNode
     * @return A not null List of Traceabilitylinks. Links can be crossplatform traceabilitylinks or from the same platform.
     */
    private List<TraceabilityLink> getTraceLinks(ClassNode classNode) {
        if(_isCrossPlatform)
        {
            return getCrossPlatfromTraceabilityLinks(classNode);
        }
        else{
            return  getPlatfromTraceabilityLinks(classNode);
        }
    }

    /**
     * Finds the TraceabiltyLink representing the given ClassNode. Java -> Swift, Swift -> Java
     * @param classNode The
     * @return Return a List of Traceabilitylinks. List can not be null, but can be empty
     */
    private List<TraceabilityLink> getCrossPlatfromTraceabilityLinks(final ClassNode classNode)
    {
        if(classNode.getClassLanguageType() == ClassLanguageType.Java)
        {
            return _service.getSwiftTraceabilityLinks(classNode);
        }
        if(classNode.getClassLanguageType() ==  ClassLanguageType.Swift)
        {
            return _service.getJavaTraceabilityLinks(classNode);
        }
        return Collections.emptyList();
    }

    /**
     * Finds the TraceabiltyLink representing the given ClassNode. Java -> Java, Swift -> Swift
     * @param classNode The
     * @return Return a List of Traceabilitylinks. List can not be null, but can be empty
     */
    private List<TraceabilityLink> getPlatfromTraceabilityLinks(final ClassNode classNode)
    {
        if(classNode.getClassLanguageType() == ClassLanguageType.Java)
        {
            return _service.getJavaTraceabilityLinks(classNode);
        }
        if(classNode.getClassLanguageType() ==  ClassLanguageType.Swift)
        {
            return _service.getSwiftTraceabilityLinks(classNode);
        }
        return Collections.emptyList();
    }

    /**
     * Registers all Listeners for the UI-Elements
     */
    private void registerListener()
    {
        _ui.getShowCorrespondingButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final String filenameExtension = "swift";
                TraceabilityLink link = _ui.getTracebilityTableModel().getTraceabilityLink(_ui.getJBTable().getSelectedRow());
                TraceabilityPointer target = link.getTarget();
                String targetExtension = FilenameUtils.getExtension(target.getSourceFilePath());
                if(targetExtension.equals(filenameExtension))
                {
                    openXCodeAtLine(target.getSourceFilePath(), target.getStartLine());
                }
                VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(target.getSourceFilePath());
                new OpenFileDescriptor(_project, virtualFile).navigate(true);
            }
        });
        _ui.getSelectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TraceabilityLink link = _ui.getTracebilityTableModel().getTraceabilityLink(_ui.getJBTable().getSelectedRow());
                link.setSource(new TypePointer());
                if(_classNode.getClassLanguageType() == ClassLanguageType.Java)
                {
                    _propagationProcessService.addTraceabilityLinkJavaSource(_classNode, link);
                }
                if(_classNode.getClassLanguageType() == ClassLanguageType.Swift)
                {
                    _propagationProcessService.addTraceabilityLinkSwiftSource(_classNode, link);
                }
                _ui.hide();
            }
        });
    }

    /**
     * shows the window.
     */
    public void show()
    {
        _ui.show();
    }


    public static void openXCodeAtLine(String path, int lineNumber) {

        Runtime runtime = Runtime.getRuntime();
        String appleScript = "tell application \"Xcode\"\n" +
                "\topen \"" + path + "\"\n" +
                "\tactivate\n" +
                "\ttell application \"System Events\"\n" +
                "\t\ttell process \"Xcode\"\n" +
                "\t\t\tkeystroke \"l\" using command down\n" +
                "\t\t\trepeat until window \"Open Quickly\" exists\n" +
                "\t\t\tend repeat\n" +
                "\t\t\tset value of text field 1 of window \"Open Quickly\" to \"" + lineNumber + "\"\n" +
                "\t\t\t--set winstuff to entire contents of front window\n" +
                "\t\t\t--return winstuff -- comment this out too to get just menustuff\n" +
                "\t\t\tkeystroke return\n" +
                "\t\tend tell\n" +
                "\tend tell\n" +
                "end tell";
        String[] args = {"osascript", "-e", appleScript};
        try {
            runtime.exec(args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
