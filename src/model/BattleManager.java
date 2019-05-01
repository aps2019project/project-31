package model;

import org.graalvm.compiler.replacements.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BattleManager {
    public static final int PERMENANT = 100;
    public static final String CONTINUOUS = "continuous";
    protected Map map;
    protected String gameMode;
    protected Player currentPlayer;
    protected Player player1;
    protected Player player2;
    protected BattleType gameMode;
    protected final int maxNumberOfFlags;
    protected final int maxTurnsOfHavingFlag;

    public int getMaxNumberOfFlags() {
        return maxNumberOfFlags;
    }

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

    public BattleManager(Map map, String gameMode, Player currentPlayer, int maxNumberOfFlags) {
        this.map = map;
        this.gameMode = gameMode;
        this.currentPlayer = currentPlayer;
        this.maxNumberOfFlags = maxNumberOfFlags;
        maxTurnsOfHavingFlag = 0;
    }

    public boolean playMinion(Minion minion, int x, int y) {
        if (!checkCoordinates(x, y)) {
            //insert invalid coordinates error for view
            Log.println("Invalid Coordinates");
            return false;

        }

        if (minion.manaCost > currentPlayer.getMana()) {
            //insert not enough mana message for view
            Log.println("Not enough mana");
            return false;
        }

        for (Function function : minion.getFunctions()) {
            if (function.getFunctionType() == FunctionType.OnSpawn) {
                compileFunction(function, x, y);
            }
        }
        Map.putCardInCell(minion, x, y);
        currentPlayer.addCardToBattlefield(minion);
        currentPlayer.removeFromHand(minion);
        return true;

    }

    public boolean compileTargetString(ArrayList<Card> targetCards, ArrayList<Cell> targetCells, String target,
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
                    return false;
                }
            }

            if (target.matches("(.*)" + TargetStrings.ANY_UNIT + "(.*)")) {
                Card card = Map.getCardInCell(x1, x2);
                if (card != null) {
                    targetCards.add(card);
                } else {
                    //error message for view
                    return false;

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
                    return false;

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
                addSurroundingCards(targetCards, x1, x2);
            }

            if (target.matches("(.*)" + TargetStrings.RANDOM_SURROUNDING_ENEMY_MINION + "(.*)")) {
                ArrayList<Card> cardsToPickFrom = new ArrayList<>();
                addSurroundingCards(cardsToPickFrom, x1, x2);
                Random random = new Random();
                targetCards.add(cardsToPickFrom.get(random.nextInt(cardsToPickFrom.size())));

            }

            if (target.matches("(.*)" + TargetStrings.RANDOM_ENEMY_MINION + "(.*)")) {
                ArrayList<Card> cardsToPickFrom = new ArrayList<>();
                for (Card card : getOtherPlayer().getCardsOnBattleField()) {
                    if (card.getType() == CardType.minion) {
                        cardsToPickFrom.add(card);
                    }
                }
                Random random = new Random();
                targetCards.add(cardsToPickFrom.get(random.nextInt(cardsToPickFrom.size())));


            }


        } catch (IllegalStateException e) {
            //Input error message for view
            Log.println(e.toString());
            return false;
        }
        return true;
    }

    private void addSurroundingCards(ArrayList<Card> list, int x1, int x2) {
        for (int i = x1 - 1; i < x1 + 2; i++) {
            for (int j = x2 - 1; j < x2 + 2; j++) {
                if (!Map.getCardInCell(x1, x2).getAccount().equals(currentPlayer.getAccount())) {
                    list.add(Map.getCardInCell(x1, x2));
                }
            }
        }
    }

    public void compileFunction(Function function, int x1, int x2) {
        compileFunction(function, x1, x2, null);
    }

    public void compileFunction(Function function, int x1, int x2, Deployable attackTarget) {
        ArrayList<Cell> targetCells = new ArrayList<>();
        ArrayList<Card> targetCards = new ArrayList<>();
        if (!compileTargetString(targetCards, targetCells, function.getTarget(), x1, x2, attackTarget)) {
            Log.println("Invalid target");
            return;
        }

        try {
            handleBuffs(function, targetCards);

            handleDamage(function, targetCards);

            handleDispel(function, targetCards);

            handleAttackIncrease(function, targetCards);

            handleFireAndPoisonCells(function, targetCells);

            handleMurder(function, targetCards);

            handleIndisarmable(function, x1, x2);

            handleUnpoisonable(function, x1, x2);

            handleAccumilatingAttack(function, x1, x2, attackTarget);

        } catch (IllegalStateException e) {
            //error message for view

        }
    }

    private void handleAccumilatingAttack(Function function, int x1, int x2, Deployable attackTarget) {
        Pattern pattern = Pattern.compile(FunctionStrings.ACCUMULATING_ATTACKS + "(\\d+)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()) {
            int amount = Integer.parseInt(matcher.group(1));
            attackTarget.takeDamage(attackTarget.accumilatingAttack((Deployable) Map.getCardInCell(x1, x2)) * amount);
        }
    }

    private void handleUnpoisonable(Function function, int x1, int x2) {
        if (function.getFunction().matches("(.*)" + FunctionStrings.UNPOISONABLE + "(.*)")) {
            ArrayList<Buff> toRemove = new ArrayList<>();
            for (Buff buff : ((Deployable) Map.getCardInCell(x1, x2)).getBuffs()) {
                if (buff.getBuffType() == Buff.BuffType.Poison) {
                    toRemove.add(buff);
                }
            }
            ((Deployable) Map.getCardInCell(x1, x2)).getBuffs().removeAll(toRemove);
        }
    }

    private void handleIndisarmable(Function function, int x1, int x2) {
        if (function.getFunction().matches("(.*)" + FunctionStrings.INDISARMABLE + "(.*)")) {
            ArrayList<Buff> toRemove = new ArrayList<>();
            for (Buff buff : ((Deployable) Map.getCardInCell(x1, x2)).getBuffs()) {
                if (buff.getBuffType() == Buff.BuffType.Disarm) {
                    toRemove.add(buff);
                }
            }
            ((Deployable) Map.getCardInCell(x1, x2)).getBuffs().removeAll(toRemove);
        }
    }

    private void handleMurder(Function function, ArrayList<Card> targetCards) {
        if (function.getFunction().matches("(.*)" + FunctionStrings.KILL_TARGETS + "(.*)")) {
            for (Card card : targetCards) {
                killTheThing((Deployable) card);
            }
        }
    }

    private void handleFireAndPoisonCells(Function function, ArrayList<Cell> targetCells) {
        Pattern pattern = Pattern.compile(FunctionStrings.POISON_CELL + "(\\d+)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()) {
            int turns = Integer.parseInt(matcher.group(1));
            for (Cell cell : targetCells) {
                cell.setOnFireTurns(turns);
            }
        }
        pattern = Pattern.compile(FunctionStrings.SET_ON_FIRE + "(\\d+)");
        matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()) {
            int turns = Integer.parseInt(matcher.group(1));
            for (Cell cell : targetCells) {
                cell.setOnPoisonTurns(turns);
            }
        }
    }

    private void handleAttackIncrease(Function function, ArrayList<Card> targetCards) {
        Pattern pattern = Pattern.compile(FunctionStrings.INCREASE_ATTACK + "(\\d+)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()) {
            int amount = Integer.parseInt(matcher.group(1));
            for (Card card : targetCards) {
                ((Deployable) card).increaseAttack(amount);
            }
        }
    }

    private void handleDispel(Function function, ArrayList<Card> targetCards) {
        if (function.getFunction().matches("(.*)" + FunctionStrings.DISPEL + "(.*)")) {
            for (Card card : targetCards) {
                ArrayList<Buff> toRemove = new ArrayList<>();
                if (card.getAccount().equals(currentPlayer.getAccount())) {
                    for (Buff buff : ((Deployable) card).getBuffs()) {
                        if (!buff.isBeneficial()) {
                            if (buff.isContinuous()) {
                                buff.setActive(false);
                            } else {
                                toRemove.add(buff);
                            }
                        }
                    }
                } else {
                    for (Buff buff : ((Deployable) card).getBuffs()) {
                        if (buff.isBeneficial()) {
                            if (buff.isContinuous()) {
                                buff.setActive(false);
                            } else {
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
            Pattern pattern1 = Pattern.compile(FunctionStrings.BLEED + "(\\d+)(\\d+)");
            Matcher matcher1 = pattern.matcher(matcher.group(1));
            if (matcher1.matches()) {
                int one = Integer.parseInt(matcher1.group(1));
                int two = Integer.parseInt(matcher1.group(2));
                Buff buff = new Buff(Buff.BuffType.Bleed, 2, 0,
                        0, false);
                buff.setBleed(one, two);
                addBuffs(targetCards, buff);
            }
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
                    if (Map.getCardInCell(x1 - 1 + i, x2 - 1 + j).getAccount().equals(currentPlayer.getAccount())) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public Card cardInHandByCardId(int cardId) {
        for (Card card : currentPlayer.getHand()) {
            if (card.getId() == cardId) {
                return card;
            }
        }
        return null;
    }

    public boolean isInHand(Card card) {
        for (Card card1 : currentPlayer.getHand()) {
            if (card1.equals(card)) {
                return true;
            }
        }
        return false;
    }

    public boolean playSpell(Spell spell, int x1, int x2) {
        return true;
    }

    public boolean useItem(Item item, int x1, int x2) {
        return true;
    }

    public void move(Deployable card, int x1, int x2) {
        if (Map.getDistance(Map.getCell(x1, x2), card.cell) <= Map.getMaxMoveRange()) {
            if (Map.getCell(x1, x2).getCardInCell() == null && !card.isMoved && !card.isStunned()) {
                card.cell = Map.getCell(x1, x2);
                Map.getCell(x1, x2).setCardInCell(card);
                //cardid move to x1,x2
            } else {
                //invalid target
            }
        } else {
            //invalid target
        }
    }

    public void killTheThing(Deployable enemy) {
        for (Function function : enemy.functions) {
            if (function.getFunctionType() == FunctionType.OnDeath) {
                compileFunction(function, enemy.cell.getX1Coordinate(), enemy.cell.getX2Coordinate());
            }
        }
        Cell cell = Map.findCellByCardId(enemy.uniqueId);
        cell.setCardInCell(null);
        if (player1.doesPlayerHaveDeployable(enemy))
            player1.getCardsOnBattleField().remove(enemy);
        else
            player2.getCardsOnBattleField().remove(enemy);
    }

    public void comboAtack(Deployable enemy, ArrayList<Deployable> comboAttackers) {
        attack(comboAttackers.get(0), enemy);
        for (int i = 1; i < comboAttackers.size(); i++) {
            dealAttackDamageAndDoOtherStuff(comboAttackers.get(i), enemy);
        }

    }

    public void attack(Deployable card, Deployable enemy) {
        if (!card.isAttacked && !card.isStunned() && isAttackTypeValidForAttack(card, enemy)) {
            dealAttackDamageAndDoOtherStuff(card, enemy);
            counterAttack(card, enemy);
        } else {
            //opponent minion is unavailable for attack OR Card with [card id] can′t attack message
        }
    }

    private void dealAttackDamageAndDoOtherStuff(Deployable card, Deployable enemy) {
        if (!card.isAttacked && !card.isStunned() && isAttackTypeValidForAttack(card, enemy)) {
            enemy.currentHealth -= enemy.theActualDamageReceived(card.theActualDamage());
            if (enemy.currentHealth <= 0) {
                killTheThing(enemy);
            }
            applyOnAttackFunction(card);
            applyOnDefendFunction(enemy);
        }
    }

    private void applyOnAttackFunction(Deployable card) {
        for (Function function : card.functions) {
            if (function.getFunctionType() == FunctionType.OnAttack) {
                compileFunction(function, card.cell.getX1Coordinate(), card.cell.getX2Coordinate());
            }
        }
    }

    private void applyOnDefendFunction(Deployable enemy) {
        for (Function function : enemy.functions) {
            if (function.getFunctionType() == FunctionType.OnDefend) {
                compileFunction(function, enemy.cell.getX1Coordinate(), enemy.cell.getX2Coordinate());
            }

        }
    }

    private void counterAttack(Deployable attacker, Deployable counterAttacker) {
        if (!counterAttacker.isDisarmed() && isAttackTypeValidForCounterAttack(attacker, counterAttacker)) {
            attacker.currentHealth -= attacker.theActualDamageReceived(counterAttacker.theActualDamage());
        }
    }

    public static boolean isAttackTypeValidForAttack(Deployable attacker, Deployable counterAttacker) {
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


    public static boolean isNear(Cell cell1, Cell cell2) {
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

    public void checkTheEndSituation() {
        switch (gameMode) {
            case :
            isFinishedDueToHeroDying();
            break;
            case:
            isFinishedDueToHeroDying();
            isFinishedDueToHavingTheFlag();
            break;
            case :
            isFinishedDueToHeroDying();
            isFinishedDueToHavingMostOfFlags();
            break;
        }
    }

    public void isFinishedDueToHavingTheFlag() {
        if (player1.getNumberOfTurnsHavingFlag() >= maxTurnsOfHavingFlag)
            player1Won();
        if (player2.getNumberOfTurnsHavingFlag() >= maxTurnsOfHavingFlag)
            player2Won();
    }

    public void isFinishedDueToHeroDying() {
        if (player1.isHeroDead() && !player2.isHeroDead())
            player2Won();
        if (player2.isHeroDead() && !player1.isHeroDead())
            player1Won();
        if (player1.isHeroDead() && player2.isHeroDead())
            draw();
    }

    public void isFinishedDueToHavingMostOfFlags() {
        if (2 * player1.getNumbereOfFlags() > maxNumberOfFlags)
            player1Won();
        if (2 * player1.getNumbereOfFlags() > maxNumberOfFlags)
            player2Won();
    }


}
