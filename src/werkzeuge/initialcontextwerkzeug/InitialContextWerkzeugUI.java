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

package werkzeuge.initialcontextwerkzeug;

import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import materials.ClassNodeMaterial;
import werkzeuge.ClassNodeCellRenderer;
import werkzeuge.DynamicListModel;

import javax.swing.*;
import java.util.ArrayList;

public class InitialContextWerkzeugUI {

    private  JBPanel _mainPanel;
    private JBList _initialContextList;
    private DynamicListModel<ClassNodeMaterial> _model;
    private JBLabel _label;
    private ToolbarDecorator _toolbarDecorator;

    public InitialContextWerkzeugUI()
    {
        createLabel();
        createJBList();
        createMainPanel();
        _initialContextList.setCellRenderer(new ClassNodeCellRenderer());
        initToolbar();
    }

    private void createMainPanel() {
        _mainPanel = new JBPanel();
        _mainPanel.setLayout(new BoxLayout(_mainPanel, BoxLayout.Y_AXIS));
        _initialContextList.setModel(_model);
        _mainPanel.add(_label);
        _mainPanel.add(new JBScrollPane(_initialContextList));
        _mainPanel.setSize(300,100);
    }

    private void createLabel()
    {
        _label = new JBLabel();
    }

    public void createJBList()
    {
        _initialContextList = new JBList();
        _model = new DynamicListModel<>(new ArrayList<ClassNodeMaterial>());
    }

    private void initToolbar()
    {
        _toolbarDecorator = ToolbarDecorator.createDecorator(_initialContextList);
    }

    public void setLabelText(final String text)
    {
        _label.setText(text);
    }
    public JBList getJBList()
    {
        return _initialContextList;
    }
    public DynamicListModel<ClassNodeMaterial> getModel()
    {
        return _model;
    }

    public JBPanel getPanel()
    {
        return _mainPanel;
    }

    public ToolbarDecorator getToolbarDecorator()
    {
        return _toolbarDecorator;
    }
}
