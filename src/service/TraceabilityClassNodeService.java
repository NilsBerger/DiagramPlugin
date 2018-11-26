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

    public List<TraceabilityLink> getJavaTraceabilityLinks(final ClassNode classNode)
    {
        ITraceabilityRecoveryService service = ServiceManager.getService(_project, ITraceabilityRecoveryService.class);
        return filterListForTypPointer(service.getSortedTraceabilityLinksForQuery(Language.JAVA, classNode.getSimpleClassName()));
    }

    public List<TraceabilityLink> getSwiftTraceabilityLinks(final ClassNode classNode)
    {
        ITraceabilityRecoveryService service = ServiceManager.getService(_project, ITraceabilityRecoveryService.class);
        List<TraceabilityLink> sortedTraceabilityLinks = service.getSortedTraceabilityLinksForQuery(Language.SWIFT, classNode.getSimpleClassName());
        return filterListForTypPointer(sortedTraceabilityLinks);
    }
    private List<TraceabilityLink> filterListForTypPointer(List<TraceabilityLink> traceabilityLinkList)
    {
        return traceabilityLinkList.stream().filter(p -> p.getTarget() instanceof TypePointer).collect(Collectors.toList());
    }
}


