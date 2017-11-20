
abstract class Term {
    protected String name;
    protected Enum<Type> type;

    Term(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    abstract Term getTerm();
    String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Term term = (Term) o;

        if (!name.equals(term.name)) return false;
        return type.equals(term.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}

class Constant extends Term {

    Constant(String name) {
        super(name, Type.CONSTANT);
    }

    @Override
    Term getTerm() {
        return new Constant(name);
    }
}

class Variable extends Term {

    Variable(String name) {
        super(name, Type.VARIABLE);
    }

    @Override
    Term getTerm() {
        return new Variable(name);
    }
}