import java.util.ArrayList;
import java.util.TreeMap;

public class KB {

    Sentence[] original;
    ArrayList<Sentence> sentences;

    KB(Sentence[] original, ArrayList<Sentence> sentences) {
        this.original = original;
        this.sentences = sentences;
    }

    TreeMap<Literal, ArrayList<Sentence>> getUnifiableSentences(Sentence s) {
        TreeMap<Literal, ArrayList<Sentence>> litToSent = new TreeMap<>();
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
