import java.util.concurrent.atomic.AtomicLong;

public class PerformanceMetrics {
    private final AtomicLong totalRequests = new AtomicLong();
    private final AtomicLong allowedRequests = new AtomicLong();
    private final AtomicLong blockedRequests = new AtomicLong();
    private final AtomicLong successfulRequests = new AtomicLong();
    private final AtomicLong failedRequests = new AtomicLong();
    private final AtomicLong cacheHits = new AtomicLong();
    private final AtomicLong cacheMisses = new AtomicLong();
    private final AtomicLong totalResponseTime = new AtomicLong();

    public void record(boolean allowed, int statusCode, long durationMs, Boolean cacheHit) {
        totalRequests.incrementAndGet();
        totalResponseTime.addAndGet(durationMs);

        if (allowed) {
            allowedRequests.incrementAndGet();
        } else {
            blockedRequests.incrementAndGet();
        }

        if (statusCode >= 200 && statusCode < 400) {
            successfulRequests.incrementAndGet();
        } else if (statusCode != -1) {
            failedRequests.incrementAndGet();
        }

        if (cacheHit != null) {
            if (cacheHit) {
                cacheHits.incrementAndGet();
            } else {
                cacheMisses.incrementAndGet();
            }
        }
    }

    public double averageResponseTime() {
        long total = totalRequests.get();
        return total == 0 ? 0 : (double) totalResponseTime.get() / total;
    }

    public void printSummary() {
        System.out.println("\n========== PROXY PERFORMANCE ==========");
        System.out.println("Total requests   : " + totalRequests.get());
        System.out.println("Allowed requests : " + allowedRequests.get());
        System.out.println("Blocked requests : " + blockedRequests.get());
        System.out.println("Successful       : " + successfulRequests.get());
        System.out.println("Failed           : " + failedRequests.get());
        System.out.println("Cache hits       : " + cacheHits.get());
        System.out.println("Cache misses     : " + cacheMisses.get());
        System.out.printf("Average time     : %.2f ms%n", averageResponseTime());
        System.out.println("========================================\n");
    }
}
