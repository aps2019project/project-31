package model;


import controller.BattleMenu;

public class Ai extends Player {
    public Ai(Account account) {
        super(account);
    }

    public void play(){
        considerAllMoves();
    }
    public void considerAllMoves(){
        for (Deployable deployable:getCardsOnBattleField() ) {
            int x2Moves=coefficient(deployable.cell.getX2Coordinate()-getOtherPlayer().getHero().cell.getX2Coordinate()),
                    x1Moves=deployable.cell.getX1Coordinate()-getOtherPlayer().getHero().cell.getX1Coordinate();
            battle.move(deployable,deployable.getCell().getX1Coordinate()+x1Moves,
                    deployable.getCell().getX2Coordinate()+x2Moves);
        }
        outerLoop: for (Card card:hand) {
            for (int i = 1; i <= Map.MAP_X1_LENGTH ; i++) {
                for (int j = 1; j <Map.MAP_X2_LENGTH ; j++) {
                    /*if(playCard(card,i,j)) {
                        continue outerLoop;
                    }*/
                    //needs to be fixed
                }
            }
        }
        for (Deployable deployable: cardsOnBattleField) {
            for (Deployable enemy:getOtherPlayer().getCardsOnBattleField()) {
                if(BattleManager.isNear(deployable.cell,enemy.cell)){
                    battle.attack(deployable,enemy);
                }
            }
        }
    }
    public  Player getOtherPlayer(){
        return battle.player2;
    }
    public int coefficient(int a){
        return a/ Math.abs(a);
    }
}
