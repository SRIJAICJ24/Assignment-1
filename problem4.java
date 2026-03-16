import java.util.*;

public class Problem4 {
    private final Map<String, Set<String>> ngramIndex = new HashMap<>();
    private final Map<String, Set<String>> documentNgrams = new HashMap<>();
    private final int n;

    public Problem4(int n) {
        this.n = n;
    }

    private Set<String> extractNgrams(String text) {
        String[] words = text.toLowerCase().replaceAll("[^a-z0-9 ]", "").split("\\s+");
        Set<String> ngrams = new HashSet<>();
        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if (j > 0) sb.append(" ");
                sb.append(words[i + j]);
            }
            ngrams.add(sb.toString());
        }
        return ngrams;
    }

    public void addDocument(String docId, String text) {
        Set<String> ngrams = extractNgrams(text);
        documentNgrams.put(docId, ngrams);
        for (String gram : ngrams) {
            ngramIndex.computeIfAbsent(gram, k -> new HashSet<>()).add(docId);
        }
    }

    public Map<String, Double> analyzeDocument(String docId) {
        Map<String, Integer> matchCounts = new HashMap<>();
        Set<String> current = documentNgrams.getOrDefault(docId, Collections.emptySet());

        for (String gram : current) {
            for (String otherDoc : ngramIndex.getOrDefault(gram, Collections.emptySet())) {
                if (!otherDoc.equals(docId)) {
                    matchCounts.put(otherDoc, matchCounts.getOrDefault(otherDoc, 0) + 1);
                }
            }
        }

        Map<String, Double> similarity = new HashMap<>();
        for (Map.Entry<String, Integer> e : matchCounts.entrySet()) {
            int total = current.size();
            similarity.put(e.getKey(), total == 0 ? 0.0 : e.getValue() * 100.0 / total);
        }
        return similarity;
    }

    public static void main(String[] args) {
        Problem4 detector = new Problem4(3);
        detector.addDocument("essay_1", "java is a programming language and java is widely used");
        detector.addDocument("essay_2", "java is a programming language used in many systems");
        detector.addDocument("essay_3", "plants need water and sunlight to grow well");
        System.out.println(detector.analyzeDocument("essay_1"));
    }
}
