package server;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.lang.model.util.ElementScanner6;

import com.sun.tools.sjavac.CleanProperties;

public class Server{

    //Constant variables
    public static final int port = 1233;
    public static final int clientNum = 2;

    //Declarations for connection
    private static ServerSocket server = null;                         //Server
    private static Socket[] playersSocket = new Socket[4];             //Sockets to players
    private static DataInputStream[] in = new DataInputStream[4];          //Input
    private static DataOutputStream[] out = new DataOutputStream[4];       //Output

    //Declarations for game
    private static Board board = new Board();


    private static Player[] players = new Player[4];
    private static ArrayList<String> cards = new ArrayList<String>();      //Stack of cards
    private static ArrayList<String> pool = new ArrayList<String>();      //Stack of cards
    private static int next = 0;
    private static char state = 'T';
    private static int turn = 0;
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
        serIni();

        
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
            } catch(IOException e){
                System.out.printf("Player %d ", i + 1);
                System.out.printf("connecting problem\n", i + 1);
                e.printStackTrace();
            }
        }


        //Initialization for game
        

        //Start of the game
        gameStart();
        for(int i = 0; i < clientNum; i++){
            sortCards(players[i].hands);
        }
            
        for(int j = 0; j < clientNum; j++){
            flowerOff(players[j]);
            sortCards(players[j].hands);
        }
            

        for(int i = 0; i < clientNum; i++)
            handSend(players[i].hands, out[i]);

        for(int j = 0; j < clientNum; j++){
            System.out.print("Player");
            for(int i = 0; i < 17; i++)
                System.out.print(players[j].hands[i].str_generate() + ", ");
            System.out.println("");
            System.out.println(players[j].consume);
        }
        System.out.println(pool);
        
        //GAME
        try{
            while(true){

                //"TURN": Someone has to discard a card 
                if(state == 'T'){
                    System.out.println("T");
                    who_pong = -1;
                    who_kong = -1;

                    discard(turn);                              //discard
                    sortCards(players[turn].hands);             //sort
                    handSend(players[turn].hands, out[turn]);   //send to client

                    //Just print out
                    for(int j = 0; j < clientNum; j++){
                        System.out.print("Player");
                        for(int i = 0; i < 17; i++)
                            System.out.print(players[j].hands[i].str_generate() + ", ");
                        System.out.println("");
                        System.out.println(players[j].consume);
                    }
                    System.out.println(pool);

                    //Go to check
                    state = 'C'; //Check
                    continue;
                }

                //"CHECK": Check after someone discard one (win -> kong/pong -> chow)
                else if(state == 'C'){
                    System.out.println("C");

                    /*Check win first*/

                    //Check for CHOW
                    check_turn = turn + 1;
                    if(check_turn == clientNum) check_turn = 0;
                    boolean[] result = ckeckChow(check_turn);
                    
                    //Check for KONG/PONG
                    check_turn = turn;
                    for(int i = 0; i < clientNum - 1; i++){
                        check_turn++;
                        if(check_turn == clientNum) check_turn = 0;
                        int temp = checkPong(check_turn);
                        if(temp == 2){
                            who_kong = check_turn;
                            who_pong = check_turn;
                            break;
                        }
                        else if(temp == 1){
                            who_pong = check_turn;
                            break;
                        }
                    }

                    //Send result to clients
                    int temp_turn = turn;
                    for(int i = 0; i < clientNum - 1; i++){
                        temp_turn++;
                        if(temp_turn == clientNum) temp_turn = 0;
                        out[temp_turn].writeChar('M'); //start

                        //Win

                        //chow
                        if(temp_turn == turn + 1 || temp_turn == turn + 1 - clientNum){
                            out[temp_turn].writeChar('c');
                            for(int j = 0; j < 3; j++){
                                if(result[j])
                                    out[temp_turn].writeChar('y');
                                else
                                    out[temp_turn].writeChar('n');
                            }
                        }
                        else{
                            out[temp_turn].writeChar('n');
                        }

                        //pong
                        if(who_pong == temp_turn)
                            out[temp_turn].writeChar('y');
                        else
                            out[temp_turn].writeChar('n');

                        //kong
                        if(who_kong == temp_turn)
                            out[temp_turn].writeChar('y');
                        else
                            out[temp_turn].writeChar('n');
                    }

                    //Go to options
                    state = 'O';
                    continue;
                }

                else if(state == 'O'){
                    System.out.println("O");
                    boolean operation = false;
                    int temp_turn = turn;
                    char temp_cmd = 'n';
                    if(who_pong != -1){
                        //Send card
                        out[temp_turn].writeUTF(pool.get(pool.size() - 1));
                        out[who_pong].writeChar('C');
                        temp_cmd = in[who_pong].readChar();
                    }

                    for(int i = 0; i < clientNum - 1; i++){

                        temp_turn++;
                        if(temp_turn == clientNum) temp_turn = 0;
                        if(temp_turn == who_pong) continue;

                        //Send card
                        out[temp_turn].writeUTF(pool.get(pool.size() - 1));


                        //No movement
                        temp_cmd = in[temp_turn].readChar();
                        if(temp_cmd == 'N') continue;
                        else if(temp_cmd == '1'){
                            chow(temp_turn, temp_cmd);
                            operation = true;
                            System.out.println("Messenge recieve");
                            break;
                        }
                        else if(temp_cmd == '2'){
                            chow(temp_turn, temp_cmd);
                            operation = true;
                            System.out.println("Messenge recieve");
                            break;
                        }
                        else if(temp_cmd == '3'){
                            chow(temp_turn, temp_cmd);
                            operation = true;
                            System.out.println("Messenge recieve");
                            break;
                        }
                        else if(temp_cmd == 'P'){
                            pong(temp_turn);
                            operation = true;
                            System.out.println("Messenge recieve");
                            break;
                        }
                        else if(temp_cmd == 'K'){
                            kong(temp_turn);
                            operation = true;
                            System.out.println("Messenge recieve");
                            break;
                        }
                        else if(temp_cmd == 'W'){
                            //WIN
                            operation = true;
                            System.out.println("Messenge recieve");
                            break;
                        }
                    }

                    if(!operation){
                        turn++;
                        draw(players[turn], 1);
                    }
                    else{
                        turn = temp_turn;
                    }
                    if(turn == clientNum) turn = 0;
                    state = 'T';
                    System.out.println("Messenge recieve");
                    continue;
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

    // Initialize of the server
    public static void serIni(){

        // Assignment of Players
        for(int i = 0; i < clientNum; i++)
            players[i] = new Player();
    }


    // Initialize of the board
    public static void boardIni(){
        
    }


    // Game Start
    public static void gameStart(){
        
        // Shuffled stack & empty pool
        board.newGame();    

        // Deal cards
        for(int i = 0; i < clientNum; i++)
            players[i].dealedCard(board.cardDeal());

        // Send cards to client

        for(int i = 0; i < clientNum; i++)
            players[i].sortCards();

        // Send cards to client

        // Flower off
        
        

    }


    /* Movement with cards */

    //Take out flower 
    public static void flowerOff(Player player){
        for(int i = 0; i < 16; i++)
            if(player.hands[i].sort == 'F'){
                do{
                    player.consume.add(new Card (player.hands[i]));
                    swapCard(player.hands, i, 16);
                    draw(player, 1);
                    swapCard(player.hands, i, 16);
                }while(player.hands[i].sort == 'F');
            }
        return;
    }
    
    //Draw card(s)
    public static void draw(Player player, int num){

        if(num == 1){
            player.hands[16] = new Card(cards.get(next));
            next++;
            while(player.hands[16].sort == 'F'){
                player.consume.add(new Card (player.hands[16]));
                player.hands[16] = new Card(cards.get(next));
                next++;
            }
        }
        else if(num == 16){
            for(int i = 0; i < 16; i++){
                player.hands[i] = new Card(cards.get(next));
                next++;
            }      
        }
        return;
    }

    //Discard
    public static void discard(int player){
        int temp = 0;
        try{
            for(int i = 0; i < clientNum; i++){
                if(player != i) out[i].writeChar('N');
                else if(player == i) out[i].writeChar('D');
            }
            temp = in[player].readInt();
        } catch(IOException e){
            e.printStackTrace();
        }
        
        pool.add(new Card(players[player].hands[temp]).str_generate());
        players[player].hands[temp] = new Card(empty);
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

    //Check if is available to PONG or KONG, 0 for no, 1 for Pong, 2 for Kong and Pong
    public static int checkPong(int player){
        int count = 0;
		for (int i = 0; i < 16; i++)
			if (pool.get(pool.size() - 1).equals(players[player].hands[i]))
                count += 1;
        
        if(count == 3) return 2;
        else if(count == 2) return 3;
        return 0;
    }

    //Check if available to CHOW
    public static boolean[] ckeckChow(int player){
        boolean[] result = new boolean[3];
        for(int i = 0; i < 3; i++)
            result[i] = false;
        char sort = pool.get(pool.size() - 1).charAt(0);
        char value = pool.get(pool.size() - 1).charAt(1);
        boolean a = false;
        boolean b = false;
        boolean c = false;
        boolean d = false;
        for(int i = 0; i < 16; i++){
            if(players[player].hands[i].sort == sort){
                char val = players[player].hands[i].val;
                if(value == val - 2) a = true;
                else if(value == val - 1) b = true;
                else if(value == val + 1) c = true;
                else if(value == val + 2) d = true;
            }
        }
        if(a && b) result[0] = true;
        if(b && c) result[1] = true;
        if(c && d) result[2] = true;
        return result;
    }

    //Pong
    public static void pong(int player){
        int count = 0;
        String last_card = pool.get(pool.size() - 1);
        for(int i = 0; i < 16; i++){
            if(players[player].hands[i].str_generate().equals(last_card)){
                players[player].consume.add(players[player].hands[i]);
                players[player].hands[i] = new Card(empty);
                count++;
            }
            if(count == 2) break;
        }
        sortCards(players[player].hands);
    }

    //Kong
    public static void kong(int player){
        int count = 0;
        String last_card = pool.get(pool.size() - 1);
        for(int i = 0; i < 16; i++){
            if(players[player].hands[i].str_generate().equals(last_card)){
                players[player].consume.add(players[player].hands[i]);
                players[player].hands[i] = new Card(empty);
                count++;
            }
            if(count == 3) break;
        }
        players[player].consume.add(new Card(last_card));
        pool.remove(pool.size()-1);
        sortCards(players[player].hands);
    }

    //Chow
    public  static void chow(int player, char set){
        String last_card = pool.get(pool.size() - 1);
        boolean first = false;
        if(set == '1'){
            for(int i = 0; i < 16; i++){
                if(players[player].hands[i].sort == last_card.charAt(0) && players[player].hands[i].val == last_card.charAt(1) - 2 && !first){
                    players[player].consume.add(players[player].hands[i]);
                    players[player].hands[i] = new Card(empty);
                    players[player].consume.add(new Card(last_card));
                    first = true;
                }
                else if(players[player].hands[i].sort == last_card.charAt(0) && players[player].hands[i].val == last_card.charAt(1) - 1){
                    players[player].consume.add(players[player].hands[i]);
                    players[player].hands[i] = new Card(empty);
                    break;
                }
            }
            pool.remove(pool.size()-1);
            sortCards(players[player].hands);
        }
        else if(set == '2'){
            for(int i = 0; i < 16; i++){
                if(players[player].hands[i].sort == last_card.charAt(0) && players[player].hands[i].val == last_card.charAt(1) - 1 && !first){
                    players[player].consume.add(players[player].hands[i]);
                    players[player].hands[i] = new Card(empty);
                    players[player].consume.add(new Card(last_card));
                    first = true;
                }
                else if(players[player].hands[i].sort == last_card.charAt(0) && players[player].hands[i].val == last_card.charAt(1) + 1){
                    players[player].consume.add(players[player].hands[i]);
                    players[player].hands[i] = new Card(empty);
                    break;
                }
            }
            pool.remove(pool.size()-1);
            sortCards(players[player].hands);
        }
        else if(set == '3'){
            for(int i = 0; i < 16; i++){
                if(players[player].hands[i].sort == last_card.charAt(0) && players[player].hands[i].val == last_card.charAt(1) + 1 && !first){
                    players[player].consume.add(players[player].hands[i]);
                    players[player].hands[i] = new Card(empty);
                    players[player].consume.add(new Card(last_card));
                    first = true;
                }
                else if(players[player].hands[i].sort == last_card.charAt(0) && players[player].hands[i].val == last_card.charAt(1) + 2){
                    players[player].consume.add(players[player].hands[i]);
                    players[player].hands[i] = new Card(empty);
                    break;
                }
            }
            pool.remove(pool.size()-1);
            sortCards(players[player].hands);
        }
    }
}

