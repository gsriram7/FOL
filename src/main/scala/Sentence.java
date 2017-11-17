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
            if (literal.isNegated && positiveLiteral.containsKey(literal.name))
                return true;
            else if (!literal.isNegated && negativeLiteral.containsKey(literal.name))
                return true;
        }

        return false;
    }

//    Sentence unify(Sentence sentence) {
//        if (!unifiable(sentence))
//            return new Sentence(new Literal[]{});
//
//
//    }

    boolean isEmpty(){return literals.length == 0;}

    @Override
    public String toString() {
        String literals = Arrays.stream(this.literals).map(Literal::toString).collect(Collectors.joining(" | "));

        return literals;
    }
}
