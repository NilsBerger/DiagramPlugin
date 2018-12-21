package werkzeuge.finalcontextwerkzeug;

import service.functional.ChangePropagationProcess;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.ui.components.JBPanel;
import materials.ClassNode;
import service.functional.GraphChangeListener;
import valueobjects.ClassLanguageType;
import valueobjects.Marking;
import javafx.collections.SetChangeListener;
import werkzeuge.tracebilitychooserwerkzeug.TraceabilityChooserWerkzeug;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FinalContextWerkzeug implements GraphChangeListener {

    private final FinalContextWerkzeugUI _ui;
    private final ChangePropagationProcess _cpProcess;
    private JBPopupMenu _popup;
    private final JBMenuItem _inspectedMenuItem = new JBMenuItem("Inspected");
    private final JBMenuItem _propagatesMenuItem = new JBMenuItem("Propagates");
    private final JBMenuItem _changedMenuItem = new JBMenuItem("Changed");
    private final JBMenuItem _showSourcecodeItem = new JBMenuItem("Show sourcecode");
    private final JBMenuItem _showCorrespondingClassItem = new JBMenuItem("Show corresponding class in other platform");

    private final ClassLanguageType _type;
    private ClassNode _selectedClass;

    public FinalContextWerkzeug(final String text, ClassLanguageType type)
    {
        _ui = new FinalContextWerkzeugUI();
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
        _cpProcess.addGraphChangeListener(this);
        registerUIActions();

    }
    private void addEntry(final ClassNode classnode)
    {
        if(classnode.getClassLanguageType() == _type)
        {
            _ui.getModel().addEntry(classnode);
        }
    }
    private void removeEntry(final ClassNode classnode)
    {
        _ui.getModel().removeEntry(classnode);
    }

    private void registerUIActions() {
        _cpProcess.getAffectedClassesByChange().addListener(new SetChangeListener<ClassNode>(){
            @Override
            public void onChanged(Change<? extends ClassNode> change) {
                if (change.wasAdded()) {
                    if(!_ui.getModel().contains(change.getElementAdded()))
                    {
                        addEntry(change.getElementAdded());
                    }

                }
                if (change.wasRemoved()) {
                    //removeEntry(change.getElementRemoved());
                }
            }
        });
        _ui.getJBList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e))
                {
                    if(_ui.getJBList().getSelectedValue() instanceof ClassNode)
                    {
                        ClassNode classNode = (ClassNode) _ui.getJBList().getSelectedValue();
                        //TODO Zoom
                    }

                }
                if (SwingUtilities.isRightMouseButton(e)
                        && !_ui.getJBList().isSelectionEmpty()
                        && _ui.getJBList().locationToIndex(e.getPoint()) == _ui.getJBList().getSelectedIndex())
                {
                   _selectedClass = (ClassNode) _ui.getJBList().getSelectedValue();
                   _popup.show(_ui.getJBList(),e.getX(),e.getY());

                }
            }
        });
        _propagatesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _cpProcess.update(_selectedClass, Marking.PROPAGATES);
            }
        });
        _changedMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _cpProcess.update(_selectedClass, Marking.CHANGED);
                _selectedClass.setMarking(Marking.CHANGED);
            }
        });
        _inspectedMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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

    @Override
    public void updateView() {
        _ui.getPanel().revalidate();
        _ui.getModel().reload();
        _ui.getJBList().repaint();
        _ui.getJBList().revalidate();
        _ui.getRenderer().repaint();
    }

    public JBPanel getPanel()
    {
        return _ui.getPanel();
    }

    public JList getList()
    {
        return  _ui.getJBList();
    }


}
