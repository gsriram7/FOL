import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void shouldParseTerms() throws Exception {
        assertThat(Parser.parseTerm("x"), is(new Variable("x")));
        assertThat(Parser.parseTerm("xyz"), is(new Variable("xyz")));
        assertThat(Parser.parseTerm("West"), is(new Constant("West")));
        assertThat(Parser.parseTerm("Billy"), is(new Constant("Billy")));
    }

    @Test
    public void shouldParseSimpleLiterals() throws Exception {
        Literal literal = Parser.parseLiteral("~American(x)");

        assertThat(literal.getTerms().length, is(1));
        assertThat(literal.name, is("American"));
        assertThat(literal.isNegated, is(true));
        assertThat(literal.getTerms(), is(new Term[]{new Variable("x")}));
    }

    @Test
    public void shouldParseComplexLiterals() throws Exception {
        Literal literal = Parser.parseLiteral("Sells(West,x,Nono)");

        assertThat(literal.getTerms().length, is(3));
        assertThat(literal.name, is("Sells"));
        assertThat(literal.isNegated, is(false));
        assertThat(literal.getTerms(), is(new Term[]{new Constant("West"), new Variable("x"), new Constant("Nono")}));
    }

    @Test
    public void shouldParseLiteralsWithSpace() throws Exception {
        Literal literal = Parser.parseLiteral(" Sells(West,x,Nono) ");

        assertThat(literal.getTerms().length, is(3));
        assertThat(literal.name, is("Sells"));
        assertThat(literal.isNegated, is(false));
        assertThat(literal.getTerms(), is(new Term[]{new Constant("West"), new Variable("x"), new Constant("Nono")}));
    }

    @Test
    public void shouldParseSentences() throws Exception {
        Sentence sentence = Parser.parseSentence("~American(x) | ~Weapon(y) | ~Sells(x,y,z) | ~Enemy(z,America) | Criminal(x)");

        Literal[] literals = sentence.getLiterals();
        assertThat(literals.length, is(5));
        assertThat(Arrays.stream(literals).filter(l -> l.isNegated).count(), is(4L));
        assertThat(checkLiteralEquality(literals[0], new Literal("American", new Term[]{new Variable("x")}, true)), is(true));
        assertThat(checkLiteralEquality(literals[2], new Literal("Sells", new Term[]{new Variable("x"), new Variable("y"), new Variable("z")}, true)), is(true));
        assertThat(checkLiteralEquality(literals[3], new Literal("Enemy", new Term[]{new Variable("z"), new Constant("America")}, true)), is(true));
    }

    @Test
    public void shouldTestInputFileParsing() throws Exception {
        for (int i = 1; i <= 3; i++) {
            File file = new File(this.getClass().getResource(String.format("ip%d.txt", i)).getFile());

            BufferedReader br = new BufferedReader(new FileReader(file));

            assertTrue(br.lines().map(s -> Parser.parseSentence(s).toString().equals(s)).reduce(Boolean::logicalAnd).get());

            br.close();
        }
    }

    boolean checkLiteralEquality(Literal a, Literal b) {
        return a.name.equals(b.name) && Arrays.equals(a.getTerms(), b.getTerms()) && a.isNegated == b.isNegated;
    }
}