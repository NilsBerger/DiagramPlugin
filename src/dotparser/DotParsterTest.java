package dotparser;

import org.junit.Test;
import java.io.IOException;
import dotparser.DotParser;

/**
 * Created by Nils-Pc on 06.08.2018.
 */

public class DotParsterTest {
        @Test
        public void testReverse()
        {
            try {
                DotParser.createClassDependencyGraph("/Users/nilsberger/Downloads/DotParser/test.jar.dot");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
