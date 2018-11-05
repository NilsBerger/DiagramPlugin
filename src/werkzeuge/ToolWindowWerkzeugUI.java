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

package werkzeuge;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;


public class ToolWindowWerkzeugUI extends SimpleToolWindowPanel {



    private JBPanel _panel;
    private JBPanel _topPanel;
    private JBLabel _selectClassDependencyGraph;
    private ComboBox<File> _fileComboBox;
    private JBPanel _listPanel;

    public ToolWindowWerkzeugUI(JPanel graphSelection, JPanel initialContext, JPanel finalContextJava, JPanel finalContextSwift, JPanel tracelinkPanel) {

        super(true, true);

        _panel = new JBPanel();
        _panel.setLayout(new BorderLayout());
        add(_panel);
        setBorder(new EmptyBorder(10,10,10,10));

        createSelectClassDependencyGraph();
        _listPanel = new JBPanel();
        _listPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        //c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        _listPanel.add(initialContext, c);

        //c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        _listPanel.add(finalContextJava, c);

        c.gridx = 2;
        c.gridy = 0;
        _listPanel.add(finalContextSwift, c);

        c.gridx = 3;
        c.gridy = 0;
        _listPanel.add(tracelinkPanel, c);

        _panel.add(graphSelection, BorderLayout.NORTH);
        _panel.add(_listPanel, BorderLayout.CENTER);
    }

    private void createSelectClassDependencyGraph() {
        _selectClassDependencyGraph = new JBLabel();
        _selectClassDependencyGraph.setText("Select Class Dependency Graph");
        _panel.add(_selectClassDependencyGraph, BorderLayout.NORTH);
    }
    public JBLabel getselectClassDependencyGraph() {
        return _selectClassDependencyGraph;
    }

    public JPanel getPanel()
    {
        return _panel;
    }

}