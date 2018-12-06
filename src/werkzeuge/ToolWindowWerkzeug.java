package werkzeuge;

import com.intellij.ui.components.JBPanel;
import service.ChangePropagationProcess;
import valueobjects.ClassLanguageType;
import werkzeuge.finalcontextwerkzeug.FinalContextWerkzeug;
import werkzeuge.graphselectionwerkzeug.GraphSelectionWerkzeug;
import werkzeuge.initialcontextwerkzeug.InitialContextWerkzeug;
import werkzeuge.traceabilitywerkzeug.TraceabilityWerkzeug;


import javax.swing.*;

/**
 *
 */
public class ToolWindowWerkzeug {


    private GraphSelectionWerkzeug _graphSelection;
    private InitialContextWerkzeug _initialContext;
    private FinalContextWerkzeug _javaFinalContextWerkzeug;
    private FinalContextWerkzeug _swiftFinalContextWerkzeug;
    private TraceabilityWerkzeug _traceabilityWerkzeug;
    private ChangePropagationProcess service = ChangePropagationProcess.getInstance();

    //private final DependencyPersistenceAutomaton _dependencyPersistenceAutomaton;


    private ToolWindowWerkzeugUI _ui;


    public ToolWindowWerkzeug() {
        //String path = "/Users/nilsberger/Documents/Masterarbeit/IntelliJIdeaDiagramDemo-master/resources/";
        // _dependencyPersistenceAutomaton = new DependencyPersistenceAutomaton(ChangePropagationProcess.getInstance(),path);

        //_graphSelection = new GraphSelectionWerkzeug(_dependencyPersistenceAutomaton);
        _initialContext = new InitialContextWerkzeug();
        _javaFinalContextWerkzeug = new FinalContextWerkzeug("Final Context Java", ClassLanguageType.Java);
        _swiftFinalContextWerkzeug = new FinalContextWerkzeug("Final Context Swift", ClassLanguageType.Swift);
        _traceabilityWerkzeug = new TraceabilityWerkzeug();
        _traceabilityWerkzeug.setChangePropagationProcessService(service);

        _ui = new ToolWindowWerkzeugUI(new JBPanel<>(), _initialContext.getPanel(), _javaFinalContextWerkzeug.getPanel(), _swiftFinalContextWerkzeug.getPanel(), _traceabilityWerkzeug.getPanel());
    }

    public JPanel getPanel() {
        return _ui.getPanel();
    }

    public TraceabilityWerkzeug getTraceabilityWerkzeug() {
        return _traceabilityWerkzeug;
    }

    public FinalContextWerkzeug getJavaContextWerkzeuf()
    {
        return _javaFinalContextWerkzeug;
    }
    public FinalContextWerkzeug getSwiftContext()
    {
        return _swiftFinalContextWerkzeug;
    }

}