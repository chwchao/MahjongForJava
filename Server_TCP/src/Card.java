package ser;

public class Card{
    
    char sort = 'n';
    char val = 'n';

    public Card(){}

    public Card(String card){
        sort = card.charAt(0);
        val = card.charAt(1);
    }

    public void set_card(String card){
        System.out.println(card);
        sort = card.charAt(0);
        val = card.charAt(1);
    }

    public String str_generate(){
        return String.valueOf(sort) + String.valueOf(val);
    }

}