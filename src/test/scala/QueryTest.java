import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class QueryTest {

    @Test
    public void shouldReturnRefutedSentenceFromQuery() throws Exception {
        Query query = new Query(new Literal("Criminal", new Term[]{new Constant("West")}, false));

        assertThat(query.refute().getLiterals()[0].isNegated, is(true));
    }
}