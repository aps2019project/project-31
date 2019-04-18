package controller;

import model.BattleManager;
import model.Player;

public class BattleMenu {




    private void setBattleManagerMode(){
        BattleManager battleManager=new ();


        beginTheGame(battleManager);
    }
    private void beginTheGame(BattleManager battleManager){
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
        }
    }
    public void run(){
        while(true) {
            View.showModes();
            handleInputCommand();
            if(out)
                break;
        }
    }
    private void handleInputCommand(){



        setBattleManagerMode();
    }
}
