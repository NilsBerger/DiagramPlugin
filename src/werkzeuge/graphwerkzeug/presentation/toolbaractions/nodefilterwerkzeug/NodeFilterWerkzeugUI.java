package werkzeuge.graphwerkzeug.presentation.toolbaractions.nodefilterwerkzeug;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBPanel;

import javax.swing.*;
import java.awt.*;

public class NodeFilterWerkzeugUI {

    private static final String DIALOG_TITEL = "Options to filter the graph";

    private JDialog _jdialog;
    private JBCheckBox _changedCheckbox;
    private JBCheckBox _propagatesCheckbox;
    private JBCheckBox _nextCheckbox;
    private JBCheckBox _inspectedCheckbox;
    private JBCheckBox _blankCheckbox;
    private JBCheckBox _apiNodeFilterCheckbox;
    private JBCheckBox _hiddenNodeFilterCheckbox;


    public NodeFilterWerkzeugUI() {
        initDialog();
    }

    private void initDialog() {
        _jdialog = new JDialog((JFrame) null, DIALOG_TITEL);
        _jdialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        _jdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        _jdialog.setLayout(new BorderLayout());
        _jdialog.add(createCheckboxPanel());

        _jdialog.pack();
        _jdialog.setResizable(false);

    }

    private JPanel createCheckboxPanel() {
        JBPanel checkboxPanel = new JBPanel<>();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));

        _changedCheckbox = new JBCheckBox("Changed");
        _propagatesCheckbox = new JBCheckBox("Propagates");
        _nextCheckbox = new JBCheckBox("Next");
        _inspectedCheckbox = new JBCheckBox("Inspected");
        _blankCheckbox = new JBCheckBox("Blank");
        _apiNodeFilterCheckbox = new JBCheckBox("API");
        _hiddenNodeFilterCheckbox = new JBCheckBox("Hidden");

        checkboxPanel.add(_changedCheckbox);
        checkboxPanel.add(_propagatesCheckbox);
        checkboxPanel.add(_nextCheckbox);
        checkboxPanel.add(_inspectedCheckbox);
        checkboxPanel.add(_blankCheckbox);
        checkboxPanel.add(_apiNodeFilterCheckbox);
        checkboxPanel.add(_hiddenNodeFilterCheckbox);

        return checkboxPanel;
    }

    public void openDialog() {
        _jdialog.setLocationRelativeTo(null);
        _jdialog.setVisible(true);
    }

    public void hide() {
        _jdialog.setVisible(false);
    }

    public JBCheckBox getChangedCheckbox() {
        return _changedCheckbox;
    }

    public JBCheckBox getPropagatesCheckbox() {
        return _propagatesCheckbox;
    }

    public JBCheckBox getNextCheckbox() {
        return _nextCheckbox;
    }

    public JBCheckBox getInspectedCheckbox() {
        return _inspectedCheckbox;
    }

    public JBCheckBox getBlankCheckbox() {
        return _blankCheckbox;
    }

    public JBCheckBox getApiNodeFilterCheckbox() {
        return _apiNodeFilterCheckbox;
    }

    public JBCheckBox getHiddenNodeFilterCheckbox() {
        return _hiddenNodeFilterCheckbox;
    }

    public JDialog getDialog() {
        return _jdialog;
    }
}
