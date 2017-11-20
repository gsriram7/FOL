public class Tuple {
    Sentence left;
    Sentence right;
    Literal literal;

    public Tuple(Sentence left, Sentence right, Literal literal) {
        this.left = new Sentence(left.getLiterals());
        this.right = new Sentence(right.getLiterals());
        this.literal = new Literal(literal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple tuple = (Tuple) o;

        if (left != null ? !left.equals(tuple.left) : tuple.left != null) return false;
        if (right != null ? !right.equals(tuple.right) : tuple.right != null) return false;
        return literal != null ? literal.equals(tuple.literal) : tuple.literal == null;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        result = 31 * result + (literal != null ? literal.hashCode() : 0);
        return result;
    }

}
