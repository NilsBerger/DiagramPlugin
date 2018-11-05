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

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ClassDependencyMaterialTest {

    @Test
    public void equalsAndHashCodeTest() {

        ClassDependencyMaterial classDependencyMaterial = new ClassDependencyMaterial(new JavaClassNodeMaterial("a"), new JavaClassNodeMaterial("b"));
        ClassDependencyMaterial otherclassDependencyMaterial = new ClassDependencyMaterial(new JavaClassNodeMaterial("a"), new JavaClassNodeMaterial("b"));

        assertThat(classDependencyMaterial, is((classDependencyMaterial)));
        assertThat(classDependencyMaterial, is(otherclassDependencyMaterial));

        assertThat(classDependencyMaterial.hashCode(), is(not(otherclassDependencyMaterial.hashCode()+1)));
        assertThat(classDependencyMaterial.hashCode(), is(otherclassDependencyMaterial.hashCode()));
    }

    @Test
    public void getDependenciesTest() {
        JavaClassNodeMaterial javaClassNodeMaterial = new JavaClassNodeMaterial("a");
        SwiftClassNodeMaterial swiftClassNodeMaterial = new SwiftClassNodeMaterial("b");
        ClassDependencyMaterial classDependencyMaterial = new ClassDependencyMaterial(javaClassNodeMaterial, swiftClassNodeMaterial);

        assertThat(javaClassNodeMaterial, is(classDependencyMaterial.getDependentClass()));
        assertThat(swiftClassNodeMaterial, is(classDependencyMaterial.getIndependentClass()));

    }

    @Test
    public void getSwitchedDependenciesTest() {

        JavaClassNodeMaterial javaClassNodeMaterial = new JavaClassNodeMaterial("a");
        SwiftClassNodeMaterial swiftClassNodeMaterial = new SwiftClassNodeMaterial("b");
        ClassDependencyMaterial classDependencyMaterial = new ClassDependencyMaterial(javaClassNodeMaterial, swiftClassNodeMaterial);
        ClassDependencyMaterial switchedClassDependencyMaterial = new ClassDependencyMaterial(swiftClassNodeMaterial, javaClassNodeMaterial);

        assertThat(classDependencyMaterial, is(switchedClassDependencyMaterial.switchDependencies()));
    }
}
