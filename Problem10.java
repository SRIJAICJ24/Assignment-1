import java.util.*;

public class Problem10 {
    static class VideoData {
        String videoId;
        String content;

        VideoData(String videoId, String content) {
            this.videoId = videoId;
            this.content = content;
        }

        public String toString() {
            return content;
        }
    }

    private final int l1Capacity;
    private final int l2Capacity;

    private final LinkedHashMap<String, VideoData> l1;
    private final LinkedHashMap<String, VideoData> l2;
    private final Map<String, VideoData> l3 = new HashMap<>();

    private int l1Hits = 0, l2Hits = 0, l3Hits = 0;

    public Problem10(int l1Capacity, int l2Capacity) {
        this.l1Capacity = l1Capacity;
        this.l2Capacity = l2Capacity;

        l1 = new LinkedHashMap<>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > Problem10.this.l1Capacity;
            }
        };

        l2 = new LinkedHashMap<>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > Problem10.this.l2Capacity;
            }
        };
    }

    public void addToDatabase(String videoId, String content) {
        l3.put(videoId, new VideoData(videoId, content));
    }

    public String getVideo(String videoId) {
        if (l1.containsKey(videoId)) {
            l1Hits++;
            return "L1 Cache HIT -> " + l1.get(videoId);
        }

        if (l2.containsKey(videoId)) {
            l2Hits++;
            VideoData data = l2.get(videoId);
            l1.put(videoId, data);
            return "L2 Cache HIT -> Promoted to L1 -> " + data;
        }

        if (l3.containsKey(videoId)) {
            l3Hits++;
            VideoData data = l3.get(videoId);
            l2.put(videoId, data);
            return "L3 Database HIT -> Added to L2 -> " + data;
        }

        return "Video not found";
    }

    public String getStatistics() {
        int total = l1Hits + l2Hits + l3Hits;
        if (total == 0) return "No requests yet";
        return "L1: " + l1Hits + ", L2: " + l2Hits + ", L3: " + l3Hits +
                ", Overall Hit Count: " + total;
    }

    public static void main(String[] args) {
        Problem10 cache = new Problem10(2, 3);
        cache.addToDatabase("video_123", "Movie A");
        cache.addToDatabase("video_999", "Movie B");

        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_999"));
        System.out.println(cache.getStatistics());
    }
}