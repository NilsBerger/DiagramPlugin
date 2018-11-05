/*
 * Copyright 1998-2018 Konstantin Bulenkov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package werkzeuge;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileMutableModel implements MutableComboBoxModel {

    private List<File> _files = new ArrayList<>();
    private File _selectedItem;

    public void addElements(final List<File> filelist)
    {
        _files.addAll(filelist);
    }

    @Override
    public void addElement(Object item) {
    _files.add((File) item);

    }

    @Override
    public void removeElement(Object obj) {
        if(_files.contains(obj))
        {
            _files.remove(obj);
        }
    }

    @Override
    public void insertElementAt(Object item, int index) {
        _files.add(index,(File) item);
    }

    @Override
    public void removeElementAt(int index) {
        _files.remove(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        _selectedItem = (File) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return _selectedItem;
    }

    @Override
    public int getSize() {
        return _files.size();
    }

    @Override
    public Object getElementAt(int index) {
        return _files.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
