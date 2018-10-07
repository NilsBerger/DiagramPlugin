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

import com.intellij.diagram.DiagramNodeBase;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Konstantin Bulenkov
 */
public class ClassNode extends DiagramNodeBase<String> {
  private final String _className;

  public ClassNode(final String className) {
    super(ClassDiagramProvider.getInstance());
    _className = className;
  }

  @Nullable
  @Override
  public String getTooltip() {
    return _className;
  }

  @Override
  public Icon getIcon() {
    return AllIcons.Nodes.Class;
  }

  @NotNull
  @Override
  public String getIdentifyingElement() {
    return _className;
  }
}
