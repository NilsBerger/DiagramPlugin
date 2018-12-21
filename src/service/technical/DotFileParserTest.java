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

package service.technical;

import materials.ClassDependency;
import org.junit.Test;
import valueobjects.ClassLanguageType;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DotFileParserTest
{

    public final String _filepath = "/Users/nilsberger/Documents/Masterarbeit/clean2/DiagramPlugin/src/test/resources/android_graph.dot";


    @Test
    public void paserJavaDepenendicesFormDotFileTest()
    {

        Set<ClassDependency> dependencies = DotFileParser.parseJavaDependenciesFromDotFile(_filepath);

        assertThat(dependencies.size(), greaterThan(0));
        assertThat(dependencies.size(), is(13));

        for(ClassDependency dependency : dependencies)
        {
            assertThat(dependency.getDependentClass().getClassLanguageType(), is(ClassLanguageType.Java));
            assertThat(dependency.getIndependentClass().getClassLanguageType(), is(ClassLanguageType.Java));
        }
    }

    @Test
    public void paserSwiftDepenendicesFormDotFileTest()
    {

        List<ClassDependency> dependencies = DotFileParser.parseSwiftDependenciesFromDotFile(_filepath);

        assertThat(dependencies.size(), greaterThan(0));
        assertThat(dependencies.size(), is(21));

        for(ClassDependency dependency : dependencies)
        {
            assertThat(dependency.getDependentClass().getClassLanguageType(), is(ClassLanguageType.Swift));
            assertThat(dependency.getIndependentClass().getClassLanguageType(), is(ClassLanguageType.Swift));
        }
    }
}
