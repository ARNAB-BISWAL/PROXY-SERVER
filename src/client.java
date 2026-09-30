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
        BufferedReader in =new BufferedReader(new InputStreamReader(client.getInputStream()));
        Scanner scanner =new Scanner(System.in);
        System.out.print("Enter host (example.com): ");
        String host = scanner.nextLine();
// here is the http request
        out.println("GET /index.html HTTP/1.1");
        out.println( "Host: " + host);
        out.println("User-Agent: MyJavaClient");
        out.println("Accept: text/html");
        out.println("Connection: close");
        out.println("");
        
         System.out.println( "\n----- RESPONSE -----\n");
         String line;
         while ((line = in.readLine()) != null) {
             System.out.println(line);
        }


        client.close();
        scanner.close();
    }
}
