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
        this.terms = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            terms[i] = terms[i];
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
}