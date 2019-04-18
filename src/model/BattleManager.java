package model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Minion deployedMinion = new Minion(minion.getPrice(), minion.getManaCost(), minion.getCardText(),
                minion.getFunctions(), minion.getAccount(), minion.getName(), minion.getId(), minion.getType(),
                true, true, true, Map.getCell(x, y), minion.getHealth(), minion.getAttack(),
                minion.buffs, minion.getFunctionTime(), minion.getAttackRange(), minion.getAttackType(),
                minion.getAttack(), minion.getHealth());
        Map.putCardInCell(deployedMinion, x, y);
        currentPlayer.addCardToBattlefield(deployedMinion);
        currentPlayer.removeFromHand(minion);

    }

    public void compileTargetString(ArrayList<Card> targetCards, ArrayList<Cell> targetCells, String target,
                                    int x, int y) {
        try {
            Pattern pattern = Pattern.compile(TargetStrings.MINIONS_WITH_DISTANCE + "(\\d+)");
            Matcher matcher = pattern.matcher(target);
            if (matcher.matches()) {
                int distance = Integer.parseInt(matcher.group(1));
                for (int i = 0; i < distance * 2; i++) {
                    for (int j = 0; j < distance * 2; j++) {
                        Card cardInCell = Map.getCardInCell( x - distance + i, y - distance + j);
                        if (cardInCell != null) {
                            if (!cardInCell.getAccount().equals(currentPlayer.getAccount()) &&
                                    cardInCell.getType() == CardType.minion) {
                                targetCards.add(cardInCell);
                            }
                        }
                    }
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
                if (matcher.group(1).matches("disarm\\d+")){

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
    public void move(Deployed card, int x, int y) {
        if (Map.getDistance(Map.getCell(x, y), card.cell) <= Map.getMaxMoveRange()) {
            if (Map.getCell(x, y).getCardInCell() == null && !card.isMoved && !card.isStunned()) {
                card.cell = Map.getCell(x, y);
                Map.getCell(x, y).setCardInCell(card);
            }
        }
    }

    public void attack(Deployed card, Deployed enemy) {
        if (Map.getDistance(card.cell, enemy.cell) < card.attackRange && !card.isAttacked && !card.isStunned()) {
            enemy.currentHealth -= enemy.theActualDamageReceived(card.theActualDamage());
            counterAttack(card, enemy);
        }
    }

    private void counterAttack(Deployed attacker, Deployed counterAttacker) {
        if (!counterAttacker.isDisarmed()) {  //does being Stunned matters or not
            attacker.currentHealth -= attacker.theActualDamageReceived(counterAttacker.theActualDamage());
        }
    }

    public abstract Player getOtherPlayer(String thisPlayerUserName);
}
