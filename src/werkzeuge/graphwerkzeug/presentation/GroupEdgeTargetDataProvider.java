package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.base.DataProvider;
import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.base.Graph;
import materials.ProgramEntityRelationship;
import valueobjects.RelationshipType;
import werkzeuge.graphwerkzeug.ImpactAnalysisGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * DataProviderKey for Edges in a ClassGraph-Diagram, that use the same Port.
 */
public class GroupEdgeTargetDataProvider implements DataProvider {
    private final ImpactAnalysisGraph _impactAnalysisGraph;
    private final Graph _graph;
    private final Set<String> TARGET_ID_KEYS = new HashSet();
    private final HashMap<String, String> _target_id_key_map = new HashMap<>();


    public GroupEdgeTargetDataProvider(ImpactAnalysisGraph impactAnalysisGraph, Graph graph)
    {
        _impactAnalysisGraph = impactAnalysisGraph;
        _graph = graph;
    }
    @Override
    public Object get(Object o) {
       if(o instanceof Edge)
       {
           Edge edge = (Edge)o;
           final ProgramEntityRelationship programEntityRelationship = _impactAnalysisGraph.getProgramEntityRelationship(edge);
           {
               final RelationshipType relationshipType = programEntityRelationship.getRelationshipType();
               String simpleName = programEntityRelationship.getIndependentClass().getSimpleName();
               final String relationshipTypeName = programEntityRelationship.getRelationshipType().name();

               StringBuilder builder = new StringBuilder();
               builder.append(simpleName);
               builder.append(relationshipTypeName);
               if (relationshipType == RelationshipType.Implements || relationshipType == RelationshipType.Extends) {
                   String key = builder.toString();
                   if (!_target_id_key_map.containsKey(key)) {
                       _target_id_key_map.put(key, key);
                   }
                   return _target_id_key_map.get(key);
               }
           }
       }
        return null;
    }
//        if(o instanceof Edge)
//        {
//            Edge edge = (Edge) o;
//            EdgeRealizer edgeRealizer = _graph.getRealizer(edge);
//            final ProgramEntityRelationship relationship = _impactAnalysisGraph.getProgramEntityRelationship(edge);
//            if(relationship.getRelationshipType() == RelationshipType.Extends|| relationship.getRelationshipType() == RelationshipType.Implements)
//            {
//                return edgeRealizer.getLineType();
//            }
//        }
//        return null;


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
