package Server;


import model.BattleManager;
import model.GameCompiler;

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
        while (!command.equals("T") || !command.equals(ServerStrings.CONCEDE)) {
            if (gameCompiler.whatIsThePlay(command)) {
                System.out.println("we pass command :" + command + "");
                user1.os.writeUTF(command);
                user2.os.writeUTF(command);
            } else {
                currentPlayer().os.writeUTF(ServerStrings.NOTALLOWED);
            }
            //    battleServer.updateBothUsers();
            System.out.println("get the next command from current player");
            command = currentPlayer().is.readUTF();
            System.out.println("the command is : " + command);
        }
        if (command.equals(ServerStrings.CONCEDE)) {
            System.out.println("the concede has been received");


            return false;
        }
        System.out.println("the end turn has been received");
        //   battleServer.updateBothUsers();
        return true;
    }


    private void gameFinished() {

    }

    @Override
    public void run() {
        System.out.println("battle server is going to start its job :)");
        synchronized (User.battle) {
            System.out.println("we are in the battle :)");
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
