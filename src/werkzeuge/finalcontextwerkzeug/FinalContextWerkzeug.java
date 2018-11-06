
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

package werkzeuge.finalcontextwerkzeug;

import service.ChangePropagationProcessService;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.ui.components.JBPanel;
import materials.ClassNodeMaterial;
import materials.JavaClassNodeMaterial;
import valueobjects.Marking;
import materials.SwiftClassNodeMaterial;
import javafx.collections.SetChangeListener;
import werkzeuge.tracebilitychooserwerkzeug.CorrespondingTraceabilityChooserWerkzeug;
import werkzeuge.tracebilitychooserwerkzeug.TracebilityChooserWerkzeug;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FinalContextWerkzeug {

    private final FinalContextWerkzeugUI _ui;
    private final ChangePropagationProcessService _cpProcess;
    private JBPopupMenu _popup;
    private final JBMenuItem _inspectedMenuItem = new JBMenuItem("Inspected");
    private final JBMenuItem _propagatesMenuItem = new JBMenuItem("Propagates");
    private final JBMenuItem _changedMenuItem = new JBMenuItem("Changed");
    private final JBMenuItem _showSourcecodeItem = new JBMenuItem("Show sourcecode");
    private final JBMenuItem _showCorrespondingClassItem = new JBMenuItem("Show corresponding class in other platform");
    private final boolean _forSwift;

    private ClassNodeMaterial _selectedClass;

    public FinalContextWerkzeug(final String text,final boolean isSwift)
    {
        _ui = new FinalContextWerkzeugUI();
        _ui.setLabelText(text);

       _forSwift =isSwift;

        _popup = new JBPopupMenu();
        _popup.add(_inspectedMenuItem);
        _popup.add(_propagatesMenuItem);
        _popup.add(_changedMenuItem);
        _popup.addSeparator();
        _popup.add(_showSourcecodeItem);
        _popup.add(_showCorrespondingClassItem);
        _cpProcess = ChangePropagationProcessService.getInstance();
        registerUIActions();
    }
    private void addEntry(final ClassNodeMaterial classnode)
    {
        if(_forSwift && (classnode instanceof SwiftClassNodeMaterial))
        {
            _ui.getModel().addEntry(classnode);
        }
        if(!_forSwift && (classnode instanceof JavaClassNodeMaterial))
        {
            _ui.getModel().addEntry(classnode);
        }

    }
    private void removeEntry(final ClassNodeMaterial classnode)
    {
        _ui.getModel().removeEntry(classnode);
    }

    private void registerUIActions() {
        _cpProcess.getAffectedClassesByChange().addListener(new SetChangeListener<ClassNodeMaterial>(){
            @Override
            public void onChanged(Change<? extends ClassNodeMaterial> change) {
                if (change.wasAdded()) {
                    if(!_ui.getModel().contains(change.getElementAdded()))
                    {
                        addEntry(change.getElementAdded());
                    }

                }
                if (change.wasRemoved()) {
                    //removeEntry(change.getElementRemoved());
                }
            }
        });
        _ui.getJBList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)
                        && !_ui.getJBList().isSelectionEmpty()
                        && _ui.getJBList().locationToIndex(e.getPoint()) == _ui.getJBList().getSelectedIndex())
                {
                   _selectedClass = (ClassNodeMaterial) _ui.getJBList().getSelectedValue();
                   _popup.show(_ui.getJBList(),e.getX(),e.getY());

                }
            }
        });
        _propagatesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _selectedClass.setMarking(Marking.PROPAGATES);
            }
        });
        _changedMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _selectedClass.setMarking(Marking.CHANGED);
            }
        });
        _inspectedMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _selectedClass.setMarking(Marking.INSPECTED);
            }
        });
        _showSourcecodeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TracebilityChooserWerkzeug(_selectedClass);
            }
        });
        _showCorrespondingClassItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CorrespondingTraceabilityChooserWerkzeug(_selectedClass);
            }
        });
    }

    public JBPanel getPanel()
    {
        return _ui.getPanel();
    }

}
