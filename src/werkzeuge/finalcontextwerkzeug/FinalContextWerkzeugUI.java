package werkzeuge.finalcontextwerkzeug;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import materials.ClassNode;
import werkzeuge.ClassNodeCellRenderer;
import werkzeuge.DynamicListModel;

import javax.swing.*;
import java.util.ArrayList;

public class FinalContextWerkzeugUI {

    private  JBPanel _mainPanel;
    private JBList _finalContextList;
    private DynamicListModel<ClassNode> _model;
    private ClassNodeCellRenderer _renderer;
    private JBLabel _label;

    public FinalContextWerkzeugUI()
    {
        _finalContextList = new JBList();
        _model = new DynamicListModel<ClassNode>(new ArrayList<ClassNode>());
        _renderer = new ClassNodeCellRenderer();
        _finalContextList.setModel(_model);
        _finalContextList.setCellRenderer(_renderer);
        createLabel();
        createMainPanel();

    }

    private void createMainPanel() {
        _mainPanel = new JBPanel();
        _mainPanel.setLayout(new BoxLayout(_mainPanel, BoxLayout.Y_AXIS));
        _mainPanel.add(_label);

        _mainPanel.add(new JBScrollPane(_finalContextList));
    }

    private void createLabel()
    {
        _label = new JBLabel();
    }



    public void setLabelText(final String text)
    {
        _label.setText(text);
    }
    public JBList getJBList()
    {
        return _finalContextList;
    }
    public DynamicListModel<ClassNode> getModel()
    {
        return _model;
    }
    public ClassNodeCellRenderer getRenderer() {return _renderer;}
    public JBPanel getPanel()
    {
        return _mainPanel;
    }
}
