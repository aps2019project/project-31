package model;


import controller.MultiPlayerBattlePageController;
import controller.Shop;
import controller.SinglePlayerBattlePageController;
import constants.AttackType;
import constants.CardType;
import constants.FunctionType;
import constants.GameMode;
import controller.BattleMenu;

import javafx.application.Platform;
import view.Output;

import java.util.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BattleManager {
    public static final int PERMANENT = 100;
    public static final String CONTINUOUS = "continuous";
    protected GameMode gameMode;
    protected Player currentPlayer;
    protected Player player1;
    protected Player player2;
    protected final int maxNumberOfFlags;
    protected final int maxTurnsOfHavingFlag;
    protected int turn = 1;
    private int[] turnsAppearingTheCollectibleItem = {2, 3, 5, 8, 11, 12, 15, 18, 20, 21, 24, 27, 31, 32, 36, 37,
            40, 43, 46, 49};
    protected GameRecord gameRecord;
    protected boolean isThisRecordedGame;
    protected boolean isTheGameFinished = false;
    private final boolean isMultiPlayer;

    public int getTurn() {
        return turn;
    }

    public boolean isMultiPlayer() {
        return isMultiPlayer;
    }

    public BattleManager(Player player1, Player player2, int maxNumberOfFlags, int maxTurnsOfHavingFlag, GameMode gameMode, boolean isThisRecordedGame, boolean isMultiPlayer) {
        this.maxNumberOfFlags = maxNumberOfFlags;
        this.maxTurnsOfHavingFlag = maxTurnsOfHavingFlag;
        setPlayer1(player1);
        setPlayer2(player2);
        this.gameMode = gameMode;
        gameRecord = new GameRecord(player1, player2, maxNumberOfFlags, maxTurnsOfHavingFlag, gameMode);
        this.isThisRecordedGame = isThisRecordedGame;
        isTheGameFinished = false;
        this.isMultiPlayer = isMultiPlayer;
    }

    public BattleManager(Player player1, Player player2, int maxNumberOfFlags, int maxTurnsOfHavingFlag, GameMode gameMode, boolean isMultiPlayer) {
        this.maxNumberOfFlags = maxNumberOfFlags;
        this.maxTurnsOfHavingFlag = maxTurnsOfHavingFlag;
        setPlayer1(player1);
        setPlayer2(player2);
        this.gameMode = gameMode;
        this.isThisRecordedGame = true;
        isTheGameFinished = false;
        this.isMultiPlayer = isMultiPlayer;
    }

    public boolean isTheGameFinished() {
        return isTheGameFinished;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public boolean isThisRecordedGame() {
        return isThisRecordedGame;
    }

    public GameRecord getGameRecord() {
        return gameRecord;
    }

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

    public GameMode getGameMode() {
        return gameMode;
    }

    private void addFaceToGraphic(Minion minion) {
        if (!isMultiPlayer)
            SinglePlayerBattlePageController.getInstance().addFaceToBattlePage(minion, this);
        else if (MultiPlayerBattlePageController.getInstance().getHboxInTop() != null) {
            Platform.runLater(() -> {
                MultiPlayerBattlePageController.getInstance().addFaceToBattlePage(minion);

            });
        }
    }

    private void removeFaceInHand() {
        if (!isMultiPlayer)
            SinglePlayerBattlePageController.getInstance().removeMinionFromHand(((Deployable) SinglePlayerBattlePageController
                    .getInstance().getMe().selectedCard).face);
        else if (MultiPlayerBattlePageController.getInstance().getHboxInTop() != null &&
                this.currentPlayer.equals(MultiPlayerBattlePageController.getInstance().getMe())) {
            if (((Deployable) MultiPlayerBattlePageController.getInstance().getMe().selectedCard).face == null) {
                System.out.println("it doesn't have a face i guess");
            }
            Platform.runLater(() -> MultiPlayerBattlePageController.getInstance().removeMinionFromHand
                    (((Deployable) MultiPlayerBattlePageController.getInstance().getMe().selectedCard).face));
            System.out.println("remove face from hand called, selected card is :" + MultiPlayerBattlePageController.getInstance().getMe().selectedCard.getName());
        }
    }

    private void refreshTheWholeMap() {
        if (!isMultiPlayer)
            SinglePlayerBattlePageController.getInstance().refreshTheStatusOfMap(this);
        else if (MultiPlayerBattlePageController.getInstance().getHboxInTop() != null)
            Platform.runLater(() -> MultiPlayerBattlePageController.getInstance().refreshTheStatusOfMap(this));
    }

    public String whoIsCurrentPlayer() {
        if (currentPlayer == player1)
            return "1";
        else
            return "2";
    }

    public void addTurn() {
        turn++;
    }

    public int generateUniqueId(int cardId) {
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

    public boolean compileTargetString(ArrayList<Card> targetCards, ArrayList<Cell> targetCells, String target,
                                       int x1, int x2, Deployable attackTarget) {
        if (isTheGameFinished)
            return false;
        try {
            Pattern pattern = Pattern.compile(TargetStrings.MINIONS_WITH_DISTANCE + "(\\d+)");
            Matcher matcher = pattern.matcher(target);
            if (matcher.matches()) {
                int distance = Integer.parseInt(matcher.group(1));
                for (int i = 0; i < distance * 2; i++) {
                    for (int j = 0; j < distance * 2; j++) {
                        Card cardInCell = Map.getInstance().getCardInCell(x1 - distance + i, x2 - distance + j);
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
                targetCards.add(deployables.get(deployables.size() == 0 ?
                        0 : random.nextInt(deployables.size())));
            }

            if (target.matches("(.*)" + TargetStrings.RANDOM_UNIT + "(.*)")) {
                ArrayList<Deployable> deployables = new ArrayList<>();
                deployables.addAll(getOtherPlayer().getCardsOnBattleField());
                deployables.addAll(currentPlayer.getCardsOnBattleField());
                Random random = new Random();
                targetCards.add(deployables.get(deployables.size() == 0 ?
                        0 : random.nextInt(deployables.size())));
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
                targetCards.add(deployables.get(deployables.size() == 0 ?
                        0 : random.nextInt(deployables.size())));
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
                Card card = Map.getInstance().getCardInCell(x1, x2);
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
                if (Map.getInstance().getCardInCell(x1, x2) != null &&
                        Map.getInstance().getCardInCell(x1, x2).getAccount().equals(currentPlayer.getAccount())) {
                    //wrong target
                    System.err.println("Invalid target");
                    return false;
                } else {
                    targetCards.add(Map.getInstance().getCardInCell(x1, x2));
                }
            }

            if (target.matches("(.*)" + TargetStrings.ALL_ALLIES + "(.*)")) {
                targetCards.addAll(currentPlayer.getCardsOnBattleField());
            } else if (target.matches("(.*)" + TargetStrings.ALLY + "(.*)")) {
                Card card = Map.getInstance().getCardInCell(x1, x2);
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
                Card card = Map.getInstance().getCardInCell(x1, x2);
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
                    if (Map.getInstance().getCardInCell(x1, x2) != null &&
                            !Map.getInstance().getCardInCell(i, x2).getAccount().equals(currentPlayer.getAccount())) {
                        targetCards.add(Map.getInstance().getCardInCell(i, x2));
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
                if (Map.getInstance().getCardInCell(x1, x2) != null
                        && Map.getInstance().getCardInCell(x1, x2).getType() == CardType.minion
                        && Map.getInstance().getCardInCell(x1, x2).getAccount().equals(currentPlayer.getAccount())) {
                    // isn't it better if we make haveCardInBattle instead of .equals ?
                    targetCards.add(Map.getInstance().getCardInCell(x1, x2));
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
                        targetCells.add(Map.getInstance().getCell(i, j));
                        targetCards.add(Map.getInstance().getCardInCell(i, j));
                    }
                }
            }

            if (target.matches("(.*)" + TargetStrings.SELF + "(.*)")) {
                targetCards.add(Map.getInstance().getCardInCell(x1, x2));
            }

            if (target.matches("(.*)" + TargetStrings.SURROUNDING_ALLIED_MINIONS + "(.*)")) {
                for (int i = x1 - 1; i < x1 + 2; i++) {
                    for (int j = x2 - 1; j < x2 + 2; j++) {
                        if (Map.getInstance().getCardInCell(x1, x2) != null &&
                                Map.getInstance().getCardInCell(x1, x2).getAccount().equals(currentPlayer.getAccount())) {
                            targetCards.add(Map.getInstance().getCardInCell(x1, x2));
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
                targetCards.add(cardsToPickFrom.get(cardsToPickFrom.size() == 0 ?
                        0 : random.nextInt(cardsToPickFrom.size())));

            }

            if (target.matches("(.*)" + TargetStrings.RANDOM_ENEMY_MINION + "(.*)")) {
                ArrayList<Card> cardsToPickFrom = new ArrayList<>();
                for (Card card : getOtherPlayer().getCardsOnBattleField()) {
                    if (card.getType() == CardType.minion) {
                        cardsToPickFrom.add(card);
                    }
                }
                Random random = new Random();
                targetCards.add(cardsToPickFrom.get(cardsToPickFrom.size() == 0 ?
                        0 : random.nextInt(cardsToPickFrom.size())));


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
            if (Map.getInstance().getCardInCell(rowNum, i) != null &&
                    !Map.getInstance().getCardInCell(rowNum, i).getAccount().equals(currentPlayer.getAccount())) {
                targetCards.add(Map.getInstance().getCardInCell(rowNum, i));
            }
        }
    }

    private void addSurroundingCards(ArrayList<Card> list, int x1, int x2) {
        for (int i = x1 - 1; i < x1 + 2; i++) {
            for (int j = x2 - 1; j < x2 + 2; j++) {
                if (Map.getInstance().getCardInCell(x1, x2) != null &&
                        !Map.getInstance().getCardInCell(x1, x2).getAccount().equals(currentPlayer.getAccount())) {
                    list.add(Map.getInstance().getCardInCell(x1, x2));
                }
            }
        }
    }

    public void compileFunction(Function function, int x1, int x2) {
        compileFunction(function, x1, x2, null);
    }

    public void compileFunction(Function function, int x1, int x2, Deployable attackTarget) {
        if (isTheGameFinished)
            return;
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
            attackTarget.takeDamage(attackTarget.accumulatingAttack((Deployable) Map.getInstance().getCardInCell(x1, x2)) * amount);
        }
    }

    private void handleUnpoisonable(Function function, int x1, int x2) {
        if (function.getFunction().matches("(.*)" + FunctionStrings.UNPOISONABLE + "(.*)")) {
            ArrayList<Buff> toRemove = new ArrayList<>();
            for (Buff buff : ((Deployable) Map.getInstance().getCardInCell(x1, x2)).getBuffs()) {
                if (buff.getBuffType() == Buff.BuffType.Poison) {
                    toRemove.add(buff);
                }
            }
            ((Deployable) Map.getInstance().getCardInCell(x1, x2)).getBuffs().removeAll(toRemove);
        }
    }

    private void handleIndisarmable(Function function, int x1, int x2) {
        if (function.getFunction().matches("(.*)" + FunctionStrings.INDISARMABLE + "(.*)")) {
            ArrayList<Buff> toRemove = new ArrayList<>();
            for (Buff buff : ((Deployable) Map.getInstance().getCardInCell(x1, x2)).getBuffs()) {
                if (buff.getBuffType() == Buff.BuffType.Disarm) {
                    toRemove.add(buff);
                }
            }
            ((Deployable) Map.getInstance().getCardInCell(x1, x2)).getBuffs().removeAll(toRemove);
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

    public int[] getTurnsAppearingTheCollectibleItem() {
        return turnsAppearingTheCollectibleItem;
    }

    private void handleDispel(Function function, ArrayList<Card> targetCards) {
        if (isTheGameFinished)
            return;
        if (function.getFunction().matches("(.*)" + FunctionStrings.DISPEL + "(.*)")) {
            for (Card card : targetCards) {
                ArrayList<Buff> toRemove = new ArrayList<>();
                if (card == null)
                    continue;
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
        if (isTheGameFinished)
            return;
        Pattern pattern = Pattern.compile(FunctionStrings.DEAL_DAMAGE + "(\\d+)");
        Matcher matcher = pattern.matcher(function.getFunction());
        if (matcher.matches()) {
            System.out.println("dealing damage!");
            int amount = Integer.parseInt(matcher.group(1));
            for (Card card : targetCards) {
                if (card != null)
                    ((Deployable) card).takeDamage(amount);
                System.out.println("Dealt damage!");
            }
        }
    }

    private void handleBuffs(Function function, ArrayList<Card> targetCards) {
        if (isTheGameFinished)
            return;
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
            if (card != null) {
                ((Deployable) card).addBuff(buff);
                System.out.println("Applying buff: " + buff.getBuffType() + " to " +
                        card.getName());
            }
        }
    }

    private void addUnholyBuff(ArrayList<Card> targetCards) {
        Buff buff = new Buff(Buff.BuffType.Unholy, PERMANENT, 0, 0, false);
        addBuffs(targetCards, buff);
    }

    public boolean checkCoordinates(int x1, int x2) {
        if (Map.getInstance().getCell(x1, x2) == null ||
                Map.getInstance().getCardInCell(x1, x2) != null) {
            return false;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    if (x1 + i >= 1 && x1 + i <= Map.MAP_X1_LENGTH && x2 + j >= 1 &&
                            x2 + j <= Map.MAP_X2_LENGTH && Map.getInstance().getCardInCell(x1 + i, x2 + j) != null) {
                        if (Map.getInstance().getCardInCell(x1 + i, x2 + j).getAccount().equals(currentPlayer.getAccount())) {
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

    public void onSpawnFunctions(Card card, int x1, int x2) {
        for (Function function : card.getFunctions()) {
            if (function.getFunctionType() == FunctionType.OnSpawn) {
                compileFunction(function, x1, x2);
            }
        }
    }

    public boolean playMinion(Minion minion, int x1, int x2) {
        Minion theMinion = minion.duplicateDeployed(Map.getInstance().getCell(x1, x2),
                currentPlayer.account, this);
        Map.getInstance().putCardInCell(theMinion, x1, x2);

        Output.insertionSuccessful(theMinion, x1, x2);
        applyItemFunctions(theMinion, FunctionType.OnSpawn);
        onSpawnFunctions(minion, x1, x2);
        currentPlayer.addCardToBattlefield(theMinion);

        addFaceToGraphic(theMinion);

        theMinion.isMoved = false;
        currentPlayer.removeFromHand(minion);
        applyOnSpawnFunction(theMinion);
        currentPlayer.decreaseMana(theMinion.manaCost);
        if (!currentPlayer.isAi() && !isThisRecordedGame)
            removeFaceInHand();
        if (!isMultiPlayer)
            currentPlayer.selectedCard = null;
        removeItemIfThereIsSomething(theMinion);
        removeFlagIfThereIsSomething(theMinion, x1, x2);

        refreshTheWholeMap();

        if (!isThisRecordedGame)
            gameRecord.addAction(whoIsCurrentPlayer() + "I" + theMinion.id + x1 + x2);
        return true;
    }

    public boolean playSpell(Spell spell, int x1, int x2) {
        if (!spell.equals(currentPlayer.getHero().heroSpell)) {
            currentPlayer.hand.remove(spell);
        } else if (currentPlayer.getHero().getHeroSpell().getCoolDownRemaining() != 0) {
            System.out.println("cool down!");
            return false;
        }
        onSpawnFunctions(spell, x1, x2);
        for (Function function : spell.functions) {
            compileFunction(function, x1, x2);
        }
        currentPlayer.decreaseMana(spell.manaCost);
        currentPlayer.selectedCard = null;


        removeCardFromGraphic(spell);
        refreshTheWholeMap();
        if (!isThisRecordedGame)
            gameRecord.addAction(whoIsCurrentPlayer() + "I" + spell.id + x1 + x2);
        return true;
    }

    private void removeCardFromGraphic(Card card) {
        if (!isMultiPlayer)
            SinglePlayerBattlePageController.getInstance().removeCardFromHand(card, this);
        else if (MultiPlayerBattlePageController.getInstance().getHboxInTop() != null &&
                this.currentPlayer == MultiPlayerBattlePageController.getInstance().getMe()
                && this.currentPlayer.isInHand(MultiPlayerBattlePageController.getInstance().getMe().selectedCard))
            Platform.runLater(() -> MultiPlayerBattlePageController.getInstance().removeCardFromHand(card, this));

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

        refreshTheWholeMap();

        return true;
    }


    public boolean move(Deployable card, int x1, int x2) {
        if (isTheGameFinished)
            return false;
        refreshTheWholeMap();

        if (card.cell == null) {
            System.err.println("the cell is null in the move method");
            return false;
        }
        if (Map.getInstance().getDistance(Map.getInstance().getCell(x1, x2), card.cell) <= Map.getInstance().getMaxMoveRange()) {
            if (Map.getInstance().getCell(x1, x2).getCardInCell() == null && !card.isMoved && !card.isStunned()) {
                doTheActualMove_noTarof(card, x1, x2);
                return true;
            } else {
                Output.invalidTargetForMove();
                return false;
            }
        } else {
            Output.tooFarToMove();
            return false;
        }
    }

    public void doTheActualMove_noTarof(Deployable card, int x1, int x2) {
        if (!isThisRecordedGame)
            gameRecord.addAction(whoIsCurrentPlayer() + "M" + card.cell.getX1Coordinate() + card.cell.getX2Coordinate() + x1 + x2);
        System.out.println("we are at move_noTarof");
        card.cell.setCardInCell(null);
        card.cell = Map.getInstance().getCell(x1, x2);
        card.setMoved(true);
        card.cell.setCardInCell(card);
        removeItemIfThereIsSomething(card);
        removeFlagIfThereIsSomething(card, x1, x2);
        refreshTheWholeMap();

        Output.movedSuccessfully(card);
    }

    public void removeFlagIfThereIsSomething(Deployable card, int x1, int x2) {
        if (!card.hasFlag && Map.getInstance().getCell(x1, x2).doesHaveFlag()) {
            if (gameMode == GameMode.Domination)
                currentPlayer.numberOfFlags++;
            card.setHasFlag(true);
            Map.getInstance().getCell(x1, x2).setHasFlag(false);

        }
    }

    public void removeItemIfThereIsSomething(Deployable card) {
        if (card.cell.getItem() != null && card.item == null) {
            card.item = card.cell.getItem();
            card.cell.setItem(null);
        }
    }


    public void killTheThing(Deployable enemy) {
        if (isTheGameFinished)
            return;
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
        if (enemy.item != null && enemy.cell.getItem() == null) {
            enemy.cell.setItem(enemy.item);
        }
        applyItemFunctions(enemy, FunctionType.OnDeath);
        for (Function function : enemy.functions) {
            if (function.getFunctionType() == FunctionType.OnDeath) {
                compileFunction(function, enemy.cell.getX1Coordinate(), enemy.cell.getX2Coordinate());
            }
        }

        enemy.cell.setCardInCell(null);
        System.out.println("we killed this poor thing");
        removeFaceInGraphic(enemy.getFace());

        player.addCardToGraveYard(new DisplayableDeployable(enemy));
        player.getCardsOnBattleField().remove(enemy);

    }

    private void removeFaceInGraphic(DisplayableDeployable face) {
        if (!isMultiPlayer)
            SinglePlayerBattlePageController.getInstance().mainPane.getChildren().remove(face);
        else if (MultiPlayerBattlePageController.getInstance().getHboxInTop() != null)
            Platform.runLater(() -> MultiPlayerBattlePageController.getInstance().mainPane.getChildren().remove(face));
    }


    public void comboAttack(Deployable enemy, ArrayList<Deployable> comboAttackers) {
        attack(comboAttackers.get(0), enemy);
        for (int i = 1; i < comboAttackers.size(); i++) {
            dealAttackDamageAndDoOtherStuff(comboAttackers.get(i), enemy);
        }

        refreshTheWholeMap();


    }

    public void doTheActualAttack_noTarof(Deployable card, Deployable enemy) {
        dealAttackDamageAndDoOtherStuff(card, enemy);
        counterAttack(card, enemy);
        if (!isThisRecordedGame)
            gameRecord.addAction(whoIsCurrentPlayer() + "A" + card.cell.getX1Coordinate() +
                    card.cell.getX2Coordinate() + enemy.cell.getX1Coordinate() + enemy.cell.getX2Coordinate());
    }

    public boolean attack(Deployable card, Deployable enemy) {
        if (isTheGameFinished)
            return false;
        if (canAttack(card, enemy) && !card.account.getUsername().equals(enemy.account.getUsername())) {
            doTheActualAttack_noTarof(card, enemy);
            refreshTheWholeMap();
            return true;
            /*enemy.getFace().attack();
            card.getFace().getHit();*/
        } else {
            System.out.println("loook at the next line! why not attack ??");
            if (card.isAttacked)
                Output.hasAttackedBefore(card);
            if (card.isStunned())
                Output.isStunned();
            if (!isAttackTypeValidForAttack(card, enemy))
                Output.enemyNotThere();
            return false;
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

        applyOnAttackFunction(card, enemy);
        applyOnDefendFunction(enemy, card);
        applyItemOnAttackDefendFunctions(card, FunctionType.OnAttack, currentPlayer);
        applyItemOnAttackDefendFunctions(enemy, FunctionType.OnDefend, getOtherPlayer());
        card.isAttacked = true;
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

        refreshTheWholeMap();

    }

    private void applyOnSpawnFunction(Deployable card) {
        for (Function function : card.functions) {
            if (function.getFunctionType() == FunctionType.OnSpawn) {
                System.out.println("Doing on spawn:" + card.getName());
                compileFunction(function, card.cell.getX1Coordinate(), card.cell.getX2Coordinate());
            }
        }

        refreshTheWholeMap();

    }

    public void applyItemFunctions(Deployable card, FunctionType functionType) {
        try {
            if (player1.currentDeck.getItem() != null) {
                player1.currentDeck.getItem().setAccount(player1.account);
                for (Function function : player1.currentDeck.getItem().functions) {
                    if (function.getFunctionType() == functionType) {
                        compileFunction(function, card.cell.getX1Coordinate(), card.cell.getX2Coordinate());
                    }
                }
            }
            if (player2.currentDeck.getItem() != null) {
                player2.currentDeck.getItem().setAccount(player2.account);
                for (Function function : player2.currentDeck.getItem().functions) {
                    if (function.getFunctionType() == functionType)
                        compileFunction(function, card.cell.getX1Coordinate(), card.cell.getX2Coordinate());
                }
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

        refreshTheWholeMap();

    }

    private void applyOnDefendFunction(Deployable enemy, Deployable card) {
        for (Function function : enemy.functions) {
            if (function.getFunctionType() == FunctionType.OnDefend) {
                compileFunction(function, enemy.cell.getX1Coordinate(), enemy.cell.getX2Coordinate(), card);
            }

        }

        refreshTheWholeMap();

    }

    private void counterAttack(Deployable attacker, Deployable counterAttacker) {
        if (!counterAttacker.isDisarmed() && isAttackTypeValidForCounterAttack(attacker, counterAttacker)) {
            attacker.currentHealth -= attacker.theActualDamageReceived(counterAttacker.theActualDamage());
        } else {
            System.out.println("counter attack doesn't work");
        }
        if (attacker.theActualHealth() <= 0)
            killTheThing(attacker);
        if (counterAttacker.theActualHealth() <= 0) {
            killTheThing(counterAttacker);
        }

        refreshTheWholeMap();


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
                        Map.getInstance().getDistance(attacker.cell, counterAttacker.cell) <= attacker.attackRange) ||
                (attacker.attackType == AttackType.hybrid &&
                        Map.getInstance().getDistance(attacker.cell, counterAttacker.cell) <= attacker.attackRange);
    }

    private boolean isAttackTypeValidForCounterAttack(Deployable attacker, Deployable counterAttacker) {
        return counterAttacker.attackType == AttackType.melee && isNear(attacker.cell, counterAttacker.cell) ||
                (counterAttacker.attackType == AttackType.ranged && !isNear(attacker.cell, counterAttacker.cell) &&
                        Map.getInstance().getDistance(attacker.cell, counterAttacker.cell) <= counterAttacker.attackRange) ||
                counterAttacker.attackType == AttackType.hybrid;
    }


    public static boolean isNear(Cell cell1, Cell cell2) {
        return Math.abs(cell1.getX1Coordinate() - cell2.getX1Coordinate()) < 2 &&
                Math.abs(cell1.getX2Coordinate() - cell2.getX2Coordinate()) < 2;
    }

    public void player1Won() {
        MatchHistory matchHistory1 = new MatchHistory(player2, player1, "WIN", gameRecord, gameMode);
        player1.getAccount().addMatchHistories(matchHistory1);
        MatchHistory matchHistory2 = new MatchHistory(player1, player2, "LOSE", gameRecord, gameMode);
        player2.getAccount().addMatchHistories(matchHistory2);

        player1.getAccount().incrementWins();
        player2.getAccount().incrementLosses();
        gameEnded(player1);

    }

    private void gameEnded(Player winner) {
        isTheGameFinished = true;
        if (!isThisRecordedGame)
            gameRecord.addAction("E");
        System.out.println(gameRecord.game);
        if (isThisRecordedGame) {
            showGameEnding();
        }
        BattleMenu.setGameFinished(true);
        if (winner != null)
            Output.print(winner.getAccount().getUsername() + " won");
        else System.out.println("draw");

        showGameEnding();
    }

    private void showGameEnding() {
        if (!isMultiPlayer)
            SinglePlayerBattlePageController.getInstance().showThatGameEnded();
        else Platform.runLater(() -> MultiPlayerBattlePageController.getInstance().showThatGameEnded());
    }

    public void player2Won() {
        MatchHistory matchHistory1 = new MatchHistory(player2, player1, "LOSE", gameRecord, gameMode);
        player1.getAccount().addMatchHistories(matchHistory1);
        MatchHistory matchHistory2 = new MatchHistory(player1, player2, "WIN", gameRecord, gameMode);
        player2.getAccount().addMatchHistories(matchHistory2);

        player1.getAccount().incrementLosses();
        player2.getAccount().incrementWins();
        gameEnded(player2);
    }

    public void draw() {
        MatchHistory matchHistory1 = new MatchHistory(player2, player1, "DRAW", gameRecord, gameMode);
        player1.getAccount().addMatchHistories(matchHistory1);
        MatchHistory matchHistory2 = new MatchHistory(player1, player2, "DRAW", gameRecord, gameMode);
        player2.getAccount().addMatchHistories(matchHistory2);

        player1.getAccount().incrementDraw();
        player2.getAccount().incrementDraw();
        gameEnded(null);
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
        if (2 * player2.getNumberOfFlags() > maxNumberOfFlags)
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
        manaAdderItem();
        if (!isThisRecordedGame) {
            gameRecord.setMap(Map.getInstance().getMap());
        }
        getPlayer1().generateDeckArrangement();
        getPlayer2().generateDeckArrangement();
        setCurrentPlayer(BattleMenu.getBattleManager().getPlayer2());
        applyItemFunctions(BattleMenu.getBattleManager().getCurrentPlayer().getHero(), FunctionType.GameStart);
        setCurrentPlayer(BattleMenu.getBattleManager().getPlayer1());
        applyItemFunctions(BattleMenu.getBattleManager().getCurrentPlayer().getHero(), FunctionType.GameStart);

        player2.initNextcard();
        player1.initNextcard();
        player1.getHero().setCell(Map.getInstance().getCell(3, 1));
        player2.getHero().setCell(Map.getInstance().getCell(3, 9));
        player1.getHero().getCell().setCardInCell(player1.getHero());
        player2.getHero().getCell().setCardInCell(player2.getHero());
        getPlayer1().addCardToBattlefield(player1.getHero());
        getPlayer2().addCardToBattlefield(player2.getHero());
        doAllAtTheBeginningOfTurnThings();
    }

    private void initialTheHands() {
        for (int i = 0; i < 6; i++) {
            player1.hand.add(player1.currentDeck.getCards().get(i));
            player2.hand.add(player2.currentDeck.getCards().get(i));
        }
        for (int i = 6; i >= 0; i--) {
            player1.getCurrentDeck().getCards().remove(i);
            player2.getCurrentDeck().getCards().remove(i);
        }
    }

    public static int[] flagPosition() {
        for (Cell[] cells : Map.getInstance().getMap()) {
            for (Cell cell : cells) {
                if (cell.doesHaveFlag())
                    return new int[]{cell.getX1Coordinate(), cell.getX2Coordinate()};
            }
        }
        return new int[]{-1, -1};
    }

    public Deployable findCardByUniqueid(int uniqueCardId) {
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

    public void makeIsMovedAndIsAttackedFalse() {
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

    public void doAllThingsInEndingOfTheTurns() {
        makeIsMovedAndIsAttackedFalse();
        applyItemFunctions(getPlayer1().getHero(), FunctionType.Passive);
        getCurrentPlayer().placeNextCardToHand();
        getCurrentPlayer().endOfTurnBuffsAndFunctions();
        getOtherPlayer().endOfTurnBuffsAndFunctions();
        checkTheEndSituation();
        flagModeSitAndAddTurnAndHeroSpellSit();
        //    battleManager.refreshTheStatusOfMap();
        BattleMenu.showGlimpseOfMap();
    }

    public void flagModeSitAndAddTurnAndHeroSpellSit() {
        if (getGameMode() == GameMode.Flag) {
            getPlayer1().handleNumberOfTurnHavingFlagAtTheEndOfTurn();
            getPlayer2().handleNumberOfTurnHavingFlagAtTheEndOfTurn();
        }
        addTurn();
        getPlayer1().getHero().getHeroSpell().decrementCooldonwRemaining();
        getPlayer2().getHero().getHeroSpell().decrementCooldonwRemaining();
    }

    public void isTimeToPutItem() {
        for (int theTurn : getTurnsAppearingTheCollectibleItem()) {
            if (theTurn == getTurn()) {
                Collections.shuffle(Shop.getAllCollectibles());
                putItemOnMap(Shop.getAllCollectibles().get(0));
            }
        }
    }

    public void doAllAtTheBeginningOfTurnThings() {
        for (Deployable deployable : getCurrentPlayer().getCardsOnBattleField()) {
            deployable.setMoved(false);
            deployable.setAttacked(false);
        }
        getCurrentPlayer().setHasReplaced(false);
        assignManaToPlayers();
        //   isTimeToPutItem();
    }

    public void putItemOnMap(Item item) {
        Random random = new Random();
        int x1 = random.nextInt(5) + 1;
        int x2 = random.nextInt(9) + 1;
        if (Map.getInstance().getCell(x1, x2).getCardInCell() != null) {
            Map.getInstance().getCell(x1, x2).getCardInCell().setItem(item);
            if (!isMultiPlayer)
                SinglePlayerBattlePageController.getInstance().displayMessage("Item put on Minion!!!");
            else if (MultiPlayerBattlePageController.getInstance().getHboxInTop() != null)
                Platform.runLater(() -> MultiPlayerBattlePageController.getInstance().displayMessage("Item put on a Minion"));
            System.out.println("item was put on someones minion with coordination: " + x1 + " , " + x2);
        } else {
            Map.getInstance().getCell(x1, x2).setItem(item);
        }

        refreshTheWholeMap();
    }


    public boolean insert(Card card, int x1, int x2) {
        if (isTheGameFinished())
            return false;
        if (card == null) {
            System.err.println("insert(method) -> card is null");
            return false;
        }
        if (card.getType() == CardType.item) {
            card.setAccount(getCurrentPlayer().getAccount());
            useItem((Item) card, x1, x2);
            getGameRecord().addAction(whoIsCurrentPlayer() + "I" + card.getId() + x1 + x2);
            return true;
        }
        if (getCurrentPlayer().getHero().getHeroSpell().getId() == card.getId()) {
            if (card.getManaCost() > getCurrentPlayer().getMana()) {
                System.err.println("Not enough mana");
                return false;
            }
            System.out.println("Using hero spell " + card.getName());
            playSpell((Spell) card, x1, x2);
            return true;
        }
        if (cardInHandByCardId(card.getId()) != null) {
            if (card.getManaCost() > getCurrentPlayer().getMana()) {
                System.err.println("Not enough mana");
                return false;
            }
            if (card.getType() == CardType.minion) {
                if (!checkCoordinates(x1, x2)) {
                    Output.invalidInsertionTarget();
                    System.err.println("Invalid Coordinates");
                    return false;

                }
                playMinion((Minion) card, x1, x2);
            }
            if (card.getType() == CardType.spell) {
                card.setAccount(getCurrentPlayer().getAccount());
                playSpell((Spell) card, x1, x2);
            }
            if (card.getName() == "Eagle") {
                System.out.println("found eagle");
                for (Buff buff : ((Deployable) card).getBuffs()) {
                    System.out.println(buff.getBuffType());
                }
            }
        } else {
            System.err.println("Minion not in hand");
            return false;
        }
        return true;
    }


    public void doWhatIAmToldTo(String command) {
        if (gameRecord == null)
            System.out.println("game record is null in battle manager");
        else {
            System.out.println("we are going to command battle manager to do things with no tarof");
            gameRecord.doWhatIAmToldTo(command, this);
        }
    }

    public void endTurn() {
        System.out.println("current player was : " + currentPlayer.account.getUsername());
        doAllThingsInEndingOfTheTurns();
        setCurrentPlayer(getOtherPlayer());
        doAllAtTheBeginningOfTurnThings();
        if (MultiPlayerBattlePageController.getInstance().getHboxInTop() != null)
            Platform.runLater(() -> {
                System.out.println("we the gay people wants our card to be update");
                MultiPlayerBattlePageController.getInstance().putNextCardInHand(this);
                MultiPlayerBattlePageController.getInstance().updateNextCard();
                MultiPlayerBattlePageController.getInstance().refreshTheStatusOfMap(this);
            });
        System.out.println("now the current player is : " + currentPlayer.account.getUsername());
    }


}
