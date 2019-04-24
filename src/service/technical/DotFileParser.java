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

import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import valueobjects.Language;
import valueobjects.RelationshipType;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DotFileParser {

    public final static Charset ENCODING = StandardCharsets.UTF_8;
    public final static String delimiter = "->";

    public static final String DEPCHECK_FILENAME = "graph.dot";
    public static final String ANDROID_JAR_NAME = "android_workspace.jar";
    public static final String JAVA_DOT_FILENAME = new File(".").getAbsolutePath() + "/dot/" + ANDROID_JAR_NAME + ".dot";

    private static final Map<String, ProgramEntity> _javaLookUp = new HashMap();
    private static final Map<String, ProgramEntity> _swiftLookUp = new HashMap();

    private DotFileParser()
    {
    }

    public static Set<ProgramEntityRelationship> parseJavaDependenciesFromDotFile(final String filepath)
    {
        Set<ProgramEntityRelationship> dependencies = new HashSet<>();
        File dotFile = new File(filepath);
        if(!dotFile.isFile())
        {
            throw new IllegalArgumentException("Filpath is not a file:'" + filepath + "'" );
        }
        if(!dotFile.getName().endsWith(".dot"))
        {
            throw new IllegalArgumentException("File:'" + filepath + "' does not end wit '.dot'.");
        }
        Path path = dotFile.toPath();
        try(BufferedReader reader = Files.newBufferedReader(path,ENCODING))
        {
            String line;
            while((line = reader.readLine()) != null) {
                if (line.contains(delimiter))
                {
                    String[] parts = line.split(delimiter);
                    ProgramEntity dependentClass = getClassNode(parts[0], Language.Java);
                    ProgramEntity independentClass = getClassNode(parts[1], Language.Java);
                    ProgramEntityRelationship programEntityRelationship = new ProgramEntityRelationship(dependentClass, independentClass, RelationshipType.Dependency);
                    dependencies.add(programEntityRelationship);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String lang = ".lang";
        //Delete "lang." dependencies
        Iterator<ProgramEntityRelationship> it = dependencies.iterator();
        while(it.hasNext())
        {
            ProgramEntityRelationship dependency = it.next();
            if (dependency.getDependentClass().getFullEntityName().contains(lang))
           {
               it.remove();
           }
            if (dependency.getIndependentClass().getFullEntityName().contains(lang))
           {
               it.remove();
           }
        }
        final Set<ProgramEntityRelationship> javaMissedDependencies = MissedDependencies.getJavaMissedDependencies();
        for (ProgramEntityRelationship programEntityRelationship : javaMissedDependencies) {
            ProgramEntity dependentClass = getClassNode(programEntityRelationship.getDependentClass().getSimpleName(), Language.Java);
            ProgramEntity independentClass = getClassNode((programEntityRelationship.getIndependentClass().getSimpleName()), Language.Java);
            ProgramEntityRelationship dependency = new ProgramEntityRelationship(dependentClass, independentClass, RelationshipType.Dependency);
            dependencies.add(dependency);
        }
        final Set<ProgramEntityRelationship> dependencies1 = deleteWrongDependencies(dependencies);
        return Collections.unmodifiableSet(dependencies1);
    }

    private static ProgramEntity getClassNode(String name, Language languageType) {
        if (languageType == Language.Java) {
            return findeNode(name, languageType, _javaLookUp);
        }
        if (languageType == Language.Swift) {
            return findeNode(name, languageType, _swiftLookUp);
        }
        throw new IllegalStateException("Could not find ClassLanguage: " + languageType);

    }

    private static ProgramEntity findeNode(String className, Language type, Map<String, ProgramEntity> map) {
        //TmpNode to get the Simple Name of the class that is used as a key
        ProgramEntity tmpNode = new ProgramEntity(className, Language.Default);
        if (!map.containsKey(tmpNode.getSimpleName())) {
            ProgramEntity newProgramEntity = new ProgramEntity(className, type);
            map.put(tmpNode.getSimpleName(), newProgramEntity);
            return newProgramEntity;
        } else {
            return map.get(tmpNode.getSimpleName());
        }
    }

    public static List<ProgramEntityRelationship> parseSwiftDependenciesFromDotFile(final String filepath) {
        List<ProgramEntityRelationship> dependencies = new ArrayList<>();
        File dotFile = new File(filepath);
        if(!dotFile.isFile())
        {
            throw new IllegalArgumentException("Filpath is not a file:'" + filepath + "'" );
        }
        if(!dotFile.getName().endsWith(".dot"))
        {
            throw new IllegalArgumentException("File:'" + filepath + "' does not end wit '.dot'.");
        }
        Path path = dotFile.toPath();
        try(BufferedReader reader = Files.newBufferedReader(path,ENCODING))
        {
            String line;
            while((line = reader.readLine()) != null) {
                if (line.contains(delimiter))
                {
                    String[] parts = line.split(delimiter);
                    ProgramEntity dependentClass = getClassNode(parts[0], Language.Swift);
                    ProgramEntity independentClass = getClassNode(parts[1], Language.Swift);
                    ProgramEntityRelationship programEntityRelationship = new ProgramEntityRelationship(dependentClass, independentClass, RelationshipType.Dependency);
                    dependencies.add(programEntityRelationship);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.unmodifiableList(dependencies);
    }

    /**
     * Delete the dependencies containing the ClassNodes "InstantReloadException" or "IncrementalChange".
     *
     * @param dependencyList
     * @return
     */
    private static Set<ProgramEntityRelationship> deleteWrongDependencies(final Set<ProgramEntityRelationship> dependencyList) {
        final String badNameA = "IncrementalChange";
        final String badNameB = "InstantReloadException";
        final Iterator<ProgramEntityRelationship> iterator = dependencyList.iterator();
        while (iterator.hasNext()) {
            ProgramEntityRelationship dependency = iterator.next();
            ProgramEntity nodeA = dependency.getIndependentClass();
            ProgramEntity nodeB = dependency.getDependentClass();
            if (nodeA.getSimpleName().equals(badNameA) || nodeA.getSimpleName().equals(badNameB)) {
                iterator.remove();
            }
            if (nodeB.getSimpleName().equals(badNameA) || nodeB.getSimpleName().equals(badNameB)) {
                iterator.remove();
            }
        }
        return dependencyList;
    }
}

