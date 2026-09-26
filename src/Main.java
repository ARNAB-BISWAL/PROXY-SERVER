import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

class HttpRequest {
    String method;
    String path;
    String version;
    Map<String, String> headers = new HashMap<>();

    HttpRequest(String method, String path, String version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }
}

class handleClient implements Runnable{
    Socket clientSocket;
    handleClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
   public void run(){
        try{
            clientSocket.setSoTimeout(80000);
            BufferedReader in= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
           
            OutputStream clientOut = clientSocket.getOutputStream();
            // Read request line
            String firstLine = in.readLine();
            if (firstLine == null || firstLine.isEmpty()) {
                return;
            }
            String[] parts = firstLine.split(" ");
            if (parts.length != 3) {
                return;
            }
            HttpRequest request =
                    new HttpRequest(parts[0], parts[1], parts[2]);
            // Read headers
            
            String line;
             while ((line = in.readLine()) != null && !line.isEmpty()) {
                int colon = line.indexOf(":");

                if (colon > 0) {
                    String key = line.substring(0, colon).trim();
                    String value = line.substring(colon + 1).trim();

                    request.headers.put(key, value);
                }
            }
            System.out.println("Method: " + request.method);
            System.out.println("Path: " + request.path);
            System.out.println("Version: " + request.version);
            System.out.println("Host: " + request.headers.get("Host"));

            if (!request.method.equals("GET")) {
                String response =
                        "HTTP/1.1 501 Not Implemented\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: 18\r\n" +
                        "Connection: close\r\n\r\n" +
                        "Only GET supported";

                clientOut.write(response.getBytes());
                return;
            }

            String host = request.headers.get("Host");
            if (host == null) {
                return;
            }
            int port = 80;
            if (host.contains(":")) {
                String[] hostParts = host.split(":");
                host = hostParts[0];
                port = Integer.parseInt(hostParts[1]);
            }
            // Connect to website
            Socket serverSocket = new Socket(host, port);
            System.out.println("Connected to: " + host);
            OutputStream serverOut = serverSocket.getOutputStream();
            InputStream serverIn = serverSocket.getInputStream();
            // Send request to website
            StringBuilder requestText = new StringBuilder();
            requestText.append(request.method)
                    .append(" ")
                    .append(request.path)
                    .append(" ")
                    .append(request.version)
                    .append("\r\n");
            for (Map.Entry<String, String> entry : request.headers.entrySet()) {
                requestText.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\r\n");
            }

            requestText.append("\r\n");
            serverOut.write(requestText.toString().getBytes());
            serverOut.flush();

            // Receive response and send it to browser
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = serverIn.read(buffer)) != -1) {
                clientOut.write(buffer, 0, bytesRead);
                clientOut.flush();
            }

            serverSocket.close();
        }catch(SocketTimeoutException e){
        System.out.println("Client took too long");
    }
    catch(IOException e){
        e.printStackTrace();
    }
    finally {
            try{
            clientSocket.close();
        }
        catch(IOException e){
            e.printStackTrace();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket= new ServerSocket(8081);
        System.out.println("Server is running...");
        ExecutorService executor= Executors.newFixedThreadPool(10);
        while(true){
            Socket clientSocket= serverSocket.accept();
            System.out.println("Client connected");
            executor.execute(
                new handleClient(clientSocket));
            }
           }

}
