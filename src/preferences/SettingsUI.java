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

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.VerticalFlowLayout;

import javax.swing.*;

class SettingsUI {
    private JPanel _jPanel;
    private JLabel _androidPathLabel;
    private JLabel _swiftPathLabel;
    private JLabel _swiftSchemePathLabel;
    private JButton _generateDepModelButton;
    private TextFieldWithBrowseButton _androidTextFieldWithBrowseButton;
    private TextFieldWithBrowseButton _swingTextFieldWithBrowseButton;
    private TextFieldWithBrowseButton _swingSchemeTextFieldWithBrowseButton;


    SettingsUI() {
        _jPanel = new JPanel();
        _jPanel.setLayout(new VerticalFlowLayout(true, false));
        _jPanel.add(new JSeparator());
        createAndroidLabel();
        _jPanel.add(new JSeparator());
        createSwiftPathComponent();
        createSwiftSchemeComponent();

        _generateDepModelButton = new JButton();
        _generateDepModelButton.setText("Generate Dependency Models");
        _jPanel.add(new JSeparator());
        _jPanel.add(_generateDepModelButton);

    }

    private void createSwiftPathComponent() {
        _swiftPathLabel = new JLabel();
        _swiftPathLabel.setText("Add path to Swift-Workspace");

        _swingTextFieldWithBrowseButton = new TextFieldWithBrowseButton();
        _swingTextFieldWithBrowseButton.setEditable(false);

        _jPanel.add(_swiftPathLabel);
        _jPanel.add(_swingTextFieldWithBrowseButton);
    }

    private void createSwiftSchemeComponent() {
        _swiftSchemePathLabel = new JLabel();
        _swiftSchemePathLabel.setText("Add path of Scheme-Folder");

        _swingSchemeTextFieldWithBrowseButton = new TextFieldWithBrowseButton();
        _swingSchemeTextFieldWithBrowseButton.setEditable(false);

        _jPanel.add(_swiftSchemePathLabel);
        _jPanel.add(_swingSchemeTextFieldWithBrowseButton);
    }

    TextFieldWithBrowseButton getSwingTextFieldWithBrowseButton()
    {
        return _swingTextFieldWithBrowseButton;
    }

    private void createAndroidLabel() {
        _androidPathLabel = new JLabel();
        _androidPathLabel.setText("Add path to Android-Workspace");


        _androidTextFieldWithBrowseButton = new TextFieldWithBrowseButton();
        _androidTextFieldWithBrowseButton.setEditable(false);

        _jPanel.add(_androidPathLabel);

        _jPanel.add(_androidTextFieldWithBrowseButton);
    }

    JPanel getJPanel()
    {
        return _jPanel;
    }

    TextFieldWithBrowseButton getSwingSchemeTextFieldWithBrowseButton() {
        return _swingSchemeTextFieldWithBrowseButton;
    }
    TextFieldWithBrowseButton getAndroidTextFieldWithBrowseButton(){return _androidTextFieldWithBrowseButton;}

    JButton getGenerateDepModelButton()
    {
        return _generateDepModelButton;
    }
}
