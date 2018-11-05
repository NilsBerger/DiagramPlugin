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

import com.intellij.diagram.*;
import com.intellij.diagram.extras.DiagramExtras;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import material.ClassNodeMaterial;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClassTwoDiagramProvider extends BaseDiagramProvider<ClassNodeMaterial> {

    public static final String ID = "FileDiagramProvider";
    private DiagramElementManager<ClassNodeMaterial> myElementManager = new ClassDiagramElementManager();
    private DiagramVfsResolver<ClassNodeMaterial> myVfsResolver = new ClassDiagramVfsResolver();
    private DiagramExtras<ClassNodeMaterial> myExtras = new ClassDiagramExtras();
    private DiagramColorManager myColorManager = new ClassDiagramColorManager();
    @Override
    public String getID() {
        return ID;
    }

    @Override
    public DiagramElementManager<ClassNodeMaterial> getElementManager() {
        return myElementManager;
    }

    @Override
    public DiagramVfsResolver<ClassNodeMaterial> getVfsResolver() {
        return myVfsResolver;
    }

    @Override
    public String getPresentableName() {
        return "Graph 2";
    }

    @Override
    public DiagramDataModel<ClassNodeMaterial> createDataModel(@NotNull Project project, @Nullable ClassNodeMaterial material, @Nullable VirtualFile virtualFile, DiagramPresentationModel diagramPresentationModel) {
        return null;
    }
}
