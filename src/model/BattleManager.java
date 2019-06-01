package model;

import constants.AttackType;
import constants.CardType;
import constants.FunctionType;
import constants.GameMode;
import controller.BattleMenu;
import view.Input;
import view.Output;

import javax.print.DocFlavor;
import java.util.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BattleManager {
    public static final int PERMANENT = 100;
    public static final String CONTINUOUS = "continuous";
    protected static GameMode gameMode;
    protected static Player currentPlayer;
    protected static Player player1;
    protected static Player player2;
    protected final int maxNumberOfFlags;
    protected final int maxTurnsOfHavingFlag;
    protected int turn = 1;
    private int[] turnsAppearingTheCollectibleFlags = {2, 3, 5, 8, 11, 12, 15, 18, 20, 21, 24, 27, 31, 32, 36, 37,
            40, 43, 46, 49};

    public int getTurn() {
        return turn;
    }

    public BattleManager(Player player1, Player player2, int maxNumberOfFlags, int maxTurnsOfHavingFlag, GameMode gameMode) {
        this.maxNumberOfFlags = maxNumberOfFlags;
        this.maxTurnsOfHavingFlag = maxTurnsOfHavingFlag;
        setPlayer1(player1);
        setPlayer2(player2);
        BattleManager.gameMode = gameMode;
    }


    public static void setPlayer1(Player player1) {
        BattleManager.player1 = player1;
    }

    public static void setPlayer2(Player player2) {
        BattleManager.player2 = player2;
    }


    public int getMaxNumberOfFlags() {
        return maxNumberOfFlags;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        BattleManager.currentPlayer = currentPlayer;
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


    public static GameMode getGameMode() {
        return gameMode;
    }

    public boolean playMinion(Minion minion, int x1, int x2) {
        Minion theMinion = minion.duplicateDeployed(Map.getCell(x1, x2), currentPlayer.account);
        Map.putCardInCell(theMinion, x1, x2);
        if (Map.getCell(x1, x2).doesHaveFlag()) {
            Map.getCell(x1, x2).setHasFlag(false);
            theMinion.setHasFlag(true);
            if (gameMode == GameMode.Domination)
                currentPlayer.numberOfFlags++;
        }
        Output.insertionSuccessful(theMinion, x1, x2);
        applyItemFunctions(theMinion, FunctionType.OnSpawn);
        currentPlayer.addCardToBattlefield(theMinion);
        currentPlayer.removeFromHand(minion);
        applyOnSpawnFunction(theMinion);
        currentPlayer.decreaseMana(theMinion.manaCost);
        currentPlayer.selectedCard = null;
        return true;

    }

    public void addTurn() {
        turn++;
    }

    public static int generateUniqueId(int cardId) {
        int numberOfMinionInBattleField = 0;
        for (Deployable card : player1.cardsOnBattleField) {
            if (card.id == cardId)
                numberOfMinionInBattleField++;
        }
        for (Deployable card : player2.cardsOnBattleField) {
            if (card.id == cardId)
                numberOfMinionInBattleField++;
        }
        return (cardId * 100) + numberOfMinionInBattleField;

    }

    public static int generateUniqueIdForCollectibleItem(int cardId) {
        int numberOfItems = 0;
        for (Cell[] cells : Map.getMap()) {
            for (Cell cell : cells) {
                if (cell.getItem() != null && cell.getItem().getId() == cardId)
                    numberOfItems++;
            }
        }
        return (cardId * 100) + numberOfItems;
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

            if (target.matches("(.*)" + TargetStrings.RANDOM_ALLIED_UNIT + "(.*)")) {
                Random random = new Random();
                targetCards.add(currentPlayer.getCardsOnBattleField().
                        get(random.nextInt(currentPlayer.getCardsOnBattleField().size())));
            }

            if (target.matches("(.*)" + TargetStrings.RANDOM_ENEMY_UNIT + "(.*)")) {
                Random random = new Random();
                targetCards.add(getOtherPlayer().getCardsOnBattleField().
                        get(random.nextInt(getOtherPlayer().getCardsOnBattleField().size())));
            }

            if (target.matches("(.*)" + TargetStrings.ALL_MELEE_UNITS + "(.*)")) {
                for (Deployable deployable : currentPlayer.getCardsOnBattleField()) {
                    if (deployable.getAttackType() == AttackType.melee) {
                        targetCards.add(deployable);
                    }
                }

                for (Deployable deployable : getOtherPlayer().getCardsOnBattleField()) {
                    if (deployable.getAttackType() == AttackType.melee) {
                        targetCards.add(deployable);
                    }
                }
            }
            if (target.matches("(.*)" + TargetStrings.RANDOM_MINION + "(.*)")) {
                ArrayList<Deployable> deployables = new ArrayList<>();
                deployables.addAll(getOtherPlayer().getCardsOnBattleField());
                deployables.addAll(currentPlayer.getCardsOnBattleField());
                deployables.remove(currentPlayer.getHero());
                deployables.remove(getOtherPlayer().getHero());
                Random random = new Random();
                targetCards.add(deployables.get(random.nextInt(deployables.size())));
            }

            if (target.matches("(.*)" + TargetStrings.RANDOM_UNIT + "(.*)")) {
                ArrayList<Deployable> deployables = new ArrayList<>();
                deployables.addAll(getOtherPlayer().getCardsOnBattleField());
                deployables.addAll(currentPlayer.getCardsOnBattleField());
                Random random = new Random();
                targetCards.add(deployables.get(random.nextInt(deployables.size())));
            }

            if (target.matches("(.*)" + TargetStrings.RANDOM_RANGED_HYBRID + "(.*)")) {
                ArrayList<Deployable> deployables = new ArrayList<>();
                for (Deployable deployable : currentPlayer.getCardsOnBattleField()) {
                    if (deployable.getAttackType() == AttackType.ranged ||
                            deployable.getAttackType() == AttackType.hybrid) {
                        deployables.add(deployable);
                    }
                }
                for (Deployable deployable : getOtherPlayer().getCardsOnBattleField()) {
                    if (deployable.getAttackType() == AttackType.ranged ||
                            deployable.getAttackType() == AttackType.hybrid) {
                        deployables.add(deployable);
                    }
                }
                Random random = new Random();
                targetCards.add(deployables.get(random.nextInt(deployables.size())));
            }

            if (target.matches("(.*)" + TargetStrings.ALLIED_GENERAL_RANGED_HYBRID + "(.*)")) {
                if (currentPlayer.getHero().getAttackType() == AttackType.ranged ||
                        currentPlayer.getHero().getAttackType() == AttackType.hybrid)
                    targetCards.add(currentPlayer.getHero());

            }
            if (target.matches("(.*)" + TargetStrings.ENEMY_GENERAL_RANGED_HYBRID + "(.*)")) {
                if (getOtherPlayer().getHero().getAttackType() == AttackType.ranged ||
                        getOtherPlayer().getHero().getAttackType() == AttackType.hybrid) {
                    targetCards.add(getOtherPlayer().getHero());
                }
            }

            if (target.matches("(.*)" + TargetStrings.ALLIED_MINION + "(.*)")) {
                Card card = Map.getCardInCell(x1, x2);
                if (card == null || card.getAccount() == null) {
                    System.err.println("null mikhorim to allied_minion");
                    return false;
                }
                if (card.getAccount().equals(currentPlayer.getAccount()) &&
                        card.getType() == CardType.minion) {
                    targetCards.add(card);
                } else {
                    //Invalid target
                    System.err.println("Invalid target");
                    return false;
                }
            }

            if (target.matches("(.*)" + TargetStrings.ALLIED_GENERAL_ROW + "(.*)")) {
                addEnemiesInRow(targetCards, currentPlayer.getHero().getCell().getX1Coordinate());
            }

            if (target.matches("(.*)" + TargetStrings.ALLIED_HERO + "(.*)")) {
                targetCards.add(getCurrentPlayer().getHero());
            }

            if (target.matches("(.*)" + TargetStrings.ENEMY + "(.*)")) {
                if (Map.getCardInCell(x1, x2) != null &&
                        Map.getCardInCell(x1, x2).getAccount().equals(currentPlayer.getAccount())) {
                    //wrong target
                    System.err.println("Invalid target");
                    return false;
                } else {
                    targetCards.add(Map.getCardInCell(x1, x2));
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
            } else if (target.matches("(.*)" + TargetStrings.ALL_ALLIED_MINIONS + "(.*)")) {
                targetCards.addAll(currentPlayer.getCardsOnBattleField());
                targetCards.remove(getCurrentPlayer().getHero());
                targetCards.remove(getOtherPlayer().getHero());
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
                    if (Map.getCardInCell(x1, x2) != null &&
                            !Map.getCardInCell(i, x2).getAccount().equals(currentPlayer.getAccount())) {
                        targetCards.add(Map.getCardInCell(i, x2));
                    }
                }
            } else if (target.matches("(.*)" + TargetStrings.ALL_ENEMIES_IN_ROW + "(.*)")) {
                addEnemiesInRow(targetCards, x1);
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
                        if (Map.getCardInCell(x1, x2) != null &&
                                Map.getCardInCell(x1, x2).getAccount().equals(currentPlayer.getAccount())) {
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
            System.err.println(e.toString());
            return false;
        }
        return true;
    }

    private void addEnemiesInRow(ArrayList<Card> targetCards, int rowNum) {
        for (int i = 1; i <= Map.MAP_X1_LENGTH; i++) {
            if (Map.getCardInCell(rowNum, i) != null &&
                    !Map.getCardInCell(rowNum, i).getAccount().equals(currentPlayer.getAccount())) {
                targetCards.add(Map.getCardInCell(rowNum, i));
            }
        }
    }

    private void addSurroundingCards(ArrayList<Card> list, int x1, int x2) {
        for (int i = x1 - 1; i < x1 + 2; i++) {
            for (int j = x2 - 1; j < x2 + 2; j++) {
                if (Map.getCardInCell(x1, x2) != null &&
                        !Map.getCardInCell(x1, x2).getAccount().equals(currentPlayer.getAccount())) {
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
            System.err.println("Invalid target");
            return;
        }

        try {
            handleBuffs(function, targetCards);

            handleDamage(function, targetCards);

            handleDispel(function, targetCards);

            handleAttackIncrease(function, targetCards);

            handleCells(function, targetCells);

            handleMurder(function, targetCards);

            handleIndisarmable(function, x1, x2);

            handleUnpoisonable(function, x1, x2);

            handleAccumilatingAttack(function, x1, x2, attackTarget);

            handleHealing(function, targetCards);

            handleGiveFunction(function, targetCards);

        } catch (IllegalStateException e) {
            //error message for view
            System.err.println(e.toString());
        }
    }

    private void handleGiveFunction(Function function, ArrayList<Card> targetCards) {
        Pattern pattern = Pattern.compile(FunctionStrings.GIVE_FUNCTION + "type:(.*)" + "function:(.*)" + "target:(.*)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()) {
            FunctionType functionType = null;
            switch (matcher.group(1).replaceAll("type:", "")) {
                case "OnDeath":
                    functionType = FunctionType.OnDeath;
                    break;
                case "OnAttack":
                    functionType = FunctionType.OnAttack;
                    break;
                case "OnDefend":
                    functionType = FunctionType.OnDefend;
            }
            Function function1 = new Function(functionType, matcher.group(2).replaceAll("function:", ""),
                    matcher.group(3).replaceAll("target:", ""));
            for (Card card : targetCards) {
                ((Deployable) card).addFunction(function1);
            }
        }
    }

    private void handleHealing(Function function, ArrayList<Card> targetCards) {
        Pattern pattern = Pattern.compile(FunctionStrings.HEAL + "(\\d+)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()) {
            for (Card card : targetCards) {
                ((Deployable) card).healUp(Integer.parseInt(matcher.group(1)));
            }
        }
    }

    private void handleAccumilatingAttack(Function function, int x1, int x2, Deployable attackTarget) {
        Pattern pattern = Pattern.compile(FunctionStrings.ACCUMULATING_ATTACKS + "(\\d+)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()) {
            int amount = Integer.parseInt(matcher.group(1));
            attackTarget.takeDamage(attackTarget.accumulatingAttack((Deployable) Map.getCardInCell(x1, x2)) * amount);
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

    private void handleCells(Function function, ArrayList<Cell> targetCells) {

        Pattern pattern = Pattern.compile(FunctionStrings.HOLY_CELL + "(\\d+)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()) {
            int turns = Integer.parseInt(matcher.group(1));
            for (Cell cell : targetCells) {
                cell.setIsHolyTurns(turns);
            }
        }
        pattern = Pattern.compile(FunctionStrings.POISON_CELL + "(\\d+)");
        matcher = pattern.matcher(function.getFunction());
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
        Pattern pattern = Pattern.compile(FunctionStrings.INCREASE_ATTACK + "([-+]?\\d+)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()) {
            int amount = Integer.parseInt(matcher.group(1));
            for (Card card : targetCards) {
                if (card != null)
                    ((Deployable) card).increaseAttack(amount);
            }
        }
    }

    public int[] getTurnsAppearingTheCollectibleFlags() {
        return turnsAppearingTheCollectibleFlags;
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
                if (card != null)
                    ((Deployable) card).takeDamage(amount);
            }
        }
    }

    private void handleBuffs(Function function, ArrayList<Card> targetCards) {
        Pattern pattern = Pattern.compile(FunctionStrings.APPLY_BUFF + "(.*)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()) {
            Pattern pattern1 = Pattern.compile(FunctionStrings.BLEED + "((\\d+\\s*)+)\\s*");
            Matcher matcher1 = pattern.matcher(matcher.group(1));
            if (matcher1.matches()) {
                String[] damages = matcher1.group(1).trim().split(" ");
                Buff buff = new Buff(Buff.BuffType.Bleed, damages.length + 1, 0,
                        0, false);
                buff.setBleed(damages);
                addBuffs(targetCards, buff);
            }
            if (matcher.group(1).trim().matches("unholy")) {
                addUnholyBuff(targetCards);
            }
            if (matcher.group(1).trim().matches("disarm(\\d+|continuous)")) {
                if (matcher.group(1).replace("disarm", "").matches(CONTINUOUS)) {
                    Buff buff = new Buff(Buff.BuffType.Disarm, PERMANENT, 0, 0, false);
                    buff.makeContinuous();
                    addBuffs(targetCards, buff);
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replace("disarm", ""));
                Buff buff = new Buff(Buff.BuffType.Disarm, turns, 0, 0, false);
                addBuffs(targetCards, buff);
            }
            if (matcher.group(1).trim().matches("(\\d+)holy(\\d+|continuous)")) {
                int amount = Integer.parseInt(matcher.group(1).replaceAll("holy(.*)", ""));
                if (matcher.group(1).replace("holy", "").matches(CONTINUOUS)) {
                    Buff buff = new Buff(Buff.BuffType.Holy, PERMANENT, 0, 0, true);
                    buff.makeContinuous();
                    for (int i = 0; i < amount; i++) {
                        addBuffs(targetCards, buff);
                    }
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replace("holy", ""));
                Buff buff = new Buff(Buff.BuffType.Holy, turns, 0, 0, true);
                for (int i = 0; i < amount; i++) {
                    addBuffs(targetCards, buff);
                }

            }
            if (matcher.group(1).trim().matches("holy(\\d+|continuous)")) {
                if (matcher.group(1).replace("holy", "").matches(CONTINUOUS)) {
                    Buff buff = new Buff(Buff.BuffType.Holy, PERMANENT, 0, 0, true);
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
                    Buff buff = new Buff(Buff.BuffType.Stun, PERMANENT, 0, 0, false);
                    buff.makeContinuous();
                    addBuffs(targetCards, buff);
                    return;
                }
                int turns = Integer.parseInt(matcher.group(1).replace("stun", ""));
                Buff buff = new Buff(Buff.BuffType.Stun, turns * 2, 0, 0, false);
                addBuffs(targetCards, buff);
            }

            if (matcher.group(1).matches("pwhealth\\d+for(\\d+|continuous)")) {
                int amount = Integer.parseInt(matcher.group(1).replaceFirst("pwhealth", "")
                        .replaceFirst("for(.*)", ""));
                if (matcher.group(1).replaceFirst("pwhealth\\d+for", "").matches(CONTINUOUS)) {
                    Buff buff = new Buff(Buff.BuffType.Power, PERMANENT, amount, 0, true);
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
                        .replaceFirst("for(.*)", ""));
                if (matcher.group(1).replaceFirst("pwattack\\d+for", "").matches(CONTINUOUS)) {
                    Buff buff = new Buff(Buff.BuffType.Power, PERMANENT, 0, amount, true);
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
                        .replaceFirst("for(.*)", ""));
                if (matcher.group(1).replaceFirst("wkhealth\\d+for", "").matches(CONTINUOUS)) {
                    Buff buff = new Buff(Buff.BuffType.Weakness, PERMANENT, amount, 0, false);
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
                        .replaceFirst("for(.*)", ""));
                if (matcher.group(1).replaceFirst("wkattack\\d+for", "").matches(CONTINUOUS)) {
                    Buff buff = new Buff(Buff.BuffType.Weakness, PERMANENT, 0, amount, false);
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
            if (card != null)
                ((Deployable) card).addBuff(buff);
        }
    }

    private void addUnholyBuff(ArrayList<Card> targetCards) {
        Buff buff = new Buff(Buff.BuffType.Unholy, PERMANENT, 0, 0, false);
        addBuffs(targetCards, buff);
    }

    public boolean checkCoordinates(int x1, int x2) {
        if (Map.getCell(x1, x2) == null ||
                Map.getCardInCell(x1, x2) != null) {
            return false;
        }


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != 1 || j != 1) {
                    if (x1 - 1 + i >= 1 && x1 - 1 + i <= Map.MAP_X1_LENGTH && x2 - 1 + j >= 1 &&
                            x2 - 1 + j <= Map.MAP_X2_LENGTH && Map.getCardInCell(x1 - 1 + i, x2 - 1 + j) != null) {
                        if (Map.getCardInCell(x1 - 1 + i, x2 - 1 + j).getAccount().equals(currentPlayer.getAccount())) {
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }

    public Card cardInHandByCardId(int cardId) {
        for (Card card : currentPlayer.getHand()) {
            if (card != null && card.getId() == cardId) {
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
        for (Function function : spell.functions) {
            compileFunction(function, x1, x2);
        }
        currentPlayer.decreaseMana(spell.manaCost);
        currentPlayer.selectedCard = null;
        if (!spell.equals(currentPlayer.getHero().heroSpell))
            currentPlayer.hand.remove(spell);
        return true;
    }

    public boolean useItem(Item item, int x1, int x2) {
        for (Function function : item.functions) {
            compileFunction(function, x1, x2);
        }
        if (item.id == 509)
            currentPlayer.increaseManaInTheTurn(turn + 2, 3);
        currentPlayer.decreaseMana(item.manaCost);
        currentPlayer.selectedCard = null;
        for (Deployable deployable : currentPlayer.cardsOnBattleField) {
            if (deployable != null && deployable.getItem() != null) {
                if (deployable.getItem().getId() == item.id)
                    deployable.setItem(null);
            }
        }
        return true;
    }


    public void move(Deployable card, int x1, int x2) {
        if (card.cell == null) {
            System.err.println("the cell is null in the move method");
            return;
        }
        if (Map.getDistance(Map.getCell(x1, x2), card.cell) <= Map.getMaxMoveRange()) {
            if (Map.getCell(x1, x2).getCardInCell() == null && !card.isMoved && !card.isStunned()) {
                if (!card.hasFlag && Map.getCell(x1, x2).doesHaveFlag()) {
                    if (gameMode == GameMode.Domination)
                        currentPlayer.numberOfFlags++;
                    card.setHasFlag(true);
                    Map.getCell(x1, x2).setHasFlag(false);
                }
                card.cell.setCardInCell(null);
                card.cell = Map.getCell(x1, x2);
                card.setMoved(true);
                if(card.cell.getItem()!=null && card.item!=null)
                Map.getCell(x1, x2).setCardInCell(card);
                Output.movedSuccessfully(card);
            } else {
                Output.invalidTargetForMove();
            }
        } else {
            Output.tooFarToMove();
        }
    }

    public void killTheThing(Deployable enemy) {
        Player player;
        if (player1.doesPlayerHaveDeployable(enemy))
            player = player1;
        else player = player2;
        if (enemy.hasFlag) {
            if (gameMode == GameMode.Domination)
                getOtherPlayer().numberOfFlags--;
            if (gameMode == GameMode.Flag)
                getOtherPlayer().numberOfTurnsHavingFlag = 0;
            enemy.cell.setHasFlag(true);
        }
        applyItemFunctions(enemy, FunctionType.OnDeath);
        for (Function function : enemy.functions) {
            if (function.getFunctionType() == FunctionType.OnDeath) {
                compileFunction(function, enemy.cell.getX1Coordinate(), enemy.cell.getX2Coordinate());
            }
        }

        enemy.cell.setCardInCell(null);

        player.addCardToGraveYard(enemy);
        player.getCardsOnBattleField().remove(enemy);

    }

    public void comboAtack(Deployable enemy, ArrayList<Deployable> comboAttackers) {
        attack(comboAttackers.get(0), enemy);
        for (int i = 1; i < comboAttackers.size(); i++) {
            dealAttackDamageAndDoOtherStuff(comboAttackers.get(i), enemy);
        }

    }

    public void attack(Deployable card, Deployable enemy) {
        if (canAttack(card, enemy) && !card.account.getUsername().equals(enemy.account.getUsername())) {
            dealAttackDamageAndDoOtherStuff(card, enemy);
            counterAttack(card, enemy);
        } else {
            if (card.isAttacked)
                Output.hasAttackedBefore(card);
            if (card.isStunned())
                Output.isStunned();
            if (isAttackTypeValidForAttack(card, enemy))
                Output.enemyNotThere();
        }
    }

    private boolean canAttack(Deployable card, Deployable enemy) {
        boolean sit = !card.isAttacked && !card.isStunned() && isAttackTypeValidForAttack(card, enemy);
        for (Function function : enemy.functions) {
            if (function.getFunction().equals(FunctionStrings.INVULNERABLE)) {
                System.err.println("attack nemikhore , invulnerable e");
                return false;
            }
            if (function.getFunction().equals(FunctionStrings.IGNORE_LESSER_ATTACK)) {
                if (enemy.theActualDamage() > card.theActualDamage()) {
                    System.err.println("ingore lesser attack e");
                    return false;
                }
            }
        }
        if (card.isAttacked)
            System.err.println("card has attacked in this turn");
        if (card.isStunned())
            System.err.println("card is stunned this turn");
        if (!isAttackTypeValidForAttack(card, enemy))
            System.err.println("attack type isn't valid for this attack");
        return sit;
    }

    private void dealAttackDamageAndDoOtherStuff(Deployable card, Deployable enemy) {
        if (ignoreHolyBuff(card)) {
            enemy.currentHealth -= card.theActualDamage();
        } else
            enemy.currentHealth -= enemy.theActualDamageReceived(card.theActualDamage());
        if (enemy.currentHealth <= 0) {
            killTheThing(enemy);
        }
        applyOnAttackFunction(card, enemy);
        applyOnDefendFunction(enemy, card);
        applyItemOnAttackDefendFunctions(card, FunctionType.OnAttack, currentPlayer);
        applyItemOnAttackDefendFunctions(enemy, FunctionType.OnDefend, getOtherPlayer());


    }

    private boolean ignoreHolyBuff(Card card) {
        for (Function function : card.functions) {
            if (function.getFunction().equals(FunctionStrings.IGNORE_HOLYBUFF))
                return true;
        }
        return false;
    }

    private void applyOnAttackFunction(Deployable card, Deployable enemy) {
        for (Function function : card.functions) {
            if (function.getFunctionType() == FunctionType.OnAttack) {
                compileFunction(function, card.cell.getX1Coordinate(), card.cell.getX2Coordinate(), enemy);
            }
        }
    }

    private void applyOnSpawnFunction(Deployable card) {
        for (Function function : card.functions) {
            if (function.getFunctionType() == FunctionType.OnSpawn) {
                compileFunction(function, card.cell.getX1Coordinate(), card.cell.getX2Coordinate());
            }
        }
    }

    public void applyItemFunctions(Deployable card, FunctionType functionType) {
        try {
            player1.currentDeck.getItem().setAccount(player1.account);
            player2.currentDeck.getItem().setAccount(player2.account);
            for (Function function : player1.currentDeck.getItem().functions) {
                if (function.getFunctionType() == functionType){
                    compileFunction(function, card.cell.getX1Coordinate(), card.cell.getX2Coordinate());
                }
            }
            for (Function function : player2.currentDeck.getItem().functions) {
                if (function.getFunctionType() == functionType)
                    compileFunction(function, card.cell.getX1Coordinate(), card.cell.getX2Coordinate());
            }
        } catch (Exception e) {
            System.err.println("there isn't usable item in your deck");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void applyItemOnAttackDefendFunctions(Deployable card, FunctionType functionType, Player player) {
        if (player.currentDeck.getItem() == null) {
            System.err.println("the player doesn't have item so on attack function in item doesn't work");
            return;
        }
        for (Function function : player.currentDeck.getItem().functions) {
            if (function.getFunctionType() == functionType)
                compileFunction(function, card.cell.getX1Coordinate(), card.cell.getX2Coordinate());
        }
    }

    private void applyOnDefendFunction(Deployable enemy, Deployable card) {
        for (Function function : enemy.functions) {
            if (function.getFunctionType() == FunctionType.OnDefend) {
                compileFunction(function, enemy.cell.getX1Coordinate(), enemy.cell.getX2Coordinate(), card);
            }

        }
    }

    private void counterAttack(Deployable attacker, Deployable counterAttacker) {
        if (!counterAttacker.isDisarmed() && isAttackTypeValidForCounterAttack(attacker, counterAttacker)) {
            attacker.currentHealth -= attacker.theActualDamageReceived(counterAttacker.theActualDamage());
            if (attacker.currentHealth <= 0)
                killTheThing(attacker);
        }
    }

    public static boolean isAttackTypeValidForAttack(Deployable attacker, Deployable counterAttacker) {
        if (attacker.attackType == null || counterAttacker.attackType == null) {
            System.err.println("attack type null e");
            System.err.println(attacker.name + " " + attacker.attackType);
            System.err.println(counterAttacker.name + " " + counterAttacker.attackType);
            return false;
        }
        if (attacker.cell == null || counterAttacker.cell == null) {
            System.err.println("cell ha null e");
            return false;
        }
        return (attacker.attackType == AttackType.melee && isNear(attacker.cell, counterAttacker.cell)) ||
                (attacker.attackType == AttackType.ranged &&
                        Map.getDistance(attacker.cell, counterAttacker.cell) <= attacker.attackRange) ||
                (attacker.attackType == AttackType.hybrid &&
                        Map.getDistance(attacker.cell, counterAttacker.cell) <= attacker.attackRange);
    }

    private boolean isAttackTypeValidForCounterAttack(Deployable attacker, Deployable counterAttacker) {
        return counterAttacker.attackType == AttackType.melee && isNear(attacker.cell, counterAttacker.cell) ||
                (counterAttacker.attackType == AttackType.ranged && !isNear(attacker.cell, counterAttacker.cell) &&
                        Map.getDistance(attacker.cell, counterAttacker.cell) <= counterAttacker.attackRange) ||
                counterAttacker.attackType == AttackType.hybrid;
    }


    public static boolean isNear(Cell cell1, Cell cell2) {
        return Math.abs(cell1.getX1Coordinate() - cell2.getX1Coordinate()) < 2 &&
                Math.abs(cell1.getX2Coordinate() - cell2.getX2Coordinate()) < 2;
    }

    public void player1Won() {
        MatchHistory matchHistory = new MatchHistory(player2.getAccount().getUsername(), "win");
        player1.getAccount().addMatchHistories(matchHistory);
        //player1.account.addDaric();
        matchHistory = new MatchHistory(player1.getAccount().getUsername(), "lose");
        player2.getAccount().addMatchHistories(matchHistory);
        BattleMenu.setGameFinished(true);
        Output.print(player1.getAccount().getUsername() + " won");

    }

    public void player2Won() {
        MatchHistory matchHistory = new MatchHistory(player2.getAccount().getUsername(), "lose");
        player1.getAccount().addMatchHistories(matchHistory);
        matchHistory = new MatchHistory(player1.getAccount().getUsername(), "win");
        player2.getAccount().addMatchHistories(matchHistory);
        BattleMenu.setGameFinished(true);
        Output.print(player2.getAccount().getUsername() + " won");
    }

    public void draw() {
        MatchHistory matchHistory = new MatchHistory(player2.getAccount().getUsername(), "draw");
        player1.getAccount().addMatchHistories(matchHistory);
        matchHistory = new MatchHistory(player1.getAccount().getUsername(), "draw");
        player2.getAccount().addMatchHistories(matchHistory);
        Output.print("draw");
        BattleMenu.setGameFinished(true);
    }

    public Player getOtherPlayer() {
        if (currentPlayer == player1)
            return player2;
        else
            return player1;
    }

    public void checkTheEndSituation() {
        isFinishedDueToHeroDying();
        if (gameMode == GameMode.Flag) {
            isFinishedDueToHavingTheFlag();
        }
        if (gameMode == GameMode.Domination) {
            isFinishedDueToHavingMostOfFlags();
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
        if (2 * player1.getNumberOfFlags() > maxNumberOfFlags)
            player1Won();
        if (2 * player1.getNumberOfFlags() > maxNumberOfFlags)
            player2Won();

    }

    public void initialTheGame() {
        player1.duplicateTheDeck();
        player2.duplicateTheDeck();
        Collections.shuffle(player1.currentDeck.getCards());
        Collections.shuffle(player2.currentDeck.getCards());
        initialTheHands();
        player1.getHero().setAccount(player1.account);
        player2.getHero().setAccount(player2.account);
        generateFlags();
        manaAdderItem();
    }

    private void generateFlags() {
        Random random = new Random();
        if (gameMode == GameMode.Flag) {
            Map.getCell(3, 5).setHasFlag(true);
        }
        if (gameMode == GameMode.Domination) {
            for (int i = 0; i < maxNumberOfFlags; i++) {
                int x1 = random.nextInt(5) + 1;
                int x2 = random.nextInt(5) + 3;
                Map.getCell(x1, x2).setHasFlag(true);
            }
        }

    }

    private static void initialTheHands() {
        for (int i = 0; i < 6; i++) {
            player1.hand.add(player1.currentDeck.getCards().get(i));
            player2.hand.add(player2.currentDeck.getCards().get(i));
        }
        for (int i = 0; i < 6; i++) {
            player1.getCurrentDeck().getCards().remove(i);
            player2.getCurrentDeck().getCards().remove(i);
        }
    }

    public static int[] flagPosition() {
        for (Cell[] cells : Map.getMap()) {
            for (Cell cell : cells) {
                if (cell.doesHaveFlag())
                    return new int[]{cell.getX1Coordinate(), cell.getX2Coordinate()};
            }
        }
        return new int[]{-1, -1};
    }

    public static Deployable findCardByUniqueid(int uniqueCardId) {
        for (Deployable deployable : player1.getCardsOnBattleField()) {
            if (deployable.uniqueId == uniqueCardId)
                return deployable;
        }
        for (Deployable deployable : player2.getCardsOnBattleField()) {
            if (deployable.uniqueId == uniqueCardId)
                return deployable;
        }
        return null;
    }

    public void assignManaToPlayers() {
        if (turn <= 14) {
            if (currentPlayer == player1) {
                player1.mana = (turn - 1) / 2 + 2 + player1.manaChangerInTurn[turn];

            } else {
                player2.mana = turn / 2 + 2 + player2.manaChangerInTurn[turn];
            }
            getOtherPlayer().mana = 0;
        } else {
            if (currentPlayer == player1) {
                player1.mana = 9 + player1.manaChangerInTurn[turn];
            } else {
                player2.mana = 9 + player2.manaChangerInTurn[turn];
            }
            getOtherPlayer().mana = 0;
        }
    }

    public void manaAdderItem() {

        if (player1.currentDeck.getItem() == null || player2.currentDeck.getItem() == null) {
            System.err.println("midunm item nadarim");
            return;
        }
        if (player1.currentDeck.getItem().id == 601) {
            for (int i = 0; i < 3; i++) {
                player1.increaseManaInTheTurn(i * 2 + 1, 1);
            }
        }
        if (player1.currentDeck.getItem().id == 606) {
            for (int i = 0; i < 90; i++) {
                player1.increaseManaInTheTurn(i * 2 + 1, 1);
            }
        }
        if (player2.currentDeck.getItem().id == 601) {
            for (int i = 1; i < 3; i++) {
                player2.increaseManaInTheTurn(i * 2, 1);
            }
        }
        if (player2.currentDeck.getItem().id == 606) {
            for (int i = 1; i <= 90; i++) {
                player2.increaseManaInTheTurn(i * 2, 1);
            }
        }
    }

    public void makeIsMovedAndStunnedAndStuffFalse() {
        for (Deployable deployable : player1.getCardsOnBattleField()) {
            if (deployable != null) {
                deployable.setAttacked(false);
                deployable.setMoved(false);
            }
        }
        for (Deployable deployable : player2.getCardsOnBattleField()) {
            if (deployable != null) {
                deployable.setAttacked(false);
                deployable.setMoved(false);
            }
        }
    }

    public void putFlagOnMap(Item item) {
        Random random = new Random();
        int x1 = random.nextInt(5) + 1;
        int x2 = random.nextInt(9) + 1;
        if (Map.getCell(x1, x2).getCardInCell() != null)
            Map.getCell(x1, x2).getCardInCell().setItem(item);
        else
            Map.getCell(x1, x2).setItem(item);
    }
}
