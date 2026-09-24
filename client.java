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
        Scanner sc= new Scanner(System.in);
        String message= sc.nextLine();
        out.println(message);
        BufferedReader br= new BufferedReader(new InputStreamReader(client.getInputStream()));
        System.out.println(br.readLine());
    }
}
