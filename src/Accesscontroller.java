import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AccessController {
    private final Set<String> blockedHosts = ConcurrentHashMap.newKeySet();
    private final Set<String> blockedPaths = ConcurrentHashMap.newKeySet();

    public AccessController() {
        blockedPaths.add("/admin");
        blockedPaths.add("/private");
    }

    public boolean isAllowed(String host, String path) {
        if (host == null || path == null) {
            return false;
        }

        String cleanHost = host.toLowerCase();
        int colon = cleanHost.indexOf(':');
        if (colon > 0) {
            cleanHost = cleanHost.substring(0, colon);
        }

        for (String blocked : blockedHosts) {
            if (cleanHost.equals(blocked) || cleanHost.endsWith("." + blocked)) {
                return false;
            }
        }

        for (String blocked : blockedPaths) {
            if (path.equals(blocked) || path.startsWith(blocked + "/")) {
                return false;
            }
        }

        return true;
    }

    public void blockHost(String host) {
        if (host != null && !host.isBlank()) {
            blockedHosts.add(host.toLowerCase());
        }
    }

    public void unblockHost(String host) {
        if (host != null) {
            blockedHosts.remove(host.toLowerCase());
        }
    }

    public void blockPath(String path) {
        if (path != null && !path.isBlank()) {
            blockedPaths.add(path);
        }
    }

    public void unblockPath(String path) {
        if (path != null) {
            blockedPaths.remove(path);
        }
    }
}
