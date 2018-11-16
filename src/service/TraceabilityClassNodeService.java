package service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TypePointer;
import de.unihamburg.swk.traceabilityrecovery.ITraceabilityRecoveryService;
import de.unihamburg.swk.traceabilityrecovery.Language;
import materials.ClassNode;

import java.util.List;
import java.util.stream.Collectors;

public class TraceabilityClassNodeService {

    private Project _project;

    public TraceabilityClassNodeService(Project currentProject)
    {
        _project = currentProject;
    }

    public List<TraceabilityLink> getJavaTracebiliityLinksForJavaClassNode(final ClassNode javaNode)
    {
        ITraceabilityRecoveryService service = ServiceManager.getService(_project, ITraceabilityRecoveryService.class);
        return filterListForTypPointer(service.getSortedTraceabilityLinksForQuery(Language.JAVA, javaNode.getSimpleClassName()));
    }

    public List<TraceabilityLink> getJavaTracebiliityLinksForSwiftClassNode(final ClassNode swiftNode)
    {
        ITraceabilityRecoveryService service = ServiceManager.getService(_project, ITraceabilityRecoveryService.class);
        return filterListForTypPointer(service.getSortedTraceabilityLinksForQuery(Language.JAVA, swiftNode.getSimpleClassName()));
    }

    public List<TraceabilityLink> getSwiftTracebiliityLinksForJavaClassNode(final ClassNode javaNode)
    {
        ITraceabilityRecoveryService service = ServiceManager.getService(_project, ITraceabilityRecoveryService.class);
        return filterListForTypPointer(service.getSortedTraceabilityLinksForQuery(Language.SWIFT, javaNode.getSimpleClassName()));
    }
    public List<TraceabilityLink> getSwiftTracebiliityLinksForSwiftClassNode(final ClassNode swiftNode)
    {
        ITraceabilityRecoveryService service = ServiceManager.getService(_project, ITraceabilityRecoveryService.class);
        return filterListForTypPointer(service.getSortedTraceabilityLinksForQuery(Language.SWIFT, swiftNode.getSimpleClassName()));

    }
    private List<TraceabilityLink> filterListForTypPointer(List<TraceabilityLink> traceabilityLinkList)
    {
        return traceabilityLinkList.stream().filter(p -> p.getTarget() instanceof TypePointer).collect(Collectors.toList());
    }
}


