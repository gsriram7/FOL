import java.util.ArrayList;

class Sentence {
    ArrayList<Literal> literals;

    public Sentence(ArrayList<Literal> literals) {
        this.literals = literals;
    }

    public ArrayList<Literal> getLiterals() {
        return literals;
    }
}
