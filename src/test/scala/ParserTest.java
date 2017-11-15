import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

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
        assertThat(checkLiteralEquality(literals[0], new Literal("American", new Term[] {new Variable("x")}, true)), is(true));
        assertThat(checkLiteralEquality(literals[2], new Literal("Sells", new Term[] {new Variable("x"), new Variable("y"), new Variable("z")}, true)), is(true));
        assertThat(checkLiteralEquality(literals[3], new Literal("Enemy", new Term[] {new Variable("z"), new Constant("America")}, true)), is(true));
    }

    boolean checkLiteralEquality(Literal a, Literal b) {
        return a.name.equals(b.name) && Arrays.equals(a.getTerms(), b.getTerms()) && a.isNegated==b.isNegated;
    }
}