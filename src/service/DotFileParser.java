package service;

import materials.ClassDependency;
import materials.ClassNode;
import valueobjects.ClassLanguageType;
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

    private DotFileParser()
    {
    }

    public static Set<ClassDependency> parseJavaDependenciesFromDotFile(final String filepath)
    {
        Set<ClassDependency> dependencies = new HashSet<>();
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
                    ClassNode dependentClass = new ClassNode(parts[0], ClassLanguageType.Java);
                    ClassNode independentClass = new ClassNode(parts[1], ClassLanguageType.Java);
                    ClassDependency classDependency = new ClassDependency(dependentClass,independentClass, RelationshipType.Dependency);
                    dependencies.add(classDependency);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String lang = ".lang";
        //Delete "lang." dependencies
        Iterator<ClassDependency> it = dependencies.iterator();
        while(it.hasNext())
        {
            ClassDependency dependency = it.next();
           if(dependency.getDependentClass().getFullClassName().contains(lang))
           {
               it.remove();
           }
           if(dependency.getIndependentClass().getFullClassName().contains(lang))
           {
               it.remove();
           }
        }
        return dependencies;
    }

    public static List<ClassDependency> parseSwiftDependenciesFromDotFile(final String filepath)
    {
        List<ClassDependency> dependencies = new ArrayList<>();
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
                    ClassNode dependentClass = new ClassNode(parts[0], ClassLanguageType.Swift);
                    ClassNode independentClass = new ClassNode(parts[1], ClassLanguageType.Swift);
                    ClassDependency classDependency = new ClassDependency(dependentClass,independentClass, RelationshipType.Dependency);
                    dependencies.add(classDependency);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dependencies;
    }
}

