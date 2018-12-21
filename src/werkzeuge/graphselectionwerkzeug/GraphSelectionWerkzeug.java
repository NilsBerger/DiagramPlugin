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

import service.technical.DependencyPersistenceAutomaton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GraphSelectionWerkzeug {
    private final GraphSelectionWerkzeugUI _ui;
    private final DependencyPersistenceAutomaton _service;

    public GraphSelectionWerkzeug(final DependencyPersistenceAutomaton service)
    {
        _service = service;
        _ui = new GraphSelectionWerkzeugUI();
        _ui.getModel().addElements(getContent());
        registerListener();
    }

    private void registerListener()
    {
        _ui.getCombobox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _ui.getModel().addElements(getContent());
            }
        });
    }
    private List<File> getContent()
    {
        return _service.getAllXmlFils().stream().collect(Collectors.toCollection(ArrayList<File>::new));
    }

    public JPanel getPanel()
    {
        return _ui.getPanel();
    }

}
