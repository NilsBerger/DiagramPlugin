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

package werkzeuge.graphselectionwerkzeug;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBPanel;
import werkzeuge.FileMutableModel;

import javax.swing.*;
import java.io.File;

public class GraphSelectionWerkzeugUI {

    private final JBPanel _panel;
    private JComboBox<File> _xmlFileComboBox;
    private final FileMutableModel _model;
    public GraphSelectionWerkzeugUI()
    {
        _panel = new JBPanel();
        _model = new FileMutableModel();
        _xmlFileComboBox = new ComboBox<>(_model);

        _panel.setLayout(new BoxLayout(_panel,BoxLayout.X_AXIS));
        _panel.add(_xmlFileComboBox);
    }

    public JBPanel getPanel()
    {
        return _panel;
    }

    public JComboBox<File> getCombobox()
    {
        return _xmlFileComboBox;
    }
    public FileMutableModel getModel()
    {
        return _model;
    }
}
