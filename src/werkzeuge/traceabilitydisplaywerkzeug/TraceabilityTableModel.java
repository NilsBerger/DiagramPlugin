package werkzeuge.traceabilitydisplaywerkzeug;

import materials.TraceLinkProgramEntityAssociation;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TraceabilityTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Java Class", "Precision", "Swift Class"};
    private static final Class<?>[] COLUMN_CLASSES = {String.class, Double.class, String.class};

    private static final int COLUMN_IDX_JAVA_CLASS_NAME = 0;
    private static final int COLUMN_IDX_PRECISION = 1;
    private static final int COLUMN_IDX_SWIFT_CLASS_NAME = 2;

    private static List<TraceLinkProgramEntityAssociation> _traceabilityList;

    TraceabilityTableModel(final List<TraceLinkProgramEntityAssociation> traceabilityList) {
        _traceabilityList = traceabilityList;
    }

    @Override
    public int getRowCount() {
        return _traceabilityList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_CLASSES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final TraceLinkProgramEntityAssociation classDependency = _traceabilityList.get(rowIndex);
        if (columnIndex == COLUMN_IDX_JAVA_CLASS_NAME) {
            return classDependency.getDependentClass();
        }
        if (columnIndex == COLUMN_IDX_PRECISION) {
            return classDependency.getTracelinkValue();
        }
        if (columnIndex == COLUMN_IDX_SWIFT_CLASS_NAME) {
            return classDependency.getIndependentClass();
        }
        throw new IllegalArgumentException("Invalid columnIndex " + columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_CLASSES[columnIndex];
    }

    public void addEntry(TraceLinkProgramEntityAssociation traceLinkProgramEntityAssociation) {
        _traceabilityList.add(traceLinkProgramEntityAssociation);
        final int newRowIndex = _traceabilityList.size() - 1;
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public TraceLinkProgramEntityAssociation getTraceLinkforRow(final int rowIndex) {
        return _traceabilityList.get(rowIndex);
    }
}
