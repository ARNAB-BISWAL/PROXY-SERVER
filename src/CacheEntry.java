
public class CacheEntry{
    final byte[] response;
    final long cachedAt;
    final long ttlMillis;

    public CacheEntry(byte[] response, long ttlMillis){
        this.response = response;
        this.cachedAt = System.currentTimeMillis();
        this.ttlMillis = ttlMillis;
    }
    public boolean isExpired(){
        return System.currentTimeMillis()-cachedAt > ttlMillis;
    }
}