package werkzeuge.tracebilitychooserwerkzeug;


import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TraceabilityTableModel extends AbstractTableModel {

    private static final Class<?>[] COLUMN_CLASSES = {Double.class, String.class, String.class};
    private static final String[] COLUMN_NAMES = {"Coincidence","Type", "Filename"};

    //Indicies of the columns
    private static final int COLUMN_IDX_PROBABILITY = 0;
    private static final int COLUMN_IDX_TYPE = 1;
    private static final int COLUMN_IDX_FILENAME = 2;

    private final List<TraceabilityLink> _traceabilityPointerList;

    public TraceabilityTableModel()
    {
        _traceabilityPointerList = new ArrayList<>();

    }

    public void setContent(final List<TraceabilityLink> traceabilityLinks)
    {
        _traceabilityPointerList.addAll(traceabilityLinks);
    }
    @Override
    public int getRowCount() {
        return _traceabilityPointerList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final TraceabilityLink link = _traceabilityPointerList.get(rowIndex);

        if(columnIndex == COLUMN_IDX_PROBABILITY) {
            return link.getProbability();
        }
        if(columnIndex == COLUMN_IDX_TYPE) {
            return link.getTarget().getPointerType();
        }
        if(columnIndex == COLUMN_IDX_FILENAME)
        {
            return link.getTarget().getDisplayName();
        }
        throw new IllegalArgumentException("Invalid columnIndex " + columnIndex);
    }

    public TraceabilityLink getTraceabilityLink(int selectedRow)
    {
        return _traceabilityPointerList.get(selectedRow);
    }

    @Override
    public String getColumnName(final int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_CLASSES[columnIndex];
    }
}
