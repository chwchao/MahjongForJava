package ser;

import java.io.*;
import java.net.*;
import java.util.*;

public class Ser{

    public static ArrayList<String> cards = new ArrayList<String>();      //Stack of cards
    public static int next = 0;

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
        
        
        //Declarations for game
        Player[] players = new Player[4];
        

        //Initialization for game
        for(int i = 0; i < 4; i++){
            players[i] = new Player();
            for(int j = 0; j < 17; j++)
                players[i].hands[j] = new Card();
        }

        //Shuffle cards
        cards = shuffled_cards();

        //Assign cards at start
        for(int i = 0; i < 4; i++)
            draw(players[i], 16);

        for(int j = 0; j < 4; j++){
            System.out.println("Player");
            for(int i = 0; i < 17; i++)
                System.out.print(players[j].hands[i].str_generate() + ", ");
        }
            
        for(int j = 0; j < 4; j++)
            sortCards(players[j].hands);

        handSend(players[0].hands, out[0]);

        for(int j = 0; j < 4; j++){
            System.out.println("Player");
            for(int i = 0; i < 17; i++)
                System.out.print(players[j].hands[i].str_generate() + ", ");
        }

        // //GAME
        // while(true){
            
        // }


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

    //Draw card(s)
    public static void draw(Player player, int num){

        if(num == 1){
            player.hands[16] = new Card(cards.get(next));
            next++;
        }
        else if(num == 16){
            for(int i = 0; i < 16; i++){
                player.hands[i] = new Card(cards.get(next));
                next++;
            }      
        }
        return;
    }

    //Sort cards in order
    public static void sortCards(Card[] hands){

        for(int i = 0; i < 16; i++)
            for(int j = i; j < 16; j++){
                if(hands[i].sort > hands[j].sort) swapCard(hands, i, j);
                else if(hands[i].sort == hands[j].sort && hands[i].val > hands[j].val) swapCard(hands, i, j);
            }
    }

    //Swap two card
    public static void swapCard(Card[] hands, int a, int b){
        Card temp1 = new Card(hands[a]);
        Card temp2 = new Card(hands[b]);
        hands[a] = temp2;
        hands[b] = temp1;
        return;
    }

    //Send players' cards state to clients
    public static void handSend(Card[] player, DataOutputStream out){
        String tmp = player[0].str_generate() + " ";
        for(int i = 1; i < 17; i++)
            tmp += (player[i].str_generate() + " ");
        
        try{
            out.writeUTF(tmp);
        } catch(IOException e){
            System.out.println("ERROR: Sending hands problem");
            e.printStackTrace();
        }
    }

    // //Get card from stack
    // public static void touch(){
        
    // }

}

