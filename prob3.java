import java.util.*;

public class Problem3 {
    static class DNSEntry {
        String ip;
        long expiryTime;

        DNSEntry(String ip, long ttlSeconds) {
            this.ip = ip;
            this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final int capacity;
    private final LinkedHashMap<String, DNSEntry> cache;
    private int hits = 0;
    private int misses = 0;

    public Problem3(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > Problem3.this.capacity;
            }
        };
    }

    public synchronized String resolve(String domain) {
        cleanupExpired();
        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);
            if (!entry.isExpired()) {
                hits++;
                return "Cache HIT -> " + entry.ip;
            } else {
                cache.remove(domain);
            }
        }
        misses++;
        String ip = queryUpstream(domain);
        cache.put(domain, new DNSEntry(ip, 5));
        return "Cache MISS -> " + ip;
    }

    private String queryUpstream(String domain) {
        return "192.168." + Math.abs(domain.hashCode() % 255) + "." + (Math.abs(domain.hashCode() / 255) % 255);
    }

    private void cleanupExpired() {
        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, DNSEntry> e = it.next();
            if (e.getValue().isExpired()) it.remove();
        }
    }

    public String getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);
        return "Hit Rate: " + String.format("%.2f", hitRate) + "%";
    }

    public static void main(String[] args) throws Exception {
        Problem3 dns = new Problem3(3);
        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));
        Thread.sleep(6000);
        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.getCacheStats());
    }
}
