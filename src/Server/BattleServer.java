package Server;

import controller.BattleMenu;
import model.BattleManager;

public class BattleServer {
    protected BattleManager battleManager;
    protected User user1;
    protected User user2;

    public BattleServer(BattleManager battleManager, User user1, User user2) {
        this.battleManager = battleManager;
        this.user1 = user1;
        this.user2 = user2;
    }
}
