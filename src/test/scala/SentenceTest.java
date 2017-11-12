import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SentenceTest {

    @Test
    public void shouldTestSimpleSentenceInit() throws Exception {
        SimpleSentence ss = new SimpleSentence("American", new SimpleLiteral(new Variable("x")), true);

        assertThat(ss.isNegated, is(true));
        assertThat(ss.getName().get(0), is("American"));
    }

    @Test
    public void shouldTestCompoundSentenceInit() throws Exception {

        ArrayList<Sentence> sentences = new ArrayList<>(5);
        sentences.add(new SimpleSentence("American", new SimpleLiteral(new Variable("x")), true));
        sentences.add(new SimpleSentence("Weapon", new SimpleLiteral(new Variable("y")), true));
        sentences.add(new SimpleSentence("Sells", new SimpleLiteral(new Variable("x")), true));
        sentences.add(new SimpleSentence("Enemy", new SimpleLiteral(new Variable("x")), true));
        sentences.add(new SimpleSentence("Criminal", new SimpleLiteral(new Variable("x")), false));

        CompoundSentence cp = new CompoundSentence(sentences, sentences.size());
        List<Literal> literals = cp.getLiterals();

        assertThat(literals.size(), is(5));
        assertThat(cp.sentences.stream().filter(Sentence::isNegated).count(), is(4l));

        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 1; i <= 100000; i++) list.add(i);

        list.parallelStream().forEach(System.out::println);
    }
}