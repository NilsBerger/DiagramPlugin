package werkzeuge.finalcontextwerkzeug;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import utility.MarkingComparator;
import valueobjects.Language;
import valueobjects.Marking;
import werkzeuge.ClassNodeTableCellRenderer;
import werkzeuge.ContainsFilter;
import werkzeuge.ContextTableModel;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;

public class FinalContextWerkzeugUI {

    private JBPanel _mainPanel;
    private JBTable _finalContextTable;
    private final ContextTableModel _model;
    private JBLabel _label;
    private JBTextField _textfield;
    private JBLabel _amountLabel;
    private final TableRowSorter _tableRowSorter;
    private final ContainsFilter _containsFilter;

    public FinalContextWerkzeugUI(Language language)
    {
        _finalContextTable = new JBTable();
        _model = new ContextTableModel(new ArrayList(), language);
        _textfield = new JBTextField();
        _amountLabel = new JBLabel();

        _finalContextTable.setModel(_model);
        _finalContextTable.setDefaultRenderer(Marking.class, new ClassNodeTableCellRenderer());
        _finalContextTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        _tableRowSorter = new TableRowSorter(_finalContextTable.getModel());
        _containsFilter = new ContainsFilter("");
        _tableRowSorter.setRowFilter(_containsFilter);
        _finalContextTable.setRowSorter(_tableRowSorter);

        _amountLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        _textfield.setMaximumSize(new Dimension(20000, 40));

        createLabel();
        createMainPanel();
        initTableRowSorter();

    }

    private void createMainPanel() {
        _mainPanel = new JBPanel();
        _mainPanel.setLayout(new BorderLayout());
        _mainPanel.add(createSearchPanel(), BorderLayout.NORTH);
        _mainPanel.add(new JBScrollPane(_finalContextTable), BorderLayout.CENTER);
        _mainPanel.add(_amountLabel, BorderLayout.SOUTH);
    }

    private JPanel createSearchPanel() {
        JBPanel panel = new JBPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(_label);
        panel.add(_textfield);
        return panel;
    }

    private void createLabel()
    {
        _label = new JBLabel();
    }

    private void initTableRowSorter() {
        _tableRowSorter.setComparator(ContextTableModel.COLUMN_IDX_MARKING, new MarkingComparator());
    }



    public void setLabelText(final String text)
    {
        _label.setText(text);
    }

    public void setAmount(final int amount) {
        _amountLabel.setText("Amount of classes:" + Integer.toString(amount));
    }

    public JBTable getJBTable()
    {
        return _finalContextTable;
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

    public JBLabel getAmountLabel() {
        return _amountLabel;
    }

    public TableRowSorter getRowSorter() {
        return _tableRowSorter;
    }
}
