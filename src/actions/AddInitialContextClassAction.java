package actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import service.functional.ChangePropagationProcess;
import service.functional.RandomChangeAndFixStrategy;
import service.technical.DotFileParser;
import valueobjects.Language;

import java.util.HashSet;
import java.util.Set;

public class AddInitialContextClassAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        PsiElement element = anActionEvent.getData(DataKeys.PSI_ELEMENT);

        final com.intellij.lang.Language javaLanguage = com.intellij.lang.Language.findLanguageByID("JAVA");
        com.intellij.lang.Language psiElementLanguage = element.getNode().getElementType().getLanguage();

        ChangePropagationProcess propagationProcessService = ChangePropagationProcess.getInstance();

        Set<ProgramEntityRelationship> dependencyList = new HashSet<>();
        dependencyList.addAll(DotFileParser.parseJavaDependenciesFromDotFile(DotFileParser.JAVA_DOT_FILENAME));
        dependencyList.addAll(DotFileParser.parseSwiftDependenciesFromDotFile(DotFileParser.DEPCHECK_FILENAME));
        if (!propagationProcessService.isInitialized()) {
            propagationProcessService.initialize(dependencyList, new RandomChangeAndFixStrategy());
            //propagationProcessService.reloadData(EvaluationDependencies.getChangedDependencies(), new RandomChangeAndFixStrategy());
        }
        if (psiElementLanguage.equals(javaLanguage)) {
            PsiNamedElement namedElement = (PsiNamedElement) element;
            propagationProcessService.changeInitial(new ProgramEntity(namedElement.getName(), Language.Java));
        }
        Set<ProgramEntityRelationship> dependencyaJava = new HashSet<>();
        Set<ProgramEntityRelationship> dependencyaSwift = new HashSet<>();

        Set<ProgramEntity> dependencyaUniqueJava = new HashSet<>();
        Set<ProgramEntity> dependencyaUniqueSwift = new HashSet<>();

        dependencyaJava.addAll(DotFileParser.parseJavaDependenciesFromDotFile(DotFileParser.JAVA_DOT_FILENAME));
        dependencyaSwift.addAll(DotFileParser.parseJavaDependenciesFromDotFile(DotFileParser.DEPCHECK_FILENAME));

        System.out.println("____________________________________");
        System.out.println("Anzahl Java Abhängigkeiten: " + dependencyaJava.size());
        System.out.println("Anzahl Siwft Abhängigkeiten: " + dependencyaSwift.size());

        for (ProgramEntityRelationship javar : dependencyaJava) {
            dependencyaUniqueJava.add(javar.getIndependentClass());
            dependencyaUniqueJava.add(javar.getDependentClass());
        }

        for (ProgramEntityRelationship swiftr : dependencyaSwift) {
            dependencyaUniqueSwift.add(swiftr.getIndependentClass());
            dependencyaUniqueSwift.add(swiftr.getDependentClass());
        }

        System.out.println("Anzahl Java Enitäten: " + dependencyaUniqueJava.size());
        System.out.println("Anzahl Siwft Entitäten: " + dependencyaUniqueSwift.size());
    }

    @Override
    public void update(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        PsiElement psiElement = dataContext.getData(CommonDataKeys.PSI_ELEMENT);
        VirtualFile virtualFile = dataContext.getData(CommonDataKeys.VIRTUAL_FILE);
        if(virtualFile != null && psiElement != null)
        {
            final com.intellij.lang.Language javaLanguage = com.intellij.lang.Language.findLanguageByID("JAVA");
            com.intellij.lang.Language psiElementLanguage = psiElement.getNode().getElementType().getLanguage();
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
