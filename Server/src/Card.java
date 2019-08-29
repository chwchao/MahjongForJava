package server;

public class Card{
    
    private char sort;
    private char val;

    /* Construct */

    // by default (default 'nn' -> none)
    public Card(){
        this.sort = 'n';
        this.sort = 'n';
    }

    // with a Card class
    public Card(Card card){
        this.sort = card.sort;
        this.val = card.val;
    }

    // with a String ( 'sort' + 'val' )
    public Card(String card){
        sort = card.charAt(0);
        val = card.charAt(1);
    }


    /* Methods */

    // Set card
    public void setCard(String card){
        sort = card.charAt(0);
        val = card.charAt(1);
    }

    // Get card
    public String getCard(){
        return String.valueOf(sort) + String.valueOf(val);
    }

    // Get sort
    public char getSort(){
        return this.sort;
    }

    // Get val
    public char getVal(){
        return this.val;
    }

}