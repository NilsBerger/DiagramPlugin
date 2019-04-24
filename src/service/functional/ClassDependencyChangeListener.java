package service.functional;

import materials.ProgramEntityRelationship;

import java.util.Set;

public interface ClassDependencyChangeListener {

    void updateDependencies(Set<ProgramEntityRelationship> changedDependencies);


}
