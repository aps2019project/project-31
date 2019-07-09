package model;

import controller.BattleMenu;
import controller.SinglePlayerBattlePageController;


public class Ai extends Player {
    public Ai(Account account) {
        super(account, true);
    }

    public void play() {
        considerAllMoves();
    }

    public void considerAllMoves() {
        BattleManager battle = BattleMenu.getBattleManager();
        Player me = SinglePlayerBattlePageController.getInstance().getMe();
        Player opponent = SinglePlayerBattlePageController.getInstance().getOpponent();
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
                    if (i < hand.size() && hand.get(i) != null && BattleMenu.getBattleManager().insert(hand.get(i), k, j)) ;
                }
            }
        }
        for (int i = 0; i < opponent.cardsOnBattleField.size(); i++) {
            for (int j = 0; j < me.cardsOnBattleField.size(); j++) {
                if (!battle.isTheGameFinished && i < opponent.cardsOnBattleField.size())
                    battle.attack(opponent.cardsOnBattleField.get(i), me.cardsOnBattleField.get(j));
            }
        }
        this.placeNextCardToHand();
        if (battle.getGameRecord() != null)
            battle.getGameRecord().addAction("T");
    }

    public int coefficient(int a) {
        if (a != 0)
            return a / Math.abs(a);
        else
            return 0;
    }
}
