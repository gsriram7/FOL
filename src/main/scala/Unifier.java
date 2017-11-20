import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

class Unifier {

    static boolean containsDifferentConstants(Term a, Term b) {
        return (a.type == Type.CONSTANT && b.type == Type.CONSTANT && !a.equals(b));
    }

    static HashMap<Term, Term> findSubstitutionFor(Literal a, Literal b) {
        HashMap<Term, Term> subs = new HashMap<>();

        Term[] aTerms = a.getTerms();
        Term[] bTerms = b.getTerms();

        for (int i = 0; i < aTerms.length; i++) {
            if (containsDifferentConstants(aTerms[i], bTerms[i]))
                return new HashMap<>();
            if (aTerms[i].type == Type.CONSTANT && bTerms[i].type == Type.VARIABLE)
                subs.put(bTerms[i], aTerms[i]);
            else if(bTerms[i].type == Type.CONSTANT && aTerms[i].type == Type.VARIABLE)
                subs.put(aTerms[i], bTerms[i]);
            else
                subs.put(bTerms[i], aTerms[i]);
        }

        return subs;
    }

    static boolean canSubstitute(HashMap<Term, Term> subs, Literal a, Literal b) {
        Term[] aTerms = a.getTerms();
        Term[] bTerms = b.getTerms();

        boolean canSubstitute = false;

        for (int i = 0; i < aTerms.length; i++) {
            Term aT = aTerms[i];
            Term bT = bTerms[i];

            if (containsDifferentConstants(aT, bT)) canSubstitute = false;

            if (aT.equals(bT))
                canSubstitute = true;

            else {
                if (aT.type == Type.VARIABLE && bT.type == Type.CONSTANT) {
                    canSubstitute = bT.equals(subs.getOrDefault(aT, aT));
                }
                else if (bT.type == Type.VARIABLE && aT.type == Type.CONSTANT) {
                    canSubstitute = aT.equals(subs.getOrDefault(bT, bT));
                }
                else if (aT.type == Type.VARIABLE && bT.type == Type.VARIABLE)
                    canSubstitute = aT.equals(subs.getOrDefault(bT, bT));
            }

            if (!canSubstitute)
                break;
        }

        return canSubstitute;
    }

    static boolean applySubstitution(HashMap<Term, Term> subs, Literal a, Literal b) {

        if (!canSubstitute(subs, a, b))
            return false;

        Term[] aTerms = a.getTerms();
        Term[] bTerms = b.getTerms();

        for (int i = 0; i < aTerms.length; i++) {
            Term aCurr = aTerms[i];
            Term bCurr = bTerms[i];

            if (aCurr != bCurr) {
                if (bCurr.type == Type.VARIABLE) {
                    Term toSubstitute = subs.get(bCurr);
                    bTerms[i] = toSubstitute;
                }
                else {
                    Term toSubstitute = subs.get(aCurr);
                    aTerms[i] = toSubstitute;
                }
            }
        }

        return Arrays.equals(aTerms, bTerms);
    }

    static Sentence unifySentence(Sentence a, Sentence b, Literal aLit) {
        if (!a.unifiable(b))
            return a;
        else {
            Literal bLit = Arrays.stream(b.getLiterals()).filter(l -> l.name.equals(aLit.name) && (aLit.isNegated != l.isNegated)).collect(Collectors.toList()).get(0);
            HashMap<Term, Term> substitutions = findSubstitutionFor(aLit, bLit);

            if (!applySubstitution(substitutions, aLit, bLit))
                return new FailureSentence();

            Literal[] aLits = a.removeLiteral(aLit);
            Literal[] bLits = b.removeLiteral(aLit);

            return b;
        }
    }

}