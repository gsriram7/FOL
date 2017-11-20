import java.util.Arrays;
import java.util.stream.Collectors;

class Literal implements Comparable<Literal> {
    String name;
    private Term[] terms;
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

    public Literal(Literal literal) {
        this(literal.name, literal.getTerms(), literal.isNegated);
    }

    Term[] getTerms() {
        Term[] termsToReturn = new Term[terms.length];
        for (int i = 0; i < terms.length; i++)
            termsToReturn[i] = terms[i].getTerm();
        return termsToReturn;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Literal literal = (Literal) o;

        if (isNegated != literal.isNegated) return false;
        if (numTerms != literal.numTerms) return false;
        if (numConstants != literal.numConstants) return false;
        if (numVariables != literal.numVariables) return false;
        if (!name.equals(literal.name)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(terms, literal.terms);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + Arrays.hashCode(terms);
        result = 31 * result + (isNegated ? 1 : 0);
        result = 31 * result + numTerms;
        result = 31 * result + numConstants;
        result = 31 * result + numVariables;
        return result;
    }

    @Override
    public int compareTo(Literal o) {
        return Integer.compare(o.numConstants, numConstants);
    }
}