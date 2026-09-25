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

class handleClient implements Runnable{
    Socket clientSocket;
    handleClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
   public void run(){
        try{
            clientSocket.setSoTimeout(80000);
            BufferedReader in= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;

            while (!(line = in.readLine()).isEmpty()) {

                System.out.println(line);

            }
        }
    catch(SocketTimeoutException e){
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
            e.printStackTrace();}
        }
    }}

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
