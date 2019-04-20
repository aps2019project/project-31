package controller;

import model.BattleManager;
import model.Player;

public class BattleMenu extends Menu {


    private void setBattleManagerMode(){
        BattleManager battleManager=new ();


        runTheGame(battleManager);
    }
    private void runTheGame(BattleManager battleManager){
        boolean isPlayer1Turn=false;
        battleManager.setCurrentPlayer(battleManager.getPlayer2());
        while(true){
            isPlayer1Turn=!isPlayer1Turn;
            if(battleManager.getCurrentPlayer().isAi()){
                battleManager.getCurrentPlayer().play();
            }
            else {
                battleManager.setCurrentPlayer(battleManager.getOtherPlayer());
                processTheInput(inputFromUser());

            }
            battleManager.getCurrentPlayer().placeNextCardToHand();
            battleManager.getCurrentPlayer().endOfTurn();
            battleManager.getOtherPlayer().endOfTurn();
        }
    }

    @Override
    public void run(){
        while(true) {
            View.showModes();
            handleInputCommand();
            if(out)
                break;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void help() {

    }

    private void handleInputCommand(){



        setBattleManagerMode();
    }
}
