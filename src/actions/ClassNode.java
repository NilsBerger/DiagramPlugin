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

import Utils.HashUtils;
import com.intellij.diagram.DiagramBuilder;
import com.intellij.openapi.application.ApplicationManager;
import service.ChangePropagationProcessService;
import service.GraphChangeListener;
import com.intellij.diagram.DiagramNodeBase;
import material.ClassNodeMaterial;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import werkzeuge.StatusIcons;

import javax.swing.*;

/**
 * @author Konstantin Bulenkov
 */
public class ClassNode extends DiagramNodeBase<ClassNodeMaterial> implements GraphChangeListener {


    private final ClassNodeMaterial _classNodeMaterial;
    private ChangePropagationProcessService _cp;
    private String _className = "";
    private Icon _icon;



    public ClassNode(final ClassNodeMaterial material)
    {
        super(ClassDiagramProvider.getInstance());
        _cp = ChangePropagationProcessService.getInstance();
        _className = material.getSimpleClassName();
        _classNodeMaterial = _cp.getClassNodeMaterial(material);
        _classNodeMaterial.addGraphChangeListener(this);
        _icon = StatusIcons.getIconForMarking(_classNodeMaterial.getMarking());

    }
    @Nullable
    @Override
    public String getTooltip() {
        return _className;
    }

    @Override
    public Icon getIcon() {
        return _icon;
    }

    @NotNull
    @Override
    public ClassNodeMaterial getIdentifyingElement() {
        return _classNodeMaterial;
    }

    @Override
    public void update(ClassNodeMaterial dataProvider) {
        try{
            _icon = StatusIcons.getIconForMarking(dataProvider.getMarking());
            DiagramBuilder builder = ClassDiagramProvider.getInstance().getDataModel().getBuilder();
            ApplicationManager.getApplication().invokeLater(()->  builder.update(true, true));
        }catch (AssertionError e)
        {
            //Error only occurs at the beginning
        }

    }

    @Override
    public boolean equals(final Object obj) {
        if(obj == null)
            return false;
        if(this == obj)
            return true;
        if(this.getClass() != obj.getClass())
            return false;
        final ClassNode otherClassNode = (ClassNode) obj;
        return this.getIdentifyingElement().equals(otherClassNode.getIdentifyingElement());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = HashUtils.calcHashCode(hash, _className);
        hash = HashUtils.calcHashCode(hash, _classNodeMaterial.getClass().getName());
        return hash;
    }
}


