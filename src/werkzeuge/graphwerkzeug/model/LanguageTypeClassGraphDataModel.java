package werkzeuge.graphwerkzeug.model;

import materials.ClassDependency;
import materials.ClassNode;
import valueobjects.ClassLanguageType;

import java.util.Set;

public class LanguageTypeClassGraphDataModel extends GeneralClassGraphDataModel {

    private final ClassLanguageType _languageType;
    public LanguageTypeClassGraphDataModel(ClassLanguageType classLanguageType) {
        super();
        _languageType = classLanguageType;
    }

    @Override
    public void refreshDataModel(final ClassNode changedClassNode)
    {
        if(changedClassNode.getType() == _languageType)
        {
            addNode(changedClassNode);
            Set<ClassDependency> affectedDependencies = _changePropagationProcess.getAffectedDependencies(changedClassNode);
            addAll(affectedDependencies);

        }
    }

}
