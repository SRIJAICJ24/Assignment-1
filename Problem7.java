import java.util.*;

public class Problem7 {
    private final Map<String, Integer> frequency = new HashMap<>();

    public void addQuery(String query) {
        frequency.put(query, frequency.getOrDefault(query, 0) + 1);
    }

    public List<String> search(String prefix) {
        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> {
                    if (!a.getValue().equals(b.getValue())) return a.getValue() - b.getValue();
                    return b.getKey().compareTo(a.getKey());
                });

        for (Map.Entry<String, Integer> e : frequency.entrySet()) {
            if (e.getKey().startsWith(prefix)) {
                pq.offer(e);
                if (pq.size() > 10) pq.poll();
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) result.add(pq.poll().getKey() + " (" + frequency.get(result.isEmpty() ? pq.peek() == null ? "" : "" : "") + ")");
        result.clear();

        List<Map.Entry<String, Integer>> temp = new ArrayList<>();
        for (Map.Entry<String, Integer> e : frequency.entrySet()) {
            if (e.getKey().startsWith(prefix)) temp.add(e);
        }
        temp.sort((a, b) -> {
            if (!b.getValue().equals(a.getValue())) return b.getValue() - a.getValue();
            return a.getKey().compareTo(b.getKey());
        });

        for (int i = 0; i < Math.min(10, temp.size()); i++) {
            result.add(temp.get(i).getKey() + " (" + temp.get(i).getValue() + ")");
        }
        return result;
    }

    public static void main(String[] args) {
        Problem7 system = new Problem7();
        system.addQuery("java tutorial");
        system.addQuery("javascript");
        system.addQuery("java tutorial");
        system.addQuery("java download");
        system.addQuery("java 21 features");
        System.out.println(system.search("jav"));
    }
}