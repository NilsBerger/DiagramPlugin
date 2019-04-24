
package service.functional;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TypePointer;
import de.unihamburg.swk.traceabilityrecovery.ITraceabilityRecoveryService;
import de.unihamburg.swk.traceabilityrecovery.Language;
import materials.ProgramEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A service that creates Lists of TraceabilityLinks for a given Classnode
 */
public class ProgramEntityTraceLinkRecovery {


    private final ITraceabilityRecoveryService _recoveryService;

    public ProgramEntityTraceLinkRecovery(Project currentProject) {
        _recoveryService = ServiceManager.getService(currentProject, ITraceabilityRecoveryService.class);
    }

    /**
     * Returns a List containing TraceabilityLinks that likely represent a Java-Classnodes
     *
     * @param programEntity
     * @return The List of "Java"-Tracelinks
     */
    public List<TraceabilityLink> getJavaCrossPlatformTraceabilityLinks(final ProgramEntity programEntity) {

        final List<TraceabilityLink> linksByClassName = _recoveryService.getLinksByClassName(programEntity.getSimpleName(), Language.SWIFT);
        return filterListForTypPointer(linksByClassName);
    }

    /**
     * Returns a List containing TraceabilityLinks that likely represent a Swift-Classnode
     *
     * @param programEntity
     * @return The List of "Swift"-Tracelinks
     */
    public List<TraceabilityLink> getSwiftCrossPlatformTraceabilityLinks(final ProgramEntity programEntity) {
        final List<TraceabilityLink> linksByClassName = _recoveryService.getLinksByClassName(programEntity.getSimpleName(), Language.JAVA);
        return filterListForTypPointer(linksByClassName);
    }

    /**
     * Returns a List containing TraceabilityLinks that likely represent a Java-Classnodes
     *
     * @param programEntity
     * @return The List of "Java"-Tracelinks
     */
    public List<TraceabilityLink> getJavaPlatformTraceabilityLinks(final ProgramEntity programEntity) {
        final List<TraceabilityLink> linksByClassName = _recoveryService.getLinksByClassName(programEntity.getSimpleName(), Language.JAVA);
        return filterListForTypPointer(linksByClassName);
    }

    /**
     * Returns a List containing TraceabilityLinks that likely represent a Swift-Classnode
     *
     * @param programEntity
     * @return The List of "Swift"-Tracelinks
     */
    public List<TraceabilityLink> getSwiftPlatformTraceabilityLinks(final ProgramEntity programEntity) {
        final List<TraceabilityLink> linksByClassName = _recoveryService.getLinksByClassName(programEntity.getSimpleName(), Language.SWIFT);
        return filterListForTypPointer(linksByClassName);
    }

    /**
     * Filters a List containing TraceabililtyLink and sorts out all Tracelink that are not a TypePointer
     *
     * @param traceabilityLinkList
     * @return The filtered List<></>
     */
    private List<TraceabilityLink> filterListForTypPointer(List<TraceabilityLink> traceabilityLinkList) {
        return traceabilityLinkList.stream().filter(p -> p.getTarget() instanceof TypePointer).collect(Collectors.toList());
    }

    private static Language convertLanguageEnumToLanguageClass(ProgramEntity entity) {
        if (valueobjects.Language.Java == entity.getLanguage()) {
            return Language.JAVA;
        }
        if (valueobjects.Language.Swift == entity.getLanguage()) {
            return Language.SWIFT;
        }
        throw new IllegalArgumentException("Unkown typ of language");
    }
}


