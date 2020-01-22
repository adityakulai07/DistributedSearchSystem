package model;

import java.util.HashMap;
import java.util.Map;

public class DocumentData {

    private Map<String, Double> termToFrequencyMap = new HashMap<>();

    public void putTermFrequency(String term, double frequency) {
        termToFrequencyMap.put(term, frequency);
    }

    public double getFrequency(String term) {
        return termToFrequencyMap.get(term);
    }
}
