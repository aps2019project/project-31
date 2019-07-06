package Server;


import model.BattleManager;
import model.GameCompiler;

public class BattleServer {
    protected BattleManager battleManager;
    protected User user1;
    protected User user2;
    protected GameCompiler gameCompiler;

    public BattleServer(BattleManager battleManager, User user1, User user2) {
        this.battleManager = battleManager;
        this.user1 = user1;
        this.user2 = user2;
        gameCompiler = new GameCompiler(battleManager);
    }

    public void serverEndTurn() {
        battleManager.doAllThingsInEndingOfTheTurns();
        battleManager.setCurrentPlayer(battleManager.getOtherPlayer());
        battleManager.doAllAtTheBeginningOfTurnThings(battleManager.isMultiPlayer());
    }

    public User currentPlayer() {
        if (battleManager.getCurrentPlayer().getAccount() == user1.getAccount())
            return user1;
        return user2;
    }
    public void updateBothUsers(){
        System.out.println("update both users");
        user2.sendMapAndBattle();
        user1.sendMapAndBattle();
    }

    public void isThisPlayAllowed(){

    }

}
