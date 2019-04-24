package werkzeuge;

import javax.swing.*;

public class ContainsFilter extends RowFilter<Object, Object> {
    private String _filterValue = "";

    public ContainsFilter(final String filterValue) {
        this._filterValue = filterValue.toLowerCase();
    }

    @Override
    public boolean include(Entry<? extends Object, ? extends Object> entry) {

        if (_filterValue.isEmpty()) {
            return true;
        }
        for (int i = 0; i < entry.getValueCount(); i++) {
            if (entry.getStringValue(i).toLowerCase().contains(_filterValue)) {
                return true;
            }
        }
        return false;
    }

    public void setFilterValue(final String filterValue) {
        this._filterValue = filterValue.toLowerCase();
    }
}
