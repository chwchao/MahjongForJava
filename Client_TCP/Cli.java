import java.io.*;
import java.net.*;
import java.util.*;

//import java.awt.*;
//import javax.swing.*;

//import javax.swing.JFrame;

public class Cli{

    //Declarations for connection
    private static Socket cli = null;
    private static BufferedReader local_input = null;      //Reader for local
    private static DataInputStream in = null;         //Input from server
    public static DataOutputStream out = null;     //Output to server

    //Deprivateclarations for game
    private static String[] hands;
    private static char state = 'n';

    private static boolean win = false;
    private static boolean kong = false;
    private static boolean pong = false;
    private static boolean[] chow = new boolean[3];
    
    

    private static Scanner discard = new Scanner(System.in);

    public static void main(String[] args){

        

        //Connecting to server
        try{
            cli = new Socket("218.164.171.48", 1233);
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

        

        //Action of reading and writing through server

        hands = hands_recieve(in);

        System.out.println(Arrays.asList(hands));

        char cmd = 'n';
        //GAME
        try{
            while(true){
                state = in.readChar();

                //Player's discard turn
                if(state == 'D'){
                    //Refresh scene
                    //Discard
                    out.writeInt(discard.nextInt());
                    //Refresh scene
                    hands = hands_recieve(in);
                    System.out.println(Arrays.asList(hands));
                }

                //Non of your business
                else if(state == 'N'){
                    hands = hands_recieve(in);
                    System.out.println(Arrays.asList(hands));
                }

                //State for available movements
                else if(state == 'M'){

                    //Recieve chow
                    if(in.readChar() == 'c'){
                        for(int i = 0; i < 3; i++)
                        if(in.readChar() == 'y')
                            chow[i] = true;
                        else if(in.readChar() == 'n')
                            chow[i] = false;
                    }

                    //Recieve pong
                    if(in.readChar() == 'y')
                        pong = true;
                    else
                        pong = false;

                    //Recieve kong
                    if(in.readChar() == 'y')
                        kong = true;
                    else
                        kong = false;
                }

                //Choose movement
                else if(state == 'C'){
                    
                    //Recieve card
                    String temp_card = in.readUTF();

                    //refresh frame

                    //Send cmd
                    //no movement
                    if(!win && !pong && !kong && !chow[0] && !chow[1] && !chow[2]);
                    else{
                        out.writeChar(discard.nextLine().charAt(0));
                    }
                    win = false;
                    kong = false;
                    pong = false;
                    chow = new boolean[3];
                }

                
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        

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
