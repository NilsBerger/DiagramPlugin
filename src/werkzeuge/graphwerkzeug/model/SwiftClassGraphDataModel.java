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

package werkzeuge.graphwerkzeug.model;

import materials.ClassDependency;
import materials.ClassNode;
import valueobjects.ClassNodeType;

import java.util.Set;
import java.util.stream.Collectors;

public class SwiftClassGraphDataModel extends GerneralClassGraphDataModel {

    @Override
   public void refreshDataModel(final ClassNode changedClassNode) {
        if(changedClassNode.getType() == ClassNodeType.Swift)
        {
            addNode(changedClassNode);
            Set<ClassDependency> affectedDependencies = _changePropagationProcess.getAffectedDependencies(changedClassNode);
            addAll(affectedDependencies);

//            Set<ClassNode> topDependencies = _changePropagationProcess.getModel().getTopDependencies(changedClassNode).stream().filter(node -> node.getType() == ClassNodeType.Swift).collect(Collectors.toSet());
//            Set<ClassNode> bottompDependencies = _changePropagationProcess.getModel().getBottomDependencies(changedClassNode).stream().filter(node -> node.getType() == ClassNodeType.Swift).collect(Collectors.toSet());
//
//            addNeighbourhoodForClass(changedClassNode, topDependencies);
//            addNeighbourhoodForClass(changedClassNode, bottompDependencies);
        }
   }
}

