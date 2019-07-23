import java.io.*;
import java.net.*;
import java.util.*;

//import java.awt.*;
//import javax.swing.*;

//import javax.swing.JFrame;

public class Cli{
    public static void main(String[] args){

        //Declarations for connection
        Socket cli = null;
        BufferedReader local_input = null;      //Reader for local
        DataInputStream in = null;         //Input from server
        DataOutputStream out = null;     //Output to server

        //Connecting to server
        try{
            cli = new Socket("36.238.86.180", 1233);
            in = new DataInputStream(cli.getInputStream());
            out = new DataOutputStream(cli.getOutputStream());
            System.out.println("Connected.");
        } catch(IOException e){
            System.out.println("ERROR: Connection to server problem");
            e.printStackTrace();
        }

        //Local input buliding
        try{
            local_input = new BufferedReader(new InputStreamReader(System.in));      //Reader for local
        }catch(Exception e){
            System.out.println("Error");
        }

        //Declarations for game
        String[] hands;

        //Action of reading and writing through server

        hands = hands_recieve(in);

        System.out.println(Arrays.asList(hands));

        
        //Setup for window
        // JFrame frame = new JFrame();
        // frame.setSize(1024, 768);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setVisible(true);

        //Closing sockets
        try{
            cli.close();
        } catch(IOException e){
            System.out.println("ERROR: Closing socket problem");
            e.printStackTrace();
        }
    }

    public static String[] hands_recieve(DataInputStream in){
        String tmp = "";
        try{
            tmp = in.readUTF();
        } catch(IOException e){
            e.printStackTrace();
        }
        return tmp.split("\\s+");
    }
}
