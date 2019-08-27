package server;

import java.util.ArrayList;
import java.util.Collections;

public class Board{

    private ArrayList<String> stack;
    private ArrayList<String> pool;

    // Empty constructor
    public Board(){
        this.stack = new ArrayList<String>();
        this.pool = new ArrayList<String>();
    }

    // Start a new game with creating full shuffled stack and empty pool
    public void newGame(){
        // Empty pool
        this.pool = new ArrayList<String>();

        // Shuffled stack
        this.stack = new ArrayList<String>(
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
        Collections.shuffle(stack);
    }

    // Card draw, and removed
    public String cardDraw(){
        String temp = stack.get(0);
        stack.remove(0);
        return temp;
    }

    // Deal cards at start
    public String[] cardDeal(){
        String[] temp = new String[16];
        for(int i = 0; i < 16; i++)
            temp[i] = cardDraw();
        return temp;
    }
}