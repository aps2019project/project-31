package model;


import controller.Shop;
import controller.SinglePlayerBattlePageController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameCompiler {
    BattleManager battleManager;

    public GameCompiler(BattleManager battleManager) {
        this.battleManager = battleManager;
    }

    public Deployable deployableInCell(String sX1, String sX2) {
        int x1 = Integer.parseInt(sX1);
        int x2 = Integer.parseInt(sX2);
        Cell deployableCell = Map.getInstance().getCell(x1, x2);
        return deployableCell.getCardInCell();
    }

    public boolean checkIfAttack(String action) {
        System.out.println("check if action is attack");
        Pattern pattern = Pattern.compile("\\dA(\\d)(\\d)(\\d)(\\d)");
        Matcher matcher = pattern.matcher(action);
        if (matcher.matches()) {
            System.out.println("the action was attack");
            return battleManager.attack(deployableInCell(matcher.group(1), matcher.group(2)), deployableInCell(matcher.group(3), matcher.group(4)));
        }
        return false;
    }

    public boolean checkIfMove(String action) {
        System.out.println("check if action is move");
        Pattern pattern = Pattern.compile("\\dM(\\d)(\\d)(\\d)(\\d)");
        Matcher matcher = pattern.matcher(action);
        if (matcher.matches()) {
            System.out.println("the action was move");
            return battleManager.move(deployableInCell(matcher.group(1), matcher.group(2)), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
        }
        return false;
    }

    public boolean checkIfInsert(String action) {
        System.out.println("check if action is insert");
        Pattern pattern = Pattern.compile("\\dI(\\d\\d\\d)(\\d)(\\d)");
        Matcher matcher = pattern.matcher(action);
        if (matcher.matches()) {
            System.out.println("the action was insert");
            int id = Integer.parseInt(matcher.group(1));
            int x1 = Integer.parseInt(matcher.group(2));
            int x2 = Integer.parseInt(matcher.group(3));
            Card card = Shop.findCardById(id);
            return battleManager.insert(card, x1, x2);
        }
        return false;
    }

    public boolean whatIsThePlay(String action) {
        System.out.println("what is the play");
        if (action.contains("A"))
            return this.checkIfAttack(action);
        if (action.contains("I"))
            return this.checkIfInsert(action);
        if (action.contains("M"))
            return this.checkIfMove(action);
        return false;
    }
}

