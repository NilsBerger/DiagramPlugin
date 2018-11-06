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

package actions;

import service.ChangePropagationProcessService;
import com.intellij.diagram.*;
import com.intellij.diagram.presentation.DiagramLineType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.openapi.vfs.VirtualFileManager;
import materials.ClassNodeMaterial;
import javafx.collections.SetChangeListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;

/**
 * @author Konstantin Bulenkov
 */
public class ClassDiagramDataModel extends DiagramDataModel<ClassNodeMaterial> {
  private Set<ClassNode> myNodes = new HashSet<ClassNode>();
  private Set<ClassEdge> myEdges = new HashSet<ClassEdge>();
  private Map<ClassNodeMaterial, ClassNode> path2Node = new HashMap<>(myNodes.size());
  private ChangePropagationProcessService _cpProcess;


    public ClassDiagramDataModel(Project project, ChangePropagationProcessService propagationProcess) {
        super(project, ClassDiagramProvider.getInstance());
        this._cpProcess = propagationProcess;
        refreshDataModel();
        registerListener();
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
    public String getNodeName(DiagramNode<ClassNodeMaterial> e) {
        return e.toString();
    }

  @Nullable
  @Override
  public ClassNode addElement(ClassNodeMaterial material) {
    ClassNode node = path2Node.get(material);
    if (node == null) {
      node = new ClassNode(material);
      path2Node.put(material, node);
      myNodes.add(node);
    }
    return node;
  }


  @Override
  public void refreshDataModel() {

      for (ClassNodeMaterial classNodeMaterial : _cpProcess.getAffectedClassesByChange()) {
          addElement(classNodeMaterial);

          Set<ClassNodeMaterial> topdependencies = _cpProcess.getModel().getTopDependencies(classNodeMaterial);
          Set<ClassNodeMaterial> bottompdependencies = _cpProcess.getModel().getBottomDependencies(classNodeMaterial);
          Set<ClassNodeMaterial> neighbourhood = new HashSet<>();
          neighbourhood.addAll(topdependencies);
          neighbourhood.addAll(bottompdependencies);

          for (ClassNodeMaterial topdependency : topdependencies) {
              if (_cpProcess.getAffectedClassesByChange().contains(topdependency)) {
                  ClassNode dependentNode = new ClassNode(classNodeMaterial);
                  ClassNode independentNode = new ClassNode(topdependency);
                  myEdges.add(new ClassEdge(dependentNode, independentNode, getDiagramrelationshipInfo()));
              }
          }

          for (ClassNodeMaterial bottomdependency : bottompdependencies) {
              if (_cpProcess.getAffectedClassesByChange().contains(bottomdependency)) {

                  ClassNode dependentNode = new ClassNode(classNodeMaterial);
                  ClassNode independentNode = new ClassNode(bottomdependency);

                  myEdges.add(new ClassEdge(independentNode, dependentNode, getDiagramrelationshipInfo()));
              }
          }
//          DiagramBuilder builder;
//          try{
//               builder = ClassDiagramProvider.getInstance().getDataModel().getBuilder();
//              if(builder != null)
//              {
//                  ApplicationManager.getApplication().invokeLater(()-> builder.update(true, true));
//              }
//
//          }catch (AssertionError e)
//          {
//              //Error only occurs at the beginning
//          }
//
      }
  }

  private DiagramRelationshipInfo getDiagramrelationshipInfo()
  {
      return new DiagramRelationshipInfoAdapter("SOFT", DiagramLineType.DASHED) {
          @Override
          public Shape getStartArrow() {
              return STANDARD;
          }

          @Override
          public String getLabel() {
              return "   " + String.valueOf("");
          }
      };
  }

  @Override
  public void removeNode(DiagramNode<ClassNodeMaterial> node) {
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

  private void registerListener()
  {
      _cpProcess.getAffectedClassesByChange().addListener(new SetChangeListener<ClassNodeMaterial>() {
          @Override
          public void onChanged(Change<? extends ClassNodeMaterial> change) {
              refreshDataModel();
          }
      });
  }
}
