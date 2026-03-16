import java.util.*;

public class Problem6 {
    static class TokenBucket {
        int tokens;
        int maxTokens;
        long lastRefillTime;
        long refillIntervalMillis;

        TokenBucket(int maxTokens, long refillIntervalMillis) {
            this.tokens = maxTokens;
            this.maxTokens = maxTokens;
            this.lastRefillTime = System.currentTimeMillis();
            this.refillIntervalMillis = refillIntervalMillis;
        }

        void refill() {
            long now = System.currentTimeMillis();
            if (now - lastRefillTime >= refillIntervalMillis) {
                tokens = maxTokens;
                lastRefillTime = now;
            }
        }

        boolean allowRequest() {
            refill();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }
    }

    private final Map<String, TokenBucket> clients = new HashMap<>();
    private final int limit;
    private final long intervalMillis;

    public Problem6(int limit, long intervalMillis) {
        this.limit = limit;
        this.intervalMillis = intervalMillis;
    }

    public synchronized String checkRateLimit(String clientId) {
        clients.putIfAbsent(clientId, new TokenBucket(limit, intervalMillis));
        TokenBucket bucket = clients.get(clientId);
        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.tokens + " requests remaining)";
        }
        long retryAfter = (bucket.lastRefillTime + intervalMillis - System.currentTimeMillis()) / 1000;
        return "Denied (0 requests remaining, retry after " + Math.max(retryAfter, 0) + "s)";
    }

    public static void main(String[] args) {
        Problem6 limiter = new Problem6(3, 5000);
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
    }
}