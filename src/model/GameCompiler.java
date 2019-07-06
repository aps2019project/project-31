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

    public void checkIfAttack(String action) {
        Pattern pattern = Pattern.compile("\\dA(\\d)(\\d)(\\d)(\\d)");
        Matcher matcher = pattern.matcher(action);
        if (matcher.matches()) {
            battleManager.doTheActualAttack_noTarof(deployableInCell(matcher.group(1), matcher.group(2)),
                    deployableInCell(matcher.group(3), matcher.group(4)));
        }
    }

    public void checkIfMove(String action) {
        Pattern pattern = Pattern.compile("\\dM(\\d)(\\d)(\\d)(\\d)");
        Matcher matcher = pattern.matcher(action);
        if (matcher.matches()) {
            battleManager.doTheActualMove_noTarof(deployableInCell(matcher.group(1), matcher.group(2)),
                    Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
        }
    }

    public void checkIfInsert(String action) {
        Pattern pattern = Pattern.compile("\\dI(\\d\\d\\d)(\\d)(\\d)");
        Matcher matcher = pattern.matcher(action);
        if (matcher.matches()) {
            int id = Integer.parseInt(matcher.group(1));
            int x1 = Integer.parseInt(matcher.group(2));
            int x2 = Integer.parseInt(matcher.group(3));
            Card card = Shop.findCardById(id);
            switch (card.getType()) {
                case minion:
                    battleManager.playMinion((Minion) card, x1, x2);
                    break;
                case spell:
                    battleManager.playSpell((Spell) card, x1, x2);
                    break;
                case herospell:
                    battleManager.playSpell((Spell) card, x1, x2);
                    break;
                case item:
                    battleManager.useItem((Item) card, x1, x2);
                    break;
            }
        }
    }

    public void whatIsThePlay(String action) {
        if (action.startsWith("E")) {
            System.out.println("the game ended");
            SinglePlayerBattlePageController.getInstance().showThatGameEnded();
            System.out.println("the game ended ? wtf ???");
        }
        if (action.contains("A"))
            this.checkIfAttack(action);
        if (action.contains("I"))
            this.checkIfInsert(action);
        if (action.contains("M"))
            this.checkIfMove(action);
    }
}

