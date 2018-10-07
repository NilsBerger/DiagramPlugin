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

import com.intellij.diagram.DiagramBuilder;
import com.intellij.diagram.DiagramNode;
import com.intellij.diagram.extras.DiagramExtras;
import com.intellij.diagram.extras.providers.DiagramDnDProvider;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author Konstantin Bulenkov
 */
public class FileDiagramExtras extends DiagramExtras<VirtualFile> {
  private DiagramDnDProvider<VirtualFile> myDnDProvider = new DiagramDnDProvider<VirtualFile>() {
    @Override
    public boolean isAcceptedForDnD(Object o, Project project) {
      return o instanceof VirtualFile
          || o instanceof PsiElement;
    }


    @Nullable
    @Override
    public VirtualFile[] wrapToModelObject(Object o, Project project) {
      if (o instanceof PsiElement) {
        final PsiFile file = ((PsiElement) o).getContainingFile();
        if (file != null) {
          return new VirtualFile[]{file.getVirtualFile()};
        } else if (o instanceof PsiDirectory) {
          return new VirtualFile[]{((PsiDirectory) o).getVirtualFile()};
        }
      } else if (o instanceof VirtualFile) {
        return new VirtualFile[]{(VirtualFile) o};
      }
      return VirtualFile.EMPTY_ARRAY;
    }
  };

  @NotNull
  @Override
  public JComponent createNodeComponent(DiagramNode<VirtualFile> node, DiagramBuilder builder, Point basePoint, JPanel wrapper) {
    if (node.getIdentifyingElement().getParent() == null)
    {
        Icon icon2 = AllIcons.Nodes.Desktop;

        ImageIcon icon = new ImageIcon("resources/icons/Refresh.png");
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH);
        icon.setImage(newimg);
        return new JLabel(icon);
    }
    return super.createNodeComponent(node, builder, basePoint, wrapper);
  }

  private Image getScaledImage(Image srcImg, int w, int h){
    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = resizedImg.createGraphics();

    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(srcImg, 0, 0, w, h, null);
    g2.dispose();

    return resizedImg;
  }

  /*  @NotNull
    @Override
    public JComponent createNodeComponent(DiagramNode<VirtualFile> node, DiagramBuilder builder, Point basePoint) {
      if (node.getIdentifyingElement().getParent() == null) {
        return new JLabel(IconLoader.getIcon("/icons/hdd.png"));
      }
      return super.createNodeComponent(node, builder, basePoint);
  }
   */
@Nullable
  @Override
  public DiagramDnDProvider<VirtualFile> getDnDProvider() {
    return myDnDProvider;
  }

  @Nullable
  @Override
  public Object getData(String dataId, List<DiagramNode<VirtualFile>> nodes, DiagramBuilder builder) {
    if (nodes.size() == 1) {
      final VirtualFile file = nodes.get(0).getIdentifyingElement();
      if (CommonDataKeys.VIRTUAL_FILE.is(dataId)) {
        return file;
      }
      if (CommonDataKeys.PSI_FILE.is(dataId) || CommonDataKeys.PSI_ELEMENT.is(dataId)) {
        if (file.isDirectory() && CommonDataKeys.PSI_ELEMENT.is(dataId)) {
          return PsiManager.getInstance(builder.getProject()).findDirectory(file);
        } else {
          return PsiManager.getInstance(builder.getProject()).findFile(file);
        }
      }
    }
    return super.getData(dataId, nodes, builder);
  }
}
