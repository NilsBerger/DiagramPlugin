/*
 * Copyright 1998-2014 Konstantin Bulenkov
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

package com.intellij.examples;

import com.intellij.diagram.DiagramColorManagerBase;
import com.intellij.diagram.DiagramEdge;
import com.intellij.ui.JBColor;

import java.awt.*;

/**
 * @author Konstantin Bulenkov
 */
public class FileDiagramColorManager extends DiagramColorManagerBase {
  @Override
  public Color getEdgeColor(DiagramEdge edge) {
    final String edgeType = edge.getRelationship().toString();
    if ("SOFT".equals(edgeType)) {
      return new JBColor(new Color(254, 128, 0), new Color(0, 0, 0));
    }
    if ("STRONG".equals(edgeType)) {
      return new JBColor(new Color(0, 26, 128), new Color(140, 177, 197));
    }
    return super.getEdgeColor(edge);
  }
}
