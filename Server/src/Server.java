package app;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server{

    //Constant variables
    public static final int port = 1233;
    public static final int clientNum = 2;

    // STATES
    public static final char GAME_START             = 'A';
    public static final char DEAL_CARDS             = 'B';
    public static final char DRAW_CARD              = 'C';
    public static final char CHECK_BEFORE_DISCARD   = 'D';
    public static final char DISCARD                = 'E';
    public static final char CHECK_ALL              = 'F';
    public static final char ACTION                 = 'G';
    public static final char WIN_END                = 'H';

    // COMMANDS
    public static final char SEND   = 'S';
    public static final char OPT    = 'O';
    public static final char POOL   = 'P';

    public static final char CANCEL = '1';
    public static final char WIN = '2';
    public static final char KONG = '3';
    public static final char PONG = '4';
    public static final char CHOW_REAR = '5';
    public static final char CHOW_MID = '6';
    public static final char CHOW_FRONT = '7';

    //Declarations for connection
    private static ServerSocket server = null;                         //Server
    private static Socket[] playersSocket = new Socket[4];             //Sockets to players
    private static DataInputStream[] in = new DataInputStream[4];          //Input
    private static DataOutputStream[] out = new DataOutputStream[4];       //Output

    //Declarations for game
    private static Board board = new Board();
    private static char state = 'A';
    private static int turn = 0;


    private static Player[] players = new Player[4];
    private static ArrayList<String> cards = new ArrayList<String>();      //Stack of cards
    private static ArrayList<String> pool = new ArrayList<String>();      //Stack of cards
    private static int next = 0;
    
    private static int check_turn = 0;

    private static int who_pong = -1;
    private static int who_kong = -1;


    private static int player_num = 2;
    private static Card empty = new Card("nn");


    private static Scanner play_card = new Scanner(System.in); //Temporary

    public static void main(String args[]){

        //Initializations
        // for(int i = 0; i < player_num; i++){
        //     playersSocket[i] = null;
        //     in[i] = null;
        //     out[i] = null;
        // }
        

        // Initialize of the server
        // Assignment of Players
        for (int i = 0; i < clientNum; i++)
            players[i] = new Player();

        
        //Creating Server
        try{
            server = new ServerSocket(port);
            System.out.println("Server created. Waiting for connection...");
        } catch(IOException e){
            System.out.println("Server creating ERROR");
            e.printStackTrace();
        }


        //Client connecting
        for(int i = 0; i < clientNum; i++){
            try{
                playersSocket[i] = server.accept();
                in[i] = new DataInputStream(playersSocket[i].getInputStream());
                out[i] = new DataOutputStream(playersSocket[i].getOutputStream());
                System.out.printf("Player %d is connected, ", i + 1);
                System.out.println("IP: " + playersSocket[0].getInetAddress());
                out[i].writeInt(i + 1); // Send each player's number
            } catch(IOException e){
                System.out.printf("Player %d ", i + 1);
                System.out.printf("connecting problem\n", i + 1);
                e.printStackTrace();
            }
        }


        //Initialization for game

        // for(int i = 0; i < clientNum; i++)
        //     handSend(players[i].hands, out[i]);

        // for(int j = 0; j < clientNum; j++){
        //     System.out.print("Player");
        //     for(int i = 0; i < 17; i++)
        //         System.out.print(players[j].hands[i].str_generate() + ", ");
        //     System.out.println("");
        //     System.out.println(players[j].consume);
        // }
        // System.out.println(pool);
        
        try{
            while(true){

                // Game start -- Shuffle & Deal
                if(state == GAME_START){

                    for(int i = 0; i < clientNum; i++)  
                        out[i].writeChar(GAME_START);

                    gameStart();
                    state = DRAW_CARD;
                }

                // A player draws
                else if(state == DRAW_CARD){
                    draw(turn);
                    handSend();
                    flowerOff(turn);
                    handSend();
                    state = CHECK_BEFORE_DISCARD;
                }

                // After draw, before discard
                else if(state == CHECK_BEFORE_DISCARD){
                    boolean[] temp = check_BeforeDiscard(turn);
                    
                    // get opt
                    if(temp[0]) state = DISCARD;
                    else{
                        char reply = getOpt(turn, temp);
                        switch(reply){
                            case CANCEL:
                                state = DISCARD;
                                break;
                            case WIN:
                                state = WIN_END;
                                break;
                            case KONG:
                                kong(turn);
                                draw(turn);
                                state = DISCARD;
                                break;
                            default:
                                break;
                        }
                    }
                    state = DISCARD;
                }

                else if(state == DISCARD){
                    discard(turn);
                    nextTurn();
                    state = CHECK_ALL;
                }

                else if(state == CHECK_ALL){

                    // Check all other players' options
                    boolean[][] temp = new boolean[clientNum - 1][];
                    int tempTurn = turn;
                    for(int i = 0; i < clientNum - 1; i++){
                        temp[i] = check_all(tempTurn);
                        tempTurn++;
                        if(tempTurn == clientNum) tempTurn = 0;
                    }

                    // Check Win, Kong, Pong first
                    tempTurn = turn;
                    for (int i = 0; i < clientNum - 1; i++) {
                        if(!temp[i][0]){    // Not skipped
                            if(temp[i][2] || temp[i][3] || temp[i][4]){     // Win, Kong, Pong
                                if(i != 0){
                                    char reply = getOpt(tempTurn, temp[i]);
                                    switch (reply) {
                                    case CANCEL:
                                        break;
                                    case WIN:
                                        turn = tempTurn;
                                        state = WIN_END;
                                        break;
                                    case KONG:
                                        kong(tempTurn);
                                        state = DRAW_CARD;
                                        break;
                                    case PONG:
                                        pong(tempTurn);
                                        state = DISCARD;
                                        break;
                                    default:
                                        break;
                                    }
                                }
                                else{
                                    char reply = getOpt(tempTurn, temp[i]);
                                    switch (reply) {
                                    case CANCEL:
                                        state = DRAW_CARD;
                                        break;
                                    case WIN:
                                        state = WIN_END;
                                        break;
                                    case KONG:
                                        kong(tempTurn);
                                        state = DRAW_CARD;
                                        break;
                                    case PONG:
                                        pong(tempTurn);
                                        state = DISCARD;
                                        break;
                                    case CHOW_REAR:
                                        chow(tempTurn, 3);
                                        state = DISCARD;
                                        break;
                                    case CHOW_MID:
                                        chow(tempTurn, 2);
                                        state = DISCARD;
                                        break;
                                    case CHOW_FRONT:
                                        chow(tempTurn, 1);
                                        state = DISCARD;
                                        break;
                                    default:
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        tempTurn++;
                        if (tempTurn == clientNum) tempTurn = 0;
                    }
                    if(state != CHECK_ALL) continue;    // If somebody do something
                    
                    // No one pong/kong/win, next player chow
                    if(!temp[0][0]){
                        if(temp[0][5] || temp[0][6] || temp[0][7]){
                            char reply = getOpt(turn, temp[0]);
                            switch (reply) {
                            case CANCEL:
                                state = DRAW_CARD;
                                break;
                            case WIN:
                                state = WIN_END;
                                break;
                            case KONG:
                                kong(turn);
                                state = DRAW_CARD;
                                break;
                            case PONG:
                                pong(turn);
                                state = DISCARD;
                                break;
                            case CHOW_REAR:
                                chow(turn, 3);
                                state = DISCARD;
                                break;
                            case CHOW_MID:
                                chow(turn, 2);
                                state = DISCARD;
                                break;
                            case CHOW_FRONT:
                                chow(turn, 1);
                                state = DISCARD;
                                break;
                            default:
                                break;
                            }
                        }
                    }

                    state = DRAW_CARD;
                }
                // Win and end
                else if(state == WIN_END){
                    String temp = "Player " + String.valueOf(turn) + " wins!!!";
                    System.out.println(temp);
                    for(int i = 0; i < clientNum; i++){
                        out[i].writeChar(WIN_END);
                        out[i].writeUTF(temp);
                    }
                    break;
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        
        //Closing sockets
        try{
            for(int i = 0; i < clientNum; i++)
                playersSocket[i].close();
        } catch(IOException e){
            System.out.println("ERROR: Closing socket problem");
            e.printStackTrace();
        }
    }

    /*** Game Processes ***/

    public static void gameStart(){

        // Shuffled stack & empty pool
        board.newGame();    
       
        // Deal cards
        for(int i = 0; i < clientNum; i++)
            players[i].dealedCard(board.cardDeal());
        handSend();

        // Sort cards
        for(int i = 0; i < clientNum; i++)  players[i].sortCards(); 
        handSend();

        // Flower off
        for(int i = 0; i < clientNum; i++)  flowerOff(i);
        handSend();
    }

    // Next turn
    public static void nextTurn() {
        turn++;
        if (turn == player_num) turn = 0;
    }


    /*** Actions taken by players ***/

    // Draw card(s)
    public static void draw(int player) {
        players[player].draw(board.cardDraw());
    }

    // Take out flower
    public static void flowerOff(int player) {
        while (players[player].hasFlower()) {
            draw(player);
        }
        players[player].sortCards();
    }

    //Pong
    public static void pong(int player){
        players[player].pong(board.poolUsed());
        turn = player;
    }

    //Kong
    public static void kong(int player){
        if(turn == player) 
            players[player].kong("nn");
        else
            players[player].kong(board.poolUsed());
        turn = player;
    }

    //Chow
    public  static void chow(int player, int set){
        players[player].chow(board.getLatest(), set);
        turn = player;
    }

    // Check before discard [ skip, cancel, win, kong ]
    public static boolean[] check_BeforeDiscard(int player) {

        String card = board.getLatest();
        boolean[] result = new boolean[8];
        for (int i = 0; i < 8; i++) result[i] = false;

        // Default skip & cancle
        result[0] = true;
        result[1] = true;

        // Check Kong
        if (players[player].checkPong(card) == 2) {
            result[3] = true;
            result[0] = false; // won't skip
        }

        return result;
    }

    // Check all options [skip, cancel, win, kong, pong, chow_rear, chow_mid, chow_front]
    public static boolean[] check_all(int player) {

        String card = board.getLatest();
        boolean[] result = new boolean[8];
        for (int i = 0; i < 8; i++) result[i] = false;

        // Default skip & cancle
        result[0] = true;
        result[1] = true;

        // Check Kong, Pong
        int temp = players[player].checkPong(card);
        if (temp == 2) {
            result[3] = true;
            result[4] = true;
            result[0] = false; // won't skip
        } else if (temp == 1) {
            result[4] = false;
            result[0] = false;
        }

        // Check Chow
        boolean[] result_chow = players[player].checkChow(card);
        result[5] = result_chow[2];
        result[6] = result_chow[1];
        result[7] = result_chow[0];
        if (result[5] || result[6] || result[7])
            result[0] = false;

        return result;
    }


    /*** Communicate Processes ***/

    // Discard
    public static void discard(int player) {
	int n = 0;
	try{
            out[player].writeChar(DISCARD); // Start sending
            n = Integer.valueOf(in[player].readUTF()); // Get number
	} catch(IOException e){
            e.printStackTrace();
        }
        board.cardDiscarded(players[player].discard(n)); // Discard
    }

    // Send players' cards state to clients (int * 1 + String * 2)
    public static void handSend() {
	try{
            for (int i = 0; i < clientNum; i++) {
                out[i].writeChar(SEND); // Start sending
                for (int j = 0; j < clientNum; j++) {
		    out[i].writeInt(j + 1); // Tell which player's
                    out[i].writeUTF(players[j].getHand()); // Send hand
                    out[i].writeUTF(players[j].getConsume()); // Send consume
                }
            }
	} catch(IOException e){
            e.printStackTrace();
        }
	try{
            Thread.sleep(1000);
        } catch(InterruptedException e){
	    e.printStackTrace();
	}
    }

    // Send pool
    public static void poolSend(){
        String temp = board.getPool();          // Get pool info
	try{
            for(int i = 0; i < clientNum; i++){
                out[i].writeChar(POOL);             // Start sending
                out[i].writeUTF(temp);
            }
	} catch(IOException e){
            e.printStackTrace();
        }
    }

    // Send opts, and get chosen one back
    public static char getOpt(int player, boolean[] opts) {
	char opt = ' ';
	try{
            out[player].writeChar(OPT);             // Start sending
            for (int i = 1; i <= 7; i++) {          // Send options
                if (opts[i]) out[player].writeChar('Y');
                else out[player].writeChar('N');
            }
            opt = in[player].readChar();           // Get option
	} catch(IOException e){
            e.printStackTrace();
        }
	return opt;
    }
}

