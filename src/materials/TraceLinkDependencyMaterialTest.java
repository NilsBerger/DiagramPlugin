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

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TraceLinkDependencyMaterialTest {

    @Test
    public void basicTest()
    {
        JavaClassNodeMaterial javaClassNodeMaterial = new JavaClassNodeMaterial("Java");
        SwiftClassNodeMaterial swiftClassNodeMaterial = new SwiftClassNodeMaterial("Swift");
        double traceLinkValue = 99.9;

        TraceLinkDependencyMaterial traceLinkDependencyMaterial = new TraceLinkDependencyMaterial(javaClassNodeMaterial,swiftClassNodeMaterial, traceLinkValue);

        assertThat(javaClassNodeMaterial, is(traceLinkDependencyMaterial.getJavaClassNode()));
        assertThat(javaClassNodeMaterial, is(traceLinkDependencyMaterial.getDependentClass()));
        assertThat(swiftClassNodeMaterial, is(traceLinkDependencyMaterial.getSwiftClassNodeMaterial()));
        assertThat(swiftClassNodeMaterial, is(traceLinkDependencyMaterial.getIndependentClass()));
        assertThat(traceLinkValue, is(traceLinkDependencyMaterial.getTracelinkValue()));
    }

    @Test
    public void setTraceLinkTest()
    {
        JavaClassNodeMaterial javaClassNodeMaterial = new JavaClassNodeMaterial("Java");
        SwiftClassNodeMaterial swiftClassNodeMaterial = new SwiftClassNodeMaterial("Swift");
        double traceLinkValue = 99.9;

        TraceLinkDependencyMaterial traceLinkDependencyMaterial = new TraceLinkDependencyMaterial(javaClassNodeMaterial,swiftClassNodeMaterial, traceLinkValue);

        double newTraceValue = 11.1;
        traceLinkDependencyMaterial.setTracelinkvalue(newTraceValue);
        assertThat(newTraceValue, is(traceLinkDependencyMaterial.getTracelinkValue()));
    }

    @Test
    public void equalsAndHashTest()
    {
        JavaClassNodeMaterial javaClassNodeMaterial = new JavaClassNodeMaterial("Java");
        SwiftClassNodeMaterial swiftClassNodeMaterial = new SwiftClassNodeMaterial("Swift");
        double traceLinkValue = 99.9;
        TraceLinkDependencyMaterial traceLinkDependencyMaterial = new TraceLinkDependencyMaterial(javaClassNodeMaterial,swiftClassNodeMaterial, traceLinkValue);

        JavaClassNodeMaterial javaClassNodeMaterial1 = new JavaClassNodeMaterial("Java");
        SwiftClassNodeMaterial swiftClassNodeMaterial1 = new SwiftClassNodeMaterial("Swift");
        double traceLinkValue1 = 99.9;
        TraceLinkDependencyMaterial traceLinkDependencyMaterial1 = new TraceLinkDependencyMaterial(javaClassNodeMaterial1,swiftClassNodeMaterial1, traceLinkValue1);

        assertThat(traceLinkDependencyMaterial, is(traceLinkDependencyMaterial1));


    }
}
