import java.util.Comparator;

class Utils {

    static Comparator<Tuple> getComparatorForTuples() {
        return new Comparator<Tuple>() {
            @Override
            public int compare(Tuple o1, Tuple o2) {
                if (o1.equals(o2))
                    return 0;

                else
                    return Integer.compare(o2.left.score, o1.left.score);
            }

        };
    }

}
