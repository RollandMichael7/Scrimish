import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Driver class for playing the card game Scrimish. Supports both console I/O version and simple text-based GUI version.
 * The console I/O version is older, repetitive code that probably needs to be cleaned up but the GUI code does everything
 * it does, only better.
 * 
 * Info about the game Scrimish: http://www.scrimish.com/
 * 
 * @author Michael Rolland
 * @version 2017.10.10
 *
 */
public class Scrimish {
	public static void main(String[] args) {
		String input;
		
		ScrimishFrame game = new ScrimishFrame("Scrimish");
		game.addTextln("Welcome to Scrimish! At any time, type \"quit\" to exit.");
		game.addTextln("Enter name: ");
		input = game.getInput();
		//game.addText(s);
		Player p1 = new Player(input);
		
		game.addTextln("You have 5 Daggers, 5 Swords, 3 Morning Stars, \n"+
		"3 War Axes, 2 Halberds, 2 Longswords, 2 Archers, 2 Shields\nand "+
		"1 Crown. The crown MUST be at the BOTTOM of a deck!\nPress enter "
		+ "to begin building your decks or enter \"help\" to read the rules.");
		input = game.getInput();
		if (input.toUpperCase().equals("HELP")) {
			game.addTextln("There are nine (9) kinds of cards in Scrimish. The first 6 have an attack level:"
					+ "\n Daggers have 1. \n Swords have 2. \n Morning Stars have 3. \n War Axes have 4. \n Halberds "
					+ "have 5. \n Longswords have 6. \n Press enter to continue.");
			input = game.getInput();
			game.addTextln("In addition to the six basic cards, there are three special cards."
					+ "\n The Archer always wins when it attacks, but is always defeated when attacked. "
					+ "\n The Shield can not attack, but when it is attacked both cards are destroyed. "
					+ "\n When a player's Crown is attacked, they lose. A Crown may be used to attack, "
					+ "but it loses to everything that is not a Crown. \n Press enter to continue.");
			input = game.getInput();
			game.addTextln("The rules of Scrimish are simple. A player has five decks of five cards each."
					+ "\n When attacking, a player chooses which deck they will attack and which deck they will attack with."
					+ "\n The top card of each deck are compared; the card with the highest attack wins, taking into account "
					+ "the rules of special cards. \n Additionally, a player may choose to discard a card instead of attacking."
					+ "\n Once a player's crown is defeated, the game is over. \n Press enter to begin building your deck.");
			input = game.getInput();
			game.addTextln("You have 5 Daggers, 5 Swords, 3 Morning Stars, \n"+
					"3 War Axes, 2 Halberds, 2 Longswords, 2 Archers, 2 Shields\nand "+
					"1 Crown. The crown MUST be at the BOTTOM of a deck!");
		}
		
		int choice=0;
		int dNum=0;
		int count=0;
		boolean finished = false;
		while (!finished) {
			game.addTextln("Pick five cards to put in deck "+(dNum-1)+" (Top first)");
			
			//Display options
			game.addTextln(" 1 for Dagger \n 2 for Sword \n 3 for Morning Star \n 4 for War Axe "
					+ "\n 5 for Halberd \n 6 for Longsword \n 7 for Archer \n 8 for Shield"
					+ "\n 9 for Crown \n OR enter 0 to display all your decks \n OR enter -1 to randomize all 5 decks:");
			for (int i=0; i<5; i++) {
				// getNumInput checks for bad input
				choice = getNumInput(game);
				
				// If -1 is entered, randomize decks and stop deck building.
				if (choice == -1) {
					p1.makeRandomDecks();
					dNum = 5;
					count = 5;
					break;
				}
				// If 0 is entered, display decks and continue taking input.
				else if (choice == 0) {
					p1.displayDecks(game);
					i--;
					continue;
				}
				// Else, check if there's any of the chosen card left, and add it to the current deck if so.
				if (p1.canAdd(choice)) {
					// If a crown is entered, make sure it is at the bottom.
					if (choice==9 && count!=4) {
						//If it's not, retry.
						game.addTextln("The Crown must be at the bottom!");
						i--;
						continue;
					}
					// If a card is successfully added, increment to the next place in the deck.
					p1.addCard(dNum, count, new Card(choice), choice);
					count++;
				// If there's none of the card left, retry.
				} else {
					game.addTextln("No more of that card left!");
					i--;
				}
			}
			
			if (count > 4) { // Once 5 cards have been added,
				count=0; // Reset card position counter
				dNum++; // Increment to next deck
				if (dNum>4) { // If all decks are filled,
					p1.displayDecks(game); // Display them
					finished = true; // and begin gameplay.
				}
				else { // If there are decks left, 
					p1.displayDeck(game, dNum); // display last filled deck
					game.addTextln("Deck "+(dNum+1)+":"); // and begin filling the next
				}
			}
		}
		
		// Once the decks are filled,
		
		// Create computer player
		// Default constructor randomizes decks
		Player cpu = new Player();
		finished = false;
		int result=0, cpuDeck=0;
		
		// Begin game
		while (!finished) {
			// Prompt player to enter deck to attack with/deck to attack, or to discard.
			game.addTextln("\nPick a deck (1-5) to attack with and which deck (1-5) to attack "
					+ "\nOR enter 0 to display decks "
					+ "\nOR enter -1 and a deck number to discard a card:");
			choice = getNumInput(game);
			//Check for invalid choice.
			if (choice!=0 && choice !=-1 && (choice>5 || choice<1)) {
				game.addTextln("Invalid choice.");
				continue;
			}
			// If player enters 0, display and skip computer's turn.
			else if (choice==0) {
				p1.displayDecks(game);
				continue;
			}
			
			cpuDeck = getNumInput(game);
			if(cpuDeck>5 || cpuDeck<1) {
				game.addTextln("Invalid choice.");
				continue;
			}
			
			// If player entered -1 for choice, try to discard top card of deck cpuDeck.
			if (choice== -1) {
				game.addTextln("You attempt to discard your "+p1.getTopCard(cpuDeck)+" in deck "+cpuDeck+"...");
				if (p1.discard(cpuDeck))
					game.addTextln("Discard successful.\n");
				else {
					game.addTextln("Your deck is empty or you attempted to discard a Crown.");
					continue;
				}
				result = computerAttack(game, p1, cpu);
				if (result == -5) // Result -5 means a crown was defeated, and the game is over.
					break;
				continue;
			}
			
			// If player entered two valid (1-5) deck numbers, attack with the first deck against the second.
			game.addTextln("You attack the computer's "+toOrdinal(cpuDeck)+" deck with your "+toOrdinal(choice)+" deck!\n");
			result = p1.attack(cpu, choice, cpuDeck, game);
			
			// Result -2 means the player attempted to attack with a shield, and is allowed to retry.
			if (result == -2)
				continue;
			else if (result == -5)
				break;
			
			result = computerAttack(game, p1, cpu);
			if (result == -5)
				break;
		}
	}
	
	
	
	/**
	 * Old implementation of console I/O version. Renamed from main() to play().
	 * @param args
	 */
	public static void play(String[] args) {
		Scanner in = new Scanner(System.in);
		int choice=1337; //Initialized to an arbitrary number to prevent infinite loops
		
		System.out.println("Welcome to Scrimish!");
		System.out.print("Enter name: ");
		String name=in.nextLine();
		Player p1 = new Player(name);
		
		//Display the amount of cards that a player is given in Scrimish.
		System.out.println("You have 5 Daggers, 5 Swords, 3 Morning Stars, \n"+
		"3 War Axes, 2 Halberds, 2 Longswords, 2 Archers, 2 Shields\nand "+
		"1 Crown. The crown MUST be at the BOTTOM of a deck!\nPress enter "
		+ "to begin building your decks or enter \"help\" to read the rules.");
		
		//If the player requests the rules, display them.
		name=in.nextLine();
		if (name.toUpperCase().equals("HELP")) {
			System.out.println("There are nine (9) kinds of cards in Scrimish. The first 6 have an attack level:"
					+ "\n Daggers have 1. \n Swords have 2. \n Morning Stars have 3. \n War Axes have 4. \n Halberds "
					+ "have 5. \n Longswords have 6. \n Press enter to continue.");
			name=in.nextLine();
			System.out.println("In addition to the six basic cards, there are three special cards."
					+ "\n The Archer always wins when it attacks, but is always defeated when attacked. "
					+ "\n The Shield can not attack, but when it is attacked both cards are destroyed. "
					+ "\n When a player's Crown is attacked, they lose. A Crown may be used to attack, "
					+ "but it loses to everything that is not a Crown. \n Press enter to continue.");
			name=in.nextLine();
			System.out.println("The rules of Scrimish are simple. A player has five decks of five cards each."
					+ "\n When attacking, a player chooses which deck they will attack and which deck they will attack with."
					+ "\n The top card of each deck are compared; the card with the highest attack wins, taking into account "
					+ "the rules of special cards. \n Additionally, a player may choose to discard a card instead of attacking."
					+ "\n Once a player's crown is defeated, the game is over. \n Press enter to begin building your deck.");
			name=in.nextLine();
			System.out.println("You have 5 Daggers, 5 Swords, 3 Morning Stars, \n"+
					"3 War Axes, 2 Halberds, 2 Longswords, 2 Archers, 2 Shields\nand "+
					"1 Crown. The crown MUST be at the BOTTOM of a deck!");
		}
    boolean finished=false;
		int dNum=0;
		int count=0;
		
		//Begin deck-building
		while (!finished) {
			System.out.println("Pick five cards to put in deck "+(dNum+1)+" (Top first):");
			
			//Display menu of options
			System.out.print(" 1 for Dagger \n 2 for Sword \n 3 for Morning Star \n 4 for War Axe \n 5 for Halberd \n 6 for Longsword \n 7 for Archer \n 8 for Shield"
					+ "\n 9 for Crown \n OR enter 0 to display all your decks \n OR enter -1 to randomize all 5 decks:"); 
			
			//Take five next inputs as cards for deck
			for (int i=0; i<5; i++) {
				
				//Catch bad input
				while (true) {
					try {
						choice = in.nextInt();
						break;
					}
					catch (InputMismatchException e) { 
						System.out.println("Bad input!");
						in.next();
					}
				}
				
				//If -1 is entered, randomize decks and stop prompting to enter cards.
				if (choice==-1) {
					p1.makeRandomDecks();
					dNum=5;
					count=5;
					break;
				}
				
				//If 0 is entered, display decks and continue taking input.
				if (choice==0) {
					p1.displayDecks();
					i--;
					continue;
				}
				
				//Else, check if there is any of the chosen card left, then add it to current deck if there is.
				if (p1.canAdd(choice)) {
					//If a crown is entered, check if it is at the bottom.
					if (choice==9 && count!=4) {
						//If the crown is not at the bottom, retry.
						System.out.println("The Crown must be at the bottom!");
						i--;
						continue;
					}
					//If a card is successfully added, increment to next position in deck.
					p1.addCard(dNum, count, new Card(choice), choice);
					count++;
				//If there's none of the card left, retry.
				} else {
					System.out.println("No more of that card left!");
					i--;
				}
			}
			
			if (count>4) { //Once 5 cards have been added,
				count=0; //Reset card position counter
				dNum++; //Increment to next deck
				if (dNum>4) { //Check if all decks are filled, and display them
					p1.displayDecks();
					finished=true;
				}
				else { //If there are decks left, display filled deck and start filling next one
					p1.displayDeck(dNum);
					System.out.print("Deck "+(dNum+1)+": ");
				}
			}
		}
		//Once the decks are filled,
		
		//Create computer player
		Player cpu = new Player();
		finished=false; 
		int result=0, cpuDeck=1337;
		
		//Begin game
		while (!finished) {
			//cpu.displayDecks(); //Statement for testing code.
			
			//Prompt user to enter deck to attack with/deck to attack, display their decks, or discard a card.
			System.out.println("\nPick a deck (1-5) to attack with and which deck (1-5) to attack \nOR enter 0 to display decks \nOR enter -1 and a deck number to discard a card: ");
			
			//Catch bad input
			while (true) {
				try {
					choice = in.nextInt();
					break;
				}
				catch (InputMismatchException e) { 
					System.out.println("Bad input!");
					in.next();
				}
			}
			
			//Check for invalid choice.
			if (choice!=0 && choice !=-1 && (choice>5 || choice<1)) {
				System.out.println("Invalid choice.");
				continue;
			}
			
			//If player enters 0, display their decks and skip computer's turn.
			if (choice==0) {
				p1.displayDecks();
				continue;
			}
			
			//Catch bad input
			while (true) {
				try {
					cpuDeck = in.nextInt();
					break;
				}
				catch (InputMismatchException e) { 
					System.out.println("Bad input!");
					in.next();
				}
			}
			
			//Check for invalid choice.
			if (cpuDeck>5 || cpuDeck<1) {
				System.out.println("Invalid choice.");
				continue;
			}
			
			//If player entered -1 first, then discard card on top of the deck entered after; then it's the computer's turn.
			if (choice== -1) {
				System.out.println("You attempt to discard your "+p1.getTopCard(cpuDeck)+" in deck "+cpuDeck+"...");
				if(p1.discard(cpuDeck))
					System.out.println("Discard successful.\n");
				else {
					System.out.println("Your deck is empty or you attempted to discard a Crown.");
					continue;
				}
					
				System.out.println("The computer attacks...");
				choice = (int)(Math.random()*5+1); cpuDeck=(int)(Math.random()*5+1);
				System.out.println("It attacks your "+toOrdinal(cpuDeck)+" deck with its "+toOrdinal(choice)+" deck!\n");
				result = cpu.attack(p1, choice, cpuDeck);
				if (result == -5) 
					break;
				continue;
			}
			
			//If player entered two valid (1-5) deck numbers, use the first deck to attack the enemy's deck of the second number.
			System.out.println("You attack the computer's "+toOrdinal(cpuDeck)+" deck with your "+toOrdinal(choice)+" deck!\n");
			result = p1.attack(cpu, choice, cpuDeck);
			
			//If the attack method returns -2, the player attempted to attack with a shield. The player retries.
			if (result==-2)
				continue;
			
			//If the attack method returns -5, a Crown has been defeated and the game is over.
			if (result == -5)
				break;
			
			//Computer's turn. The computer chooses a random deck to attack and a random deck to attack with.
			System.out.println("The computer attacks...");
			choice = (int)(Math.random()*5+1); cpuDeck=(int)(Math.random()*5+1);
			System.out.println("It attacks your "+toOrdinal(cpuDeck)+" deck with its "+toOrdinal(choice)+" deck!\n");
			result = cpu.attack(p1, choice, cpuDeck);
			if (result == -5) 
				break;
		}
	}
	
	/**
	 * Method for converting input string from ScrimishFrame into an int 
	 * and returning it, including handling exceptions.
	 */
	public static int getNumInput(ScrimishFrame game) {
		int n;
		while (true) {
			try {
				n = Integer.valueOf(game.getInput());
				break;
			}
			catch (NumberFormatException e) {
				game.addTextln("Bad input!");
			}
		}
		return n;
	}
	
	/**
	 * This method is used just to make the game more readable. Converts 1 to first, 2 to second, etc. Only goes up
	 * to five since the game only has five decks with five cards. (Driver method ensures nothing <1 or >5 is ever passed.)
	 * @param n Cardinal number
	 * @return Ordinal string
	 */
	public static String toOrdinal(int n) {
		if (n==1)
			return "first";
		if (n==2)
			return "second";
		if (n==3)
			return "third";
		if (n==4)
			return "fourth";
		if (n==5)
			return "fifth";
		else
			return null;
	}
	
	/**
	 * Method for the computer's turn; it randomly chooses a deck to attack and a deck to attack with.
	 */
	public static int computerAttack(ScrimishFrame game, Player user, Player cpu) {
		int atkWith, atk;
		game.addTextln("The computer attacks...");
		atkWith = (int)(Math.random()*5 +1);
		atk = (int)(Math.random()*5 +1);
		game.addTextln("It attacks your "+toOrdinal(atk)+" deck with its "+toOrdinal(atkWith)+" deck!");
		// The attack method prints the cards involved in the battle and returns an int representing the result.
		return cpu.attack(user, atkWith, atk, game);
	}
}
