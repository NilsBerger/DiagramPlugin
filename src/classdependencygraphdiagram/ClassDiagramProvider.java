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

import com.intellij.diagram.*;
import com.intellij.diagram.extras.DiagramExtras;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Konstantin Bulenkov
 */
public class ClassDiagramProvider extends BaseDiagramProvider<String> {

  public static final String ID = "ClassDiagramProvider";
  private DiagramElementManager<String> myElementManager = new ClassDiagramElementManager();
  private DiagramVfsResolver<String> myVfsResolver = new ClassDiagramVfsResolver();
  private DiagramExtras<String> myExtras = new ClassDiagramExtras();
  private DiagramColorManager myColorManager = new ClassDiagramColorManager();

  @Pattern("[a-zA-Z0-9_-]*")
  @Override
  public String getID() {
    return ID;
  }

  @Override
  public DiagramElementManager getElementManager() {
    return myElementManager;
  }



  @Override
  public DiagramVfsResolver<String> getVfsResolver() {
    return myVfsResolver;
  }

  @Override
  public String getPresentableName() {
    return "Neu Class Dependency Diagram";
  }

  @NotNull
  @Override
  public DiagramExtras<String> getExtras() {
    return myExtras;
  }

  @Override
  public ClassDiagramDataModel createDataModel(@NotNull Project project, @Nullable String file, @Nullable VirtualFile file2, DiagramPresentationModel model) {
    return new ClassDiagramDataModel(project, file);
  }

  @Override
  public DiagramColorManager getColorManager() {
    return myColorManager;
  }

  public static ClassDiagramProvider getInstance() {
    return (ClassDiagramProvider)DiagramProvider.findByID(ID);
  }
}
