package materials;

import java.util.Comparator;

class ProgramEntityRelationshipComparator implements Comparator<ProgramEntityRelationship> {


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
            return o2.getDependentClass().getSimpleName().compareTo(o1.getDependentClass().getSimpleName());
        }
    }
}
