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
