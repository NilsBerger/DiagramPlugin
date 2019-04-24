package werkzeuge.traceabilitydisplaywerkzeug;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import werkzeuge.ContainsFilter;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;

public class TraceabilityWerkzeugUI {

    public static final int ROW_HEIGHT = 50;

    private JBPanel _mainPanel;
    private JBTable _traceabilityTable;
    private TraceabilityTableModel _model;
    private TraceabilityLinkTableCellRenderer _renderer;
    private JBLabel _label;
    private JBTextField _textfield;
    private final TableRowSorter _tableRowSorter;
    private final ContainsFilter _containsFilter;

    public TraceabilityWerkzeugUI() {
        _mainPanel = new JBPanel();
        _traceabilityTable = new JBTable();
        _model = new TraceabilityTableModel(new ArrayList());
        _label = new JBLabel();
        _textfield = new JBTextField();
        _renderer = new TraceabilityLinkTableCellRenderer();

        createPanel();

        _tableRowSorter = new TableRowSorter(_traceabilityTable.getModel());
        _containsFilter = new ContainsFilter("");
//        _tableRowSorter.setRowFilter(_containsFilter);
//        _traceabilityTable.setRowSorter(_tableRowSorter);

        _textfield.setMaximumSize(new Dimension(20000, 20));


    }

    private void createPanel() {
        _mainPanel.setLayout(new BoxLayout(_mainPanel, BoxLayout.Y_AXIS));
        _mainPanel.add(_label);
        _mainPanel.add(_textfield);
        _mainPanel.add(new JBScrollPane(_traceabilityTable));


        _traceabilityTable.setModel(_model);
        _traceabilityTable.setDefaultRenderer(Double.class, _renderer);
        //_traceabilityTable.setAutoCreateRowSorter(true);
        _traceabilityTable.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        _traceabilityTable.setRowHeight(ROW_HEIGHT);
    }

    public void setText(String text) {
        _label.setText(text);
    }

    public JPanel getPanel() {
        return _mainPanel;
    }

    public TraceabilityTableModel getModel() {
        return _model;
    }

    public JBTable getTracebilityTable() {
        return _traceabilityTable;
    }

    public JBTextField getJBTextfield() {
        return _textfield;
    }

    public ContainsFilter getContainsFilter() {
        return _containsFilter;
    }

    public TableRowSorter getRowSorter() {
        return _tableRowSorter;
    }

}
