package server;

import java.util.*;

public class Player{

    
    private Card[] hands = new Card[17];    // Cards in hand
    private ArrayList<Card> consume = new ArrayList<Card>();    // Cards comsumed or flowers

    // Constructor with empty hands
    public Player(){
        for(int i = 0; i < 17; i++)
            hands[i] = new Card();
    }

    // Set the nth card
    public void setHand(int n, Card card){
        this.hands[n] = card;
    }

    // Sort hands in order
    public void sortCards(){
        // Bubble sort (put the smallest to front)
        for(int i = 0; i < 16; i++)
            for(int j = i; j < 16; j++){
                // sort first
                if(this.hands[i].getSort() > this.hands[j].getSort()) 
                    swapCard(i, j);
                // then val
                else if(this.hands[i].getSort() == this.hands[j].getSort() && this.hands[i].getVal() > this.hands[j].getVal()) 
                    swapCard(i, j);
            }
    }

    // Swap two card
    public void swapCard(int a, int b){
        String temp = hands[a].getCard();
        hands[a].setCard(hands[b].getCard());;
        hands[b].setCard(temp);;
        return;
    }

    // Deal cards at start
    public void dealedCard(String[] cards){
        for(int i = 0; i < 16; i++)
            hands[i].setCard(cards[i]);
    }

}