import java.io.*;
import java.net.*;

public class Cli{
    public static void main(String[] args){
        try{
            //Creating client and connecting
            Socket cli = new Socket("172.20.10.3", 5566);

            //Reader
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            //Writer
            DataOutputStream out = new DataOutputStream(cli.getOutputStream());

            String tmp;

            //Action of reading and writing through server
            tmp = in.readLine();
            out.writeUTF(tmp);

            c1.close();

        }catch(IOException e){
            System.out.println("Error");
        }
    }
}
