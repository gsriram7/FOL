import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

class Sentence {
    private Literal[] literals;
    HashMap<String, Literal> positiveLiteral;
    HashMap<String, Literal> negativeLiteral;
    final int score;

    Sentence(Literal[] literals) {
        int score = 0;
        this.literals = new Literal[literals.length];
        positiveLiteral = new HashMap<>();
        negativeLiteral = new HashMap<>();
        for (int i = 0; i < literals.length; i++) {
            score += computeScore(literals[i].numConstants, literals[i].numVariables);

            this.literals[i] = new Literal(literals[i].name, literals[i].getTerms(), literals[i].isNegated);

            if (literals[i].isNegated)
                negativeLiteral.put(this.literals[i].name, this.literals[i]);
            else positiveLiteral.put(this.literals[i].name, this.literals[i]);
        }
        this.score = score;
    }

    private int computeScore(int constants, int variable) {
        if (variable == 0)
            return constants*10;
        else
            return 2*constants;
    }

    Literal[] getLiterals() {
        Literal[] toReturn = new Literal[literals.length];

        for (int i = 0; i < literals.length; i++)
            toReturn[i] = new Literal(literals[i].name, literals[i].getTerms(), literals[i].isNegated);

        return toReturn;
    }

    boolean unifiable(Sentence s) {
        for (Literal literal : s.getLiterals()) {
            if (unifiable(literal))
                return true;
        }
        return false;
    }

    boolean unifiable(Literal literal) {
        if (literal.isNegated && positiveLiteral.containsKey(literal.name))
            return true;
        else if (!literal.isNegated && negativeLiteral.containsKey(literal.name))
            return true;
        else return false;
    }

    ArrayList<Literal> removeLiteral(Literal lit) {
        ArrayList<Literal> newLits = new ArrayList<>();

        for (Literal literal : literals)
            if (!literal.equals(lit))
                newLits.add(new Literal(literal.name, literal.getTerms(), literal.isNegated));

        return newLits;
    }

    boolean isEmpty() {
        return literals.length == 0;
    }

    @Override
    public String toString() {
        return Arrays.stream(this.literals).map(Literal::toString).collect(Collectors.joining(" | "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sentence sentence = (Sentence) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(literals, sentence.literals)) return false;
        if (positiveLiteral != null ? !positiveLiteral.equals(sentence.positiveLiteral) : sentence.positiveLiteral != null)
            return false;
        return negativeLiteral != null ? negativeLiteral.equals(sentence.negativeLiteral) : sentence.negativeLiteral == null;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(literals);
        result = 31 * result + (positiveLiteral != null ? positiveLiteral.hashCode() : 0);
        result = 31 * result + (negativeLiteral != null ? negativeLiteral.hashCode() : 0);
        return result;
    }
}

class FailureSentence extends Sentence {

    private FailureSentence(Literal[] literals) {
        super(literals);
    }

    FailureSentence() {
        this(new Literal[]{});
    }
}