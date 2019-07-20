import java.io.*;
import java.net.*;

public class Ser{
    public static void main(String[] args){
        DatagramSocket server = null;
        int socket_no = 5566;
        try{
            //Creating server
            server = new DatagramSocket(socket_no);
            System.out.println("UDP Server is Created. Waiting for data...");
            
            //Initializing buffer
            byte[] buffer = new byte[20];
            for(int i = 0; i < 20; i++)
                buffer[i] = ' ';

            //Recieving data
            while(true){

                //Action of receiving
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                server.receive(request);
                System.out.println("Receive data: " + new String(request.getData()));
                
                //Reset buffer
                for(int i = 0; i < 20; i++)
                    buffer[i] = ' ';
            }
        }
        catch(SocketException e){
            System.out.println("Socket Error");
        }
        catch(IOException e){
            System.out.println("IO Error");
        }
        finally{
            if(server != null)
                server.close();
        }
    }
}
