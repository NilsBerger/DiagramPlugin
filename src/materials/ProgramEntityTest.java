

package materials;

import com.google.common.io.Files;
import org.junit.Test;
import valueobjects.Language;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ProgramEntityTest {

    @Test
    public void EqualsAndHashCodeTest() {
        ProgramEntity programEntity = new ProgramEntity("a", Language.Default);
        ProgramEntity programEntity2 = new ProgramEntity("b", Language.Default);

        assertThat(programEntity, is(programEntity));
        assertThat(programEntity.hashCode(), is(programEntity.hashCode()));

        assertThat(programEntity, is(not(programEntity2)));
    }

    @Test
    public void setSourceFilepathTest() throws IOException {
        File tempFile = File.createTempFile("Temp", ".java");
        ProgramEntity programEntity = new ProgramEntity("a", Language.Default);

        String temppath = tempFile.getCanonicalPath();
        programEntity.setSourceFilePath(temppath);
        assertThat(temppath, is(programEntity.getSourceFilePath()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWrongSourceFilepathTest() throws IOException {
        File tempDir = Files.createTempDir();
        ProgramEntity programEntity = new ProgramEntity("a", Language.Default);

        String temppath = tempDir.getCanonicalPath();
        programEntity.setSourceFilePath(temppath);
    }


}
