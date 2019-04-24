package werkzeuge.tracebilitychooserwerkzeug;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import materials.ProgramEntity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class TracebilityChooserWerkzeugUI {

    private JBPopup _popup;
    private JBPanel _panel;
    private JBLabel _titelLabel;
    private JBTable _tracebilityTable;
    private TraceabilityTableModel _tableModel;

    private JButton _selectButton;
    private JButton _showCorrespondingButton;

    public TracebilityChooserWerkzeugUI() {
        _panel = new JBPanel();
        _panel.setLayout(new BorderLayout());
        _tableModel = new TraceabilityTableModel();
        _tracebilityTable = new JBTable(_tableModel);
        _tracebilityTable.setDefaultRenderer(Double.class, new TraceabilityListCellRenderer());
        _tracebilityTable.setDefaultRenderer(String.class, new TraceabilityListCellRenderer());
        _tracebilityTable.setAutoCreateRowSorter(true);
        _tracebilityTable.getTableHeader().setReorderingAllowed(false);
        initColumnWidths(_tracebilityTable);
        _panel.add(new JBScrollPane(_tracebilityTable), BorderLayout.CENTER);

        _titelLabel = new JBLabel();
        _titelLabel.setHorizontalAlignment(SwingUtilities.CENTER);
        _panel.add(_titelLabel, BorderLayout.NORTH);

        _selectButton = new JButton();
        _showCorrespondingButton = new JButton();
        _panel.add(erstelleBottomPanel(), BorderLayout.SOUTH);
        _panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));


        JBPopupFactory factory = ServiceManager.getService(JBPopupFactory.class);
        ComponentPopupBuilder popupBuilder = factory.createComponentPopupBuilder(_panel, _tracebilityTable);
        _panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        _popup = popupBuilder.createPopup();
        _popup.pack(true, true);
    }



    public void setContent(final List<TraceabilityLink> traceabilityLinkList) {
        _tableModel.setContent(traceabilityLinkList);
    }

    private static void initColumnWidths(final JTable table) {
        final TableColumnModel tableColumnModel = table.getColumnModel();

        tableColumnModel.getColumn(0).setPreferredWidth(75);
        tableColumnModel.getColumn(1).setPreferredWidth(150);
        tableColumnModel.getColumn(2).setPreferredWidth(150);
    }

    private JPanel erstelleBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        _selectButton = new JButton("Select");
        _showCorrespondingButton = new JButton("Show sourcecode");
        bottomPanel.add(_showCorrespondingButton);
        bottomPanel.add(_selectButton);

        return bottomPanel;
    }

    public void setTitle(ProgramEntity sourceProgramEntity) {
        setTitle(sourceProgramEntity.getSimpleName());
    }

    public void setTitle(String title) {
        _titelLabel.setText(title);
    }

    public void show() {
        _popup.showInFocusCenter();
        _panel.grabFocus();
    }
    public void hide()
    {
        _popup.setUiVisible(false);
    }

    public JButton getSelectButton() {
        return _selectButton;
    }

    public JBPopup getPopup() {
        return _popup;
    }

    public JButton getShowCorrespondingButton() {
        return _showCorrespondingButton;
    }

    public JBTable getJBTable() {
        return _tracebilityTable;
    }

    public TraceabilityTableModel getTracebilityTableModel()
    {
        return _tableModel;
    }

    private static DefaultTableCellRenderer getDefaultTableCellRenderer()
    {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        return centerRenderer;
    }
}
