package werkzeuge.graphwerkzeug.model;

import javafx.collections.ObservableSet;
import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import service.functional.ClassDependencyChangeListener;
import service.functional.ClassNodeChangeListener;
import valueobjects.Marking;

import java.util.Set;

public class EvalImpactAnalysisGraphDataModel extends ImpactAnalysisGraphDataModel implements ClassNodeChangeListener, ClassDependencyChangeListener {

    @Override
    public void addNode(ProgramEntity node) {
        if (isAffected(node)) {
            super.addNode(node);
        }
    }

    public void refreshDataModel(final ProgramEntity changedProgramEntity) {

        //refreshNeighbourhood(changedClassNode);

    }

    public void refreshNeighbourhood(final ProgramEntity programEntity) {
        //addNode(classNode);
        //Set<ClassDependency> affectedDependencies = _changePropagationProcess.getAffectedDependencies(classNode);
        //addAll(affectedDependencies);
    }


    @Override
    public void updateView() {
        clear();
        final Set<ProgramEntity> affectedProgramEntities = _changePropagationProcess.getAffectedClassesByChange();
        for (ProgramEntity node : affectedProgramEntities) {
            if (isAffected(node)) {
                addNode(node);
            }
        }

        final ObservableSet<ProgramEntityRelationship> affectedDependenciesByChange = _changePropagationProcess.getAffectedDependenciesByChange();
        for (ProgramEntityRelationship dependency : affectedDependenciesByChange) {
            if (isAffected(dependency.getDependentClass()) && isAffected(dependency.getIndependentClass())) {
                addEdge(dependency);
            }
        }

    }

    private static boolean isAffected(final ProgramEntity node) {
        if (node.getMarking() == Marking.CHANGED || node.getMarking() == Marking.PROPAGATES) {
            return true;
        }
        return false;
    }


}