package actions;

import Utils.PluginProperties;
import Utils.PropertyName;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import service.technical.ProjectDependencyExtractor;

import java.io.IOException;

public class UpdateDependencyModelAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent)
    {
        final PluginProperties properties;
        try {
            properties = PluginProperties.getInstance();
            properties.readPluginProperties();

            final String swift_filepath = properties.getProperty(PropertyName.SWIFT_WORKSPACE);
            final String swift_scheme_folder = properties.getProperty(PropertyName.Swift_SCHEME_FOLDER);
            final String android_filepath = properties.getProperty(PropertyName.ANDROID_WORKSPACE);

            ProjectDependencyExtractor.extractDependenciesFromSwiftProject(swift_filepath, swift_scheme_folder);
            ProjectDependencyExtractor.extractDependenciesFromJavaProject(android_filepath);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
