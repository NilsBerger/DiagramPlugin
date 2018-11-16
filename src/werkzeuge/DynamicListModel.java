

package werkzeuge;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DynamicListModel<E> extends AbstractListModel<E> {
    private final List<E> _entries;

    public DynamicListModel(final List<E> entries)
    {
        this._entries = entries;
    }
    @Override
    public int getSize() {
        return _entries.size();
    }

    @Override
    public E getElementAt(int index) {
        return _entries.get(index);
    }

    public void addEntry(final E newEntry)
    {
        _entries.add(newEntry);
        fireIntervalAdded(this,getSize()-1,getSize()-1);
    }

    public void removeEntry(final E newEntry)
    {
        _entries.remove(newEntry);
        fireIntervalAdded(this,getSize()-1,getSize()-1);
    }
    public void setNewContent(final List<E> newEntries)
    {
        _entries.clear();
        _entries.addAll(newEntries);
        fireContentsChanged(this, 0, getSize()-1);
    }
    public void rowContentChanged(final int startIndex, final int endIndex)
    {
        fireContentsChanged(this, startIndex, endIndex);
    }
    public boolean contains(E e)
    {
        return _entries.contains(e);
    }

    public void reload()
    {
        rowContentChanged(0, _entries.size()-1);
    }


}
