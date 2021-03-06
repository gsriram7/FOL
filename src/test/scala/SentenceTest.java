import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        List<Literal> literalList = IntStream.range(2, 100).mapToObj(i -> new Literal("Missile" + i, new Term[]{new Constant("X1")}, false)).collect(Collectors.toList());

        Sentence q1 = new Sentence(literalList.toArray(new Literal[literalList.size()]));

        assertThat(s.unifiable(q1), is(false));

        literalList.add(new Literal("Missile", new Term[]{new Constant("X1")}, false));

        Sentence q2 = new Sentence(literalList.toArray(new Literal[literalList.size()]));

        assertThat(s.unifiable(q2), is(true));
    }

    @Test
    public void shouldRemoveALiteralFromSentence() throws Exception {
        Sentence sentence = Parser.parseSentence("~D(x,y) | ~Q(y) | C(x,y)");

        assertThat(sentence.removeLiteral(Parser.parseLiteral("~Q(y)")).size(), is(2));
        assertThat(sentence.removeLiteral(Parser.parseLiteral("~Q(y)")).contains(Parser.parseLiteral("~Q(y)")), is(false));
        assertThat(sentence.removeLiteral(Parser.parseLiteral("~E(y)")).size(), is(3));
    }

    @Test
    public void shouldRemoveAConstantLiteralFromSentence() throws Exception {
        Sentence sentence = Parser.parseSentence("~D(x,y) | ~H(Jon)");

        assertThat(sentence.removeLiteral(Parser.parseLiteral("~H(Jon)")).size(), is(1));
        assertThat(sentence.removeLiteral(Parser.parseLiteral("~H(Jon)")).get(0), is(Parser.parseLiteral("~D(x,y)")));
    }

    @Test
    public void shouldReturnScoreBasedOnNumberOfConstants() throws Exception {
        Sentence sentence1 = Parser.parseSentence("~D(Bill,Jon) | ~H(Jon)");
        Sentence sentence2 = Parser.parseSentence("~D(x,Jon) | ~H(x)");
        Sentence sentence3 = Parser.parseSentence("~D(x,y) | ~H(x)");

        assertThat(sentence1.score, is(30));
        assertThat(sentence2.score, is(2));
        assertThat(sentence3.score, is(0));
    }

    @Test
    public void shouldTestUnifiableWithSampleInput() throws Exception {
        File file = new File(this.getClass().getResource("ip1.txt").getFile());
        BufferedReader br = new BufferedReader(new FileReader(file));

        List<String> lines = br.lines().collect(Collectors.toList());
        br.close();

        List<Sentence> sentences = lines.stream().skip(6).map(Parser::parseSentence).collect(Collectors.toList());

        lines.stream().limit(6).forEach(l -> printUnifiableSentences(sentences, l));

    }

    private void printUnifiableSentences(List<Sentence> sentences, String l) {
        Sentence q = Parser.parseSentence(l);
        System.out.println(q + "\n---------");
        sentences.stream().filter(sent -> sent.unifiable(q)).forEach(System.out::println);
        System.out.println();
    }
}