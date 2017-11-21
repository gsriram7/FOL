import java.util.Comparator;

class Utils {

    static Comparator<Tuple> getComparatorForTuples() {
        return (o1, o2) -> {
            if (o1.equals(o2))
                return 0;
            else
                return Integer.compare(o2.left.score, o1.left.score);
        };
    }

}
