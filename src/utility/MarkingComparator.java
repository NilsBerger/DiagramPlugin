package utility;

import valueobjects.Marking;

import java.util.Comparator;

public class MarkingComparator implements Comparator<Marking> {

    @Override
    public int compare(Marking o1, Marking o2) {
        return Integer.compare(getAssignedValue(o1), getAssignedValue(o2));
    }

    int getAssignedValue(Marking marking) {
        switch (marking) {
            case CHANGED:
                return 0;
            case PROPAGATES:
                return 1;
            case NEXT:
                return 2;
            case INSPECTED:
                return 3;
            case BLANK:
                return 4;
            default:
                throw new IllegalArgumentException("Could not find case for: " + marking);
        }
    }
}
