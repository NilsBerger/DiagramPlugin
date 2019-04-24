package werkzeuge.initialcontextwerkzeug;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import valueobjects.Marking;
import werkzeuge.ClassNodeTableCellRenderer;
import werkzeuge.ContainsFilter;
import werkzeuge.ContextTableModel;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;

public class InitialContextWerkzeugUI {

    private JBPanel _mainPanel;
    private JBTable _initialContextTabel;
    private ContextTableModel _model;
    private JBLabel _label;
    private JBTextField _textfield;
    private final TableRowSorter _tableRowSorter;
    private final ContainsFilter _containsFilter;

    public InitialContextWerkzeugUI()
    {
        createLabel();
        createTextField();
        createJBTable();
        createMainPanel();

        _tableRowSorter = new TableRowSorter(_initialContextTabel.getModel());
        _containsFilter = new ContainsFilter("");
        _tableRowSorter.setRowFilter(_containsFilter);
        _initialContextTabel.setRowSorter(_tableRowSorter);

        _textfield.setMaximumSize(new Dimension(20000, 40));
    }

    private void createTextField() {
        _textfield = new JBTextField();
    }

    private void createMainPanel() {
        _mainPanel = new JBPanel();
        _mainPanel.setLayout(new BoxLayout(_mainPanel, BoxLayout.Y_AXIS));
        _initialContextTabel.setModel(_model);
        _initialContextTabel.setDefaultRenderer(Marking.class, new ClassNodeTableCellRenderer());
        _initialContextTabel.setAutoCreateRowSorter(true);
        _mainPanel.add(_label);
        _label.setAlignmentY(Component.CENTER_ALIGNMENT);
        _mainPanel.add(_textfield);
        _mainPanel.add(new JBScrollPane(_initialContextTabel));
    }

    private void createLabel()
    {
        _label = new JBLabel();
    }

    public void createJBTable()
    {
        _initialContextTabel = new JBTable();
        _model = new ContextTableModel(new ArrayList());
    }

    public void setLabelText(final String text)
    {
        _label.setText(text);
    }

    public ContextTableModel getModel()
    {
        return _model;
    }

    public JBPanel getPanel()
    {
        return _mainPanel;
    }

    public JBTextField getTextField() {
        return _textfield;
    }

    public ContainsFilter getContainsFilter() {
        return _containsFilter;
    }

    public TableRowSorter getRowSorter() {
        return _tableRowSorter;
    }
}
