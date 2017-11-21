import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class homework {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));

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


        BufferedWriter out = new BufferedWriter(new FileWriter("output.txt"));

        StringBuilder result = new StringBuilder();
        for (Query query : queries) {
            KB kb = KB.getKB(sentences);
            boolean res = kb.ask(query.refute());
            out.write(result.append(String.valueOf(res).toUpperCase()).append("\n").toString());
        }

        out.close();

    }
}
