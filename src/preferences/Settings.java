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

package preferences;

import Utils.PluginProperties;
import Utils.PropertyName;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import service.ProjectDependencyExtrator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Settings implements Configurable {
    SettingsUI _ui;

    private boolean _isSwiftModified = false;
    private boolean _isAndroidModified = false;
    public Settings() {
        _ui = new SettingsUI();
        addSwingTextFieldWithBrowseButton();
        addSwingSchemeTextFieldWithBrowseButton();
        addAndroidTextFieldWithBrowseButton();
        addGenerateDepedencyModelButtonListener();
    }

    private void addSwingTextFieldWithBrowseButton() {
        final XworkspaceFileChooserDescriptor descriptor = new XworkspaceFileChooserDescriptor(false, true, false, false, false, false);
        final PluginProperties pluginProperties;
        try {
            pluginProperties = PluginProperties.getInstance();
            pluginProperties.readPluginProperties();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not read Property-File!");
            return;
        }
        _ui.getSwingTextFieldWithBrowseButton().setText(pluginProperties.getProperty(PropertyName.SWIFT_WORKSPACE));
        _ui.getSwingTextFieldWithBrowseButton().getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VirtualFile virtualFile = FileChooser.chooseFile(descriptor, null, null);
                _ui.getSwingTextFieldWithBrowseButton().setText(virtualFile.getCanonicalPath());
                pluginProperties.setProperty(PropertyName.SWIFT_WORKSPACE,virtualFile.getCanonicalPath());

                try {
                    pluginProperties.writeAppProperties();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                _isSwiftModified = true;
            }
        });
    }

    private void addSwingSchemeTextFieldWithBrowseButton() {
        final FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        final PluginProperties pluginProperties;
        try {
            pluginProperties = PluginProperties.getInstance();
            pluginProperties.readPluginProperties();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not read Property-File!");
            return;
        }
        _ui.getSwingSchemeTextFieldWithBrowseButton().setText(pluginProperties.getProperty(PropertyName.Swift_SCHEME_FOLDER));
        _ui.getSwingSchemeTextFieldWithBrowseButton().getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VirtualFile virtualFile = FileChooser.chooseFile(descriptor, null, null);
                _ui.getSwingSchemeTextFieldWithBrowseButton().setText(virtualFile.getCanonicalPath());
                pluginProperties.setProperty(PropertyName.Swift_SCHEME_FOLDER,virtualFile.getCanonicalPath());

                try {
                    pluginProperties.writeAppProperties();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                _isSwiftModified = true;
            }
        });
    }

    private void addAndroidTextFieldWithBrowseButton() {
        final FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        final PluginProperties pluginProperties;
        try {
            pluginProperties = PluginProperties.getInstance();
            pluginProperties.readPluginProperties();
        } catch (IOException e) {
            System.out.println("Can't access property file '" + PluginProperties.getPropertyFilePath() + "'");
            e.printStackTrace();
            return;
        }
        _ui.getAndroidTextFieldWithBrowseButton().setText(pluginProperties.getProperty(PropertyName.ANDROID_WORKSPACE));
        _ui.getAndroidTextFieldWithBrowseButton().getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VirtualFile virtualFile = FileChooser.chooseFile(descriptor, null, null);
                _ui.getAndroidTextFieldWithBrowseButton().setText(virtualFile.getCanonicalPath());
                pluginProperties.setProperty(PropertyName.ANDROID_WORKSPACE,virtualFile.getCanonicalPath());

                try {
                    pluginProperties.writeAppProperties();
                } catch (IOException ex) {
                    System.out.println("Can't access property file '" + PluginProperties.getPropertyFilePath() + "'");
                    ex.printStackTrace();
                }

                _isAndroidModified = true;
            }
        });
    }

    private void addGenerateDepedencyModelButtonListener()
    {
        _ui.getGenerateDepModelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final PluginProperties properties;
                try {
                    properties = PluginProperties.getInstance();
                    properties.readPluginProperties();

                    final String android_filepath = properties.getProperty(PropertyName.ANDROID_WORKSPACE);
                    final String swift_filepath = properties.getProperty(PropertyName.SWIFT_WORKSPACE);
                    final String swift_scheme_folder = properties.getProperty(PropertyName.Swift_SCHEME_FOLDER);

                    ProjectDependencyExtrator.extractDependenciesFromSwiftProject(swift_filepath, swift_scheme_folder);
                    ProjectDependencyExtrator.extractDependenciesFromJavaProject(android_filepath);

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        });
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return _ui.getJPanel();
    }

    @Override
    public boolean isModified() {
        return _isAndroidModified || _isSwiftModified;
    }

    @Override
    public void apply() throws ConfigurationException {
    }


}
