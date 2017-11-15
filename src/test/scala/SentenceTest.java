import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SentenceTest {

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
}