

package materials;

import com.google.common.io.Files;
import org.junit.Test;
import valueobjects.Marking;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ClassNodeTest {

    @Test
    public void EqualsAndHashCodeTest()
    {
        ClassNode classNode = new ClassNode("a");
        ClassNode classNode2 = new ClassNode("b");

        assertThat(classNode, is(classNode));
        assertThat(classNode.hashCode(), is(classNode.hashCode()));

        assertThat(classNode, is(not(classNode2)));
    }

    @Test
    public void setSourceFilepathTest() throws IOException {
        File tempFile = File.createTempFile("Temp", ".java");
        ClassNode classNode = new ClassNode("a");

        String temppath = tempFile.getCanonicalPath();
        classNode.setSourceFilePath(temppath);
        assertThat(temppath, is(classNode.getSourceFilePath()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void setWrongSourceFilepathTest() throws IOException {
        File tempDir = Files.createTempDir();
        ClassNode classNode = new ClassNode("a");

        String temppath = tempDir.getCanonicalPath();
        classNode.setSourceFilePath(temppath);
    }


    @Test
    public void oldMarking()
    {
        ClassNode classNode = new JavaClassNode("C");
        classNode.setMarking(Marking.BLANK);

        Marking oldMarking = classNode.getMarking();
        Marking newMarking = Marking.CHANGED;
        classNode.setMarking(newMarking);
        assertThat(oldMarking, is(classNode.getOldMarking()));
        assertThat(classNode.getMarking(), is(newMarking));
    }
}
