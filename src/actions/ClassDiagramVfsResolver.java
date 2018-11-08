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

import com.intellij.diagram.DiagramVfsResolver;
import com.intellij.openapi.project.Project;
import materials.ClassNode;
import org.jetbrains.annotations.Nullable;

/**
 * @author Konstantin Bulenkov
 */
public class ClassDiagramVfsResolver implements DiagramVfsResolver<ClassNode> {
  @Override
  public String getQualifiedName(ClassNode classNode) {
    return "";
  }

  @Nullable
  @Override
  public ClassNode resolveElementByFQN(String s, Project project) {
    return null;
  }
}

