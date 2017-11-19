import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class KBTest {

    KB kb;

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
        HashMap<Literal, ArrayList<Sentence>> map = kb.getUnifiableSentences(sentence);

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
    public void shouldReturnNoUnifiableSentenceForANonMatchingSentence() throws Exception {
        Sentence sentence = Parser.parseSentence("P(John) | L(x,y)");
        HashMap<Literal, ArrayList<Sentence>> map = kb.getUnifiableSentences(sentence);

        System.out.println(map.toString());
        assertThat(map.size(), is(0));
        assertThat(map.containsKey(Parser.parseLiteral("P(John)")), is(false));
        assertThat(map.containsKey(Parser.parseLiteral("L(x,y)")), is(false));
    }
}