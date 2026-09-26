public class CacheTest {
    public static void main(String[] args) throws InterruptedException{
        Cache cache = new Cache();
        String key = Cache.keyFor("GET", "http://example.com/page.html");

        System.out.println("First lookup:");
        System.out.println(cache.lookup(key)==null?"CACHE MISS": "CACHE HIT");
        if(CachePolicy.isCacheable("GET", 200)){
            cache.store(key, "Hello from example.com".getBytes(), 2000);
        }
        System.out.println("Second lookup:");
        System.out.println(cache.lookup(key)==null?"CACHE MISS": "CACHE HIT");

        System.out.println("Sleeping past TTL");
        Thread.sleep(2500);

        System.out.println("Third lookup, after TTL expiry (expect MISS):");
        System.out.println(cache.lookup(key) == null ? "CACHE MISS" : "CACHE HIT");
    }
}
