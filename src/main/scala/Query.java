class Query {
    Literal literal;

    Query(Literal literal) {
        this.literal = literal;
    }

    Sentence refute() {
        Literal[] list = new Literal[]{literal.refute()};
        return new Sentence(list);
    }
}
