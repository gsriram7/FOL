import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Parser {

    static Term parseTerm(String term) {
        if (Character.isLowerCase(term.charAt(0)))
            return new Variable(term);
        else
            return new Constant(term);
    }

    static Literal parseLiteral(String literal) {
        String cleansed = literal.trim();
        boolean isNegated = false;

        if (cleansed.charAt(0) == '~') {
            isNegated = true;
            cleansed = cleansed.substring(1);
        }

        String[] parts = cleansed.split("\\(");
        String name = parts[0];
        String[] terms = parts[1].replace(")", "").split(",");

        List<Term> temp = Arrays.stream(terms).map(Parser::parseTerm).collect(Collectors.toList());
        Term[] parsedTerms = temp.toArray(new Term[temp.size()]);

        return new Literal(name, parsedTerms, isNegated);
    }

    static Sentence parseSentence(String sentence) {
        String[] lits = sentence.split("\\|");

        List<Literal> tempLits = Arrays.stream(lits).map(Parser::parseLiteral).collect(Collectors.toList());

        return new Sentence(tempLits.toArray(new Literal[tempLits.size()]));
    }

}
