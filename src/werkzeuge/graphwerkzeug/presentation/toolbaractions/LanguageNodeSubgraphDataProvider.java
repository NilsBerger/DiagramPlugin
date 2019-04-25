package werkzeuge.graphwerkzeug.presentation.toolbaractions;

import com.intellij.openapi.graph.base.DataProvider;
import com.intellij.openapi.graph.base.Node;
import materials.ProgramEntity;
import valueobjects.Language;
import werkzeuge.graphwerkzeug.ImpactAnalysisGraph;

public class LanguageNodeSubgraphDataProvider implements DataProvider {

    private final ImpactAnalysisGraph _impactAnalysisGraph;
    private final Language _language;


    public LanguageNodeSubgraphDataProvider(ImpactAnalysisGraph impactAnalysisGraph, Language language) {
        _impactAnalysisGraph = impactAnalysisGraph;
        _language = language;
    }

    @Override
    public Object get(Object o) {
        return null;
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
        if (o instanceof Node) {
            final ProgramEntity programEntity = _impactAnalysisGraph.getProgramEnitity((Node) o);
            if (programEntity != null) {
                return programEntity.getLanguage() == _language;
            }
        }
        System.out.println("Could not map Node to ClassNode");
        return false;
    }
}
