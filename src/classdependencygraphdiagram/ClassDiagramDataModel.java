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

package classdependencygraphdiagram;

import Utils.PluginProperties;
import com.intellij.diagram.DiagramDataModel;
import com.intellij.diagram.DiagramNode;
import com.intellij.diagram.DiagramRelationshipInfo;
import com.intellij.diagram.DiagramRelationshipInfoAdapter;
import com.intellij.diagram.presentation.DiagramLineType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.openapi.vfs.VirtualFileManager;
import dotparser.DependencyModelProvider;
import dotparser.DependencyFachwert;
import dotparser.DotParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author Konstantin Bulenkov
 */
public class ClassDiagramDataModel extends DiagramDataModel<String> {
  private List<ClassNode> myNodes = new ArrayList<ClassNode>();
  private List<ClassEdge> myEdges = new ArrayList<ClassEdge>();
  private Map<String, ClassNode> path2Node = new HashMap<String, ClassNode>(myNodes.size());

  public ClassDiagramDataModel(Project project, String file) {
   super(project, ClassDiagramProvider.getInstance());
   String f = file;
   myNodes.add(new ClassNode(file));
   path2Node.put(file,new ClassNode(file));
    refreshDataModel();

  }

  @NotNull
  @Override
  public Collection<ClassNode> getNodes() {
    return myNodes;
  }

  @NotNull
  @Override
  public Collection<ClassEdge> getEdges() {
    return myEdges;
  }

  @NotNull
  @Override
  public String getNodeName(DiagramNode<String> node) {
    return node.getIdentifyingElement();
  }

  @Nullable
  @Override
  public ClassNode addElement(String classname) {
    ClassNode node = path2Node.get(classname);
    if (node == null) {
      node = new ClassNode(classname);
      path2Node.put(classname, node);
      myNodes.add(node);
    }
    return node;
  }

  @Override
  public void refreshDataModel() {
    myEdges.clear();

    PluginProperties properties;
    try{
      properties = PluginProperties.getInstance();
      properties.readPluginProperties();
      List<DependencyFachwert> dependencyList = DotParser.getClassDependencyGraph(DependencyModelProvider.DEPCHECK_FILENAME);

      for(DependencyFachwert relationship: dependencyList) {
        ClassNode dependentNode = new ClassNode(relationship.getDependentClass().getSimpleClassName());
        ClassNode independentNode = new ClassNode(relationship.getIndependentClass().getSimpleClassName());
        addElement(relationship.getDependentClass().getSimpleClassName());
        addElement(relationship.getIndependentClass().getSimpleClassName());
        DiagramRelationshipInfo r = new DiagramRelationshipInfoAdapter("SOFT", DiagramLineType.DASHED) {
                  @Override
                  public Shape getStartArrow() {
                    return STANDARD;
                  }

                  @Override
                  public String getLabel() {
                    return "   " + String.valueOf("");
                  }
                };
        myEdges.add(new ClassEdge(dependentNode, independentNode, r));

      }

    } catch (IOException e) {
      e.printStackTrace();
    }

//
//    for (ClassNode node : myNodes) {
//      VirtualFile f = node.getIdentifyingElement().getParent();
//      int i = 1;
//      while (f != null) {
//        final ClassNode n = path2Node.get(f.getPath());
//        if (n != null) {
//          final int level = i;
//          DiagramRelationshipInfo r = level == 1 ?
//              ClassDiagramRelationships.STRONG : new DiagramRelationshipInfoAdapter("SOFT", DiagramLineType.DASHED) {
//            @Override
//            public Shape getStartArrow() {
//              return STANDARD;
//            }
//
//            @Override
//            public String getLabel() {
//              return "   " + String.valueOf(level);
//            }
//          };
//          myEdges.add(new ClassEdge(node, n, r));
//          f = null;
//        } else {
//          f = f.getParent();
//          i++;
//        }
//      }
//    }
  }

  @Override
  public void removeNode(DiagramNode<String> node) {
    myNodes.remove(node);
    path2Node.remove(node.getIdentifyingElement());
    refreshDataModel();
  }

  @NotNull
  @Override
  public ModificationTracker getModificationTracker() {
    return VirtualFileManager.getInstance();
  }

  @Override
  public void dispose() {

  }
}
