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

import com.intellij.ui.components.JBPanel;
import service.ChangePropagationProcess;
import werkzeuge.finalcontextwerkzeug.FinalContextWerkzeug;
import werkzeuge.graphselectionwerkzeug.GraphSelectionWerkzeug;
import werkzeuge.initialcontextwerkzeug.InitialContextWerkzeug;
import werkzeuge.traceabilitywerkzeug.TraceabilityWerkzeug;


import javax.swing.*;

/**
 *
 */
public class ToolWindowWerkzeug{


    private GraphSelectionWerkzeug _graphSelection;
    private InitialContextWerkzeug _initialContext;
    private FinalContextWerkzeug _javaFinalContext;
    private FinalContextWerkzeug _swiftFinalContext;
    private TraceabilityWerkzeug _traceabilityWerkzeug;
    private ChangePropagationProcess service = ChangePropagationProcess.getInstance();

  //private final DependencyPersistenceAutomaton _dependencyPersistenceAutomaton;


    private ToolWindowWerkzeugUI _ui;


    public ToolWindowWerkzeug()
    {
        //String path = "/Users/nilsberger/Documents/Masterarbeit/IntelliJIdeaDiagramDemo-master/resources/";
       // _dependencyPersistenceAutomaton = new DependencyPersistenceAutomaton(ChangePropagationProcess.getInstance(),path);

        //_graphSelection = new GraphSelectionWerkzeug(_dependencyPersistenceAutomaton);
        _initialContext = new InitialContextWerkzeug();
        _javaFinalContext = new FinalContextWerkzeug("Final Context Java", false);
        _swiftFinalContext = new FinalContextWerkzeug("Final Context Swift", true);
        _traceabilityWerkzeug = new TraceabilityWerkzeug();
        _traceabilityWerkzeug.setChangePropagationProcessService(service);

        _ui = new ToolWindowWerkzeugUI(new JBPanel<>(), _initialContext.getPanel(), _javaFinalContext.getPanel(), _swiftFinalContext.getPanel(), _traceabilityWerkzeug.getPanel());
    }

    public JPanel getPanel()
    {
        return _ui.getPanel();
    }

    public TraceabilityWerkzeug getTaceabilityWerkzeug()
    {
        return _traceabilityWerkzeug;
    }

}