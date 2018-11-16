package werkzeuge.graphwerkzeug.model;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.builder.CustomGraphUpdater;
import com.intellij.openapi.graph.util.GraphHider;
import com.intellij.openapi.graph.view.Graph2D;
import com.intellij.openapi.graph.view.Graph2DView;
import materials.ClassDependency;
import materials.ClassNode;
import service.ClassNodeFilter;
import werkzeuge.graphwerkzeug.presentation.ClassGraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClassGraphFilterer extends CustomGraphUpdater {

    private GraphHider _graphHider;
    private ClassGraph _classGraph;
    //private Map<String, Boolean> _edgeShouldHideMap;
    private Map<ClassNode, Boolean> _nodeShouldHideMap;

    public ClassGraphFilterer(ClassGraph classGraph) {
        _classGraph = classGraph;
        //_edgeShouldHideMap = new HashMap<>();
        _nodeShouldHideMap = new HashMap<>();
    }

    @Override
    public void update(Graph2D graph2D, Graph2DView graph2DView)
    {
        _graphHider = GraphManager.getGraphManager().createGraphHider(graph2D);
        _graphHider.setFireGraphEventsEnabled(true);
        _graphHider.unhideAll();
        Collection<ClassNode> nodes = _classGraph.getDataModel().getNodes();
        for(ClassNode node : nodes)
        {
            if(shouldFilter(node))
            {
                filter(node);
            }
        }

        //Edge Filter?
    }

    public void unfilterAll() {
        _graphHider.unhideAll();
    }

    public void filter(Node node)
    {
        _graphHider.hide(node);
    }

    public void filter(ClassNode classGraphNode)
    {
        Node node = _classGraph.getNode(classGraphNode);
        if(node != null)
        {
            filter(node);
        }
    }

    public boolean shouldFilter(ClassNode node)
    {
        return ClassNodeFilter.isClassNodeFromAPI(node);
    }
//    public boolean shouldFilter(ClassGraphNode.Type type)
//    {
//        Boolean shoulHide = _nodeShouldHideMap.get(type);
//        return shoulHide != null && shoulHide;
//    }

//    public setShouldFilter(St)
//    {
//        _edgeShouldHideMap.put()
//    }
    public boolean shouldFilter(ClassDependency edge )
    {
        return true;
    }

    public void setShouldFilter(ClassNode node, boolean value)
    {

    }
    public void clear()
    {
        _nodeShouldHideMap.clear();
    }
}
