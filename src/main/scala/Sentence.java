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

    boolean isEmpty(){return literals.length == 0;}

    @Override
    public String toString() {
        return Arrays.stream(this.literals).map(Literal::toString).collect(Collectors.joining(" | "));
    }
}
