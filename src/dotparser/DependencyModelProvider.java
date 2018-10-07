package dotparser;

import Utils.JarUtils;
import Utils.PluginProperties;
import Utils.PropertyName;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DependencyModelProvider {
    public static final String DEPCHECK_COMMAND = "depcheck graph --dot  --workspace PATH --scheme SCHEME";
    public static final String DEPCHECK_FILENAME = "graph.dot";


    //JDEPS Command needs name of jar-File.
    public static final String JDEPS_COMMAND = "jdeps -v -dotoutput dot ";
    public static final String ANDROID_JAR_NAME = "android_workspace.jar";

    public static List<DependencyFachwert> getSwiftDependencies(final String swift_workspace_path, final String scheme_path) {

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

        System.out.println("START: Swift Dependencies");
        System.out.println("");
        System.out.println("");


        CommandExecutor executor = new CommandExecutor();
        executor.executeCommand(DEPCHECK_COMMAND.replace("PATH", swift_workspace_path).replace("SCHEME", new File(scheme_path).getName()));


        if (doesFileExist(DEPCHECK_FILENAME)) {
            try {
                DotParser.createClassDependencyGraph(DEPCHECK_FILENAME);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<DependencyFachwert>();
    }
    public static List<DependencyFachwert> getAndroidDependencies(final String android_workspace_path)
    {
        if(android_workspace_path == null || android_workspace_path.length()== 0)
        {
            throw new IllegalArgumentException("The path: " + android_workspace_path +"is null or of zero length");
        }
        if(!(new File(android_workspace_path).isDirectory()))
        {
            throw new IllegalArgumentException("The path: " + android_workspace_path +" is not a directory");
        }

        System.out.println("START: Android Dependencies");


        try {
            JarUtils.createJar(android_workspace_path, DependencyModelProvider.ANDROID_JAR_NAME);
            CommandExecutor executor = new CommandExecutor();
            executor.executeCommand(DependencyModelProvider.JDEPS_COMMAND + DependencyModelProvider.ANDROID_JAR_NAME);
            DotParser.createClassDependencyGraph(new java.io.File(".").getCanonicalPath() + "/dot/" + DependencyModelProvider.ANDROID_JAR_NAME + ".dot");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not create jar.");
        }
        return new ArrayList<DependencyFachwert>();
    }
    public static void updateDependencyModels()
    {
        PluginProperties properties;

        try {
            properties = PluginProperties.getInstance();
            properties.readPluginProperties();

            //Android
            getAndroidDependencies(properties.getProperty(PropertyName.ANDROID_WORKSPACE));
            //Swift
            getSwiftDependencies(properties.getProperty(PropertyName.SWIFT_WORKSPACE), properties.getProperty(PropertyName.Swift_SCHEME_FOLDER));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean doesFileExist(final String filename)
    {
        return new File(filename).isFile();
    }
}
