import java.util.Random;
import java.util.Scanner;

class blackjack {
    /* I'm going to make blackjack (this is so ambitious what am I thinking?)
     * Sections I'll need to make:
     * 1. code to set up the game
     *   a. get the player's name (DONE)
     *   b. generate the deck (see section 2) (DONE)
     *   c. give the player starting funds (DONE)
     * 2. code to make the deck (DONE)
     *   a. code to generate a deck of cards as an array (DONE)
     *   b. code to shuffle and return the array (DONE)
     * 3. code to run the game
     *   a. code to begin the round
     *     a1. each player places a bet
     *     a2. 2 rounds of dealing, so that the player and the dealer each have 2 cards
     *     a3. deal cards starting with the player
     *     a4. the second card the dealer gets is face down
     *   b. code for player's actions
     *     b1. "hit" - get another card
     *     b2. "stand" - end turn
     *     b3. if card total is > 21, the player loses (see section 3d)
     *     b4. other actions likely won't be added
     *   c. code for dealer's actions
     *     c1. dealer must hit if card total is < 17 and stand otherwise
     *     c2. dealer must count ace as 11 if it would bring its total to at least 17 but not over 21 (see section 3d)
     *   d. code for counting totals
     *     d1. number cards are scored as their value
     *     d2. face cards all count as 10
     *     d3. aces are 11 in this version
     *     d4. if the player's score is greater than 21, they bust for the round
     *   e. code for awarding points
     *     e1. After the dealer plays, if player the has a greater total than the dealer, they pay what they bet
     *     e2. If the player wins by a certain margin, give the player a bit more I
     *     e3. If the player has exactly 21 and wins, they get more
     *     e4. If the dealer busts, then the player gets what they bet
     * 4. ending the game
     *   a. game ends when the player puts in the correct input or runs out of money
     *   b. print out final scores
    */
    static String playerName;
    static int credits;
    static String thinLine = "---------------------------";
    static String thickLine = "===========================";

    static int spendings = 0;
    static int earnings = 0;
    static int bet;

    static int playerTotal = 0;
    static int dealerTotal = 0;

    static String[] playerCards = {"", "", "", "", "", "", "", "", "", "", "",};
    static String[] dealerCards = {"", "", "", "", "", "", "", "", "", "", "",};
    static String[] deck;

    static int cardPosition;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        deck = makeDeck();
        setup();
        boolean again = true;
        while (again) {
            playRound();
        }
        
    }
    
    // Methods for Setup

    // Initialize game
    // Get player name, give starting credits, explain gameplay
    public static void setup() {
        System.out.println(thickLine);
        System.out.println("Welcome to Blackjack Lite!\n");
        System.out.println(thickLine);
        
        System.out.print("What is your name? ");
        playerName = scanner.next();
        
        System.out.println();

        credits = 15;
        System.out.println("Hello " + playerName + "! you'll start with " + credits + " credits, use them wisely");
    }

    // Methods to print rules
    public static void printRules() {
        System.out.println(thinLine);
        System.out.println("The objective of Blackjack is to build a hand worth more than the dealers hand without exceding 21.\nOn your turn you can choose to \"hit\" and draw a card,\nor \"stand\" and end your turn");
        System.out.println("Number cards are worth their number\nFace cards are worth 10\nIn this version specifically Aces only count as 11");
    }

    // Method to print and get player actions
    public static String actions() {
        System.out.println(thinLine);
        System.out.println("Enter the name of an action to perform that action");
        System.out.println("Hit: draw a card (can be taken multiple times)");
        System.out.println("Stand: end your turn and move to the dealer\n");
        
        System.out.print("What would you like to do? ");
        return scanner.next();
    }

    // Method to create a deck of cards as an array
    // remember the poker game we made last semester
    // start with an array of faces and an array of values
    // use nested for loops to make the deck array, which gets returned
    public static String[] makeDeck() {
        String[] cardValues = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        String[] cardFaces = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] deck = new String[52];

        int cardNum = 0; // iterator for adding cards to the deck array 

        for (String value : cardValues) {
            for (String face : cardFaces) {
                deck[cardNum] = value + " of " + face;
                cardNum++;
            }
        }

        shuffleDeck(deck);
        cardPosition = 0;

        return deck;
    }

    // Method to shuffle the deck
    // use the fisher-yates shuffle method
    public static void shuffleDeck(String[] deck) {
        Random random = new Random();

        for (int i = deck.length - 1; i >= 0; i--) {
            // Generate a random index
            int j = random.nextInt(i + 1); // 0 <= j <= i
            
            // swap elements
            String temp = String.valueOf(deck[i]);
            deck[i] = String.valueOf(deck[j]);
            deck[j] = String.valueOf(temp);
        }
    }

    // Method to play a round of blackjack
    // if player to play again after the round, return true, otherwise return false
    public static void playRound() {
        printRules();
        makeBet();
        getStartingHands();
        printPlayerHand();
        printDealerHand();
        String action;
        while (true) {
            if (playerTotal > 21) {
                System.out.println("Your hand is too large! You've bust!");
                break;
            }

            action = actions();
            if (action.equalsIgnoreCase("hit")) {
                hit(playerCards, playerName);
            } else if (action.equalsIgnoreCase("stand")) {
                break;
            } else {
                System.out.print("Invalid Input!");
                action = actions();
            }
        }

        if (playerTotal <= 21) {
            while (dealerTotal < 17) {
                hit(dealerCards, "Dealer");
            }
        }
    }

    // Method to collect bet
    public static void makeBet() {
        System.out.println(thinLine);
        System.out.println("You have " + credits + "credits");
        System.out.print("How many credits would you like to bet? ");
        while (true) { 
            bet = scanner.nextInt();
            if (bet < 1) {
                System.out.print("Invalid bet, please try again. ");
                bet = scanner.nextInt();
            }
            break;
        }
        

        System.out.println("\nBetting " + bet + " credits");
        credits -= bet;
        spendings += bet;
    }

    // method to hit (gain a card)
    // personHand is the array that holds that person's cards (dealer or player)
    // cardPosition is the variable which holds the position of the card
    public static void hit(String[] personHand, String person) {
        int spot = getNextSlot(personHand);
        personHand[spot] = deck[cardPosition];
        if (person.equals(playerName)) {
            System.out.println("You drew a(n) " + deck[cardPosition]);
            playerTotal += getValue(deck[cardPosition]);
            printPlayerHand();
        } else {
            System.out.println("Dealer drew a(n) " + deck[cardPosition]);
            dealerTotal += getValue(deck[cardPosition]); 
        }

        cardPosition++;
    }

    // method to get first empty index in array
    public static int getNextSlot(String[] a) {
        for (int i = 0; i < a.length; i++) {
            if (a[i].equals("")) {
                return i;
            }
        }

        return -1;
    }

    // method to get the value of the card
    public static int getValue(String card) {
        String value = card.substring(0,1);

        if (value.matches("\\d")) {
            int num = Integer.parseInt(value);
            if (num != 1) {
                return num;
            } else {
                return 10;
            }
        } else if (!value.equals("A")) {
            return 10;
        } else {
            return 11;
        }
    }

    public static void getStartingHands(){
        System.err.println(thinLine);
        hit(playerCards, playerName);
        hit(dealerCards, "Dealer");
        hit(playerCards, playerName);
        hit(dealerCards, "Dealer");
    }

    public static void printPlayerHand() {
        System.out.println(thinLine);
        System.out.print("Your hand: ");
        for (String card : playerCards) {
            if (!card.equals("")) {
                System.out.print(card + ", ");
            } else {
                break;
            }
        }
        System.out.print("Your total is " + playerTotal);
        System.out.println();
    }

    public static void printDealerHand() {
        System.out.println(thinLine);
        System.out.print("Dealer hand: ");
        for (String card : dealerCards) {
            if (!card.equals("")) {
                System.out.print(card + ", ");
            } else {
                break;
            }
        }
        System.out.print("Dealer total is " + dealerTotal);
        System.out.println();
    }
}