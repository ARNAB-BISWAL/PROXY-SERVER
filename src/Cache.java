import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private final Map<String, CacheEntry> store = new ConcurrentHashMap<>();
    private final LinkedHashMap<String, Boolean> lruOrder = new LinkedHashMap<String, Boolean>(16, 0.75f, true){
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Boolean> eldest){
            if(size()>CachePolicy.MAX_ENTRIES){
                store.remove(eldest.getKey());
                return true;
            }
            return false;
        }
    };
    public static String keyFor(String method, String url){
        return method + ""+ url;
    }
    public CacheEntry lookup(String key){
        CacheEntry entry = store.get(key);
        if(entry==null){
            return null;
        }
        if(entry.isExpired()){
            store.remove(key);
            synchronized (lruOrder){
                lruOrder.remove(key);
            }
            return null;
        }
        synchronized (lruOrder){
            lruOrder.get(key);
        }
        return entry;
    }
    public void store(String key, byte[] response){
        store(key, response, CachePolicy.DEFAULT_TTL_MILLIS);
    }
    public void store(String key, byte[] response, long ttlMillis) {
        store.put(key, new CacheEntry(response, ttlMillis));
        synchronized (lruOrder) {
            lruOrder.put(key, Boolean.TRUE);
        }
    }
}
