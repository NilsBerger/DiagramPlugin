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

package materials;

import Utils.HashUtils;

/**
 * Created by Nils-Pc on 06.08.2018.
 */
public class ClassDependencyMaterial implements DependencyIF {
    protected final ClassNodeMaterial dependentClass;
    protected final ClassNodeMaterial independentClass;


    public ClassDependencyMaterial(ClassNodeMaterial dependentClass, ClassNodeMaterial independentClass)
    {
        if((dependentClass) == null ||(independentClass) == null)
        {
            throw new IllegalArgumentException("ClassNode should not be null");
        }
        this.dependentClass = dependentClass;
        this.independentClass = independentClass;
    }
    public ClassDependencyMaterial switchDependencies()
    {
        return new ClassDependencyMaterial(this.independentClass, this.dependentClass);
    }

    /**
     * Returns a ClassNode, that dependents on another class
     * @return ClassNode
     */

    public ClassNodeMaterial getDependentClass() {
        return dependentClass;
    }

    /**
     * Returns a ClassNode that the depeendent ClassNode depends on.
     * @return
     */
    public ClassNodeMaterial getIndependentClass() {
        return independentClass;
    }

    @Override
    public String toString() {
        return "Class '" + dependentClass.getSimpleClassName() +"' is dependent on Class '" + independentClass.getSimpleClassName()+ "'.";
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = HashUtils.calcHashCode(hash, independentClass);
        hash = HashUtils.calcHashCode(hash, dependentClass);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(this == obj)
            return true;
        if(this.getClass() != obj.getClass())
            return false;
        final ClassDependencyMaterial otherClassNodeFachwert = (ClassDependencyMaterial) obj;
        return  this.getDependentClass().equals(otherClassNodeFachwert.getDependentClass()) &&
                this.getIndependentClass().equals(otherClassNodeFachwert.getIndependentClass());
    }
}
