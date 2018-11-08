package materials;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SwiftClassNodeTest {

    @Test
    public void equalsTest()
    {
        ClassNode m1 = new SwiftClassNode("C");
        ClassNode m2 = new SwiftClassNode("C.java");
        assertThat(m1, is(m2));
        assertThat(m1.hashCode(), is(m2.hashCode()));
    }
}
