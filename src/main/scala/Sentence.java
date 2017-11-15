import java.util.Arrays;
import java.util.stream.Collectors;

class Sentence {
    Literal[] literals;

    Sentence(Literal[] literals) {
        this.literals = literals;
    }

    Literal[] getLiterals() {
        return literals;
    }

    @Override
    public String toString() {
        String literals = Arrays.stream(this.literals).map(Literal::toString).collect(Collectors.joining(" | "));

        return literals;
    }
}
