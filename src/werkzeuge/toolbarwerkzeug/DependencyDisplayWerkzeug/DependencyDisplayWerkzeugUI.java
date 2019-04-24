package werkzeuge.toolbarwerkzeug.DependencyDisplayWerkzeug;


import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.table.JBTable;
import valueobjects.Marking;
import werkzeuge.ClassNodeTableCellRenderer;
import werkzeuge.ContainsFilter;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;

public class DependencyDisplayWerkzeugUI {

    private final JFrame _jFrame;
    private final JTable _dependencyTable;
    private final JBTextField _searchTextField;
    private final DependencyTableModel _model;
    private final TableRowSorter _tableRowSorter;
    private final ContainsFilter _containsFilter;

    public DependencyDisplayWerkzeugUI() {
        _jFrame = new JFrame();
        _dependencyTable = new JBTable();
        _searchTextField = new JBTextField();
        _model = new DependencyTableModel(new ArrayList<>());

        _dependencyTable.setModel(_model);
        _dependencyTable.setDefaultRenderer(Marking.class, new ClassNodeTableCellRenderer());
        _dependencyTable.setAutoCreateRowSorter(true);
        _dependencyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        _tableRowSorter = new TableRowSorter(_dependencyTable.getModel());
        _containsFilter = new ContainsFilter("");
        _tableRowSorter.setRowFilter(_containsFilter);
        _dependencyTable.setRowSorter(_tableRowSorter);
        setUpFrame();

        _jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        _jFrame.pack();
    }


    private void setUpFrame() {


        JBScrollPane scrollPane = new JBScrollPane(_dependencyTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        _jFrame.setLayout(new BorderLayout());
        _jFrame.add(_searchTextField, BorderLayout.NORTH);
        _jFrame.add(scrollPane, BorderLayout.CENTER);
    }

    public void show() {
        _jFrame.setVisible(true);
    }

    public DependencyTableModel getModel() {
        return _model;
    }

    public JBTextField getTextField() {
        return _searchTextField;
    }

    public ContainsFilter getContainsFilter() {
        return _containsFilter;
    }

    public TableRowSorter getRowSorter() {
        return _tableRowSorter;
    }
}
