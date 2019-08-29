package app;

import java.util.*;

public class Player{

    
    private Card[] hands = new Card[17];    // Cards in hand
    private ArrayList<String> consume = new ArrayList<String>();    // Cards comsumed or flowers

    // Constructor with empty hands
    public Player(){
        for(int i = 0; i < 17; i++)
            hands[i] = new Card();
    }

    // Set the nth card
    public void setHand(int n, Card card){
        this.hands[n] = card;
    }

    // Deal cards at start
    public void dealedCard(String[] cards) {
        for (int i = 0; i < 16; i++)
            hands[i].setCard(cards[i]);
    }


    /*** Sorting ***/

    // Sort hands in order
    public void sortCards(){
        // Bubble sort (put the smallest to front)
        for(int i = 0; i < 17; i++)
            for(int j = i; j < 17; j++){
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
    
    
    /*** Check Functions ***/

    // Check if is available to PONG or KONG, 0 for no, 1 for Pong, 2 for Kong and Pong
    public int checkPong(String card){
        int count = 0;
        for (int i = 0; i < 16; i++)
            if (card.equals(this.hands[i].getCard())) count += 1;

        if (count == 3) return 2;
        else if (count == 2) return 1;
        else return 0;
    }

    // Check if available to CHOW [ front, middle, rear ]
    public boolean[] checkChow(String card) {

        // Create a new array, default all false
        boolean[] result = new boolean[3];
        for (int i = 0; i < 3; i++) result[i] = false;
        
        // The card just discarded
        char sort = card.charAt(0);
        char value = card.charAt(1);

        // Check if nearest four cards exist (front*2, rear*2)
        boolean[] near = new boolean[4];
        for (int i = 0; i < 4; i++) near[i] = false;
        for (int i = 0; i < 16; i++)
            if (this.hands[i].getSort() == sort) {
                if (value == this.hands[i].getVal() + 2) near[0] = true;
                else if (value == this.hands[i].getVal() + 1) near[1] = true;
                else if (value == this.hands[i].getVal() - 1) near[2] = true;
                else if (value == this.hands[i].getVal() - 2) near[3] = true;
            }

        // Judge if combinations work
        if (near[0] && near[1]) result[0] = true;
        if (near[1] && near[2]) result[1] = true;
        if (near[2] && near[3]) result[2] = true;
        return result;
    }
    
    
    /*** Actions ***/

    // Has flower ( remove flower ), true if has-flower, false if not
    public boolean hasFlower() {
        for (int i = 0; i < 17; i++)
            if (hands[i].getSort() == 'F') {
                consume.add(hands[i].getCard());
                hands[i].setCard("nn");
                return true;
            }
        return false;
    }

    // Discard
    public String discard(int n) {

        String temp = hands[n].getCard();
        hands[n].setCard("nn");
        swapCard(n, 16);
        this.sortCards();
        return temp;
    }

    // Draw
    public void draw(String card) {
        hands[16].setCard(card);
    }

    // Pong
    public void pong(String card){
 	this.consume.add(card);
        int count = 0;
        for (int i = 0; i < 16; i++) {
            if (this.hands[i].getCard().equals(card)){
                this.consume.add(this.hands[i].getCard());
                this.hands[i] = new Card("nn");
                count++;
            }
            if (count == 2)
                break;
        }
        sortCards();
    }

    //Kong
    public void kong(String card){
        int count = 0;
        if(card.equals("nn")){
            count = -1;
            card = this.hands[16].getCard();
            this.hands[16] = new Card("nn");
        }
        else
            this.consume.add("nn");

        for (int i = 0; i < 16; i++) {
            if (this.hands[i].getCard().equals(card)) {
                this.consume.add(this.hands[i].getCard());
                this.hands[i] = new Card("nn");
                count++;
            }
            if (count == 3)
                break;
        }
        sortCards();
    }

    //Chow (1:front, 2:mid, 3:rear)
    public void chow(String card, int set) {
        boolean first = false;
        if(set == 1) {
            for (int i = 0; i < 16; i++) {
                if (this.hands[i].getSort() == card.charAt(0) && this.hands[i].getVal() == card.charAt(1) - 2 && !first) {
                    this.consume.add(this.hands[i].getCard());
                    this.hands[i] = new Card("nn");
                    this.consume.add(card);
                    first = true;
                } 
                else if (this.hands[i].getSort() == card.charAt(0) && this.hands[i].getVal() == card.charAt(1) - 1) {
                    this.consume.add(this.hands[i].getCard());
                    this.hands[i] = new Card("nn");
                    break;
                }
            }
        } 
        else if(set == 2) {
            for (int i = 0; i < 16; i++) {
                if(this.hands[i].getSort() == card.charAt(0) && this.hands[i].getVal() == card.charAt(1) - 1 && !first) {
                    this.consume.add(this.hands[i].getCard());
                    this.hands[i] = new Card("nn");
                    this.consume.add(card);
                    first = true;
                } 
                else if(this.hands[i].getSort() == card.charAt(0) && this.hands[i].getVal() == card.charAt(1) + 1) {
                    this.consume.add(this.hands[i].getCard());
                    this.hands[i] = new Card("nn");
                    break;
                }
            }
        } 
        else if(set == 3) {
            for (int i = 0; i < 16; i++) {
                if(this.hands[i].getSort() == card.charAt(0) && this.hands[i].getVal() == card.charAt(1) + 1 && !first) {
                    this.consume.add(this.hands[i].getCard());
                    this.hands[i] = new Card("nn");
                    this.consume.add(card);
                    first = true;
                } 
                else if(this.hands[i].getSort() == card.charAt(0) && this.hands[i].getVal() == card.charAt(1) + 2) {
                    this.consume.add(this.hands[i].getCard());
                    this.hands[i] = new Card("nn");
                    break;
                }
            }
        }
        sortCards();
    }


    /*** Get Functions ***/

    // Get String of hands
    public String getHand() {
        String hand = "";
        for (int i = 0; i < 17; i++) {
            hand += this.hands[i].getCard();
            hand += " ";
        }
        return hand;
    }

    // Get String of consumes
    public String getConsume() {
        String consume = "";
        for (int i = 0; i < this.consume.size(); i++) {
            consume += this.consume.get(i);
            consume += " ";
        }
        return consume;
    }
}

   
