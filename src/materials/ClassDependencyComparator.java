package materials;

import java.util.Comparator;

public class ClassDependencyComparator implements Comparator<ProgramEntityRelationship> {
    @Override
    public int compare(ProgramEntityRelationship o1, ProgramEntityRelationship o2) {
        if (o1.getSortOrder() < o2.getSortOrder()) {
            return -1;
        }
        if (o1.getSortOrder() > o2.getSortOrder()) {
            return 1;
        }
        // 0 == 0
        else {
            return 0;
            //return o1.getDependentClass().getSimpleName().compareTo(o2.getDependentClass().getSimpleName());
        }
    }
}

