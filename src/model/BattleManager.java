package model;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Random;
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
                                    int x1, int x2, Deployable attackTarget) {
        try {
            Pattern pattern = Pattern.compile(TargetStrings.MINIONS_WITH_DISTANCE + "(\\d+)");
            Matcher matcher = pattern.matcher(target);
            if (matcher.matches()) {
                int distance = Integer.parseInt(matcher.group(1));
                for (int i = 0; i < distance * 2; i++) {
                    for (int j = 0; j < distance * 2; j++) {
                        Card cardInCell = Map.getCardInCell(x1 - distance + i, x2 - distance + j);
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
                Card card = Map.getCardInCell(x1, x2);
                if (card != null &&
                        card.getAccount().equals(currentPlayer.getAccount())) {
                    targetCards.add(card);
                } else {
                    //error message for view
                }
            }

            if (target.matches("(.*)" + TargetStrings.ANY_UNIT + "(.*)")) {
                Card card = Map.getCardInCell(x1, x2);
                if (card != null) {
                    targetCards.add(card);
                } else {
                    //error message for view


                }
            }

            if (target.matches("(.*)" + TargetStrings.ALL_ENEMIES + "(.*)")) {
                targetCards.addAll(getOtherPlayer().getCardsOnBattleField());
            } else if (target.matches("(.*)" + TargetStrings.ALL_ENEMIES_IN_COLUMN + "(.*)")) {
                for (int i = 1; i <= Map.MAP_X2_LENGTH; i++) {
                    if (!Map.getCardInCell(i, x2).getAccount().equals(currentPlayer.getAccount())) {
                        targetCards.add(Map.getCardInCell(i, x2));
                    }
                }
            } else if (target.matches("(.*)" + TargetStrings.ALL_ENEMIES_IN_ROW + "(.*)")) {
                for (int i = 1; i <= Map.MAP_X1_LENGTH; i++) {
                    if (!Map.getCardInCell(x1, i).getAccount().equals(currentPlayer.getAccount())) {
                        targetCards.add(Map.getCardInCell(x1, i));
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
                if (Map.getCardInCell(x1, x2) != null
                        && Map.getCardInCell(x1, x2).getType() == CardType.minion
                        && Map.getCardInCell(x1, x2).getAccount().equals(currentPlayer.getAccount())) {
                    // isn't it better if we make haveCardInBattle instead of .equals ?
                    targetCards.add(Map.getCardInCell(x1, x2));
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
                for (int i = x1; i < Integer.parseInt(matcher.group(1)); i++) {
                    for (int j = x2; j < Integer.parseInt(matcher.group(1)); j++) {
                        targetCells.add(Map.getCell(i, j));
                        targetCards.add(Map.getCardInCell(i, j));
                    }
                }
            }

            if (target.matches("(.*)" + TargetStrings.SURROUNDING_ALLIED_MINIONS + "(.*)")) {
                for (int i = x1 - 1; i < x1 + 2; i++) {
                    for (int j = x2 - 1; j < x2 + 2; j++) {
                        if (Map.getCardInCell(x1, x2).getAccount().equals(currentPlayer.getAccount())) {
                            targetCards.add(Map.getCardInCell(x1, x2));
                        }
                    }
                }
            }

            if (target.matches("(.*)" + TargetStrings.SURROUNDING_ENEMY_MINIONS + "(.*)")) {
                for (int i = x1 - 1; i < x1 + 2; i++) {
                    for (int j = x2 - 1; j < x2 + 2; j++) {
                        if (!Map.getCardInCell(x1, x2).getAccount().equals(currentPlayer.getAccount())) {
                            targetCards.add(Map.getCardInCell(x1, x2));
                        }
                    }
                }
            }

            if (target.matches("(.*)" + TargetStrings.RANDOM_SURROUNDING_ENEMY + "(.*)")){
                ArrayList<Card> cardsToPickFrom = new ArrayList<>();
                for (int i = x1 - 1; i < x1 + 2; i++) {
                    for (int j = x2 - 1; j < x2 + 2; j++) {
                        if (!Map.getCardInCell(x1, x2).getAccount().equals(currentPlayer.getAccount())) {
                            cardsToPickFrom.add(Map.getCardInCell(x1, x2));
                        }
                    }
                }

                Random random = new Random();
                targetCards.add(cardsToPickFrom.get(random.nextInt(cardsToPickFrom.size())));

            }




            // pattern = Pattern.compile(TargetStrings.)
        } catch (IllegalStateException e) {
            //Input error message for view
        }
    }

    public void compileFunction(Function function, int x1, int x2) {
        compileFunction(function, x1, x2, null);
    }

    public void compileFunction(Function function, int x1, int x2, Deployable attackTarget) {
        ArrayList<Cell> targetCells = new ArrayList<>();
        ArrayList<Card> targetCards = new ArrayList<>();
        compileTargetString(targetCards, targetCells, function.getTarget(), x1, x2, attackTarget);

        try {
            handleBuffs(function, targetCards);

            handleDamage(function, targetCards);

            handleDispel(function, targetCards);

            handleAttackIncrease(function, targetCards);

            handleFireAndPoisonCells(function, targetCells);

            if (function.getFunction().matches("(.*)" + FunctionStrings.KILL_TARGETS + "(.*)")){
                for (Card card: targetCards){
                    ((Deployable) card).
                }
            }


        } catch (IllegalStateException e) {
            //error message for view

        }
    }

    private void handleFireAndPoisonCells(Function function, ArrayList<Cell> targetCells) {
        if (function.getFunction().matches("(.*)" + FunctionStrings.POISON_CELL + "(.*)")){
            for (Cell cell: targetCells){
                cell.setPoisoned(true);
            }
        }
        if (function.getFunction().matches("(.*)" + FunctionStrings.SET_ON_FIRE + "(.*)")){
            for (Cell cell: targetCells){
                cell.setOnFire(true);
            }
        }
    }

    private void handleAttackIncrease(Function function, ArrayList<Card> targetCards) {
        Pattern pattern = Pattern.compile(FunctionStrings.INCREASE_ATTACK + "(\\d+)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()){
            int amount = Integer.parseInt(matcher.group(1));
            for (Card card: targetCards){
                ((Deployable) card).increaseAttack(amount);
            }
        }
    }

    private void handleDispel(Function function, ArrayList<Card> targetCards) {
        if (function.getFunction().matches("(.*)" + FunctionStrings.DISPEL + "(.*)")){
            for (Card card: targetCards){
                ArrayList<Buff> toRemove = new ArrayList<>();
                if (card.getAccount().equals(currentPlayer.getAccount())){
                    for (Buff buff: ((Deployable) card).getBuffs()){
                        if (!buff.isBeneficial()){
                            if (buff.isContinuous()){
                                buff.setActive(false);
                            }
                            else {
                                toRemove.add(buff);
                            }
                        }
                    }
                }
                else {
                    for (Buff buff: ((Deployable) card).getBuffs()){
                        if (buff.isBeneficial()){
                            if (buff.isContinuous()){
                                buff.setActive(false);
                            }
                            else {
                                toRemove.add(buff);
                            }
                        }
                    }
                }
                ((Deployable) card).getBuffs().removeAll(toRemove);
            }
        }
    }

    private void handleDamage(Function function, ArrayList<Card> targetCards) {
        Pattern pattern = Pattern.compile(FunctionStrings.DEAL_DAMAGE + "(\\d+)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()) {
            int amount = Integer.parseInt(matcher.group(1));
            for (Card card : targetCards) {
                ((Deployable) card).takeDamage(amount);
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
                if (matcher.group(1).replace("disarm", "").matches(CONTINUOUS)) {
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
                if (matcher.group(1).replace("holy", "").matches(CONTINUOUS)) {
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
                if (matcher.group(1).replace("stun", "").matches(CONTINUOUS)) {
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
                if (matcher.group(1).replaceFirst("pwhealth\\d+for", "").matches(CONTINUOUS)) {
                    Buff buff = new Buff(Buff.BuffType.Power, PERMENANT, amount, 0, true);
                    buff.makeContinuous();
                    addBuffs(targetCards, buff);
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replaceFirst("pwhealth\\d+for", ""));
                Buff buff = new Buff(Buff.BuffType.Power, turns, amount, 0, true);
                addBuffs(targetCards, buff);
            }

            if (matcher.group(1).matches("pwattack\\d+for(\\d+|continuous)")) {
                int amount = Integer.parseInt(matcher.group(1).replaceFirst("pwattack", "")
                        .replaceFirst("for\\d+", ""));
                if (matcher.group(1).replaceFirst("pwattack\\d+for", "").matches(CONTINUOUS)) {
                    Buff buff = new Buff(Buff.BuffType.Power, PERMENANT, 0, amount, true);
                    buff.makeContinuous();
                    addBuffs(targetCards, buff);
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replaceFirst("pwattack\\d+for", ""));
                Buff buff = new Buff(Buff.BuffType.Power, turns, 0, amount, true);
                addBuffs(targetCards, buff);
            }

            if (matcher.group(1).matches("wkhealth\\d+for(\\d+|continuous)")) {
                int amount = Integer.parseInt(matcher.group(1).replaceFirst("wkhealth", "")
                        .replaceFirst("for\\d+", ""));
                if (matcher.group(1).replaceFirst("wkhealth\\d+for", "").matches(CONTINUOUS)) {
                    Buff buff = new Buff(Buff.BuffType.Weakness, PERMENANT, amount, 0, false);
                    buff.makeContinuous();
                    addBuffs(targetCards, buff);
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replaceFirst("wkhealth\\d+for", ""));
                Buff buff = new Buff(Buff.BuffType.Weakness, turns, amount, 0, false);
                addBuffs(targetCards, buff);
            }

            if (matcher.group(1).matches("wkattack\\d+for(\\d+|continuous)+")) {
                int amount = Integer.parseInt(matcher.group(1).replaceFirst("wkattack", "")
                        .replaceFirst("for\\d+", ""));
                if (matcher.group(1).replaceFirst("wkattack\\d+for", "").matches(CONTINUOUS)) {
                    Buff buff = new Buff(Buff.BuffType.Weakness, PERMENANT, 0, amount, false);
                    buff.makeContinuous();
                    addBuffs(targetCards, buff);
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replaceFirst("wkattack\\d+for", ""));
                Buff buff = new Buff(Buff.BuffType.Weakness, turns, 0, amount, false);
                addBuffs(targetCards, buff);
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

    private boolean checkCoordinates(int x1, int x2) {
        if (Map.getCell(x1, x2) == null ||
                Map.getCardInCell(x1, x2) != null) {
            return false;
        }


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != 1 || j != 1) {
                    if (Map.getCardInCell(x1, x2).getAccount().equals(currentPlayer.getAccount())) { // x , y chera ?
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

    public void playSpell(Spell spell, int x1, int x2) {

    }

    public void useItem(Item item, int x1, int x2) {

    }

    public void move(Deployable card, int x1, int x2) {
        if (Map.getDistance(Map.getCell(x1, x2), card.cell) <= Map.getMaxMoveRange()) {
            if (Map.getCell(x1, x2).getCardInCell() == null && !card.isMoved && !card.isStunned()) {
                card.cell = Map.getCell(x1, x2);
                Map.getCell(x1, x2).setCardInCell(card);
            }
        }
    }

    public void attack(Deployable card, Deployable enemy) {
        if (isNear(card.cell, enemy.cell) && !card.isAttacked && !card.isStunned() &&
                isAttackTypeValidForAttack(card, enemy)) {
            for (Function function : card.getFunctions()) {
                if (function.getFunctionType() == FunctionType.OnAttack) {
                    int x1 = card.cell.getX1Coordinate();
                    int x2 = card.cell.getX2Coordinate();
                    compileFunction(function, x1, x2, enemy);
                }
            }
            enemy.currentHealth -= enemy.theActualDamageReceived(card.theActualDamage());

            counterAttack(card, enemy);
        }
    }

    private void counterAttack(Deployable attacker, Deployable counterAttacker) {
        if (!counterAttacker.isDisarmed() && isAttackTypeValidForCounterAttack(attacker, counterAttacker)) {
            attacker.currentHealth -= attacker.theActualDamageReceived(counterAttacker.theActualDamage());
        }
    }

    private boolean isAttackTypeValidForAttack(Deployable attacker, Deployable counterAttacker) {
        return attacker.attackType.equals("melee") && isNear(attacker.cell, counterAttacker.cell) ||
                (attacker.attackType.equals("ranged") &&
                        Map.getDistance(attacker.cell, counterAttacker.cell) <= attacker.attackRange) ||
                (attacker.attackType.equals("hybrid") &&
                        Map.getDistance(attacker.cell, counterAttacker.cell) <= attacker.attackRange);
    }

    private boolean isAttackTypeValidForCounterAttack(Deployable attacker, Deployable counterAttacker) {
        return counterAttacker.attackType.equals("melee") && isNear(attacker.cell, counterAttacker.cell) ||
                (counterAttacker.attackType.equals("ranged") && !isNear(attacker.cell, counterAttacker.cell) &&
                        Map.getDistance(attacker.cell, counterAttacker.cell) <= counterAttacker.attackRange) ||
                counterAttacker.attackType.equals("hybrid");
    }


    private boolean isNear(Cell cell1, Cell cell2) {
        return Math.abs(cell1.getX1Coordinate() - cell2.getX1Coordinate()) < 2 &&
                Math.abs(cell1.getX2Coordinate() - cell2.getX2Coordinate()) < 2;
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
