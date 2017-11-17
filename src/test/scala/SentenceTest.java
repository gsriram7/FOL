import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SentenceTest {

    Sentence s;

    @Before
    public void setUp() throws Exception {
        Term[] missileTerms = {new Variable("x")};
        Term[] ownsTerms = {new Constant("Nono"), new Variable("x")};
        Term[] sellsTerms = {new Constant("West"), new Variable("x"), new Constant("Nono")};

        Literal missile = new Literal("Missile", missileTerms, true);
        Literal owns = new Literal("Owns", ownsTerms, true);
        Literal sells = new Literal("Sells", sellsTerms, false);

        s = new Sentence(new Literal[]{missile, owns, sells});
    }

    @Test
    public void shouldInitSentences() throws Exception {
        Term[] missileTerms = {new Variable("x")};
        Term[] ownsTerms = {new Constant("Nono"), new Variable("x")};
        Term[] sellsTerms = {new Constant("West"), new Variable("x"), new Constant("Nono")};

        Literal missile = new Literal("Missile", missileTerms, true);
        Literal owns = new Literal("Owns", ownsTerms, true);
        Literal sells = new Literal("Sells", sellsTerms, false);

        Sentence sentence = new Sentence(new Literal[]{missile, owns, sells});

        Literal[] literals = sentence.getLiterals();

        assertThat(literals.length, is(3));
        assertThat(literals[0].getTerms()[0], is(missileTerms[0]));
        assertThat(literals[1].getTerms()[0], is(ownsTerms[0]));
        assertThat(literals[1].getTerms()[1], is(ownsTerms[1]));
        assertThat(literals[2].getTerms()[0], is(sellsTerms[0]));
        assertThat(literals[2].getTerms()[1], is(sellsTerms[1]));
        assertThat(literals[2].getTerms()[2], is(sellsTerms[2]));

        System.out.println(sentence);
    }

    @Test
    public void shouldCachePositiveAndNegativeLiterals() throws Exception {
        assertThat(s.positiveLiteral.size(), is(1));
        assertThat(s.negativeLiteral.size(), is(2));
        assertThat(s.positiveLiteral.containsKey("Sells"), is(true));
        assertThat(s.negativeLiteral.containsKey("Sells"), is(false));
    }

    @Test
    public void shouldTestIfTheSentenceIsEmpty() throws Exception {
        Sentence sentence = new Sentence(new Literal[]{});

        assertThat(sentence.isEmpty(), is(true));
    }

    @Test
    public void shouldTestIfTheSentenceIsUnifiable() throws Exception {
        Sentence q1 = new Sentence(new Literal[]{new Literal("Missile", new Term[]{new Constant("X1")}, false)});
        Sentence q2 = new Sentence(new Literal[]{new Literal("Sells", new Term[]{new Constant("West"), new Constant("X1"), new Constant("Nono")}, true)});
        Sentence q3 = new Sentence(new Literal[]{new Literal("NotPresent", new Term[]{new Constant("X1")}, false)});
        Sentence q4 = new Sentence(new Literal[]{new Literal("Missile", new Term[]{new Constant("X1")}, true)});
        Sentence q5 = new Sentence(new Literal[]{new Literal("Sells", new Term[]{new Constant("West"), new Constant("X1"), new Constant("Nono")}, false)});

        assertThat(s.unifiable(q1), is(true));
        assertThat(s.unifiable(q2), is(true));
        assertThat(s.unifiable(q3), is(false));
        assertThat(s.unifiable(q4), is(false));
        assertThat(s.unifiable(q5), is(false));
    }

    @Test
    public void shouldTestIfASentenceWithMultipleLiteralsAreUnifiable() throws Exception {
        List<Literal> literalList = IntStream.range(2, 100).mapToObj(i -> new Literal("Missle" + i, new Term[]{new Constant("X1")}, false)).collect(Collectors.toList());

        Sentence q1 = new Sentence(literalList.toArray(new Literal[literalList.size()]));

        assertThat(s.unifiable(q1), is(false));

        literalList.add(new Literal("Missile", new Term[]{new Constant("X1")}, false));

        Sentence q2 = new Sentence(literalList.toArray(new Literal[literalList.size()]));

        assertThat(s.unifiable(q2), is(true));
    }
}