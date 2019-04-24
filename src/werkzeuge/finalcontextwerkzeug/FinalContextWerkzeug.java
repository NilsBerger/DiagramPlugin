package werkzeuge.finalcontextwerkzeug;

import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.table.JBTable;
import javafx.collections.SetChangeListener;
import materials.ProgramEntity;
import service.functional.ChangePropagationProcess;
import service.functional.ClassNodeChangeListener;
import valueobjects.Language;
import valueobjects.Marking;
import werkzeuge.ContextTableModel;
import werkzeuge.tracebilitychooserwerkzeug.TraceabilityChooserWerkzeug;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FinalContextWerkzeug implements ClassNodeChangeListener {

    private final FinalContextWerkzeugUI _ui;
    private final ChangePropagationProcess _cpProcess;
    private JBPopupMenu _popup;
    private final JBMenuItem _inspectedMenuItem = new JBMenuItem("Inspected");
    private final JBMenuItem _propagatesMenuItem = new JBMenuItem("Propagates");
    private final JBMenuItem _changedMenuItem = new JBMenuItem("Changed");
    private final JBMenuItem _showSourcecodeItem = new JBMenuItem("Show sourcecode");
    private final JBMenuItem _showCorrespondingClassItem = new JBMenuItem("Show corresponding class in other platform");

    private final Language _type;
    private ProgramEntity _selectedClass;

    public FinalContextWerkzeug(final String text, final Language type) {
        _ui = new FinalContextWerkzeugUI(type);
        _ui.setLabelText(text);
        _type = type;

        _popup = new JBPopupMenu();
        _popup.add(_inspectedMenuItem);
        _popup.add(_propagatesMenuItem);
        _popup.add(_changedMenuItem);
        _popup.addSeparator();
        _popup.add(_showSourcecodeItem);
        _popup.add(_showCorrespondingClassItem);
        _cpProcess = ChangePropagationProcess.getInstance();
        _cpProcess.addClassNodeChangeListener(this);
        registerUIActions();
        registerTextfieldListeners();

    }

    private void addEntry(final ProgramEntity classnode) {
        if (classnode.getLanguage() == _type) {
            _ui.getModel().addEntry(classnode);
        }
        _ui.getModel().fireTableDataChanged();
    }

    private void registerUIActions() {
        _cpProcess.getAffectedClassesByChange().addListener(new SetChangeListener<ProgramEntity>() {
            @Override
            public void onChanged(Change<? extends ProgramEntity> change) {
                if (change.wasAdded()) {
                    if (!_ui.getModel().contains(change.getElementAdded())) {
                        addEntry(change.getElementAdded());
                    }

                }
                if (change.wasRemoved()) {
                    //removeEntry(changeInitial.getElementRemoved());
                }
                _ui.setAmount(calculateAffectedClassNodeAmount());
            }
        });
        _ui.getJBTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final int selectedRow = _ui.getJBTable().getSelectedRow();
                int convertedSelectedRow = _ui.getJBTable().convertRowIndexToModel(selectedRow);
                ProgramEntity programEntity = _ui.getModel().getClassNodeFromRow(convertedSelectedRow);

                if (programEntity != null) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        _selectedClass = programEntity;
                        _popup.show(_ui.getJBTable(), e.getX(), e.getY());
                    }
                }

            }
        });
        _propagatesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _selectedClass.setMarking(Marking.PROPAGATES);
                _cpProcess.update(_selectedClass, Marking.PROPAGATES);

            }
        });
        _changedMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _selectedClass.setMarking(Marking.CHANGED);
                _cpProcess.update(_selectedClass, Marking.CHANGED);

            }
        });
        _inspectedMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _selectedClass.setMarking(Marking.INSPECTED);
                _cpProcess.update(_selectedClass, Marking.INSPECTED);

            }
        });
        _showSourcecodeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TraceabilityChooserWerkzeug(_selectedClass, false);
            }
        });
        _showCorrespondingClassItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TraceabilityChooserWerkzeug(_selectedClass, true);
            }
        });
    }

    public void registerTextfieldListeners() {
        _ui.getTextField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _ui.getContainsFilter().setFilterValue(_ui.getTextField().getText());
                _ui.getRowSorter().sort();
            }
        });
    }

    @Override
    public void updateView() {
        _ui.getPanel().revalidate();
        _ui.getJBTable().repaint();
        _ui.getJBTable().revalidate();
    }

    public JBPanel getPanel() {
        return _ui.getPanel();
    }

    public JBTable getJBTable() {
        return _ui.getJBTable();
    }

    public ContextTableModel getTableModel() {
        return _ui.getModel();
    }

    private void reloadContent() {
        //_ui.getModel().clearAllContent();
        _ui.getModel().setNewContent(_cpProcess.getAffectedClassesByChange());
        _ui.setAmount(calculateAffectedClassNodeAmount());
    }

    private int calculateAffectedClassNodeAmount() {
        int size = 0;
        for (ProgramEntity programEntity : _cpProcess.getAffectedClassesByChange()) {
            if (programEntity.getLanguage() == _type) {
                ++size;
            }

        }
        return size;
    }

}
