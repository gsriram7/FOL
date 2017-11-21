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

    HashMap<Literal, ArrayList<Sentence>> getUnifiableSentences(Sentence s, ArrayList<Sentence> sents) {
        HashMap<Literal, ArrayList<Sentence>> litToSent = new HashMap<>();
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
        HashMap<Literal, ArrayList<Sentence>> sents = getUnifiableSentences(s, sen);
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

    boolean ask(ArrayList<Sentence> sentences, HashSet<Sentence> set, Sentence a, int depth) {

        if (depth <= 0)
            return false;

        if (!(a instanceof FailureSentence) && a.isEmpty())
            return true;

        if (a instanceof FailureSentence)
            return false;

        if (!set.contains(a)){
            sentences.add(a);
            set.add(a);
        }

        ArrayList<Tuple> children = getNextChildren(a, sentences);

        for (Tuple child : children) {
            Sentence unified = Unifier.unify(child);
            boolean isPossible = ask(sentences, set, unified, depth - 1);
            if (isPossible) {
                System.out.println("["+child.left +"]  ::  ["+child.right+"]\t-->\t"+unified+"");
                return true;
            }
        }

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

        return ask(newSent, set, s, 10);
    }
}
