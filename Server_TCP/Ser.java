import java.io.*;
import java.net.*;

public class Ser{
    public static void main(String args[]){
        try{

            //Creating Server
            ServerSocket server = new ServerSocket(1011);
            System.out.println("Server is created. Wating for connection...");
            Socket s1 = server.accept();
            System.out.println("Client is connected, IP: " + s1.getInetAddress());
            
            //Recieving data
            DataInputStream din1 = new DataInputStream(s1.getInputStream());
            String tmp = new String(din1.readUTF());
            System.out.println(tmp);
            
            s1.close();
        }catch(IOException e){
            System.out.println("Error");
        }
    }
}
