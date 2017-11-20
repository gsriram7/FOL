import java.util.*;

public class KB {

    Sentence[] original;
    ArrayList<Sentence> sentences;
    HashSet<Sentence> pool;

    KB(Sentence[] original, ArrayList<Sentence> sentences) {
        this.original = original;
        this.sentences = sentences;
        pool = new HashSet<>(sentences);
    }

    TreeMap<Literal, ArrayList<Sentence>> getUnifiableSentences(Sentence s, ArrayList<Sentence> sents) {
        TreeMap<Literal, ArrayList<Sentence>> litToSent = new TreeMap<>();
        Literal[] literals = s.getLiterals();

        for (Literal literal : literals) {
            for (Sentence sent : sents) {
                if (sent.unifiable(literal)) {
                    ArrayList<Sentence> v = litToSent.getOrDefault(literal, new ArrayList<>());
                    v.add(sent);
                    litToSent.put(literal, v);
                }
            }
        }

        return litToSent;
    }

    ArrayList<Tuple> getNextChildren(Sentence s, ArrayList<Sentence> sen) {
        TreeMap<Literal, ArrayList<Sentence>> sents = getUnifiableSentences(s, sen);
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

    boolean ask(ArrayList<Sentence> sentences, HashSet<Sentence> set, Sentence a) {

        if (!(a instanceof FailureSentence) && a.isEmpty())
            return true;

        if (a instanceof FailureSentence)
            return false;

        if (set.contains(a))
            return true;
        else {
            sentences.add(a);
            set.add(a);
        }

        ArrayList<Tuple> children = getNextChildren(a, sentences);

        for (Tuple child : children) {
            Sentence unified = Unifier.unify(child);
            boolean isPossible = ask(sentences, set, unified);
            if (isPossible)
                return true;
        }

        return false;
    }

    boolean infer(Query query) {

        return false;
    }

    static KB getKB(Sentence[] s) {
        ArrayList<Sentence> list = new ArrayList<>();
        Sentence[] arr = new Sentence[s.length];

        for (int i = 0; i < s.length; i++) {
            arr[i] = new Sentence(s[i].getLiterals());
            list.add(new Sentence(s[i].getLiterals()));
        }

        return new KB(arr, list);
    }

    boolean ask(Sentence s) {
        ArrayList<Sentence> newSent = new ArrayList<>();
        newSent.addAll(sentences);
        HashSet<Sentence> set = new HashSet<>(pool);

        return ask(newSent, set, s);
    }
}
