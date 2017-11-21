import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class homework {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("/Users/selvaram/selva/FOL/src/main/scala/input.txt"));

        int numQueries = Integer.parseInt(br.readLine());
        Query[] queries = new Query[numQueries];

        for (int i = 0; i < numQueries; i++) {
            queries[i] = new Query(Parser.parseLiteral(br.readLine()));
        }

        int numRules = Integer.parseInt(br.readLine());
        Sentence[] sentences = new Sentence[numRules];

        for (int i = 0; i < numRules; i++) {
            sentences[i] = Parser.parseSentence(br.readLine());
        }

        br.close();


        BufferedWriter out = new BufferedWriter(new FileWriter("/Users/selvaram/selva/FOL/src/main/scala/output.txt"));

        for (Query query : queries) {
            StringBuilder result = new StringBuilder();
            KB kb = KB.getKB(sentences);
            boolean res = kb.ask(query.refute());
            System.out.println();
            out.write(result.append(String.valueOf(res).toUpperCase()).append("\n").toString());
        }

        out.close();

    }
}
