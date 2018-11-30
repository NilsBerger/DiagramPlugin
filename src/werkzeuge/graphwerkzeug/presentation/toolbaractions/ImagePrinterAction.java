package werkzeuge.graphwerkzeug.presentation.toolbaractions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.graph.builder.actions.AbstractGraphAction;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import werkzeuge.ImageClassGraphWriter;
import werkzeuge.graphwerkzeug.presentation.ClassGraph;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Prints graph.
 */
public class ImagePrinterAction extends AbstractGraphAction
{
    private ImageClassGraphWriter _writer;

    public ImagePrinterAction(ClassGraph classGraph)
    {
        super(classGraph.getGraph(), "Only show changed Classes", AllIcons.Actions.Pause);
        _writer = new ImageClassGraphWriter();

    }

    @Override
    protected void actionPerformed(AnActionEvent anActionEvent, Graph2D graph2D) {
        Project[] openProjects = ProjectManager.getInstance().getOpenProjects();
        Project currentProject = openProjects[0];
        String filePath = currentProject.getBasePath() ;
        filePath = filePath + File.pathSeparator + new SimpleDateFormat("yyyy-MM-dd hh-mm-ss''").format(new Date());

        try{
            _writer.writeGraph (graph2D, filePath, true, 100);
        }catch (IOException ex){
            System.err.println("Could not print graph to image!" + ex);
        }

    }
}
