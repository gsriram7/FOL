import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TermTest {

    @Test
    public void shouldInitTerm() throws Exception {
        Constant dir = new Constant("West");
        Variable x = new Variable("x");

        assertThat(dir.type, is(Type.CONSTANT));
        assertThat(x.type, is(Type.VARIABLE));
    }

    @Test
    public void shouldGetNameOfTerm() throws Exception {
        Constant dir = new Constant("West");

        assertThat(dir.getName(), is("West"));
    }
}