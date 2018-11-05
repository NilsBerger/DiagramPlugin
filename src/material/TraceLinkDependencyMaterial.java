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

package material;

import material.ClassDependencyMaterial;
import material.ClassNodeMaterial;

public class TraceLinkDependencyMaterial extends ClassDependencyMaterial {

    private double tracelinkvalue;
    public TraceLinkDependencyMaterial(JavaClassNodeMaterial dependentClass, SwiftClassNodeMaterial independentClass, double tracelinkvalue) {
        super(dependentClass, independentClass);
        this.tracelinkvalue = tracelinkvalue;
    }
    public double getTracelinkvalue() {
        return tracelinkvalue;
    }


    public void setTracelinkvalue(double tracelinkvalue) {
        this.tracelinkvalue = tracelinkvalue;
    }

    public JavaClassNodeMaterial getJavaClassNode()
    {
        return (JavaClassNodeMaterial) getDependentClass();
    }

    public SwiftClassNodeMaterial getSwiftClassNodeMaterial()
    {
        return (SwiftClassNodeMaterial) getIndependentClass();
    }

}
