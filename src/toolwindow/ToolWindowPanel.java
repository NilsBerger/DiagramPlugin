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

package toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.*;

import javax.swing.*;


public class ToolWindowPanel extends JBPanel{

    private Project project;
    private ToolWindow toolWindow;


    private JBPanel _panel;
    private JBLabel _selectClassDependencyGraph;
    private ComboBox _jbCheckBox;

    public ToolWindowPanel(Project project, ToolWindow toolWindow) {

        this.project = project;
        this.toolWindow = toolWindow;
        _panel = new JBPanel();
        add("CDG Context", _panel);

        createSelectClassDependencyGraph();
        _jbCheckBox = new ComboBox();
        _jbCheckBox.add(new JBCheckBoxMenuItem());
        add(_jbCheckBox);

    }

    private void createSelectClassDependencyGraph()
    {
        _selectClassDependencyGraph = new JBLabel();
        _selectClassDependencyGraph.setText("Select Class Dependency Graph");
        _panel.add(_selectClassDependencyGraph);
    }
    public JBLabel getselectClassDependencyGraph() {
        return _selectClassDependencyGraph;
    }

}