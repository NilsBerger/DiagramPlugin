package actions;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import graphapi.ClassGraph;
import graphapi.ClassGraphToolWindow;
import materials.DependencyIF;
import materials.JavaClassNodeMaterial;
import service.ChangePropagationProcessService;
import service.DotFileParserService;
import service.RandomChangeAndFixStrategy;

import java.util.HashSet;
import java.util.Set;

public class AddInitialContextClassAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        PsiElement element = anActionEvent.getData(DataKeys.PSI_ELEMENT);

        final Language javaLanguage = Language.findLanguageByID("JAVA");
        Language psiElementLanguage = element.getNode().getElementType().getLanguage();

        ChangePropagationProcessService propagationProcessService = ChangePropagationProcessService.getInstance();

        Set<DependencyIF> dependencyList = new HashSet<>();
        dependencyList.addAll(DotFileParserService.parseJavaDependenciesFromDotFile(DotFileParserService.JAVA_DOT_FILENAME));
        dependencyList.addAll(DotFileParserService.parseSwiftDependenciesFromDotFile(DotFileParserService.DEPCHECK_FILENAME));
        propagationProcessService.initialize(dependencyList, new RandomChangeAndFixStrategy());


        if (psiElementLanguage.equals(javaLanguage)) {
            PsiNamedElement namedElement = (PsiNamedElement) element;
            propagationProcessService.change(new JavaClassNodeMaterial(namedElement.getName()));

            ClassGraph generalClassGraph = element.getProject().getUserData(ClassGraphToolWindow.GENERAL_GRAPH_KEY);
            generalClassGraph.clear();
            generalClassGraph.getDataModel().setChangePropagationService(propagationProcessService);
            generalClassGraph.registerListener();
            generalClassGraph.updateGraph();
            generalClassGraph.fitContent();

            ClassGraph javaClassGraph = element.getProject().getUserData(ClassGraphToolWindow.JAVA_GRAPH_KEY);
            javaClassGraph.clear();
            javaClassGraph.getDataModel().setChangePropagationService(propagationProcessService);
            javaClassGraph.registerListener();
            javaClassGraph.updateGraph();
            javaClassGraph.fitContent();

            ClassGraph swiftClassGraph = element.getProject().getUserData(ClassGraphToolWindow.SWIFT_GRAPH_KEY);
            swiftClassGraph.clear();
            swiftClassGraph.getDataModel().setChangePropagationService(propagationProcessService);
            swiftClassGraph.registerListener();
            swiftClassGraph.updateGraph();
            swiftClassGraph.fitContent();
        }
    }

    @Override
    public void update(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        PsiElement psiElement = dataContext.getData(CommonDataKeys.PSI_ELEMENT);
        VirtualFile virtualFile = dataContext.getData(CommonDataKeys.VIRTUAL_FILE);
        if(virtualFile != null && psiElement != null)
        {
            final Language javaLanguage = Language.findLanguageByID("JAVA");
            Language psiElementLanguage = psiElement.getNode().getElementType().getLanguage();
            if(psiElementLanguage != null)
            {
                if(javaLanguage.equals(psiElementLanguage))
                {
                    e.getPresentation().setEnabled(true);
                }
                else
                {
                    e.getPresentation().setEnabled(false);
                }
            }

        }
    }
}
