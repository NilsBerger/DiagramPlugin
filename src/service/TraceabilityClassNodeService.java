package service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TypePointer;
import de.unihamburg.swk.traceabilityrecovery.ITraceabilityRecoveryService;
import de.unihamburg.swk.traceabilityrecovery.Language;
import materials.JavaClassNodeMaterial;
import materials.SwiftClassNodeMaterial;

import java.util.List;
import java.util.stream.Collectors;

public class TraceabilityClassNodeService {

    private Project _project;

    public TraceabilityClassNodeService(Project currentProject)
    {
        _project = currentProject;
    }

    public List<TraceabilityLink> getJavaTracebiliityLinksForJavaClassNode(final JavaClassNodeMaterial javaClassNodeMaterial)
    {
        ITraceabilityRecoveryService service = ServiceManager.getService(_project, ITraceabilityRecoveryService.class);
        return filterListForTypPointer(service.getSortedTraceabilityLinksForQuery(Language.JAVA, javaClassNodeMaterial.getSimpleClassName()));
    }

    public List<TraceabilityLink> getJavaTracebiliityLinksForSwiftClassNode(final SwiftClassNodeMaterial swiftClassNodeMaterial)
    {
        ITraceabilityRecoveryService service = ServiceManager.getService(_project, ITraceabilityRecoveryService.class);
        return filterListForTypPointer(service.getSortedTraceabilityLinksForQuery(Language.JAVA, swiftClassNodeMaterial.getSimpleClassName()));
    }

    public List<TraceabilityLink> getSwiftTracebiliityLinksForJavaClassNode(final JavaClassNodeMaterial javaClassNodeMaterial)
    {
        ITraceabilityRecoveryService service = ServiceManager.getService(_project, ITraceabilityRecoveryService.class);
        return filterListForTypPointer(service.getSortedTraceabilityLinksForQuery(Language.SWIFT, javaClassNodeMaterial.getSimpleClassName()));
    }
    public List<TraceabilityLink> getSwiftTracebiliityLinksForSwiftClassNode(final SwiftClassNodeMaterial swiftClassNodeMaterial)
    {
        ITraceabilityRecoveryService service = ServiceManager.getService(_project, ITraceabilityRecoveryService.class);
        return filterListForTypPointer(service.getSortedTraceabilityLinksForQuery(Language.SWIFT, swiftClassNodeMaterial.getSimpleClassName()));

    }
    private List<TraceabilityLink> filterListForTypPointer(List<TraceabilityLink> traceabilityLinkList)
    {
        return traceabilityLinkList.stream().filter(p -> p.getTarget() instanceof TypePointer).collect(Collectors.toList());
    }
}


