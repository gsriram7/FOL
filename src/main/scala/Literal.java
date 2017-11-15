import java.util.Arrays;
import java.util.stream.Collectors;

class Literal {
    String name;
    Term[] terms;
    boolean isNegated;
    private final int numTerms;
    int numConstants = 0;
    int numVariables = 0;

    Literal(String name, Term[] terms, boolean isNegated) {
        this.name = name;
        this.isNegated = isNegated;
        numTerms = terms.length;
        this.terms = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            this.terms[i] = terms[i];
            if (terms[i] instanceof Constant) numConstants++; else numVariables++;
        }
    }

    Term[] getTerms() {
        return terms;
    }

    boolean canUnify() {
        return numVariables == 0;
    }

    Literal refute() {
        return new Literal(name, terms, !isNegated);
    }

    @Override
    public String toString() {
        String prepend = isNegated ? "~" : "";
        String allTerms = Arrays.stream(terms).map(Term::toString).collect(Collectors.joining(","));

        return prepend + name + "(" + allTerms + ")";
    }
}