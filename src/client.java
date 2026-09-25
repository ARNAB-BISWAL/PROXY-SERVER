import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
public class client {
    public static void main (String [] args) throws IOException{
        Socket client= new Socket("localhost",8081);
        System.out.println("Client Connected");
        PrintWriter out= new PrintWriter(client.getOutputStream(),true);
        //here true arguement makes auto flush= true
        out.println("GET /index.html HTTP/1.1");
        out.println("Host: example.com");
        out.println("User-Agent: MyJavaClient");
        out.println("Accept: text/html");
        out.println("Connection: close");
        out.println("");

        client.close();
    }
}
