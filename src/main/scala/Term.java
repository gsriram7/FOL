
abstract class Term {
    private String name;
    protected Enum<Type> type;

    Term(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    String getName() {
        return name;
    }
}

class Constant extends Term {

    Constant(String name) {
        super(name, Type.CONSTANT);
    }

}

class Variable extends Term {

    Variable(String name) {
        super(name, Type.VARIABLE);
    }
}