package werkzeuge;

import materials.ProgramEntity;
import valueobjects.Language;
import valueobjects.Marking;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ContextTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Status", "Name"};
    private static final Class<?>[] COLUMN_CLASSES = {Marking.class, String.class};

    public static final int COLUMN_IDX_MARKING = 0;
    public static final int COLUMN_IDX_NAME = 1;

    private final List<ProgramEntity> _programEntities;
    private Language _Language;

    public ContextTableModel(List<ProgramEntity> programEntities) {
        _programEntities = new ArrayList<>(programEntities);
    }

    public ContextTableModel(List<ProgramEntity> programEntities, Language language) {
        _programEntities = new ArrayList<>(programEntities);
        _Language = language;
    }

    @Override
    public int getRowCount() {
        return _programEntities.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (_programEntities.size() > rowIndex) {
            final ProgramEntity programEntity = _programEntities.get(rowIndex);
            if (columnIndex == COLUMN_IDX_MARKING) {
                return programEntity.getMarking();
            }
            if (columnIndex == COLUMN_IDX_NAME) {
                return programEntity.getSimpleName();
            }
        }
        throw new IllegalArgumentException("Invalid columnIndex: " + columnIndex);

    }

    public ProgramEntity getClassNodeFromRow(int rowIndex) {
        if (rowIndex < 0) {
            return null;
        }
        return _programEntities.get(rowIndex);
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_CLASSES[columnIndex];
    }

    public void addEntry(final ProgramEntity programEntity) {
        _programEntities.add(programEntity);
        final int newRowIndex = _programEntities.size() - 1;
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void setNewContent(final Set<ProgramEntity> newProgramEntities) {
        for (ProgramEntity programEntity : newProgramEntities) {
            if (_Language != null) {
                if (!_programEntities.contains(programEntity) && programEntity.getLanguage() == _Language) {
                    addEntry(programEntity);
                }
            } else {
                _programEntities.addAll(newProgramEntities);
            }
        }
        fireTableDataChanged();
    }

    public void clearAllContent() {
        _programEntities.clear();
    }

    public boolean contains(ProgramEntity programEntity) {
        return _programEntities.contains(programEntity);
    }
}

