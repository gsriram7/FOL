import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

class Sentence {
    Literal[] literals;
    HashMap<String, Literal> positiveLiteral;
    HashMap<String, Literal> negativeLiteral;

    Sentence(Literal[] literals) {
        this.literals = new Literal[literals.length];
        positiveLiteral = new HashMap<>();
        negativeLiteral = new HashMap<>();
        for (int i = 0; i < literals.length; i++) {
            this.literals[i] = literals[i];
            if (literals[i].isNegated)
                negativeLiteral.put(literals[i].name, literals[i]);
            else positiveLiteral.put(literals[i].name, literals[i]);
        }
    }

    Literal[] getLiterals() {
        return literals;
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

    Literal[] removeLiteral(Literal lit) {
        Literal[] newLiterals = new Literal[this.literals.length - 1];

        int j = 0;
        for (int i = 0; i < literals.length; i++) {
            if (!literals[i].equals(lit)) {
                newLiterals[j] = literals[i];
                j++;
            }
        }

        return newLiterals;
    }

    boolean isEmpty(){return literals.length == 0;}

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

    FailureSentence(Literal[] literals) {
        super(literals);
    }

    FailureSentence() {
        this(new Literal[]{});
    }
}