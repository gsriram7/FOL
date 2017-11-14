import java.util.ArrayList;

public class Query {
    Literal literal;

    public Query(Literal literal) {
        this.literal = literal;
    }

    public Sentence refute() {
        ArrayList<Literal> list = new ArrayList<>();
        list.add(literal.refute());
        return new Sentence(list);
    }
}
