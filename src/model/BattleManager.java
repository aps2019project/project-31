package model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BattleManager {
    public static final int PERMENANT = 100;
    public static final String CONTINUOUS = "continuous";
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
                                    int x, int y) {
        compileTargetString(targetCards, targetCells, target, x, y, null);
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
            } else if (target.matches("(.*)" + TargetStrings.ALLY + "(.*)")) {
                Card card = Map.getCardInCell(x, y);
                if (card != null &&
                        card.getAccount().equals(currentPlayer.getAccount())) {
                    targetCards.add(card);
                } else {
                    //error message for view
                }
            }

            if (target.matches("(.*)" + TargetStrings.ANY_UNIT + "(.*)")) {
                Card card = Map.getCardInCell(x, y);
                if (card != null) {
                    targetCards.add(card);
                } else {
                    //error message for view


                }
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

            if (target.matches("(.*)" + TargetStrings.ENEMY_MINION + "(.*)")) {
                if (Map.getCardInCell(x, y) != null
                        && Map.getCardInCell(x, y).getType() == CardType.minion
                        && Map.getCardInCell(x, y).getAccount().equals(currentPlayer.getAccount())) {
                    targetCards.add(Map.getCardInCell(x, y));
                } else {
                    //error message for view


                }
            }

            if (target.matches("(.*)" + TargetStrings.ATTACK_TARGET + "(.*)")) {
                targetCards.add(attackTarget);
            }

            pattern = Pattern.compile(TargetStrings.SQUARE + "(\\d+)");
            matcher = pattern.matcher(target);
            if (matcher.matches()) {
                for (int i = x; i < Integer.parseInt(matcher.group(1)); i++) {
                    for (int j = y; j < Integer.parseInt(matcher.group(1)); j++) {
                        targetCells.add(Map.getCell(i, j));
                        targetCards.add(Map.getCardInCell(i, j));
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
            handleBuffs(function, targetCards);

            handleDamage(function, targetCards);



        } catch (IllegalStateException e) {
            //error message for view

        }
    }

    private void handleDamage(Function function, ArrayList<Card> targetCards) {
        Pattern pattern = Pattern.compile(FunctionStrings.DEAL_DAMAGE + "(\\d+)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()){
            int amount = Integer.parseInt(matcher.group(1));
            for (Card card: targetCards){
                ((Deployable)card).takeDamage(amount);
            }
        }
    }

    private void handleBuffs(Function function, ArrayList<Card> targetCards) {
        Pattern pattern = Pattern.compile(FunctionStrings.APPLY_BUFF + "(.*)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()) {
            if (matcher.group(1).trim().matches("unholy")) {
                addUnholyBuff(targetCards);
            }
            if (matcher.group(1).trim().matches("disarm(\\d+|continuous)")) {
                if (matcher.group(1).replace("disarm", "").matches(CONTINUOUS)){
                    Buff buff = new Buff(Buff.BuffType.Disarm, PERMENANT, 0, 0, false);
                    buff.makeContinuous();
                    addBuffs(targetCards, buff);
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replace("disarm", ""));
                Buff buff = new Buff(Buff.BuffType.Disarm, turns, 0, 0, false);
                addBuffs(targetCards, buff);
            }
            if (matcher.group(1).trim().matches("holy(\\d+|continuous)")) {
                if (matcher.group(1).replace("holy", "").matches(CONTINUOUS)){
                    Buff buff = new Buff(Buff.BuffType.Holy, PERMENANT, 0, 0, true);
                    buff.makeContinuous();
                    addBuffs(targetCards, buff);
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replace("holy", ""));
                Buff buff = new Buff(Buff.BuffType.Holy, turns, 0, 0, true);
                addBuffs(targetCards, buff);
            }
            if (matcher.group(1).trim().matches("stun(\\d+|continuous)")) {
                if (matcher.group(1).replace("stun", "").matches(CONTINUOUS)){
                    Buff buff = new Buff(Buff.BuffType.Stun, PERMENANT, 0, 0, false);
                    buff.makeContinuous();
                    addBuffs(targetCards, buff);
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replace("stun", ""));
                Buff buff = new Buff(Buff.BuffType.Stun, turns, 0, 0, false);
                addBuffs(targetCards, buff);
            }

            if (matcher.group(1).matches("pwhealth\\d+for(\\d+|continuous)")) {
                int amount = Integer.parseInt(matcher.group(1).replaceFirst("pwhealth", "")
                        .replaceFirst("for\\d+", ""));
                if (matcher.group(1).replaceFirst("pwhealth\\d+for", "").matches(CONTINUOUS)){
                    Buff buff = new Buff(Buff.BuffType.Power,PERMENANT,amount,0,true);
                    buff.makeContinuous();
                    addBuffs(targetCards,buff);
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replaceFirst("pwhealth\\d+for", ""));
                Buff buff = new Buff(Buff.BuffType.Power,turns,amount,0,true);
                addBuffs(targetCards,buff);
            }

            if (matcher.group(1).matches("pwattack\\d+for(\\d+|continuous)")) {
                int amount = Integer.parseInt(matcher.group(1).replaceFirst("pwattack", "")
                        .replaceFirst("for\\d+", ""));
                if (matcher.group(1).replaceFirst("pwattack\\d+for", "").matches(CONTINUOUS)){
                    Buff buff = new Buff(Buff.BuffType.Power,PERMENANT,0,amount,true);
                    buff.makeContinuous();
                    addBuffs(targetCards,buff);
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replaceFirst("pwattack\\d+for", ""));
                Buff buff = new Buff(Buff.BuffType.Power,turns,0,amount,true);
                addBuffs(targetCards,buff);
            }

            if (matcher.group(1).matches("wkhealth\\d+for(\\d+|continuous)")) {
                int amount = Integer.parseInt(matcher.group(1).replaceFirst("wkhealth", "")
                        .replaceFirst("for\\d+", ""));
                if (matcher.group(1).replaceFirst("wkhealth\\d+for", "").matches(CONTINUOUS)){
                    Buff buff = new Buff(Buff.BuffType.Weakness,PERMENANT,amount,0,false);
                    buff.makeContinuous();
                    addBuffs(targetCards,buff);
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replaceFirst("wkhealth\\d+for", ""));
                Buff buff = new Buff(Buff.BuffType.Weakness,turns,amount,0,false);
                addBuffs(targetCards,buff);
            }

            if (matcher.group(1).matches("wkattack\\d+for(\\d+|continuous)+")) {
                int amount = Integer.parseInt(matcher.group(1).replaceFirst("wkattack", "")
                        .replaceFirst("for\\d+", ""));
                if (matcher.group(1).replaceFirst("wkattack\\d+for", "").matches(CONTINUOUS)){
                    Buff buff = new Buff(Buff.BuffType.Weakness,PERMENANT,0,amount,false);
                    buff.makeContinuous();
                    addBuffs(targetCards,buff);
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replaceFirst("wkattack\\d+for", ""));
                Buff buff = new Buff(Buff.BuffType.Weakness,turns,0,amount,false);
                addBuffs(targetCards,buff);
            }




        }
    }

    private void addBuffs(ArrayList<Card> targetCards, Buff buff) {
        for (Card card : targetCards) {
            ((Deployable) card).addBuff(buff);
        }
    }

    private void addUnholyBuff(ArrayList<Card> targetCards) {
        Buff buff = new Buff(Buff.BuffType.Unholy, PERMENANT, 0, 0, false);
        addBuffs(targetCards, buff);
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

    public void player1Won() {
        MatchHistory matchHistory = new MatchHistory(player2.getAccount().getUsername(), "win");
        player1.getAccount().addMatchHistories(matchHistory);
        matchHistory = new MatchHistory(player1.getAccount().getUsername(), "lose");
        player2.getAccount().addMatchHistories(matchHistory);
    }

    public void player2Won() {
        MatchHistory matchHistory = new MatchHistory(player2.getAccount().getUsername(), "lose");
        player1.getAccount().addMatchHistories(matchHistory);
        matchHistory = new MatchHistory(player1.getAccount().getUsername(), "win");
        player2.getAccount().addMatchHistories(matchHistory);
    }

    public void draw() {
        MatchHistory matchHistory = new MatchHistory(player2.getAccount().getUsername(), "draw");
        player1.getAccount().addMatchHistories(matchHistory);
        matchHistory = new MatchHistory(player1.getAccount().getUsername(), "draw");
        player2.getAccount().addMatchHistories(matchHistory);
    }

    public abstract Player getOtherPlayer();
}
