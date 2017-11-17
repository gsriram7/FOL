import java.util.ArrayList;

public class KB {

    Sentence[] original;
    ArrayList<Sentence> sentences;

    public KB(Sentence[] original, ArrayList<Sentence> sentences) {
        this.original = original;
        this.sentences = sentences;
    }

    boolean infer(Query query) {

        return false;
    }
}
