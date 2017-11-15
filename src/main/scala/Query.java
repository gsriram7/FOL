public class Query {
    Literal literal;

    public Query(Literal literal) {
        this.literal = literal;
    }

    public Sentence refute() {
        Literal[] list = new Literal[]{literal.refute()};
        return new Sentence(list);
    }
}
