package model;

import controller.BattleMenu;
import controller.BattlePageController;


public class Ai extends Player {
    public Ai(Account account) {
        super(account, true);
    }

    public void play() {
        considerAllMoves();
    }

    public void considerAllMoves() {
        Player me = BattlePageController.getInstance().getMe();
        Player opponent = BattlePageController.getInstance().getOpponent();
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
                    if (i < hand.size() && hand.get(i) != null && BattleMenu.insert(hand.get(i), k, j)) ;
                }
            }
        }
        for (int i = 0; i < opponent.cardsOnBattleField.size(); i++) {
            for (int j = 0; j < me.cardsOnBattleField.size(); j++) {
                battle.attack(opponent.cardsOnBattleField.get(i), me.cardsOnBattleField.get(j));
            }
        }
        this.placeNextCardToHand();
        battle.getGameRecord().addAction("T");
    }

    public int coefficient(int a) {
        if (a != 0)
            return a / Math.abs(a);
        else
            return 0;
    }
}
