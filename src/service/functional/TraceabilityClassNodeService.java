/*
 * Copyright 1998-2018 Konstantin Bulenkov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package service.functional;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityLink;
import de.unihamburg.masterprojekt2016.traceability.TypePointer;
import de.unihamburg.swk.traceabilityrecovery.ITraceabilityRecoveryService;
import de.unihamburg.swk.traceabilityrecovery.Language;
import materials.ClassNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A service that creates Lists of TraceabilityLinks for a given Classnode
 */
public class TraceabilityClassNodeService {

    private Project _project;

    public TraceabilityClassNodeService(Project currentProject)
    {
        _project = currentProject;
    }
    /**
     * Returns a List containing TraceabilityLinks that likely represent a Java-Classnodes
     * @param classNode
     * @return The List of "Java"-Tracelinks
     */
    public List<TraceabilityLink> getJavaTraceabilityLinks(final ClassNode classNode)
    {
        ITraceabilityRecoveryService service = ServiceManager.getService(_project, ITraceabilityRecoveryService.class);
        List<TraceabilityLink> sortedTraceabilityLinks = new ArrayList<>();
        sortedTraceabilityLinks.addAll(service.getSortedTraceabilityLinksForQuery(Language.JAVA, classNode.getSimpleName()));
        return filterListForTypPointer(sortedTraceabilityLinks);
    }

    /**
     * Returns a List containing TraceabilityLinks that likely represent a Swift-Classnode
     * @param classNode
     * @return The List of "Swift"-Tracelinks
     */
    public List<TraceabilityLink> getSwiftTraceabilityLinks(final ClassNode classNode)
    {
        ITraceabilityRecoveryService service = ServiceManager.getService(_project, ITraceabilityRecoveryService.class);
        List<TraceabilityLink> sortedTraceabilityLinks = new ArrayList<>();
        sortedTraceabilityLinks.addAll(service.getSortedTraceabilityLinksForQuery(Language.SWIFT, classNode.getSimpleName()));
        return filterListForTypPointer(sortedTraceabilityLinks);
    }

    /**
     * Filters a List containing TraceabililtyLink and sorts out all Tracelink that are not a TypePointer
     * @param traceabilityLinkList
     * @return The filtered List<></>
     */
    private List<TraceabilityLink> filterListForTypPointer(List<TraceabilityLink> traceabilityLinkList)
    {
        return traceabilityLinkList.stream().filter(p -> p.getTarget() instanceof TypePointer).collect(Collectors.toList());
    }
}


