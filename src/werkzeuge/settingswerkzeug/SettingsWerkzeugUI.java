package werkzeuge.settingswerkzeug;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.VerticalFlowLayout;

import javax.swing.*;

class SettingsWerkzeugUI {
    private JPanel _jPanel;
    private JLabel _androidPathLabel;
    private JLabel _swiftPathLabel;
    private JLabel _swiftSchemePathLabel;
    private JButton _generateDepModelButton;
    private TextFieldWithBrowseButton _androidTextFieldWithBrowseButton;
    private TextFieldWithBrowseButton _swingTextFieldWithBrowseButton;
    private TextFieldWithBrowseButton _swingSchemeTextFieldWithBrowseButton;


    SettingsWerkzeugUI() {
        _jPanel = new JPanel();
        _jPanel.setLayout(new VerticalFlowLayout(true, false));
        _jPanel.add(new JSeparator());
        createAndroidLabel();
        _jPanel.add(new JSeparator());
        createSwiftPathComponent();
        createSwiftSchemeComponent();

        _generateDepModelButton = new JButton();
        _generateDepModelButton.setText("Generate Dependency Models");
        _jPanel.add(new JSeparator());
        _jPanel.add(_generateDepModelButton);

    }

    private void createSwiftPathComponent() {
        _swiftPathLabel = new JLabel();
        _swiftPathLabel.setText("Add path to Swift-Workspace");

        _swingTextFieldWithBrowseButton = new TextFieldWithBrowseButton();
        _swingTextFieldWithBrowseButton.setEditable(false);

        _jPanel.add(_swiftPathLabel);
        _jPanel.add(_swingTextFieldWithBrowseButton);
    }

    private void createSwiftSchemeComponent() {
        _swiftSchemePathLabel = new JLabel();
        _swiftSchemePathLabel.setText("Add path of Scheme-Folder");

        _swingSchemeTextFieldWithBrowseButton = new TextFieldWithBrowseButton();
        _swingSchemeTextFieldWithBrowseButton.setEditable(false);

        _jPanel.add(_swiftSchemePathLabel);
        _jPanel.add(_swingSchemeTextFieldWithBrowseButton);
    }

    TextFieldWithBrowseButton getSwingTextFieldWithBrowseButton()
    {
        return _swingTextFieldWithBrowseButton;
    }

    private void createAndroidLabel() {
        _androidPathLabel = new JLabel();
        _androidPathLabel.setText("Add path to Android-Workspace");


        _androidTextFieldWithBrowseButton = new TextFieldWithBrowseButton();
        _androidTextFieldWithBrowseButton.setEditable(false);

        _jPanel.add(_androidPathLabel);

        _jPanel.add(_androidTextFieldWithBrowseButton);
    }

    JPanel getJPanel()
    {
        return _jPanel;
    }

    TextFieldWithBrowseButton getSwingSchemeTextFieldWithBrowseButton() {
        return _swingSchemeTextFieldWithBrowseButton;
    }
    TextFieldWithBrowseButton getAndroidTextFieldWithBrowseButton(){return _androidTextFieldWithBrowseButton;}

    JButton getGenerateDepModelButton()
    {
        return _generateDepModelButton;
    }
}
