import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpHandler {

    private static final Cache cache = new Cache();

    public static void handle(
            BufferedReader clientIn,
            OutputStream clientOut) throws IOException {
        String firstLine = clientIn.readLine();
        if (firstLine == null || firstLine.isEmpty()) {
            return;
        }
        String[] parts = firstLine.split(" ");
        if (parts.length != 3) {
            sendError(clientOut, 400, "Bad Request");
            return;
        }

        HttpRequest request =
                new HttpRequest(parts[0], parts[1], parts[2]);
       String line;
        while ((line = clientIn.readLine()) != null
                && !line.isEmpty()) {

            int colon = line.indexOf(":");
            if (colon > 0) {
                String key =
                        line.substring(0, colon).trim();
                String value =
                        line.substring(colon + 1).trim();
                request.headers.put(key, value);
            }
        }


        System.out.println("----- HTTP REQUEST -----");
        System.out.println("Method  : " + request.method);
        System.out.println("Path    : " + request.path);
        System.out.println("Version : " + request.version);
        System.out.println("Host    : " + request.headers.get("Host"));

       if (!request.method.equalsIgnoreCase("GET")) {
            sendError(
                    clientOut,
                    501,
                    "Only GET is currently supported"
            );
          return;
        }
     String host = request.headers.get("Host");
        if (host == null || host.isEmpty()) {
           sendError(
                    clientOut,
                    400,
                    "Host header missing"
            );
           return;
        }

         int port = 80;
        if (host.contains(":")) {
            String[] hostParts = host.split(":");
            host = hostParts[0];
            try {
                port = Integer.parseInt(hostParts[1]);
            }
            catch (NumberFormatException e) {
                sendError(
                        clientOut,
                        400,
                        "Invalid port"
                );
                return;
            }
        }
        String cacheKey =
                Cache.keyFor(
                        request.method,
                        host + request.path
                );
        CacheEntry cached =
                cache.lookup(cacheKey);

        if (cached != null) {
            System.out.println(
                    "CACHE HIT: " + cacheKey
            );
            clientOut.write(cached.response);
            clientOut.flush();
            return;
        }
        System.out.println(
                "CACHE MISS: " + cacheKey
        );   
        try (Socket serverSocket =
                     new Socket(host, port)) {
           System.out.println(
                    "Connected to: " + host
            );
          OutputStream serverOut =
                    serverSocket.getOutputStream();

            InputStream serverIn =
                    serverSocket.getInputStream();
             StringBuilder requestText =
                    new StringBuilder();
            requestText
                    .append(request.method)
                    .append(" ")
                    .append(request.path)
                    .append(" ")
                    .append(request.version)
                    .append("\r\n");

            for (Map.Entry<String, String> entry :
                    request.headers.entrySet()) {

                requestText
                        .append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\r\n");
            }

            requestText.append("\r\n");
            serverOut.write(
                    requestText
                            .toString()
                            .getBytes(StandardCharsets.UTF_8)
            );

            serverOut.flush();
            ByteArrayOutputStream responseBuffer =
                    new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead =
                    serverIn.read(buffer)) != -1) {

                responseBuffer.write(
                        buffer,
                        0,
                        bytesRead
                );
            }
            byte[] fullResponse =
                    responseBuffer.toByteArray();
            clientOut.write(fullResponse);
            clientOut.flush();
           int statusCode =
                    parseStatusCode(fullResponse);
            System.out.println(
                    "HTTP Status: " + statusCode
            );
            if (CachePolicy.isCacheable(
                    request.method,
                    statusCode)) {

                cache.store(
                        cacheKey,
                        fullResponse
                );

                System.out.println(
                        "Stored in cache: " + cacheKey
                );
            }
        }
    }
    private static int parseStatusCode(
            byte[] response) {

        try {

            String text =
                    new String(
                            response,
                            StandardCharsets.UTF_8
                    );

            int firstLineEnd =
                    text.indexOf("\r\n");

            String statusLine;

            if (firstLineEnd == -1) {
                statusLine = text;
            }
            else {
                statusLine =
                        text.substring(0, firstLineEnd);
            }

            String[] parts =
                    statusLine.split(" ");

            if (parts.length >= 2) {
                return Integer.parseInt(parts[1]);
            }

        }
        catch (Exception e) {

            System.out.println(
                    "Could not parse HTTP status"
            );
        }

        return -1;
    }

    private static void sendError(
            OutputStream out,
            int statusCode,
            String message) throws IOException {

        String body = message;

        String response =
                "HTTP/1.1 "
                        + statusCode
                        + " "
                        + message
                        + "\r\n"
                        + "Content-Type: text/plain\r\n"
                        + "Content-Length: "
                        + body.getBytes(StandardCharsets.UTF_8).length
                        + "\r\n"
                        + "Connection: close\r\n"
                        + "\r\n"
                        + body;

        out.write(
                response.getBytes(StandardCharsets.UTF_8)
        );

        out.flush();
    }
}
