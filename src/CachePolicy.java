public class CachePolicy {
    public static final long DEFAULT_TTL_MILLIS = 30000;
    public static final int MAX_ENTRIES = 100;

    public static boolean isCacheable(String method, int statusCode){
        return "GET".equalsIgnoreCase(method) && statusCode==200;
    }
}
