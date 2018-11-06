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

import com.intellij.diagram.DiagramBuilder;
import com.intellij.diagram.DiagramNode;
import com.intellij.diagram.extras.DiagramExtras;
import com.intellij.diagram.extras.providers.DiagramDnDProvider;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import materials.ClassNodeMaterial;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author Konstantin Bulenkov
 */
public class ClassDiagramExtras extends DiagramExtras<ClassNodeMaterial> {
  private DiagramDnDProvider<ClassNodeMaterial> myDnDProvider = new DiagramDnDProvider<ClassNodeMaterial>() {
    @Override
    public boolean isAcceptedForDnD(Object o, Project project) {
      return o instanceof VirtualFile
          || o instanceof PsiElement;
    }


    @Nullable
    @Override
    public ClassNodeMaterial[] wrapToModelObject(Object o, Project project) {
//      if (o instanceof PsiElement) {
//        final PsiFile file = ((PsiElement) o).getContainingFile();
//        if (file != null) {
//          return new VirtualFile[]{file.getVirtualFile()};
//        } else if (o instanceof PsiDirectory) {
//          return new VirtualFile[]{((PsiDirectory) o).getVirtualFile()};
//        }
//      } else if (o instanceof VirtualFile) {
//        return new VirtualFile[]{(VirtualFile) o};
//      }
      return new ClassNodeMaterial[0];
    }
  };

  @NotNull
  @Override
  public JComponent createNodeComponent(DiagramNode<ClassNodeMaterial> node, DiagramBuilder builder, Point basePoint, JPanel wrapper) {
    if (node.getIdentifyingElement() == null)
    {
        Icon icon2 = AllIcons.Nodes.Desktop;

        ImageIcon icon = new ImageIcon("resources/icons/Refresh.png");
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(64, 64,  Image.SCALE_SMOOTH);
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

//  @Nullable
//  @Override
//  public Layouter getCustomLayouter(GraphSettings settings, Project project) {
//    //final OrthogonalTraceabilityLayouter layouter = new OrthogonalTraceabilityLayouter(settings.);
//    final OrthogonalLayouter layouter = settings.getOrthogonalLayouter();
////    layouter.setNodeEdgeOverlapAvoided(true);
////
////    layouter.setNodeSizeAware(true);
////    layouter.setMinimalNodeDistance(30);
////    layouter.setNodeOverlapsAllowed(false);
////    layouter.setSmartComponentLayoutEnabled(true);
////    layouter.setConsiderNodeLabelsEnabled(true);
////    layouter.setDeterministic(true);
//  //  final OrthogonalTraceabilityLayouter layouter = ge
//
//    final List<CanonicMultiStageLayouter> list = new ArrayList<>();
//    list.add(layouter);
//    list.add(settings.getBalloonLayouter());
//    list.add(settings.getCircularLayouter());
//    list.add(settings.getDirectedOrthogonalLayouter());
//    list.add(settings.getGroupLayouter());
//    list.add(settings.getHVTreeLayouter());
//    list.add(settings.getOrthogonalLayouter());
//    for (CanonicMultiStageLayouter current : list) {
//      final ParallelEdgeLayouter parallelEdgeLayouter = GraphManager.getGraphManager().createParallelEdgeLayouter();
//      parallelEdgeLayouter.setLineDistance(40);
//      parallelEdgeLayouter.setUsingAdaptiveLineDistances(false);
//      current.appendStage(parallelEdgeLayouter);
//      current.setParallelEdgeLayouterEnabled(false);
//    }
//    GraphSettings setting = settings;
//    //setting.setCurrentLayouter(new OrthogonalTraceabilityLayouter());
//
//    return new OrthogonalTraceabilityLayouter();
//  }

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
  public DiagramDnDProvider<ClassNodeMaterial> getDnDProvider() {
    return myDnDProvider;
  }

  @Nullable
  @Override
  public Object getData(String dataId, List<DiagramNode<ClassNodeMaterial>> nodes, DiagramBuilder builder) {
//    if (nodes.size() == 1) {
//      final VirtualFile file = nodes.get(0).getIdentifyingElement();
//      if (CommonDataKeys.VIRTUAL_FILE.is(dataId)) {
//        return file;
//      }
//      if (CommonDataKeys.PSI_FILE.is(dataId) || CommonDataKeys.PSI_ELEMENT.is(dataId)) {
//        if (file.isDirectory() && CommonDataKeys.PSI_ELEMENT.is(dataId)) {
//          return PsiManager.getInstance(builder.getProject()).findDirectory(file);
//        } else {
//          return PsiManager.getInstance(builder.getProject()).findFile(file);
//        }
//      }
//    }
    return super.getData(dataId, nodes, builder);
  }
}
