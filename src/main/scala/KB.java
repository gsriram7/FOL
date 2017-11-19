import java.util.ArrayList;
import java.util.HashMap;

public class KB {

    Sentence[] original;
    ArrayList<Sentence> sentences;

    public KB(Sentence[] original, ArrayList<Sentence> sentences) {
        this.original = original;
        this.sentences = sentences;
    }

    HashMap<Literal, ArrayList<Sentence>> getUnifiableSentences(Sentence s) {
        HashMap<Literal, ArrayList<Sentence>> litToSent = new HashMap<>();
        Literal[] literals = s.getLiterals();

        for (Literal literal : literals) {
            for (Sentence sent : sentences) {
                if (sent.unifiable(literal)) {
                    ArrayList<Sentence> v = litToSent.getOrDefault(literal, new ArrayList<>());
                    v.add(sent);
                    litToSent.put(literal, v);
                }
            }
        }

        return litToSent;
    }

    boolean infer(Query query) {

        return false;
    }
}
