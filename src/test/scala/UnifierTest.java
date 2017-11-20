import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UnifierTest {

    @Test
    public void shouldCheckIfTwoConstantsAreSame() throws Exception {
        assertThat(Unifier.containsDifferentConstants(new Constant("x"), new Constant("y")), is(true));
        assertThat(Unifier.containsDifferentConstants(new Constant("x"), new Constant("x")), is(false));
        assertThat(Unifier.containsDifferentConstants(new Variable("x"), new Constant("y")), is(false));
    }

    @Test
    public void shouldReturnEmptySubstitutionForTwoDifferentConstants() throws Exception {
        HashMap<Term, Term> subs = Unifier.findSubstitutionFor(Parser.parseLiteral("H(Jon)"), Parser.parseLiteral("~H(Blu)"));

        assertThat(subs.size(), is(0));
    }

    @Test
    public void shouldReturnSubstitutionListForValidLiterals() throws Exception {
        HashMap<Term, Term> subs = Unifier.findSubstitutionFor(Parser.parseLiteral("H(Jon"), Parser.parseLiteral("~H(y)"));
        HashMap<Term, Term> subs2 = Unifier.findSubstitutionFor(Parser.parseLiteral("H(x"), Parser.parseLiteral("~H(Jon)"));

        assertThat(subs.size(), is(1));
        assertThat(subs2.size(), is(1));
        assertThat(subs.get(new Variable("y")), is(new Constant("Jon")));
        assertThat(subs2.get(new Variable("x")), is(new Constant("Jon")));
    }

    @Test
    public void shouldReturnSubstitutionForComplexLiterals() throws Exception {
        HashMap<Term, Term> subs = Unifier.findSubstitutionFor(Parser.parseLiteral("Sells(West,M1,z,w)"), Parser.parseLiteral("Sells(x,y,Nono,u)"));

        assertThat(subs.size(), is(4));
        assertThat(subs.get(new Variable("x")), is(new Constant("West")));
        assertThat(subs.get(new Variable("y")), is(new Constant("M1")));
        assertThat(subs.get(new Variable("z")), is(new Constant("Nono")));
        assertThat(subs.get(new Variable("u")), is(new Variable("w")));
    }

    @Test
    public void shouldSayWhetherTwoLiteralsCanBeSubstituted() throws Exception {
        Literal a = Parser.parseLiteral("H(Jon");
        Literal b = Parser.parseLiteral("~H(y)");
        HashMap<Term, Term> subs = Unifier.findSubstitutionFor(a, b);

        assertThat(Unifier.canSubstitute(subs, a, b), is(true));
        assertThat(Unifier.canSubstitute(subs, Parser.parseLiteral("H(Jon)"), Parser.parseLiteral("~H(Bla)")), is(false));
        assertThat(Unifier.canSubstitute(subs, Parser.parseLiteral("H(y)"), Parser.parseLiteral("~H(Jon)")), is(true));
        assertThat(Unifier.canSubstitute(subs, Parser.parseLiteral("H(x)"), Parser.parseLiteral("~H(Jon)")), is(false));
        assertThat(Unifier.canSubstitute(subs, Parser.parseLiteral("H(Jon)"), Parser.parseLiteral("~H(x)")), is(false));
    }

    @Test
    public void shouldSayTrueToSubstituteTwoVariableTerms() throws Exception {
        Literal a = Parser.parseLiteral("H(x");
        Literal b = Parser.parseLiteral("~H(y)");
        HashMap<Term, Term> subs = Unifier.findSubstitutionFor(a, b);

        assertThat(Unifier.canSubstitute(subs, a, b), is(true));
    }

    @Test
    public void shouldSayTrueToSubstituteTwoComplexVariableTerms() throws Exception {
        Literal a = Parser.parseLiteral("D(Jon,y)");
        Literal b = Parser.parseLiteral("~D(x,Blu)");
        HashMap<Term, Term> subs = Unifier.findSubstitutionFor(a, b);

        assertThat(Unifier.canSubstitute(subs, a, b), is(true));
    }

    @Test
    public void shouldApplySubstitutionAndCheckEquality() throws Exception {
        Literal a = Parser.parseLiteral("D(Jon,y)");
        Literal b = Parser.parseLiteral("~D(x,Blu)");
        HashMap<Term, Term> subs = Unifier.findSubstitutionFor(a, b);

        assertThat(Unifier.applySubstitution(subs, a, b), is(true));
    }

    @Test
    public void shouldNotApplySubstitutionIfCant() throws Exception {
        Literal a = Parser.parseLiteral("D(Jon,y)");
        Literal b = Parser.parseLiteral("~D(Blu,z)");
        HashMap<Term, Term> subs = Unifier.findSubstitutionFor(a, b);

        assertThat(Unifier.applySubstitution(subs, a, b), is(false));
    }
}