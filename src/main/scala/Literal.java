class Literal {
    String name;
    Term[] terms;
    boolean isNegated;
    private final int numTerms;
    int numConstants = 0;
    int numVariables = 0;

    Literal(String name, Term[] terms, boolean isNegated) {
        this.name = name;
        this.terms = terms;
        this.isNegated = isNegated;
        numTerms = terms.length;
        for (Term term : terms) {
            if (term instanceof Constant)
                numConstants++;
            else
                numVariables++;
        }
    }

    Term[] getTerms() {
        return terms;
    }

    boolean canUnify() {
        return numVariables == 0;
    }
}