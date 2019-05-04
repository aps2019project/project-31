package view;

import constants.CardType;
import model.Account;
import model.Card;
import model.Deployable;

public class Output {

    public static void showLeaderBoard() {
        Account.sortAllAccounts();
        for (int i = 0; i < Account.getAllAccounts().size(); i++) {
            System.out.println((i + 1) + " - UserName: " + Account.getAllAccounts().get(i).getUsername() + " -Wins: " +
                    Account.getAllAccounts().get(i).getWinLoseDraw()[0]);
        }
    }

    public static void notInHand() {
        System.out.println("Invalid card name");
    }

    public static void invalidInsertionTarget() {
        System.out.println("Invalid target");
    }

    public static void notHaveEnoughMana() {
        System.out.println("You don't have enough mana");
    }

    public static void insertionSuccessful(Card card, int x1, int x2) {
        if (card.getType() == CardType.minion)
            System.out.println(card.getName() + " with " + ((Deployable) card).getUniqueId() + " inserted to (" + x1 + ", " + x2 + ")");
        else
            System.out.println(card.getName() + " with " + card.getId() + " inserted to (" + x1 + ", " + x2 + ")");
    }

    public static void movedSuccessfully(Deployable card) {
        System.out.println(card.getUniqueId() + " moved to " + card.getCell().getX1Coordinate() + " " +
                card.getCell().getX2Coordinate());
    }

    public static void tooFarToMove() {
        System.out.println("too far to move");
    }

    public static void invalidTargetForMove() {
        System.out.println("invalid target");
    }

    public static void hasAttackedBefore(Deployable card) {
        System.out.println("card with " + card.getUniqueId() + " can't attack");
    }

    public static void isStunned() {
        System.out.println("card is stunned");
    }

    public static void enemyNotThere() {
        System.out.println("opponent minion is unavailable for attack");
    }

    public static void enemyNotExist() {
        System.out.println("invalid card id");
    }

    public static void badSelectedCard() {
        System.out.println("selected card is not deployable or you don't have selected card");
    }

    public static void theTurnEnded() {
        System.out.println("the turn ended");
    }

    public static void thereIsntDeck() {
        System.out.println("there isn't deck with this name");
    }

    public static void notInDeck() {
        System.out.println("there isn't card with this name");
    }

    public static void showValidationOfDeck(boolean isValid) {
        if (isValid)
            System.out.println("the deck is valid");
        else
            System.out.println("the deck isn't valid");
    }

    public static void showCardIdAndStuff(Card card) {
        card.show();
    }

    public static void print(String outpt) {
        System.out.println(outpt);
    }
    public static void showGameModes(){
        System.out.println("1.Single player\n2.Multi player");
    }
}
