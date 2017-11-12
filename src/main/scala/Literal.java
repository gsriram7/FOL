public interface Literal {
    Term[] getTerm();
}

class SimpleLiteral implements Literal {
    Term term;

    public SimpleLiteral(Term term) {
        this.term = term;
    }

    @Override
    public Term[] getTerm() {
        return new Term[]{term};
    }
}

class ComplexLiteral implements Literal {
    Term[] terms;

    public ComplexLiteral(Term[] terms) {
        this.terms = terms;
    }

    @Override
    public Term[] getTerm() {
        return terms;
    }
}