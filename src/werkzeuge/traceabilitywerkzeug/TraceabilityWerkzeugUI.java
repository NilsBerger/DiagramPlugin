package werkzeuge.traceabilitywerkzeug;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import materials.TraceLinkDependencyMaterial;
import werkzeuge.DynamicListModel;

import javax.swing.*;
import java.util.ArrayList;

public class TraceabilityWerkzeugUI {

    private JBPanel _mainPanel;
    private JBList _traceabilityList;
    private DynamicListModel<TraceLinkDependencyMaterial> _model;
    private JBLabel _label;

    public TraceabilityWerkzeugUI()
    {
        _mainPanel = new JBPanel();
        _traceabilityList = new JBList();
        _model = new DynamicListModel<>(new ArrayList<>());
        _label = new JBLabel();
        createPanel();
    }
    private void createPanel()
    {
        _mainPanel.setLayout(new BoxLayout(_mainPanel, BoxLayout.Y_AXIS));
        _mainPanel.add(_label);
        _mainPanel.add(new JBScrollPane(_traceabilityList));
        _traceabilityList.setModel(_model);
        _traceabilityList.setCellRenderer(new TraceabilityListCellRenderer());
        _traceabilityList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
    }

    public void setText(String text)
    {
        _label.setText(text);
    }

    public JPanel getPanel()
    {
        return _mainPanel;
    }

    public DynamicListModel<TraceLinkDependencyMaterial> getModel()
    {
        return _model;
    }

    public JBList<TraceLinkDependencyMaterial> getTracebilityList() { return _traceabilityList;}

}
