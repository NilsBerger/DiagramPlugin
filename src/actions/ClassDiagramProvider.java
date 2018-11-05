package actions;


import material.DependencyIF;
import service.ChangePropagationProcessService;
import service.ChangePropagationProcessServiceTest;
import service.DotFileParserService;
import service.RandomChangeAndFixStrategy;
import com.intellij.diagram.*;
import com.intellij.diagram.extras.DiagramExtras;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.ContentFactory;
import material.ClassNodeMaterial;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import werkzeuge.ToolWindowWerkzeug;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Konstantin Bulenkov
 */
public class ClassDiagramProvider extends BaseDiagramProvider<ClassNodeMaterial> {

    public static final String ID = "ClassDiagramProvider";
    private DiagramElementManager _myElementManager = new ClassDiagramElementManager();
    private DiagramVfsResolver<ClassNodeMaterial> myVfsResolver = new ClassDiagramVfsResolver();
    private DiagramExtras<ClassNodeMaterial> myExtras = new ClassDiagramExtras();
    private DiagramColorManager myColorManager = new ClassDiagramColorManager();
    private DiagramDataModel _diagramDataModel;

    private ToolWindowWerkzeug _toolWindowWerkzeug;
    private final ChangePropagationProcessService _cpProcess;

    public ClassDiagramProvider() {

        _cpProcess = ChangePropagationProcessService.getInstance();
        _cpProcess.initialize(ChangePropagationProcessServiceTest.getJavaAndSwiftDependencyList(), new RandomChangeAndFixStrategy());
        _toolWindowWerkzeug = new ToolWindowWerkzeug();
        Set<DependencyIF> dependencyList = new HashSet<>();
        dependencyList.addAll(DotFileParserService.parseJavaDependenciesFromDotFile(DotFileParserService.JAVA_DOT_FILENAME));
        dependencyList.addAll(DotFileParserService.parseSwiftDependenciesFromDotFile(DotFileParserService.DEPCHECK_FILENAME));
        _cpProcess.initialize(dependencyList, new RandomChangeAndFixStrategy());
        //GraphManager manager = GraphManager.getGraphManager();
    }

    @Pattern("[a-zA-Z0-9_-]*")
    @Override
    public String getID() {
        return ID;
    }

    @Override
    public DiagramElementManager getElementManager() {
        return _myElementManager;
    }


    @Override
    public DiagramVfsResolver<ClassNodeMaterial> getVfsResolver() {
        return myVfsResolver;
    }

    @Override
    public String getPresentableName() {
        return "Change Impact";
    }

    @NotNull
    @Override
    public DiagramExtras<ClassNodeMaterial> getExtras() {
        return myExtras;
    }

    @Override
    public DiagramDataModel<ClassNodeMaterial> createDataModel(@NotNull Project project, @Nullable ClassNodeMaterial classNodeMaterial, @Nullable VirtualFile virtualFile, DiagramPresentationModel diagramPresentationModel) {
        ToolWindowManager windowManager = ToolWindowManager.getInstance(project);
        ToolWindow window = windowManager.getToolWindow("Class Dependency Graph");


//        ApplicationManager.getApplication().invokeLater(() -> {
//            final ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
//            ToolWindow toolWindow = toolWindowManager.getToolWindow(ToolWindowWerkzeug.CROSS_PLATFORM_IMPACT_ANALYSIS);
//
//            if (toolWindow == null) {
//                toolWindowManager.registerToolWindow(ToolWindowWerkzeug.CROSS_PLATFORM_IMPACT_ANALYSIS, true, ToolWindowAnchor.BOTTOM);
//                toolWindow = toolWindowManager.getToolWindow(ToolWindowWerkzeug.CROSS_PLATFORM_IMPACT_ANALYSIS);
//                _toolWindowWerkzeug.createToolWindowContent(project, window);
//                toolWindow.getContentManager().addContent(ContentFactory.SERVICE.getInstance().createContent(_toolWindowWerkzeug.getPanel(), "", false));
//                toolWindow.setAvailable(true, null);
//                toolWindow.show(null);
//            }
//
//        });
        _diagramDataModel = new ClassDiagramDataModel(project, _cpProcess);
        return _diagramDataModel;
    }
    public DiagramDataModel getDataModel()
    {
        return _diagramDataModel;
    }

    @Override
    public DiagramColorManager getColorManager() {
        return myColorManager;
    }

    public static ClassDiagramProvider getInstance() {
        return (ClassDiagramProvider) DiagramProvider.findByID(ID);
    }



}
