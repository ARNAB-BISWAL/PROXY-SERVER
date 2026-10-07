import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class ProxyLogger {
    private static final String FILE_NAME = "proxy.log";

    public static synchronized void request(String clientIp, String method,
                                             String url, boolean allowed,
                                             int statusCode, long durationMs) {
        String message = LocalDateTime.now() +
                " | client=" + clientIp +
                " | method=" + method +
                " | url=" + url +
                " | allowed=" + allowed +
                " | status=" + statusCode +
                " | timeMs=" + durationMs;
        write(message);
    }

    public static synchronized void error(String clientIp, String message) {
        write(LocalDateTime.now() + " | client=" + clientIp + " | ERROR=" + message);
    }

    private static void write(String message) {
        System.out.println(message);
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            out.println(message);
        } catch (IOException e) {
            System.out.println("Could not write log: " + e.getMessage());
        }
    }
}
