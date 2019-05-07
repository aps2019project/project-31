package model;

import controller.BattleMenu;

public class Ai extends Player {
    public Ai(Account account) {
        super(account, true);
    }

    public void play() {
        considerAllMoves();
    }

    public void considerAllMoves() {
        for (Deployable deployable : getCardsOnBattleField()) {
            int x2Moves = coefficient(deployable.cell.getX2Coordinate() - getOtherPlayer().getHero().cell.getX2Coordinate()),
                    x1Moves = deployable.cell.getX1Coordinate() - getOtherPlayer().getHero().cell.getX1Coordinate();
            battle.move(deployable, deployable.getCell().getX1Coordinate() + x1Moves,
                    deployable.getCell().getX2Coordinate() + x2Moves);
        }

        for (int i = 0; i < hand.size(); i++) {
            for (int k = 1; k <= Map.MAP_X1_LENGTH; k++) {
                for (int j = 1; j < Map.MAP_X2_LENGTH; j++) {
                    BattleMenu.insert(hand.get(i), k, j);
                    //needs to be fixed
                }
            }
        }
        for (Deployable deployable : cardsOnBattleField) {
            for (Deployable enemy : getOtherPlayer().getCardsOnBattleField()) {
                if (BattleManager.isNear(deployable.cell, enemy.cell)) {
                    battle.attack(deployable, enemy);
                }
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
