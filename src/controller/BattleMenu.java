package controller;

import model.BattleManager;
import model.Player;
import view.Input;
import view.Output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleMenu extends Menu {
    private static BattleManager battleManager;

    public static BattleManager getBattleManager() {
        return battleManager;
    }

    private void setBattleManagerMode(){
        battleManager=new ();


        runTheGame(battleManager);
    }
    private void runTheGame(BattleManager battleManager){
        boolean isPlayer1Turn=false;
        battleManager.setCurrentPlayer(battleManager.getPlayer2());
        while(true){
            isPlayer1Turn=!isPlayer1Turn;
            battleManager.setCurrentPlayer(battleManager.getOtherPlayer());
            if(battleManager.getCurrentPlayer().isAi()){
                battleManager.getCurrentPlayer().play();
            }
            else {
                if(battleManager.getCurrentPlayer().getSelectedCard()!=null){
                    Input.handleSelectCardOrSelectComboCards(battleManager.getCurrentPlayer()); // to view vorodi migire to controller selectedCard ro mirize tush ya combo ro check mikone
                }
                else {
                    Input.moveAttackPlayCard(); // to view vorodi migire tabesho to controller seda mizane
                }
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
