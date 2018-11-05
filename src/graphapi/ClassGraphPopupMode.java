package graphapi;

import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.view.PopupMode;

import javax.swing.*;

public class ClassGraphPopupMode extends PopupMode {
    private ClassGraph _classGraph;
    private PopupMode _defaultPopupMode;

    public ClassGraphPopupMode(final ClassGraph classGraph, final PopupMode defaultPopupMode)
    {
        this._classGraph = classGraph;
        this._defaultPopupMode = defaultPopupMode;
    }

    @Override
    public JPopupMenu getNodePopup(Node v) {
        return new JPopupMenu();
    }

    @Override
    public JPopupMenu getSelectionPopup(double x, double y) {
        if (_defaultPopupMode != null) {
            return _defaultPopupMode.getSelectionPopup(x, y);
        } else {
            return null;
        }
    }

    @Override
    public JPopupMenu getPaperPopup(double x, double y) {
        if (_defaultPopupMode != null) {
            return _defaultPopupMode.getPaperPopup(x, y);
        } else {
            return null;
        }
    }
}
