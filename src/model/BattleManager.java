package model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BattleManager {
    private Map map;
    private String gameMode;
    private Player currentPlayer;
    private Player player1;
    private Player player2;

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

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
                compileFunction(function, x, y);
            }
        }
        Map.putCardInCell(minion, x, y);
        currentPlayer.addCardToBattlefield(minion);
        currentPlayer.removeFromHand(minion);

    }

    public void compileTargetString(ArrayList<Card> targetCards, ArrayList<Cell> targetCells, String target,
                                    int x, int y){
        compileTargetString(targetCards,targetCells,target,x,y,null);
    }

    public void compileTargetString(ArrayList<Card> targetCards, ArrayList<Cell> targetCells, String target,
                                    int x, int y, Deployable attackTarget) {
        try {
            Pattern pattern = Pattern.compile(TargetStrings.MINIONS_WITH_DISTANCE + "(\\d+)");
            Matcher matcher = pattern.matcher(target);
            if (matcher.matches()) {
                int distance = Integer.parseInt(matcher.group(1));
                for (int i = 0; i < distance * 2; i++) {
                    for (int j = 0; j < distance * 2; j++) {
                        Card cardInCell = Map.getCardInCell(x - distance + i, y - distance + j);
                        if (cardInCell != null) {
                            if (!cardInCell.getAccount().equals(currentPlayer.getAccount()) &&
                                    cardInCell.getType() == CardType.minion) {
                                targetCards.add(cardInCell);
                            }
                        }
                    }
                }
            }

            if (target.matches("(.*)" + TargetStrings.ALL_ALLIES + "(.*)")) {
                targetCards.addAll(currentPlayer.getCardsOnBattleField());
            }

            if (target.matches("(.*)" + TargetStrings.ALL_ENEMIES + "(.*)")) {
                targetCards.addAll(getOtherPlayer().getCardsOnBattleField());
            } else if (target.matches("(.*)" + TargetStrings.ALL_ENEMIES_IN_COLUMN + "(.*)")) {
                for (int i = 1; i <= Map.MAP_Y_LENGTH; i++) {
                    if (!Map.getCardInCell(x, i).getAccount().equals(currentPlayer.getAccount())) {
                        targetCards.add(Map.getCardInCell(x, i));
                    }
                }
            } else if (target.matches("(.*)" + TargetStrings.ALL_ENEMIES_IN_ROW + "(.*)")) {
                for (int i = 1; i <= Map.MAP_X_LENGTH; i++) {
                    if (!Map.getCardInCell(i, y).getAccount().equals(currentPlayer.getAccount())) {
                        targetCards.add(Map.getCardInCell(i, y));
                    }
                }
            } else if (target.matches("(.*)" + TargetStrings.ALL_ENEMY_MINIONS + "(.*)")) {
                for (Card card : getOtherPlayer().getCardsOnBattleField()) {
                    if (card.getType() == CardType.minion) {
                        targetCards.add(card);
                    }
                }
            } else if (target.matches("(.*)" + TargetStrings.ENEMY_HERO + "(.*)")) {
                targetCards.add(getOtherPlayer().getHero());
            }

            if (target.matches("(.*)" + TargetStrings.ENEMY_MINION + "(.*)")){
                if (Map.getCardInCell(x,y) != null
                    && Map.getCardInCell(x,y).getType()==CardType.minion
                    && Map.getCardInCell(x,y).getAccount().equals(currentPlayer.getAccount())){
                    targetCards.add(Map.getCardInCell(x,y));
                }else{
                    //error message for view


                }
            }





            // pattern = Pattern.compile(TargetStrings.)
        } catch (IllegalStateException e) {
            //Input error message for view
        }
    }

    public void compileFunction(Function function, int x, int y) {
        ArrayList<Cell> targetCells = new ArrayList<>();
        ArrayList<Card> targetCards = new ArrayList<>();
        compileTargetString(targetCards, targetCells, function.getTarget(), x, y);
        try {
            Pattern pattern = Pattern.compile(FunctionStrings.APPLY_BUFF + "(.*)");
            Matcher matcher = pattern.matcher(function.getFunction());
            if (matcher.matches()) {
                if (matcher.group(1).matches("unholy")) {
                    addUnholyBuff(targetCards);
                }
                if (matcher.group(1).matches("disarm\\d+")) {
                    int turns = Integer.parseInt(matcher.group(1).replace("disarm", ""));
                    Buff buff = new Buff(Buff.BuffType.Disarm, turns, 0, 0, false);
                }
            }

        } catch (IllegalStateException e) {
            //error message for view

        }
    }

    private void addUnholyBuff(ArrayList<Card> targetCards) {
        Buff buff = new Buff(Buff.BuffType.Unholy, PERMENANT, 0, 0, false);
        for (Card card : targetCards) {
            switch (card.getType()) {
                case minion:
                    ((DeployedMinion) card).addBuff(buff);
                    break;
                case hero:
                    ((DeployedHero) card).addBuff(buff);
            }
        }
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

    public void move(Deployable card, int x, int y) {
        if (Map.getDistance(Map.getCell(x, y), card.cell) <= Map.getMaxMoveRange()) {
            if (Map.getCell(x, y).getCardInCell() == null && !card.isMoved && !card.isStunned()) {
                card.cell = Map.getCell(x, y);
                Map.getCell(x, y).setCardInCell(card);
            }
        }
    }

    public void attack(Deployable card, Deployable enemy) {
        if (Map.getDistance(card.cell, enemy.cell) < card.attackRange && !card.isAttacked && !card.isStunned()) {
            enemy.currentHealth -= enemy.theActualDamageReceived(card.theActualDamage());
            counterAttack(card, enemy);
        }
    }

    private void counterAttack(Deployable attacker, Deployable counterAttacker) {
        if (!counterAttacker.isDisarmed()) {  //does being Stunned matters or not
            attacker.currentHealth -= attacker.theActualDamageReceived(counterAttacker.theActualDamage());
        }
    }

    public abstract Player getOtherPlayer();
}
