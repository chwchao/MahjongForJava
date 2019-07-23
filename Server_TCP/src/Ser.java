package ser;

import java.io.*;
import java.net.*;
import java.util.*;

public class Ser{
    public static void main(String args[]){

        //Declarations for connection
        ServerSocket server = null;                         //Server
        Socket[] playersSocket = new Socket[4];             //Sockets to players
        DataInputStream[] in = new DataInputStream[4];          //Input
        DataOutputStream[] out = new DataOutputStream[4];       //Output

        //Initializations
        for(int i = 0; i < 4; i++){
            playersSocket[i] = null;
            in[i] = null;
            out[i] = null;
        }
            

        //Creating Server
        try{
            server = new ServerSocket(1233);
            System.out.println("Server created. Waiting for connection...");
        } catch(IOException e){
            System.out.println("Server creating ERROR");
            e.printStackTrace();
        }

        //Client connecting
        for(int i = 0; i < 1; i++){
            try{
                playersSocket[i] = server.accept();
                in[i] = new DataInputStream(playersSocket[i].getInputStream());
                out[i] = new DataOutputStream(playersSocket[i].getOutputStream());
                System.out.println("Client is connected, IP: " + playersSocket[0].getInetAddress());
            } catch(IOException e){
                System.out.printf("ERROR: Player%d connecting problem\n", i+1);
                e.printStackTrace();
            }
        }

        // try{
        //     //Recieving data
        //     in1 = new DataInputStream(s1.getInputStream());

        //     String tmp = new String(din1.readUTF());
        //     //System.out.println(tmp);

            
        // }catch(IOException e){
        //     System.out.println("Error");
        // }


        
        
        //Declarations for game
        ArrayList<String> cards = new ArrayList<String>();      //Stack of cards
        Card[][] players = new Card[4][17];

        cards = shuffled_cards();


        for(int i = 0; i < 16; i++){
            players[0][i] = new Card(cards.get(i));
        }

        for(int i = 0; i < 16; i++)
            System.out.println(players[0][i].str_generate());

        swapCard(players[0][0], players[0][1]);

        handSend(players[0], out[0]);

        //Closing sockets
        try{
            playersSocket[0].close();
        } catch(IOException e){
            System.out.println("ERROR: Closing socket problem");
            e.printStackTrace();
        }
        
    }

    //Create a shuffled stack of cards for a new game.
    public static ArrayList<String> shuffled_cards(){
        ArrayList<String> cards = new ArrayList<String>(
            //Default cards for a game
            //Bamboo, Character, Dot, Wind, Dragon, Flower
            Arrays.asList(
                "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9",
                "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9",
                "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9",
                "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9",
                "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9",
                "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9",
                "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9",
                "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9",
                "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9",
                "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9",
                "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9",
                "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9",
                "WE", "WE", "WE", "WE",
                "WW", "WW", "WW", "WW",
                "WN", "WN", "WN", "WN",
                "WS", "WS", "WS", "WS",
                "DR", "DR", "DR", "DR",
                "DG", "DG", "DG", "DG",
                "DW", "DW", "DW", "DW",
                "FS", "Fs", "FA", "FW", //S: Spring, s: Summer
                "FP", "FO", "FB", "FC"
            )
        );
        Collections.shuffle(cards);
        return cards;
    }

    // //Sort cards in order
    // public void sortCards(Card[] hands){

    //     //Bubble sort
    //     for(int i = 0; i < 15; i++){
    //         if()
    //     }
    // }

    //Swap two card
    public static void swapCard(Card a, Card b){
        Card temp = a;
        a = b;
        b = temp;
        return;
    }

    public static void handSend(Card[] player, DataOutputStream out){
        String tmp = player[0].str_generate() + " ";
        for(int i = 1; i < 16; i++)
            tmp += (player[i].str_generate() + " ");
        
        try{
            out.writeUTF(tmp);
        } catch(IOException e){
            System.out.println("ERROR: Sending hands problem");
            e.printStackTrace();
        }
    }
}



/*

ArrayList<String> shuffled_cards()


void hands_send(Card[] player, DataOutputStream out)





*/