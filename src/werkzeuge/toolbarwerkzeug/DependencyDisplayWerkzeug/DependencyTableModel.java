package werkzeuge.toolbarwerkzeug.DependencyDisplayWerkzeug;

import materials.ProgramEntityRelationship;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DependencyTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Name of Class", "Type", "Type of Relationship", "Name of class", "Type"};
    private static final Class<?>[] COLUMN_CLASSES = {String.class, String.class, String.class, String.class, String.class};

    private static final int COLUMN_IDX_IND_NAME = 0;
    private static final int COLUMN_IDX_IND_NAME_OF_CLASS = 1;
    private static final int COLUMN_IDX_RELATIONSHIPTYPE = 2;
    private static final int COLUMN_IDX_DEP_NAME = 3;
    private static final int COLUMN_IDX_DEP_NAME_OF_CLASS = 4;

    private final List<ProgramEntityRelationship> _dependencyList;

    public DependencyTableModel(final List<ProgramEntityRelationship> dependencyList) {
        this._dependencyList = new ArrayList<>(dependencyList);
    }

    public DependencyTableModel(final Set<ProgramEntityRelationship> dependencySet) {
        this._dependencyList = new ArrayList<>();
        _dependencyList.addAll(dependencySet);
    }

    @Override
    public int getRowCount() {
        return _dependencyList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_CLASSES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final ProgramEntityRelationship dependency = _dependencyList.get(rowIndex);
        if (columnIndex == COLUMN_IDX_IND_NAME) {
            return dependency.getDependentClass().getLanguage();
        }
        if (columnIndex == COLUMN_IDX_IND_NAME_OF_CLASS) {
            return dependency.getIndependentClass().getSimpleName();
        }
        if (columnIndex == COLUMN_IDX_RELATIONSHIPTYPE) {
            return dependency.getRelationshipType();
        }
        if (columnIndex == COLUMN_IDX_DEP_NAME) {
            return dependency.getDependentClass().getLanguage();
        }
        if (columnIndex == COLUMN_IDX_DEP_NAME_OF_CLASS) {
            return dependency.getDependentClass().getSimpleName();
        }
        throw new IllegalArgumentException("Invalid columnIndex: " + columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_CLASSES[columnIndex];
    }

    public void setNewContent(final List<ProgramEntityRelationship> classDependencies) {
        _dependencyList.clear();
        _dependencyList.addAll(classDependencies);
        fireTableDataChanged();
    }

    public void setNewContent(final Set<ProgramEntityRelationship> classDependencies) {
        _dependencyList.clear();
        _dependencyList.addAll(classDependencies);
        fireTableDataChanged();
    }
}
