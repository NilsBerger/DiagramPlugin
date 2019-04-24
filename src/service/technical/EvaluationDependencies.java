package service.technical;

import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import materials.TraceLinkProgramEntityAssociation;
import valueobjects.Language;
import valueobjects.Marking;
import valueobjects.RelationshipType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EvaluationDependencies {

    public static final Set<ProgramEntityRelationship> getDepedencies() {
        Set<ProgramEntityRelationship> dependencies = new HashSet<>();

        ProgramEntity eventDetailModelJava = new ProgramEntity("EventDetailModel", Language.Java);
        eventDetailModelJava.setAsInitialClass();
        ProgramEntity eventDetailModelSwift = new ProgramEntity("EventDetailModel", Language.Swift);

        ProgramEntity eventDetailHeaderCellJava = new ProgramEntity("EventDetailHeaderCell", Language.Java);
        ProgramEntity eventDetailHeaderCellSwift = new ProgramEntity("EventDetailHeaderCell", Language.Swift);

        ProgramEntity eventModelJava = new ProgramEntity("EventModel", Language.Java);
        eventModelJava.setAsInitialClass();
        ProgramEntity eventModelSwift = new ProgramEntity("EventModel", Language.Swift);

        ProgramEntity eventImageCellJava = new ProgramEntity("EventImageCell", Language.Java);
        ProgramEntity eventImageCellSwift = new ProgramEntity("EventImageCell", Language.Swift);

        ProgramEntity eventTextImageCellJava = new ProgramEntity("EventTextImageCell", Language.Java);
        ProgramEntity eventTextImageCellSwift = new ProgramEntity("EventTextImageCell", Language.Swift);

        ProgramEntity eventTextCellJava = new ProgramEntity("EventTextCell", Language.Java);
        ProgramEntity eventTextCellSwift = new ProgramEntity("EventTextCell", Language.Swift);

        ProgramEntity eventJava = new ProgramEntity("Event", Language.Java);
        ProgramEntity eventSwift = new ProgramEntity("JsonKeysEvent", Language.Swift);

        //Dependencies Java
        ProgramEntityRelationship a = new ProgramEntityRelationship(eventImageCellJava, eventModelJava, RelationshipType.Dependency);
        ProgramEntityRelationship b = new ProgramEntityRelationship(eventTextCellJava, eventModelJava, RelationshipType.Dependency);
        ProgramEntityRelationship c = new ProgramEntityRelationship(eventModelJava, eventModelJava, RelationshipType.Dependency);
        ProgramEntityRelationship f = new ProgramEntityRelationship(eventTextImageCellJava, eventModelJava, RelationshipType.Dependency);
        ProgramEntityRelationship o = new ProgramEntityRelationship(eventModelJava, eventJava, RelationshipType.Dependency);

        ProgramEntityRelationship d = new ProgramEntityRelationship(eventDetailHeaderCellJava, eventDetailModelJava, RelationshipType.Dependency);
        ProgramEntityRelationship e = new ProgramEntityRelationship(eventDetailModelJava, eventJava, RelationshipType.Dependency);
        ProgramEntityRelationship g = new ProgramEntityRelationship(eventDetailModelJava, eventJava, RelationshipType.Dependency);
        ProgramEntityRelationship p = new ProgramEntityRelationship(eventDetailModelJava, eventDetailModelJava, RelationshipType.Dependency);

        //Dependencies Swift

        ProgramEntityRelationship h = new ProgramEntityRelationship(eventImageCellSwift, eventModelSwift, RelationshipType.Dependency);
        ProgramEntityRelationship i = new ProgramEntityRelationship(eventTextCellSwift, eventModelSwift, RelationshipType.Dependency);
        ProgramEntityRelationship j = new ProgramEntityRelationship(eventTextImageCellSwift, eventModelSwift, RelationshipType.Dependency);
        ProgramEntityRelationship k = new ProgramEntityRelationship(eventModelSwift, eventModelSwift, RelationshipType.Dependency);
        ProgramEntityRelationship n = new ProgramEntityRelationship(eventModelSwift, eventSwift, RelationshipType.Dependency);

        ProgramEntityRelationship l = new ProgramEntityRelationship(eventDetailHeaderCellSwift, eventDetailModelSwift, RelationshipType.Dependency);
        ProgramEntityRelationship m = new ProgramEntityRelationship(eventDetailModelSwift, eventSwift, RelationshipType.Dependency);
        ProgramEntityRelationship q = new ProgramEntityRelationship(eventDetailModelSwift, eventDetailModelSwift, RelationshipType.Dependency);

        //TraceLinks
        TraceLinkProgramEntityAssociation eventDetailModel = new TraceLinkProgramEntityAssociation(eventDetailModelJava, eventDetailModelSwift, 30.44);
        TraceLinkProgramEntityAssociation eventModel = new TraceLinkProgramEntityAssociation(eventModelJava, eventModelSwift, 24.02);
        TraceLinkProgramEntityAssociation eventDetailHeaderCell = new TraceLinkProgramEntityAssociation(eventDetailHeaderCellJava, eventDetailHeaderCellSwift, 40.10);
        TraceLinkProgramEntityAssociation eventImageCell = new TraceLinkProgramEntityAssociation(eventImageCellJava, eventImageCellSwift, 33.29);
        TraceLinkProgramEntityAssociation eventTextImageCell = new TraceLinkProgramEntityAssociation(eventTextImageCellJava, eventTextImageCellSwift, 38.58);
        TraceLinkProgramEntityAssociation eventTextCell = new TraceLinkProgramEntityAssociation(eventTextCellJava, eventTextCellSwift, 31.32);
        TraceLinkProgramEntityAssociation event = new TraceLinkProgramEntityAssociation(eventJava, eventSwift, 6.43);

        ProgramEntityRelationship[] allDependencies = {a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, eventDetailModel, eventModel, eventDetailHeaderCell, eventImageCell, eventTextImageCell, eventTextCell, event};

        dependencies.addAll(new ArrayList<ProgramEntityRelationship>(Arrays.asList(allDependencies)));

        return dependencies;
    }


    public static Set<ProgramEntityRelationship> getChangedDependencies() {

        final Set<ProgramEntityRelationship> depedencies = getDepedencies();
        for (ProgramEntityRelationship dependency : depedencies) {
            dependency.getDependentClass().setMarking(Marking.CHANGED);
            dependency.getIndependentClass().setMarking(Marking.CHANGED);
        }
        return depedencies;
    }
}
