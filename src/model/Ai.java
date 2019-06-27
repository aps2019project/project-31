package model;

import controller.BattleMenu;
import controller.BattlePageController;
import javafx.application.Platform;

public class Ai extends Player {
    public Ai(Account account) {
        super(account, true);
    }

    public void play() {
        considerAllMoves();
    }

    public void considerAllMoves() {
        Player me = BattlePageController.getInstance().getMe();
        for (Deployable deployable : getCardsOnBattleField()) {
            int x2Moves = coefficient(deployable.cell.getX2Coordinate() - me.getHero().cell.getX2Coordinate());
            int x1Moves = coefficient(deployable.cell.getX1Coordinate() - me.getHero().cell.getX1Coordinate());
            battle.move(deployable, deployable.getCell().getX1Coordinate() - x1Moves,
                    deployable.getCell().getX2Coordinate() - x2Moves);
            System.out.println("the minion :(" + deployable.cell.getX1Coordinate() + "," + deployable.cell.getX2Coordinate() + "" +
                    ") goes to   " + (deployable.getCell().getX1Coordinate() - x1Moves) + " , " +
                    (deployable.getCell().getX2Coordinate() - x2Moves));
        }
        outerLoop:
        for (int i = 0; i < hand.size(); i++) {
            for (int k = 1; k <= Map.MAP_X1_LENGTH; k++) {
                for (int j = 1; j < Map.MAP_X2_LENGTH; j++) {
                    if (hand.get(i) != null && BattleMenu.insert(hand.get(i), k, j)) {

                    }
                    //needs to be fixed
                }
            }
        }
        for (int i = 0; i < cardsOnBattleField.size(); i++) {
            for (int j = 0; j < getOtherPlayer().cardsOnBattleField.size(); j++) {
                battle.attack(cardsOnBattleField.get(i), getOtherPlayer().cardsOnBattleField.get(j));
            }
        }
    }

    public Player getOtherPlayer() {
        return BattleManager.player2;
    }

    public int coefficient(int a) {
        if (a != 0)
            return a / Math.abs(a);
        else
            return 0;
    }
}
