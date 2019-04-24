package werkzeuge;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TableSelectionListener implements ListSelectionListener {
    private final JTable _table;
    private ListSelectionEvent _e;

    public TableSelectionListener(JTable table) {
        _table = table;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            _e = e;
        }
    }

    public Object getSelectedRow() {
        return _table.getSelectedRow();
    }
}
