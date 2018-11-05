package werkzeuge.tracebilitychooserwerkzeug;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import werkzeuge.DynamicListModel;

import java.util.ArrayList;
import java.util.List;

public class TracebilityChooserWerkzeugUI {

    private JBPopup _popup;
    private JBPanel _panel;
    private JBList<TraceabilityLink> _tracebilitylist;
    private DynamicListModel<TraceabilityLink> _model;

    public TracebilityChooserWerkzeugUI()
    {
        _model = new DynamicListModel<>(new ArrayList<>());
        _panel = new JBPanel();
        _tracebilitylist = new JBList();
        _tracebilitylist.setCellRenderer(new TraceablityChooserListCellRenderer());
        _tracebilitylist.setModel(_model);
        _panel.add(_tracebilitylist);
        JBPopupFactory factory = ServiceManager.getService(JBPopupFactory.class);
        ComponentPopupBuilder popupBuilder = factory.createComponentPopupBuilder(_panel,_tracebilitylist);
        _popup = popupBuilder.createPopup();
    }

    public void show()
    {
        _popup.showInFocusCenter();
        _panel.grabFocus();
    }

    public void setContent(final List<TraceabilityLink> traceabilityLinkList)
    {
        _model.setNewContent(traceabilityLinkList);
    }

    public JBPopup getPopup()
    {
        return _popup;
    }

    public JBList<TraceabilityLink> getJBList()
    {
        return _tracebilitylist;
    }
}
