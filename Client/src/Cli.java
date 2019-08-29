package app;

import java.io.*;
import java.net.*;
import java.util.*;

public class Cli{

    private static final int clientNum = 2;

    // STATES
    public static final char GAME_START = 'A';
    public static final char DEAL_CARDS = 'B';
    public static final char DRAW_CARD = 'C';
    public static final char CHECK_BEFORE_DISCARD = 'D';
    public static final char DISCARD = 'E';
    public static final char CHECK_ALL = 'F';
    public static final char ACTION = 'G';
    public static final char WIN_END = 'H';

    // COMMANDS
    public static final char SEND       = 'S';
    public static final char OPT        = 'O';
    public static final char POOL       = 'P';

    public static final char CANCEL = '1';
    public static final char WIN = '2';
    public static final char KONG = '3';
    public static final char PONG = '4';
    public static final char CHOW_REAR = '5';
    public static final char CHOW_MID = '6';
    public static final char CHOW_FRONT = '7';

    public static final String[] OPTIONS = {"Cancel", "Win", "Kong", "Pong", "Chow_Rear", "Chow_Mid", "Chow_Front"};

    //Declarations for connection
    private static Socket cli = null;
    private static BufferedReader local_input = null;      //Reader for local
    private static DataInputStream in = null;         //Input from server
    public static DataOutputStream out = null;     //Output to server

    //Deprivateclarations for game
    private static int number;
    private static char cmd;

    public static void main(String[] args){

        //Connecting to server
        try{
            cli = new Socket("218.164.163.49", 1233);
            in = new DataInputStream(cli.getInputStream());
            out = new DataOutputStream(cli.getOutputStream());
            System.out.println("Connected.");
            number = in.readInt();
            System.out.printf("You're player %d.\n", number);
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
        
        // Recieve command and response
        try{
            while(true){
                cmd = in.readChar();
                
                // Game starts
                if(cmd == GAME_START)
                    System.out.println("Game Start !!!");

                // Recieve players' cards
                else if(cmd == SEND)
                    handRecieve();

                // Recieve pool info
                else if(cmd == POOL)
                    poolRecieve();
                    
                // Options
                else if(cmd == OPT)
                    doOption();

                // Discard
                else if(cmd == DISCARD)
                    doDiscard();

                // Someone wins
                else if(cmd == WIN_END){
                    System.out.println(in.readUTF());
                    break;
                }
            }
        }catch(IOException e){
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

    /*** Communicate Processes ***/

    // Recieve players' imformation and print it out ( Show all players' cards )
    public static void handRecieve(){
        System.out.println("\n\n\n\n\n\n\n\n");
        for(int i = 0; i < clientNum; i++){
            int tmpPlayer = 0;
            String tmpHand = "";
            String tmpConsume = "";
            try {
                tmpPlayer = in.readInt();
                tmpHand = in.readUTF();
                tmpConsume = in.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Print out players' imformation
            if(tmpPlayer == number){
                System.out.println("***YOU***");
                System.out.printf("hands:\t%s", tmpHand);
                System.out.printf("consumes:\t%s", tmpConsume);
                System.out.println("");
            }
            else{
                System.out.printf("Player %d\n");
                for(int j = 0; j < tmpHand.length(); j++){
                    if(tmpHand.charAt(j) == ' ') System.out.print(" ");
                    else System.out.print("*");
                }
                System.out.println("");
                for(int j = 0; j < tmpConsume.length(); j++){
                    if(tmpConsume.charAt(j) == ' ') System.out.print(" ");
                    else System.out.print("*");
                }
                System.out.println("");
            }
        }
    }

    // Recieve pool cards
    public static void poolRecieve(){
        try{
            System.out.println("Pool: " + in.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Recieve options before discarding
    public static void doOption(){
        
        // Get and show options (cancel, win, kong, pong, chow_rear, chow_mid, chow_front), and print out
        String temp = "";
        for(int i = 0; i < 7; i++){
            try{
                if(in.readChar() == 'Y') temp += String.valueOf(i + 1) + "-" + OPTIONS[i] + " / ";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Please select what to do:  %s", temp);

        // Choose and send
        try{
            out.writeChar(local_input.readLine().charAt(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Choose a card to discard
    public static void doDiscard(){

        System.out.print("Please discard: ");
        try{
            out.writeUTF(local_input.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
