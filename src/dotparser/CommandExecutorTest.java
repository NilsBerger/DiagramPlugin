package dotparser;
import org.junit.Test;

/**
 * Created by Nils-Pc on 06.08.2018.
 */
public class CommandExecutorTest {

    @Test
    public void testReverse()
    {
        CommandExecutor executor = new CommandExecutor();
        DependencyModelProvider dependencyModelProvider = new DependencyModelProvider();
        //dependencyModelCreator.getSwiftDependencies();

        //executor.executeCommand("depcheck graph --dot  --workspace /Users/nilsberger/Documents/Masterarbeit/hdw-app-ios-tilmann/HDW.xcworkspace/ --scheme HDW");
        //executor.executeCommand("depcheck graph --dot  --workspace /Users/nilsberger/Documents/Masterarbeit/hdw-app-ios-tilmann/HDW.xcworkspace/ --scheme Users/nilsberger/Documents/Masterarbeit/hdw-app-ios-tilmann/HDW > javanils.dot");
    }
    @Test
    public void test1()
    {
        //DependencyModelCreator dependencyModelCreator = new DependencyModelCreator();
        //dependencyModelCreator.getAndroidDependencies();
    }
}
