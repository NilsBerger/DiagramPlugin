

package material;

import com.google.common.io.Files;
import org.junit.Test;
import valueobjects.Marking;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ClassNodeMaterialTest {

    @Test
    public void EqualsAndHashCodeTest()
    {
        ClassNodeMaterial classNodeMaterial = new ClassNodeMaterial("a");
        ClassNodeMaterial classNodeMaterial2 = new ClassNodeMaterial("b");

        assertThat(classNodeMaterial, is(classNodeMaterial));
        assertThat(classNodeMaterial.hashCode(), is(classNodeMaterial.hashCode()));

        assertThat(classNodeMaterial, is(not(classNodeMaterial2)));
    }

    @Test
    public void setSourceFilepathTest() throws IOException {
        File tempFile = File.createTempFile("Temp", ".java");
        ClassNodeMaterial classNodeMaterial = new ClassNodeMaterial("a");

        String temppath = tempFile.getCanonicalPath();
        classNodeMaterial.setSourceFilePath(temppath);
        assertThat(temppath, is(classNodeMaterial.getSourceFilePath()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void setWrongSourceFilepathTest() throws IOException {
        File tempDir = Files.createTempDir();
        ClassNodeMaterial classNodeMaterial = new ClassNodeMaterial("a");

        String temppath = tempDir.getCanonicalPath();
        classNodeMaterial.setSourceFilePath(temppath);
    }


    @Test
    public void oldMarking()
    {
        ClassNodeMaterial classNode = new JavaClassNodeMaterial("C");
        classNode.setMarking(Marking.BLANK);

        Marking oldMarking = classNode.getMarking();
        Marking newMarking = Marking.CHANGED;
        classNode.setMarking(newMarking);
        assertThat(oldMarking, is(classNode.getOldMarking()));
        assertThat(classNode.getMarking(), is(newMarking));
    }
}
