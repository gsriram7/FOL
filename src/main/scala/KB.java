import java.util.*;

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

    ArrayList<Tuple> getNextChildren(Sentence s) {
        TreeMap<Literal, ArrayList<Sentence>> sents = getUnifiableSentences(s);
        HashSet<Tuple> addedTuples = new HashSet<>();
        ArrayList<Tuple> tuples = new ArrayList<>();

        for (Map.Entry<Literal, ArrayList<Sentence>> entry : sents.entrySet()) {
            ArrayList<Sentence> ss = entry.getValue();
            for (Sentence sentence : ss) {
                Tuple tuple = new Tuple(s, sentence, entry.getKey());
                if (!addedTuples.contains(tuple)) {
                    addedTuples.add(tuple);
                    tuples.add(tuple);
                }
            }
        }

        tuples.sort(Utils.getComparatorForTuples());

        return tuples;
    }

    boolean infer(Query query) {

        return false;
    }
}
