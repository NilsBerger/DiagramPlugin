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
import service.ChangePropagationProcess;
import service.GraphChangeListener;
import com.intellij.diagram.DiagramNodeBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import werkzeuge.StatusIcons;

import javax.swing.*;

/**
 * @author Konstantin Bulenkov
 */
public class ClassNode extends DiagramNodeBase<materials.ClassNode>{


    private final materials.ClassNode _classNode;
    private ChangePropagationProcess _cp;
    private String _className = "";
    private Icon _icon;



    public ClassNode(final materials.ClassNode material)
    {
        super(ClassDiagramProvider.getInstance());
        _cp = ChangePropagationProcess.getInstance();
        _className = material.getSimpleClassName();
        _classNode = _cp.getClassNodeMaterial(material);
        _icon = StatusIcons.getIconForMarking(_classNode.getMarking());

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
    public materials.ClassNode getIdentifyingElement() {
        return _classNode;
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
        hash = HashUtils.calcHashCode(hash, _classNode.getClass().getName());
        return hash;
    }
}


