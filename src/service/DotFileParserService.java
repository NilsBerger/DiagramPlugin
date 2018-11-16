package service;

import materials.ClassDependency;
import materials.JavaClassNode;
import materials.SwiftClassNode;
import valueobjects.RelationshipType;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DotFileParserService{

    public final static Charset ENCODING = StandardCharsets.UTF_8;
    public final static String delimiter = "->";

    public static final String DEPCHECK_FILENAME = "graph.dot";
    public static final String ANDROID_JAR_NAME = "android_workspace.jar";
    public static final String JAVA_DOT_FILENAME = new File(".").getAbsolutePath() + "/dot/" + ANDROID_JAR_NAME + ".dot";

    private DotFileParserService()
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
                    JavaClassNode dependentClass = new JavaClassNode(parts[0]);
                    JavaClassNode independentClass = new JavaClassNode(parts[1]);
                    ClassDependency classDependency = new ClassDependency(dependentClass,independentClass, RelationshipType.DirectedRelationship);
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
                    SwiftClassNode dependentClass = new SwiftClassNode(parts[0]);
                    SwiftClassNode independentClass = new SwiftClassNode(parts[1]);
                    ClassDependency classDependency = new ClassDependency(dependentClass,independentClass, RelationshipType.DirectedRelationship);
                    dependencies.add(classDependency);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dependencies;
    }
}

