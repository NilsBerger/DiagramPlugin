package werkzeuge.initialcontextwerkzeug;

import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import materials.ClassNode;
import werkzeuge.ClassNodeCellRenderer;
import werkzeuge.DynamicListModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InitialContextWerkzeugUI {

    private  JBPanel _mainPanel;
    private JBList _initialContextList;
    private DynamicListModel<ClassNode> _model;
    private JBLabel _label;
   //private ToolbarDecorator _toolbarDecorator;

    public InitialContextWerkzeugUI()
    {
        createLabel();
        createJBList();
        createMainPanel();
        _initialContextList.setCellRenderer(new ClassNodeCellRenderer());
        //initToolbar();
    }

    private void createMainPanel() {
        _mainPanel = new JBPanel();
        _mainPanel.setLayout(new BoxLayout(_mainPanel, BoxLayout.Y_AXIS));
        _initialContextList.setModel(_model);
        _mainPanel.add(_label);
        _label.setAlignmentY(Component.CENTER_ALIGNMENT);
        _mainPanel.add(new JBScrollPane(_initialContextList));
    }

    private void createLabel()
    {
        _label = new JBLabel();
    }

    public void createJBList()
    {
        _initialContextList = new JBList();
        _model = new DynamicListModel<>(new ArrayList<ClassNode>());
    }

   // private void initToolbar()
   // {
   //// }

    public void setLabelText(final String text)
    {
        _label.setText(text);
    }
    public JBList getJBList()
    {
        return _initialContextList;
    }
    public DynamicListModel<ClassNode> getModel()
    {
        return _model;
    }

    public JBPanel getPanel()
    {
        return _mainPanel;
    }

    //public ToolbarDecorator getToolbarDecorator()
   // {
   //     return _toolbarDecorator;
    //}
}
