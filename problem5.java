import java.util.*;

public class Problem5 {
    static class Event {
        String url;
        String userId;
        String source;

        Event(String url, String userId, String source) {
            this.url = url;
            this.userId = userId;
            this.source = source;
        }
    }

    private final Map<String, Integer> pageViews = new HashMap<>();
    private final Map<String, Set<String>> uniqueVisitors = new HashMap<>();
    private final Map<String, Integer> trafficSources = new HashMap<>();

    public void processEvent(Event event) {
        pageViews.put(event.url, pageViews.getOrDefault(event.url, 0) + 1);
        uniqueVisitors.computeIfAbsent(event.url, k -> new HashSet<>()).add(event.userId);
        trafficSources.put(event.source, trafficSources.getOrDefault(event.source, 0) + 1);
    }

    public List<String> getTopPages(int k) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(pageViews.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());
        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(k, list.size()); i++) {
            String url = list.get(i).getKey();
            result.add(url + " - " + list.get(i).getValue() + " views (" +
                    uniqueVisitors.getOrDefault(url, Collections.emptySet()).size() + " unique)");
        }
        return result;
    }

    public Map<String, Integer> getTrafficSources() {
        return trafficSources;
    }

    public static void main(String[] args) {
        Problem5 dashboard = new Problem5();
        dashboard.processEvent(new Event("/article/breaking-news", "user_123", "google"));
        dashboard.processEvent(new Event("/article/breaking-news", "user_456", "facebook"));
        dashboard.processEvent(new Event("/sports/championship", "user_123", "direct"));
        System.out.println(dashboard.getTopPages(10));
        System.out.println(dashboard.getTrafficSources());
    }
}