/*
 * Copyright 1998-2018 Konstantin Bulenkov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package werkzeuge.graphwerkzeug;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.graph.builder.actions.*;
import com.intellij.openapi.graph.builder.actions.layout.ApplyCurrentLayoutAction;
import com.intellij.openapi.graph.builder.actions.printing.PrintGraphAction;
import com.intellij.openapi.graph.builder.actions.printing.PrintPreviewAction;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTabbedPane;
import org.jetbrains.annotations.NotNull;
import werkzeuge.StatusIcons;
import werkzeuge.ToolWindowWerkzeug;
import werkzeuge.graphwerkzeug.model.ClassGraphFilterer;
import werkzeuge.graphwerkzeug.presentation.ClassGraph;
import werkzeuge.graphwerkzeug.presentation.CustomLayouterAction;
import werkzeuge.graphwerkzeug.presentation.NodeFilterAction;
import werkzeuge.graphwerkzeug.presentation.NodeUnfilterAction;

import javax.swing.*;
import java.awt.*;

public class ClassGraphWindowWerkzeugUI {
    private final static Icon _ICON = StatusIcons.getBlankIcon();
    private final JComponent _myComponent;
    private ClassGraph _generalClassGraph;
    private ClassGraph _javaClassGraph;
    private ClassGraph _swiftClassGraph;

    private JTabbedPane _tabbedPane;



    public ClassGraphWindowWerkzeugUI(@NotNull ClassGraph classGraph, ClassGraph javaClassGraph, ClassGraph swiftClassGraph, ToolWindowWerkzeug werkzeug) {
        _generalClassGraph = classGraph;
        _javaClassGraph = javaClassGraph;
        _swiftClassGraph = swiftClassGraph;

        _myComponent = new JBPanel(new BorderLayout());

        DefaultActionGroup generalActions = getToolbarActionGroup(_generalClassGraph);
        DefaultActionGroup javaActions = getToolbarActionGroup(_javaClassGraph);
        DefaultActionGroup swiftActions = getToolbarActionGroup(_swiftClassGraph);
        ActionToolbar generalActionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, generalActions, true);
        ActionToolbar javaActionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, javaActions, true);
        ActionToolbar swiftActionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, swiftActions, true);

        _tabbedPane = new JBTabbedPane();

        JBPanel generalPane = new JBPanel();
        generalPane.setLayout(new BorderLayout());
        generalPane.add(generalActionToolbar.getComponent(), BorderLayout.NORTH);
        generalPane.add(_generalClassGraph.getView().getComponent(), BorderLayout.CENTER);
        _tabbedPane.insertTab("Combined Class Graph", _ICON, generalPane, "", 0);

        JSplitPane splitPane = new JSplitPane();

        JBPanel javaPane = new JBPanel();
        javaPane.setLayout(new BorderLayout());
        javaPane.add(javaActionToolbar.getComponent(), BorderLayout.NORTH);
        javaPane.add(_javaClassGraph.getView().getComponent(), BorderLayout.CENTER);

        JBPanel swiftPane = new JBPanel();
        swiftPane.setLayout(new BorderLayout());
        swiftPane.add(swiftActionToolbar.getComponent(), BorderLayout.NORTH);
        swiftPane.add(_swiftClassGraph.getView().getComponent(), BorderLayout.CENTER);

        splitPane.setLeftComponent(javaPane);
        splitPane.setRightComponent(swiftPane);

        _tabbedPane.insertTab("Seperated Class Graphs", _ICON, splitPane, "",1);


        _myComponent.add(_tabbedPane, BorderLayout.CENTER);
        _myComponent.add(werkzeug.getPanel(), BorderLayout.SOUTH);

        _generalClassGraph.initialize();

    }

    private DefaultActionGroup getToolbarActionGroup(final ClassGraph classgraph) {
        DefaultActionGroup actions = new DefaultActionGroup();
        actions.add(new ShowHideGridAction(classgraph.getGraph()));
        actions.add(new SnapToGridAction(classgraph.getGraph()));
        actions.addSeparator();

        actions.add(new ZoomInAction(classgraph.getGraph()));
        actions.add(new ZoomOutAction(classgraph.getGraph()));

        actions.add(new ActualZoomAction(classgraph.getGraph()));

        actions.add(new FitContentAction(classgraph.getGraph()));
        actions.addSeparator();

        actions.add(new ApplyCurrentLayoutAction(classgraph.getGraph()));
        //actions.add(new CustomLayouterAction(classgraph.getGraphBuilder(), _generalClassGraph.getGraphBuilder().getS));
        actions.addSeparator();

        //actions.add(new DeleteSelectionAction());
        actions.addSeparator();

        actions.add(new PrintGraphAction(classgraph.getGraph()));
        actions.add(new PrintPreviewAction(classgraph.getGraph()));
        actions.addSeparator();
        actions.add(new NodeFilterAction(classgraph));
        actions.add(new NodeUnfilterAction(classgraph));

        return actions;
    }

    @NotNull
    public JComponent getComponent() {
        return _myComponent;
    }

    public JTabbedPane getTabbedPane()
    {
        return _tabbedPane;
    }

    public ClassGraph getGeneralClassGraph()
    {
        return _generalClassGraph;
    }
    public ClassGraph getJavaClassGraph()
    {
        return _javaClassGraph;
    }
    public ClassGraph getSwiftClassGraph()
    {
        return _swiftClassGraph;
    }

}



