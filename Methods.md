<h3>洗牌&發牌</h3>
<blockquote><pre>
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
    </pre></blockquote>

<h3>換牌</h3>
<blockquote><pre>
    public static void swapCard(Card[] hands, int a, int b){
        Card temp1 = new Card(hands[a]);
        Card temp2 = new Card(hands[b]);
        hands[a] = temp2;
        hands[b] = temp1;
        return;
    }
    </pre></blockquote>
    
<h3>換牌</h3>
<blockquote><pre>
    public static void handSend(Card[] player, DataOutputStream out){
        String tmp = player[0].str_generate() + " ";
        for(int i = 1; i < 16; i++)
            tmp += (player[i].str_generate() + " ")
        try{
            out.writeUTF(tmp);
        } catch(IOException e){
            System.out.println("ERROR: Sending hands problem");
            e.printStackTrace();
        }
    }
    </pre></blockquote>
