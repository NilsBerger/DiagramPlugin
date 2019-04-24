package werkzeuge;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;


public class ToolWindowWerkzeugUI extends SimpleToolWindowPanel {



    private JBPanel _panel;
    private JBPanel _topPanel;
    private JBLabel _selectClassDependencyGraph;
    private ComboBox<File> _fileComboBox;
    private JBPanel _listPanel;

    public ToolWindowWerkzeugUI(JComponent toolBar, JPanel initialContext, JPanel finalContextJava, JPanel finalContextSwift, JPanel tracelinkPanel) {

        super(true, true);

        _panel = new JBPanel();
        _panel.setLayout(new BorderLayout());
        add(_panel);
        setBorder(new EmptyBorder(10,10,10,10));

        createSelectClassDependencyGraph();
        _listPanel = new JBPanel();
 //       _listPanel.setLayout(new GridBagLayout());
//
//        GridBagConstraints c = new GridBagConstraints();
//        c.insets = new Insets(5,5,5,5);
//        c.weightx = 1.0;
//        c.fill = GridBagConstraints.HORIZONTAL;
//        //c.gridheight = GridBagConstraints.REMAINDER;
//        c.weighty = 0.0;
//
//        c.gridx = 0;
//        c.gridy = 0;
//        _listPanel.add(initialContext, c);
//
//        c.gridx = 1;
//        c.gridy = 0;
//        _listPanel.add(finalContextJava, c);
//
//        c.gridx = 2;
//        c.gridy = 0;
//        _listPanel.add(finalContextSwift, c);
//
//        c.gridx = 3;
//        c.gridy = 0;
//        _listPanel.add(tracelinkPanel, c);
//        _listPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        _listPanel.setLayout(new BoxLayout(_listPanel, BoxLayout.X_AXIS));
        _listPanel.setBorder(new EmptyBorder(10,10,10,10));
        initialContext.setBorder(new EmptyBorder(5,5,5,5));
        finalContextJava.setBorder(new EmptyBorder(5,5,5,5));
        finalContextSwift.setBorder(new EmptyBorder(5,5,5,5));
        tracelinkPanel.setBorder(new EmptyBorder(5,5,5,5));
        _listPanel.add(initialContext);
        _listPanel.add(finalContextJava);
        _listPanel.add(finalContextSwift);
        _listPanel.add(tracelinkPanel);

        _panel.add(toolBar, BorderLayout.NORTH);
        //final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,_listPanel, );
        //splitPane.
        //JBScrollPane scrollPane = new JBScrollPane(_listPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        _panel.add(_listPanel, BorderLayout.CENTER);

    }

    private void createSelectClassDependencyGraph() {
        _selectClassDependencyGraph = new JBLabel();
        _selectClassDependencyGraph.setText("Select Class Dependency Graph");
        _panel.add(_selectClassDependencyGraph, BorderLayout.NORTH);
    }
    public JBLabel getselectClassDependencyGraph() {
        return _selectClassDependencyGraph;
    }

    public JPanel getPanel()
    {
        return _panel;
    }

}