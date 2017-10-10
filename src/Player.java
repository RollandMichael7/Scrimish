/**
 * A class for representing players of the game Scrimish, with functionality for building decks and attacking other players.
 * Includes methods for working with java console I/O and the GUI of the class ScrimishFrame.
 * 
 * Info about the game Scrimish: http://www.scrimish.com/
 * 
 * @author Michael Rolland
 * @version 2017.10.10
 *
 */
public class Player {
	static int numCPUs=1;
	String name;
	private Card[] deck1 = new Card[5];
	private Card[] deck2 = new Card[5];
	private Card[] deck3 = new Card[5];
	private Card[] deck4 = new Card[5];
	private Card[] deck5 = new Card[5];
	private Card[][] decks={deck1, deck2, deck3, deck4, deck5};
	
	public Card[][] getDecks() {
		return this.decks;
	}
	//Array used by makeRandomDecks()
	Card[] cards = {new Card(1),new Card(1),new Card(1),new Card(1),new Card(1),new Card(2),new Card(2),new Card(2), 
			new Card(2),new Card(2),new Card(3),new Card(3),new Card(3),new Card(4),new Card(4),new Card(4),		
			new Card(5),new Card(5),new Card(6),new Card(6),new Card(7),new Card(7),new Card(8),new Card(8), new Card(9)};
	
	//Initial amount of each card
	int DCount=5, SCount=5, MSCount=3, WACount=3, HCount=2, LSCount=2, ACount=2, ShCount=2, CCount=1;
	
	
	/**
	 * The constructor with a String parameter is used to create a human player. Before being able to play, a
	 * human player must call the needed methods to create five decks of five cards. 
	 * @param name The player's name.
	 */
	Player(String name) {
		this.name=name;
	}
	
	/**
	 * The default constructor is used to create a computer player. Its five decks
	 * are randomized using the makeRandomDecks() method, and its name is "Computer" plus
	 * the value of the static variable numCPUs, which starts at 1. numCPUs is then incremented.
	 */
	Player() {
		this.name="Computer"+numCPUs;
		numCPUs++;
		this.makeRandomDecks();
	}
	
	/**
	 * The toString method is overridden to display a Player's name.
	 */
	public String toString() {
		return this.name;
	}
	
	/**
	 * This method discards the top card of player's deck, if it's possible. A player
	 * can not discard from an empty deck or discard a Crown.
	 * @param dNum The deck number (1-5)
	 * @return A boolean representing if a card was discarded successfully.
	 */
	public boolean discard(int dNum) {
		for (int i=0; i<5; i++) { //Find top card of player's deck and discard it
			if (this.decks[dNum-1][i]!=null && !this.decks[dNum-1][i].isCrown) {
				this.decks[dNum-1][i]=null;
				return true;
			} if (i==4)
				return false;
		}
		return false;
	}
	
	/**
	 * This method "attacks" another player and prints the outcome of the "battle". An attack
	 * is carried out by comparing the attack values of the two cards battling each other; 
	 * the one with the higher attack wins. Additionally, an archer always wins when attacking,
	 * and always loses when attacked. A shield can not attack but destroys itself and whatever 
	 * it is attacked by, unless it is an archer in which case both cards are returned.
	 * If a crown is attacked, the game is over.
	 * @param p The player being attacked.
	 * @param ownDeck The deck number (1-5) which the player is attacking with.
	 * @param enemyDeck The deck of the enemy's (1-5) that is being attacked.
	 * @return An integer representing the outcome. 0 means nothing happened, either from bad input
	 * or a tie. 1 means the attacking player has won. -1 means the attacked player has won. -2 means the player
	 * attempted to attack with a shield. -3 means both cards were destroyed.-5 means a Crown has been defeated 
	 * and the game is over. As it is now, the method itself prints the outcome; however the method could be 
	 * changed and a driver could be made that interprets different return values to print its own statements.
	 */
	public int attack(Player p, int ownDeck, int enemyDeck) { 
		Card c1=null; Card c2=null;
		int index1=0, index2=0;
		
		//Find top card of player's deck
		for (int i=0; i<5; i++) {
			if (this.decks[ownDeck-1][i]!=null) {
				c1=this.decks[ownDeck-1][i];
				index1=i;
				break;
			}
			//If deck is empty, return 0.
			if (i==4) {
				System.out.println(this+"'s deck is empty!");
				return 0;
			}
		}
		//Find top card of enemy's deck
		for (int i=0; i<5; i++) {
			if (p.decks[enemyDeck-1][i]!=null) {
				c2=p.decks[enemyDeck-1][i];
				index2=i;
				break;
			}
			//If deck is empty, return 0.
			if (i==4) {
				System.out.println(p+"'s deck is empty!");
				return 0;
			}
		}
		
		//Shield can not attack
		if (c1.isShield) {
			System.out.println("You can't attack with a shield!");
			return -2;
		}	
		//If the crown is attacked, game over. The driver must interpret -5 as "game over" value.
		else if (c1.attack>=c2.attack && c2.isCrown) {
			System.out.println(p+"'s Crown is defeated! Game over!");
			return -5;
		} else if (c2.attack>=c1.attack && c1.isCrown) {
			System.out.println(this+"'s Crown is defeated! Game over!");
			return -5;
		}
		//Archer and shield tie, return to deck.
		else if ((c1.isArcher && c2.isShield) || (c1.isShield && c2.isArcher)) {
			System.out.println("Archer and Shield are returned to their decks.");
			return 0;
		} 
		//If shield is attacked, both are discarded.
		else if (c2.isShield) {
			System.out.println(p+"'s Shield in deck "+enemyDeck+" and "+this+"'s "+c1+" in deck "+ownDeck+" are discarded!");
			this.decks[ownDeck-1][index1]=null;
			this.decks[enemyDeck-1][index2]=null;
			return -3;
		}
		//Attacker defeats attacked
		else if (c1.attack>c2.attack || c1.isArcher) {
			System.out.println(this+"'s "+c1+" defeats "+p+"'s "+c2+" in deck "+enemyDeck+"!");
			p.decks[enemyDeck-1][index2]=null;
			return 1;
		} 
		//Attacked defeats attacker
		else if (c2.attack>c1.attack || c2.isArcher) {
			System.out.println(p+"'s "+c2+" defeats "+this+"'s "+c1+" in deck "+ownDeck+"!");
			this.decks[ownDeck-1][index1]=null;
			return -1;
		} 
		//Tie, both cards are destroyed
		else if (c2.attack==c1.attack) {
			System.out.println("Tie! Both cards are destroyed.");
			this.decks[ownDeck-1][index1]=null;
			p.decks[enemyDeck-1][index2]=null;
			return -3;
		}	
		//Bad input
		else {
			System.out.println("Bad input.");
			return 0;
		}
	}
	
	/**
	 * Implementation of attack to work with a ScrimishFrame.
	 */
	/**
	 * This method "attacks" another player and prints the outcome of the "battle". An attack
	 * is carried out by comparing the attack values of the two cards battling each other; 
	 * the one with the higher attack wins. Additionally, an archer always wins when attacking,
	 * and always loses when attacked. A shield can not attack but destroys itself and whatever 
	 * it is attacked by, unless it is an archer in which case both cards are returned.
	 * If a crown is attacked, the game is over.
	 * @param p The player being attacked.
	 * @param ownDeck The deck number (1-5) which the player is attacking with.
	 * @param enemyDeck The deck of the enemy's (1-5) that is being attacked.
	 * @return An integer representing the outcome. 0 means nothing happened, either from bad input
	 * or a tie. 1 means the attacking player has won. -1 means the attacked player has won. -2 means the player
	 * attempted to attack with a shield. -3 means both cards were destroyed.-5 means a Crown has been defeated 
	 * and the game is over. As it is now, the method itself prints the outcome; however the method could be 
	 * changed and a driver could be made that interprets different return values to print its own statements.
	 */
	public int attack(Player p, int ownDeck, int enemyDeck, ScrimishFrame game) { 
		Card c1=null; Card c2=null;
		int index1=0, index2=0;
		
		//Find top card of player's deck
		for (int i=0; i<5; i++) {
			if (this.decks[ownDeck-1][i]!=null) {
				c1=this.decks[ownDeck-1][i];
				index1=i;
				break;
			}
			//If deck is empty, return 0.
			if (i==4) {
				game.addTextln(this+"'s deck is empty!");
				return 0;
			}
		}
		//Find top card of enemy's deck
		for (int i=0; i<5; i++) {
			if (p.decks[enemyDeck-1][i]!=null) {
				c2=p.decks[enemyDeck-1][i];
				index2=i;
				break;
			}
			//If deck is empty, return 0.
			if (i==4) {
				game.addTextln(p+"'s deck is empty!");
				return 0;
			}
		}
		
		//Shield can not attack
		if (c1.isShield) {
			game.addTextln("You can't attack with a shield!");
			return -2;
		}	
		//If the crown is attacked, game over. The driver must interpret -5 as "game over" value.
		else if (c1.attack>=c2.attack && c2.isCrown) {
			game.addTextln(p+"'s Crown is defeated! Game over!");
			return -5;
		} else if (c2.attack>=c1.attack && c1.isCrown) {
			game.addTextln(this+"'s Crown is defeated! Game over!");
			return -5;
		}
		//Archer and shield tie, return to deck.
		else if ((c1.isArcher && c2.isShield) || (c1.isShield && c2.isArcher)) {
			game.addTextln("Archer and Shield are returned to their decks.");
			return 0;
		} 
		//If shield is attacked, both are discarded.
		else if (c2.isShield) {
			game.addTextln(p+"'s Shield in deck "+enemyDeck+" and "+this+"'s "+c1+" in deck "+ownDeck+" are discarded!");
			this.decks[ownDeck-1][index1]=null;
			this.decks[enemyDeck-1][index2]=null;
			return -3;
		}
		//Attacker defeats attacked
		else if (c1.attack>c2.attack || c1.isArcher) {
			game.addTextln(this+"'s "+c1+" defeats "+p+"'s "+c2+" in deck "+enemyDeck+"!");
			p.decks[enemyDeck-1][index2]=null;
			return 1;
		} 
		//Attacked defeats attacker
		else if (c2.attack>c1.attack || c2.isArcher) {
			game.addTextln(p+"'s "+c2+" defeats "+this+"'s "+c1+" in deck "+ownDeck+"!");
			this.decks[ownDeck-1][index1]=null;
			return -1;
		} 
		//Tie, both cards are destroyed
		else if (c2.attack==c1.attack) {
			game.addTextln("Tie! Both cards are destroyed.");
			this.decks[ownDeck-1][index1]=null;
			p.decks[enemyDeck-1][index2]=null;
			return -3;
		}	
		//Bad input
		else {
			System.out.println("Bad input.");
			return 0;
		}
	}
	
	
	/**
	 * This method checks if it's possible to add a given card to a player's deck. This is done by comparing
	 * the entered "ID" to the amount of that card left in the player's decks, which is counted
	 * by an instance variable.
	 * @param a A card's "ID". 1 for Dagger, 2 for Sword, 3 for Morning Star, 4 for War Axe, 5 for Halberd,
	 * 6 for Longsword, 7 for Archer, 8 for Shield, 9 for Crown. Any other value returns false.
	 * @return A boolean representing if the player can add that card. 
	 */
	public boolean canAdd(int a) {
		if (a==1 && this.DCount>0)
			return true;
		else if (a==2 && this.SCount>0)
			return true;
		else if (a==3 && this.MSCount>0)
			return true;
		else if (a==4 && this.WACount>0)
			return true;
		else if (a==5 && this.HCount>0)
			return true;
		else if (a==6 && this.LSCount>0)
			return true;
		else if (a==7 && this.ACount>0)
			return true;
		else if (a==8 && this.ShCount>0)
			return true;
		else if (a==9 && this.CCount>0)
			return true;
		else
			return false;
	}
	
	/**
	 * This method adds a card to a player's deck. It is advised to call the canAdd() method before this method
	 * to minimize errors. 
	 * @param dNum The deck (1-5) that a card is being added to.
	 * @param n The position (1-5) in the deck.
	 * @param c The Card being entered. The parameter of the Card's constructor must be the same as
	 * the parameter a. 
	 * @param a The Card's "ID", used to decrement the instance variable that keeps track of how many
	 * of that card the Player can have.
	 */
	public void addCard(int dNum, int n, Card c, int a) { 
		this.decks[dNum][n]=c; //Adds card to player's deck, then decrements the amount of that card left
		if (a==1)
			this.DCount--;
		if (a==2)
			this.SCount--;
		if (a==3)
			this.MSCount--;
		if (a==4)
			this.WACount--;
		if (a==5)
			this.HCount--;
		if (a==6)
			this.LSCount--;
		if (a==7)
			this.ACount--;
		if (a==8)
			this.ShCount--;
		if (a==9)
			this.CCount--;
	}
	
	/**
	 * This method randomizes all 5 decks for a player, and ensures that the Crown is at the bottom
	 * of one of the decks. This is done by choosing random values from the array instance variable "cards" 
	 * and placing them into the player's five deck arrays. When a value is chosen, it is placed into a deck
	 * array and replaced by null in the cards array. The method skips over null values to prevent using too
	 * many cards. Consequently, it can not be used more than once by a player. 
	 */
	void makeRandomDecks() {
		//randomize deck1
		for (int i=0; i<5; i++) {
			int random = (int)(Math.random()*25);
			while (cards[random]==null)
				random=(int)(Math.random()*25);
			deck1[i] = cards[random];
			cards[random]=null;
		}
		//randomize deck2
		for (int i=0; i<5; i++) {
			int random = (int)(Math.random()*25);
			while (cards[random]==null)
				random=(int)(Math.random()*25);
			deck2[i] = cards[random];
			cards[random]=null;
		}
		//randomize deck3
		for (int i=0; i<5; i++) {
			int random = (int)(Math.random()*25);
			while (cards[random]==null)
				random=(int)(Math.random()*25);
			deck3[i] = cards[random];
			cards[random]=null;
		}
		//randomize deck4
		for (int i=0; i<5; i++) {
			int random = (int)(Math.random()*25);
			while (cards[random]==null)
				random=(int)(Math.random()*25);
			deck4[i] = cards[random];
			cards[random]=null;
		}
		//randomize deck5
		for (int i=0; i<5; i++) {
			int random = (int)(Math.random()*25);
			while (cards[random]==null)
				random=(int)(Math.random()*25);
			deck5[i] = cards[random];
			cards[random]=null;
		}
		//put Crown at bottom of deck
		Card temp;
		for (int i=0; i<decks.length; i++)
			for (int j=0; j<decks.length; j++)
				if(decks[i][j].isCrown) {
					temp=decks[i][j];
					decks[i][j]=decks[i][4];
					decks[i][4]=temp;
				}
	}
	
	/**
	 * This method displays a given deck by printing the names of the five cards in that deck.
	 * @param dNum The deck (1-5) to be displayed.
	 */
	public void displayDeck(int dNum) {
		if (dNum<6 && dNum>0) {
			System.out.print(this+ "'s deck "+(dNum)+": ");
			for(int i=0; i<5; i++) {
				if (i==4)
					System.out.print(decks[dNum-1][i]);
				else
					System.out.print(decks[dNum-1][i]+", ");
			}
			System.out.println();
		} else
			System.out.println("Invalid deck number!");
	}
	
	/**
	 * Implementation of displayDeck to work with a ScrimishFrame.
	 */
	public void displayDeck(ScrimishFrame game, int dNum) {
		if (dNum<6 && dNum>0) {
			game.addText(this+ "'s deck "+(dNum)+": ");
			for(int i=0; i<5; i++) {
				if (i==4)
					game.addText(decks[dNum-1][i]+"");
				else
					game.addText(decks[dNum-1][i]+", ");
			}
			game.addTextln("");
		} else
			game.addTextln("Invalid deck number!");
	}
	
	/**
	 * This method displays all of a player's decks by calling displayDeck() for all five
	 * decks.
	 */
	public void displayDecks() {
		this.displayDeck(1);
		this.displayDeck(2);
		this.displayDeck(3);
		this.displayDeck(4);
		this.displayDeck(5);
	}
	
	/**
	 * Implementation of displayDecks to work with a ScrimishFrame.
	 */
	public void displayDecks(ScrimishFrame game) {
		this.displayDeck(game, 1);
		this.displayDeck(game, 2);
		this.displayDeck(game, 3);
		this.displayDeck(game, 4);
		this.displayDeck(game, 5);
	}
	
	/**
	 * This method gets the top card of a player's deck dNum.
	 * @param dNum The deck that you want the top card of.
	 * @return The top card of player's deck dNum. Returns null if the deck is empty.
	 */
	public Card getTopCard(int dNum) {
		Card c1=null;
		for (int i=0; i<5; i++) {
			if (this.decks[dNum-1][i]!=null) {
				c1=this.decks[dNum-1][i];
				break;
			}
		}
		return c1;
	}
	
}
