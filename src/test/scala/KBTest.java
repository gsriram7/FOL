import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class KBTest {

    private KB kb;

    @Before
    public void setUp() throws Exception {
        File file = new File(this.getClass().getResource("ip1.txt").getFile());
        BufferedReader br = new BufferedReader(new FileReader(file));

        List<String> lines = br.lines().collect(Collectors.toList());
        br.close();

        List<Sentence> sentences = lines.stream().skip(6).map(Parser::parseSentence).collect(Collectors.toList());

        ArrayList<Sentence> sents = new ArrayList<>();
        sents.addAll(sentences);

        kb = new KB(sentences.toArray(new Sentence[sentences.size()]), sents);
    }

    @Test
    public void shouldGetAllUnifiableForASentences() throws Exception {
        Sentence sentence = Parser.parseSentence("H(John) | D(x,y)");
        HashMap<Literal, ArrayList<Sentence>> map = kb.getUnifiableSentences(sentence, kb.sentences);

        System.out.println(map.toString());
        assertThat(map.size(), is(sentence.getLiterals().length));
        assertThat(map.get(Parser.parseLiteral("D(x,y)")).size(), is(2));
        assertThat(map.get(Parser.parseLiteral("H(John)")).size(), is(2));
        assertThat(map.get(Parser.parseLiteral("D(x,y)")).contains(Parser.parseSentence("~D(x,y) | ~H(y)")), is(true));
        assertThat(map.get(Parser.parseLiteral("D(x,y)")).contains(Parser.parseSentence("~D(x,y) | ~Q(y) | C(x,y)")), is(true));
        assertThat(map.get(Parser.parseLiteral("H(John)")).contains(Parser.parseSentence("~H(x) | F(x)")), is(true));
        assertThat(map.get(Parser.parseLiteral("H(John)")).contains(Parser.parseSentence("~D(x,y) | ~H(y)")), is(true));
    }

    @Test
    public void shouldGetAllUnifiableInDecreasingOrderOfConstants() throws Exception {
        Sentence sentence = Parser.parseSentence("H(John) | D(Sid,Chill) | Q(y)");
        HashMap<Literal, ArrayList<Sentence>> map = kb.getUnifiableSentences(sentence, kb.sentences);

        System.out.println(map.toString());
        assertThat(map.size(), is(sentence.getLiterals().length));
//        assertThat(map.firstKey(), is(Parser.parseLiteral("D(Sid,Chill)")));
//        assertThat(map.higherKey(Parser.parseLiteral("D(Sid,Chill)")), is(Parser.parseLiteral("H(John)")));
//        assertThat(map.lastKey(), is(Parser.parseLiteral("Q(y)")));
    }

    @Test
    public void shouldReturnNoUnifiableSentenceForANonMatchingSentence() throws Exception {
        Sentence sentence = Parser.parseSentence("P(John) | L(x,y)");
        HashMap<Literal, ArrayList<Sentence>> map = kb.getUnifiableSentences(sentence, kb.sentences);

        System.out.println(map.toString());
        assertThat(map.size(), is(0));
        assertThat(map.containsKey(Parser.parseLiteral("P(John)")), is(false));
        assertThat(map.containsKey(Parser.parseLiteral("L(x,y)")), is(false));
    }

    @Test
    public void shouldGenerateNextChildrenTuples() throws Exception {
        ArrayList<Tuple> children = kb.getNextChildren(Parser.parseSentence("H(John) | D(x,y)"), kb.sentences);

        assertThat(children.size(), is(4));
        assertThat(children.contains(new Tuple(Parser.parseSentence("H(John) | D(x,y)"), Parser.parseSentence("~H(x) | F(x)"), Parser.parseLiteral("H(John)"))), is(true));
        assertThat(children.contains(new Tuple(Parser.parseSentence("H(John) | D(x,y)"), Parser.parseSentence("~D(x,y) | ~H(y)"), Parser.parseLiteral("H(John)"))), is(true));
        assertThat(children.contains(new Tuple(Parser.parseSentence("H(John) | D(x,y)"), Parser.parseSentence("~D(x,y) | ~H(y)"), Parser.parseLiteral("D(x,y)"))), is(true));
        assertThat(children.contains(new Tuple(Parser.parseSentence("H(John) | D(x,y)"), Parser.parseSentence("~D(x,y) | ~Q(y) | C(x,y)"), Parser.parseLiteral("D(x,y)"))), is(true));
    }

    @Test
    public void shouldGenerateNextChildrenForMultiLiteralSentences() throws Exception {
        KB kb = getKB();
        ArrayList<Tuple> children = kb.getNextChildren(Parser.parseSentence("~P(Liz,y) | ~A(y,Bill)"), kb.sentences);

        assertThat(children.size(), is(4));

        int countOfP = 0;
        int countOfA = 0;

        for (Tuple aChildren : children)
            if (aChildren.literal.equals(Parser.parseLiteral("~P(Liz,y)"))) countOfP++;
            else countOfA++;

        assertThat(countOfP, is(2));
        assertThat(countOfA, is(2));
    }

    @Test
    public void shouldNotNextGenerateNewChildrenIfASentenceCantBeUnified() throws Exception {
        ArrayList<Tuple> children = kb.getNextChildren(Parser.parseSentence("P(John) | L(x,y)"), kb.sentences);

        assertThat(children.size(), is(0));
    }

    @Test
    public void shouldFilterOutDuplicatedChildren() throws Exception {
        ArrayList<Tuple> children = kb.getNextChildren(Parser.parseSentence("H(John) | H(John)"), kb.sentences);

        assertThat(children.size(), is(2));
        assertThat(children.contains(new Tuple(Parser.parseSentence("H(John) | H(John)"), Parser.parseSentence("~H(x) | F(x)"), Parser.parseLiteral("H(John)"))), is(true));
        assertThat(children.contains(new Tuple(Parser.parseSentence("H(John) | H(John)"), Parser.parseSentence("~D(x,y) | ~H(y)"), Parser.parseLiteral("H(John)"))), is(true));
    }

    @Test
    public void shouldNextGenerateForPartiallyUnifiableSentence() throws Exception {
        ArrayList<Tuple> children = kb.getNextChildren(Parser.parseSentence("~B(x,y) | L(x,y)"), kb.sentences);

        assertThat(children.size(), is(2));
        assertThat(children.contains(new Tuple(Parser.parseSentence("~B(x,y) | L(x,y)"), Parser.parseSentence("B(John,Joe)"), Parser.parseLiteral("~B(x,y)"))), is(true));
        assertThat(children.contains(new Tuple(Parser.parseSentence("~B(x,y) | L(x,y)"), Parser.parseSentence("B(John,Alice)"), Parser.parseLiteral("~B(x,y)"))), is(true));
    }

    @Test
    public void shouldReturnChildrenTuplesInDecreasingOrderOfScores() throws Exception {
        ArrayList<Tuple> children = kb.getNextChildren(Parser.parseSentence("H(John) | D(Sid,Chill) | Q(y)"), kb.sentences);

        assertThat(children.size(), is(5));
        List<Literal> literals = children.stream().map(t -> t.literal).collect(Collectors.toList());
        assertThat(literals.stream().filter(l -> l.equals(Parser.parseLiteral("D(Sid,Chill)"))).count(), is(2L));
        assertThat(literals.stream().filter(l -> l.equals(Parser.parseLiteral("H(John)"))).count(), is(2L));
        assertThat(literals.stream().filter(l -> l.equals(Parser.parseLiteral("Q(y)"))).count(), is(1L));
    }

    @Test
    public void shouldTestAskForASimpleKB() throws Exception {
        Sentence s1 = Parser.parseSentence("~F(x,y) | P(x,y)");
        Sentence s2 = Parser.parseSentence("F(Charlie,Bill)");

        KB kb = KB.getKB(new Sentence[]{s1, s2});

        assertThat(kb.ask(Parser.parseSentence("~P(Charlie,Bill)")), is(true));
        assertThat(kb.ask(Parser.parseSentence("~P(Charlie,Bruce)")), is(false));
    }

    @Test
    public void shouldTestAskForAComplexKB() throws Exception {
        KB kb = getKB();

        assertThat(kb.ask(Parser.parseSentence("~A(Liz,Bill)")), is(true));

    }

    @Test
    public void shouldTestAskForAComplexKB2() throws Exception {
        KB kb = getKB();

        assertThat(kb.ask(Parser.parseSentence("~A(Liz,Joe)")), is(false));
    }

    private KB getKB() {
        Sentence s1 = Parser.parseSentence("~P(x,y) | ~A(y,z) | A(x,z)");
        Sentence s2 = Parser.parseSentence("~P(x,y) | A(x,y)");
        Sentence s3 = Parser.parseSentence("~M(x,y) | P(x,y)");
        Sentence s4 = Parser.parseSentence("~F(x,y) | P(x,y)");
        Sentence s5 = Parser.parseSentence("M(Liz,Carlie)");
        Sentence s6 = Parser.parseSentence("F(Carlie,Bill)");

        return KB.getKB(new Sentence[]{s1, s2, s3, s4, s5, s6});
    }

    @Test
    public void testInputFiles() throws Exception {
        for (int j = 1; j <= 10; j++) {

            String tt = Integer.toString(j);
            String file = this.getClass().getResource("additional/input" + tt + ".txt").getFile();
            BufferedReader br = new BufferedReader(new FileReader(file));

            int numQueries = Integer.parseInt(br.readLine());
            Query[] queries = new Query[numQueries];

            for (int i = 0; i < numQueries; i++) {
                queries[i] = new Query(Parser.parseLiteral(br.readLine()));
            }

            int numRules = Integer.parseInt(br.readLine());
            Sentence[] sentences = new Sentence[numRules];

            for (int i = 0; i < numRules; i++) {
                sentences[i] = Parser.parseSentence(br.readLine());
            }

            StringBuilder result = new StringBuilder();
            for (Query query : queries) {
                KB kb = KB.getKB(sentences);
                boolean res = kb.ask(query.refute());
                System.out.println(res);
                result.append(String.valueOf(res).toUpperCase());
            }

            String op = this.getClass().getResource("additional/output" + tt + ".txt").getFile();
            BufferedReader out = new BufferedReader(new FileReader(op));
            String output = out.lines().reduce(String::concat).orElse("");

            assertThat(result.toString(), is(output));

            out.close();
            br.close();
        }
    }
}