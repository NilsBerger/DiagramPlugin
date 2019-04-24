package werkzeuge;

import service.functional.ChangePropagationProcess;
import valueobjects.Language;
import werkzeuge.finalcontextwerkzeug.FinalContextWerkzeug;
import werkzeuge.graphselectionwerkzeug.GraphSelectionWerkzeug;
import werkzeuge.initialcontextwerkzeug.InitialContextWerkzeug;
import werkzeuge.toolbarwerkzeug.ToolbarWerkzeug;
import werkzeuge.traceabilitydisplaywerkzeug.TraceabilityWerkzeug;

import javax.swing.*;

/**
 *
 */
public class ToolWindowWerkzeug {


    private GraphSelectionWerkzeug _graphSelection;
    private final ToolbarWerkzeug _toolbarWerkzeug;
    private final InitialContextWerkzeug _initialContext;
    private final FinalContextWerkzeug _javaFinalContextWerkzeug;
    private final FinalContextWerkzeug _swiftFinalContextWerkzeug;
    private final TraceabilityWerkzeug _traceabilityWerkzeug;
    private ChangePropagationProcess service = ChangePropagationProcess.getInstance();

    //private final DependencyPersistenceAutomaton _dependencyPersistenceAutomaton;


    private ToolWindowWerkzeugUI _ui;


    public ToolWindowWerkzeug() {
        //String path = "/Users/nilsberger/Documents/Masterarbeit/IntelliJIdeaDiagramDemo-master/resources/";
        // _dependencyPersistenceAutomaton = new DependencyPersistenceAutomaton(ChangePropagationProcess.getInstance(),path);

        //_graphSelection = new GraphSelectionWerkzeug(_dependencyPersistenceAutomaton);
        _toolbarWerkzeug = new ToolbarWerkzeug();
        _initialContext = new InitialContextWerkzeug();
        _javaFinalContextWerkzeug = new FinalContextWerkzeug("Java: Candidate Impact Set", Language.Java);
        _swiftFinalContextWerkzeug = new FinalContextWerkzeug("Swift: Candidate Imapct Set", Language.Swift);
        _traceabilityWerkzeug = new TraceabilityWerkzeug();
        _traceabilityWerkzeug.setChangePropagationProcessService(service);

        _ui = new ToolWindowWerkzeugUI(_toolbarWerkzeug.getToolbar(), _initialContext.getPanel(), _javaFinalContextWerkzeug.getPanel(), _swiftFinalContextWerkzeug.getPanel(), _traceabilityWerkzeug.getPanel());
    }

    public JPanel getPanel() {
        return _ui.getPanel();
    }

    public TraceabilityWerkzeug getTraceabilityWerkzeug() {
        return _traceabilityWerkzeug;
    }

    public FinalContextWerkzeug getJavaContextWerkzeug()
    {
        return _javaFinalContextWerkzeug;
    }
    public FinalContextWerkzeug getSwiftContextWerkzeug()
    {
        return _swiftFinalContextWerkzeug;
    }

}