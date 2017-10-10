/**
 * A class for representing cards of the game Scrimish.
 * 
 * Info about the game Scrimish: http://www.scrimish.com/
 * 
 * @author Michael Rolland
 * @version 2017.10.10
 *
 */
public class Card {
	protected String name;
	protected int attack=0;
	protected int id;
	protected boolean isArcher=false;
	protected boolean isShield=false;
	protected boolean isCrown=false;
	
	/**
	 * toString is overridden to display a Card's name.
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * The Card class only has one constructor. The int parameter is the Card's "ID". 1 for Dagger,
	 * 2 for Sword, 3 for Morning Star, 4 for War Axe, 5 for Halberd, 6 for Longsword, 7 for Archer, 8 for
	 * Shield, 9 for Crown. For the first 6 cards, their attack is the same as their ID. Archers, Shields, and 
	 * Crowns all have special qualities so they have special boolean values that are used in the Player.attack method.
	 * Any other value is invalid, as there are only 9 types of cards in Scrimish.
	 * @param n The Card's "ID".
 	 */
	public Card(int n) {
		if (n==1) {
			name="Dagger";
			attack=1; id=1;
		} else if (n==2) {
			name="Sword";
			attack=2; id=2;
		} else if (n==3) {
			name="Morning Star";
			attack=3; id=3;
		} else if (n==4) {
			name="War Axe";
			attack=4; id=4;
		} else if (n==5) {
			name="Halberd";
			attack=5; id=5;
		} else if (n==6) {
			name="Longsword";
			attack=6; id=6;
		} else if (n==7) {
			name="Archer";
			isArcher=true; id=7;
		} else if (n==8) {
			name="Shield";
			isShield=true; id=8;
		} else if (n==9) {
			name="Crown"; id=9;
			isCrown=true;
		} else {
			// Should throw exception, but the driver class ensures this never happens.
			System.out.print("Invalid");
		}
	}
}
