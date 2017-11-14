import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LiteralTest {

    ArrayList<Literal> literals;

    @Before
    public void setUp() throws Exception {
        literals = new ArrayList<>();
    }

    @Test
    public void shouldTestLiteralInit() throws Exception {
        Term[] terms1 = new Term[]{new Variable("x")};
        Term[] terms2 = new Term[]{new Variable("x")};

        literals.add(new Literal("American", terms1, true));
        literals.add(new Literal("Missile", terms2, false));

        assertThat(literals.stream().filter(l -> l.isNegated).count(), is(1L));
        assertThat(literals.stream().filter(l -> !l.isNegated).count(), is(1L));
        assertThat(literals.stream().filter(Literal::canUnify).count(), is(0L));
    }

    @Test
    public void shouldTestComplexLiteralInit() throws Exception {
        Term[] terms1 = new Term[]{new Variable("x"), new Variable("y"), new Variable("z")};
        Term[] terms2 = new Term[]{new Variable("z"), new Constant("America")};
        Term[] terms3 = new Term[]{new Constant("West")};

        Literal sells = new Literal("Sells", terms1, true);
        Literal enemy = new Literal("Enemy", terms2, true);
        Literal criminal = new Literal("Criminal", terms3, false);

        assertThat(sells.canUnify(), is(false));
        assertThat(enemy.canUnify(), is(false));
        assertThat(criminal.canUnify(), is(true));
    }

    @Test
    public void shouldReturnAllTerms() throws Exception {
        Term[] terms = new Term[]{new Variable("x"), new Variable("y"), new Variable("z")};
        Literal sells = new Literal("Sells", terms, true);

        assertThat(Arrays.stream(sells.getTerms()).map(Term::getName).reduce(String::concat).get(), is("xyz"));
    }
}