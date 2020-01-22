package search;

import model.DocumentData;

import java.util.*;

public class TFIDF {

    public static double calculateTermFrequency(List<String> words, String term) {
        long count = 0;

        for(String word: words) {
            if(term.equalsIgnoreCase(word)) {
                count++;
            }
        }

        double termFrequency = (double)count / words.size();

        return termFrequency;
    }

    public static DocumentData createDocumentData(List<String> words, List<String> terms) {

        DocumentData documentData = new DocumentData();

        for(String term: terms) {
            double termFreq = calculateTermFrequency(words, term);
            documentData.putTermFrequency(term, termFreq);
        }

        return documentData;
    }

    private static double getInvertDocumentFrequency(String term, Map<String, DocumentData> documentResults) {
        double noOfTerms = 0;
        for(String document: documentResults.keySet()) {
            DocumentData documentData = documentResults.get(document);
            double termFrequency = documentData.getFrequency(term);
            if(termFrequency>0.0) {
                noOfTerms++;
            }
        }

        return noOfTerms == 0 ? 0 : Math.log10(documentResults.size() / noOfTerms);
    }

    public static Map<String, Double> getTermToInverseDocumentFrequencyMap(List<String> terms, Map<String, DocumentData> documentResults) {

        Map<String, Double> termToIDF = new HashMap<>();

        for(String term: terms) {
            double idf = getInvertDocumentFrequency(term, documentResults);
            termToIDF.put(term, idf);
        }

        return termToIDF;
    }

    public static double calculateDocumentScore(List<String> terms, DocumentData documentData, Map<String, Double> termToInverseDocumentFrequency) {

        double score = 0;

        for(String term: terms) {
            double termFrequency = documentData.getFrequency(term);
            double inverseTermFrequency = termToInverseDocumentFrequency.get(term);

            score = score + termFrequency + inverseTermFrequency;
        }

        return score;
    }

    public static Map<Double, List<String>> getDocumentsSortedByScore(List<String> terms, Map<String, DocumentData> documentResults) {

        TreeMap<Double, List<String>> scoreToDocuments = new TreeMap<Double, List<String>>();

        Map<String, Double> termToInverseDocumentFrequency = getTermToInverseDocumentFrequencyMap(terms, documentResults);

        for(String document: documentResults.keySet()) {

            DocumentData documentData = documentResults.get(document);

            double score = calculateDocumentScore(terms, documentData, termToInverseDocumentFrequency);

            addDocumentScoreToTreeMap(scoreToDocuments, score, document);
        }

        return scoreToDocuments.descendingMap();

    }

    public static void addDocumentScoreToTreeMap(TreeMap<Double, List<String>> scoreToDoc, double score, String document) {

        List<String> documentsWithCurrentScore = scoreToDoc.get(score);

        if(documentsWithCurrentScore == null) {
            documentsWithCurrentScore = new ArrayList<String>();

        }

        documentsWithCurrentScore.add(document);
        scoreToDoc.put(score,documentsWithCurrentScore);
    }

    public static List<String> getWordsFromLine(String line) {

        return Arrays.asList(line.split("(\\.)+|(,)+|( )+|(-)+|(\\?)+|(!)+|(;)+|(/d)+|(/n)+"));
    }

    public static List<String> getWordsFromLines(List<String> lines) {

        List<String> words = new ArrayList<String>();

        for(String line: lines) {
            words.addAll(getWordsFromLine(line));
        }

        return words;
    }
}
