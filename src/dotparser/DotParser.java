package dotparser;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nils-Pc on 06.08.2018.
 */
public class DotParser {
    public final static Charset ENCODING = StandardCharsets.UTF_8;
    public final static String delimiter = "->";
    private DotParser()
    {
    }
    public static void createClassDependencyGraph(final String fileName) throws IOException
    {
        Path path = Paths.get(fileName);
        List<DependencyFachwert> nodeList = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(path, ENCODING))
        {
            String line = null;
            while((line = reader.readLine()) != null )
            {
                if (line.contains(delimiter))
                {
                    String[] parts = line.split(delimiter);
                    ClassNodeMaterial dependentClass = new ClassNodeMaterial(parts[0]);
                    ClassNodeMaterial independentClass = new ClassNodeMaterial(parts[1]);
                    nodeList.add(new DependencyFachwert(dependentClass, independentClass));
                }
            }
        }
        for(DependencyFachwert relationship : nodeList)
        {
            System.out.println(relationship.toString());
        }

    }

    public static List<DependencyFachwert> getClassDependencyGraph(final String fileName) throws IOException
    {
        Path path = Paths.get(fileName);
        List<DependencyFachwert> nodeList = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(path, ENCODING))
        {
            String line = null;
            while((line = reader.readLine()) != null )
            {
                if (line.contains(delimiter))
                {
                    String[] parts = line.split(delimiter);
                    ClassNodeMaterial dependentClass = new ClassNodeMaterial(parts[0]);
                    ClassNodeMaterial independentClass = new ClassNodeMaterial(parts[1]);
                    nodeList.add(new DependencyFachwert(dependentClass, independentClass));
                }
            }
        }
        return nodeList;

    }
}
