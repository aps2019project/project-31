package model;

public abstract class BattleManager {
    private Map map;
    private String gameMode;
    private Player currentPlayer;

    public BattleManager(Map map, String gameMode, Player currentPlayer) {
        this.map = map;
        this.gameMode = gameMode;
        this.currentPlayer = currentPlayer;
    }

    public void playMinion(Minion minion, int x, int y) {
        if (!isInHand(minion)) {
            //insert not in hand error message for view

        }

        if (!checkCoordinates(x, y)) {
            //insert invalid coordinates error for view


        }

        if (minion.manaCost > currentPlayer.getMana()) {
            //insert not enough mana message for view


        }

        for (Function function : minion.getFunctions()) {
            if (function.getFunctionType() == FunctionType.OnSpawn) {
                compileOnSpawnFunction(function, minion, x, y);
            }
        }

        Map.putCardInCell(minion, x, y);
        currentPlayer.addCardToBattlefield(minion);
    }

    public void compileOnSpawnFunction(Function function, Minion minion, int x, int y){

    }

    private boolean checkCoordinates(int x, int y) {
        if (Map.getCell(x, y) == null ||
                Map.getCardInCell(x, y) != null) {
            return false;
        }


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != 1 || j != 1) {
                    if (Map.getCardInCell(x, y).getAccount().equals(currentPlayer.getAccount())) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private boolean isInHand(Card card) {
        for (Card card1 : currentPlayer.getHand()) {
            if (card1.equals(card)) {
                return true;
            }
        }
        return false;
    }

    public void playSpell(Spell spell, int x, int y) {

    }

    public void useItem(Item item, int x, int y) {

    }


    public abstract Player getOtherPlayer(String thisPlayerUserName);
}
