package Model;

import Model.Map;
import Model.Player;

public abstract class BattleManager {
    private Map map;
    private String gameMode;
    private Player currentPlayer;

    public BattleManager(Map map, String gameMode, Player currentPlayer) {
        this.map = map;
        this.gameMode = gameMode;
        this.currentPlayer = currentPlayer;
    }

    public void playMinion(Minion minion, int x, int y){


    }

    public void playSpell(Spell spell, int x, int y){

    }

    public void useItem(Item item, int x, int y){

    }


    public abstract Player getOtherPlayer(String thisPlayerUserName);
}
