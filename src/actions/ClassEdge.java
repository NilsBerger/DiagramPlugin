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
import com.intellij.diagram.DiagramEdgeBase;
import com.intellij.diagram.DiagramRelationshipInfo;
import materials.ClassNodeMaterial;

/**
 * @author Konstantin Bulenkov
 */
public class ClassEdge extends DiagramEdgeBase<ClassNodeMaterial> {
    public ClassEdge(ClassNode source, ClassNode target, DiagramRelationshipInfo relationship) {
        super(source, target, relationship);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(this == obj)
            return true;
        if(this.getClass() != obj.getClass())
            return false;
        final ClassEdge otherClassEdge = (ClassEdge) obj;
        return this.getSource().equals(otherClassEdge.mySource) &&
                this.getTarget().equals(otherClassEdge.getTarget());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = HashUtils.calcHashCode(hash, mySource.getIdentifyingElement());
        hash = HashUtils.calcHashCode(hash, myTarget.getIdentifyingElement());
        return hash;
    }
}
