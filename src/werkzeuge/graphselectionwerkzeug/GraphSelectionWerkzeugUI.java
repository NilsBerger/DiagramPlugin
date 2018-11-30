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
