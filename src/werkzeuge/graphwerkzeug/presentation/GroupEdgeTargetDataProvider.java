package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.base.DataProvider;
import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.view.Arrow;
import com.intellij.openapi.graph.view.EdgeRealizer;
import com.intellij.openapi.graph.view.Graph2D;
import materials.ClassDependency;

/**
 * DataProviderKey for Edges in a ClassGraph-Diagram, that use the same Port.
 */
public class GroupEdgeTargetDataProvider implements DataProvider {
    private final ClassGraph _classGraph;
    private final Graph2D _graph;


    public GroupEdgeTargetDataProvider(ClassGraph classGraph, Graph2D graph)
    {
        _classGraph = classGraph;
        _graph = graph;
    }
    @Override
    public Object get(Object o) {
       if(o instanceof Edge)
       {
           Edge edge = (Edge)o;
           final ClassDependency classDependency = _classGraph.getClassGraphEdge(edge);
           if(classDependency != null)
           {
               String simpleName = classDependency.getIndependentClass().getSimpleName();
               final String relationshipTypeName = classDependency.getRelationshipType().name();

               StringBuilder builder = new StringBuilder();
               builder.append(simpleName);
               builder.append(relationshipTypeName);
               EdgeRealizer edgeRealizer = _graph.getRealizer(edge);
               if(edgeRealizer.getArrow().equals(Arrow.WHITE_DELTA))
               {
                   return edgeRealizer.getLineType();
               }
               return null;
           }
           else{
               System.out.println("Failnode");
               return null;
           }
       }
       else{
           return null;
       }
    }

    @Override
    public int getInt(Object o) {
        return 0;
    }

    @Override
    public double getDouble(Object o) {
        return 0;
    }

    @Override
    public boolean getBool(Object o) {
        return false;
    }
}
