public interface Term {
    String getName();
}

class Constant implements Term {

    private String name;

    Constant(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

class Variable implements Term {

    private String name;

    Variable(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}