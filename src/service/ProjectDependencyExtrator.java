package service;

import Utils.JarUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectDependencyExtrator {

    private ProjectDependencyExtrator(){}

    public static void extractDependenciesFromJavaProject(final String android_workspace_filepath) {
        if (android_workspace_filepath == null || android_workspace_filepath.length() == 0) {
            throw new IllegalArgumentException("The path: " + android_workspace_filepath + "is null or of zero length");
        }
        if (!(new File(android_workspace_filepath).isDirectory())) {
            throw new IllegalArgumentException("The path: " + android_workspace_filepath + " is not a directory");
        }

        System.out.println("---------------------------------------------------------------");
        System.out.println("START: Java Dependencies");

        long startTime = System.currentTimeMillis();

        try {
            JarUtils.createJar(android_workspace_filepath, DotFileParser.ANDROID_JAR_NAME);
        } catch (IOException e) {
            System.out.println("Could not crate jar");
            e.printStackTrace();
        }
        CommandExecutor executor = new CommandExecutor();
        executor.executeCommand(CommandExecutor.JDEPS_COMMAND + DotFileParser.ANDROID_JAR_NAME);

        long endTime = System.currentTimeMillis();
        long timeInSeconds = (((endTime - startTime) / 1000) % 60);

        System.out.println("Extracted Java dependencies in " + Long.toString(timeInSeconds) + " seconds");
        System.out.println("---------------------------------------------------------------");

    }



    public static void extractDependenciesFromSwiftProject(final String swift_workspace_path, final String scheme_path) {
        if (swift_workspace_path == null || swift_workspace_path.length() == 0) {
            throw new IllegalArgumentException("The path: " + swift_workspace_path + "is null or of zero lenght");
        }
        if (!(new File(swift_workspace_path).isDirectory())) {
            throw new IllegalArgumentException("The path: " + swift_workspace_path + "is not a directory");
        }
        if (scheme_path == null || scheme_path.length() == 0) {
            throw new IllegalArgumentException("The path: " + scheme_path + "is null or of zero lenght");
        }
        if (!(new File(scheme_path).isDirectory())) {
            throw new IllegalArgumentException("The path: " + scheme_path + "is not a directory");
        }

        System.out.println("---------------------------------------------------------------");
        System.out.println("START: Swift Dependencies");
        System.out.println("%n");

        long startTime = System.currentTimeMillis();

        CommandExecutor executor = new CommandExecutor();
        executor.executeCommand(CommandExecutor.DEPCHECK_COMMAND.replace("PATH", swift_workspace_path).replace("SCHEME", new File(scheme_path).getName()));

        long endTime = System.currentTimeMillis();
        long timeInSeconds = (((endTime - startTime) / 1000) % 60);

        System.out.println("Extracted swift dependencies in " + Long.toString(timeInSeconds) + " seconds");
        System.out.println("---------------------------------------------------------------");
    }

    public static void updateDepenendecies(final String android_workspace_filepath, final String swift_workspace_path, final String scheme_path)
    {
        extractDependenciesFromJavaProject(android_workspace_filepath);
        extractDependenciesFromSwiftProject(swift_workspace_path, scheme_path);
    }


    private static boolean doesFileExistAndIsFile(final String filepath) {
        File file = new File(filepath);
        Path path = file.toPath();
        return Files.exists(path) && file.isFile();
    }
}
