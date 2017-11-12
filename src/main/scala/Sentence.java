import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

interface Sentence {
    boolean isNegated();
    ArrayList<String> getName();
    Literal getTerm();
}

class SimpleSentence implements Sentence {
    boolean isNegated;
    String name;
    Literal term;

    public SimpleSentence(String name, Literal term, boolean isNegated) {
        this.name = name;
        this.term = term;
        this.isNegated = isNegated;
    }

    @Override
    public boolean isNegated() {
        return isNegated;
    }

    @Override
    public ArrayList<String> getName() {
        ArrayList<String> names = new ArrayList<>(1);
        names.add(name);
        return names;
    }

    @Override
    public Literal getTerm() {
        return term;
    }
}

class CompoundSentence {
    int numberOfTerms;
    ArrayList<Sentence> sentences;

    public CompoundSentence(ArrayList<Sentence> sentences, int numberOfTerms) {
        this.sentences = sentences;
        this.numberOfTerms = numberOfTerms;
    }

    List<Literal> getLiterals() {
        return sentences.stream().map(Sentence::getTerm).collect(Collectors.toList());
    }
}