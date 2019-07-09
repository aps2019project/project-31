package Server;


import model.BattleManager;
import model.GameCompiler;
import model.Hero;
import model.Map;

import java.io.IOException;

public class BattleServer extends Thread {
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

    public User currentPlayer() {
        if (battleManager.getCurrentPlayer().getAccount() == user1.getAccount())
            return user1;
        return user2;
    }

    public User getOtherPlayer() {
        if (battleManager.getCurrentPlayer().getAccount() == user1.getAccount())
            return user2;
        return user1;
    }

    public void updateBothUsers() {
        System.out.println("update both users");
        user2.sendMapAndBattle();
        user1.sendMapAndBattle();
    }

    public void isThisPlayAllowed() {

    }

    private void goToBattle() throws IOException {


    }


    private boolean getCommandFromCurrentPlayer() throws IOException {
        System.out.println("get command from current player");
        System.out.println("current player is " + currentPlayer().getAccount().getUsername());
        String command = currentPlayer().is.readUTF();
        System.out.println("the command is : " + command);
        while (!command.equals(ServerStrings.ENDTURN) && !command.contains(ServerStrings.CONCEDE)) {
            if (gameCompiler.whatIsThePlay(command)) {
                System.out.println("we pass command :" + command + "");
                user1.os.writeUTF(command);
                user2.os.writeUTF(command);
            } else {
                currentPlayer().os.writeUTF(ServerStrings.NOTALLOWED);
            }
            System.out.println("get the next command from current player");
            command = currentPlayer().is.readUTF();
            System.out.println("the command is : " + command);
        }
        if (command.contains(ServerStrings.CONCEDE)) {
            System.out.println("the concede has been received");
            user1.os.writeUTF(command.charAt(0) + ServerStrings.GAMEENDED);
            user2.os.writeUTF(command.charAt(0) + ServerStrings.GAMEENDED);
            return false;
        }
        user1.os.writeUTF(ServerStrings.ENDTURN);
        user2.os.writeUTF(ServerStrings.ENDTURN);
        System.out.println("the end turn has been received");
        battleManager.endTurn();
        return true;
    }


    private void gameFinished() {
        System.out.println("the game has finished ( in battle server )");
    }
    private void initHeroes(){
        Hero hero1 = battleManager.getPlayer1().getHero();
        Hero hero2 = battleManager.getPlayer2().getHero();
        hero1.setCell(Map.getInstance().getCell(3, 1));
        hero2.setCell(Map.getInstance().getCell(3, 9));
        hero1.getCell().setCardInCell(hero1);
        hero2.getCell().setCardInCell(hero2);
    }
    @Override
    public void run() {
        System.out.println("battle server is going to start its job :)");
        synchronized (User.syncObject) {
            System.out.println("we are in the battle :)");
            initHeroes();
            updateBothUsers();
            while (true) {
                System.out.println("go to battle while loop");
                try {


                    if (!getCommandFromCurrentPlayer()) {
                        gameFinished();
                        return;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
