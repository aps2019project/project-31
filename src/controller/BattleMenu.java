package controller;

import constants.*;
import constants.GameMode;
import javafx.application.Platform;
import model.*;
import view.Input;
import view.Output;

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.Collections;

public class BattleMenu extends Menu {
    private static BattleManager battleManager;
    private static boolean areWeInMiddleOfTurn = true;

    protected static boolean isGameFinished = false;

    public static boolean isGameFinished() {
        return isGameFinished;
    }

    public static void setGameFinished(boolean gameFinished) {
        isGameFinished = gameFinished;
    }

    public static void showPlayerMinions(Player player) {
        for (Deployable deployable : player.getCardsOnBattleField()) {
            Output.print(deployable.infoToString());
            Output.print("=====================================");
        }
    }

    public static boolean isAreWeInMiddleOfTurn() {
        return areWeInMiddleOfTurn;
    }

    public static void setAreWeInMiddleOfTurn(boolean areWeInMiddleOfTurn) {
        BattleMenu.areWeInMiddleOfTurn = areWeInMiddleOfTurn;
    }

    public static BattleManager getBattleManager() {
        return battleManager;
    }

    public static void setBattleManagerForMultiPlayer(Account player1Acc, Account player2Acc, int numberOfFlags,
                                                      int maxNumberOfHavingFlag, GameMode gameMode) {
        Player player1 = new Player(player1Acc, false);
        Player player2 = new Player(player2Acc, false);
        makeInstanceOfBattleManager(player1, player2, numberOfFlags, maxNumberOfHavingFlag, gameMode);
    }

    public static void setBattleManagerForSinglePLayer(BattleManagerMode battleManagerMode, Account account,
                                                       int numberOfFlags, int maxNumberOfHavingFlag,
                                                       GameMode gameMode, int storyNumber) {
        Player player1 = new Player(account, false);


        Deck theAiDeck = null;
        switch (battleManagerMode) {
            case Story:
                switch (storyNumber) {
                    case 1:
                        theAiDeck = Story.getFirstBattleManagerDeck();
                        break;
                    case 2:
                        theAiDeck = Story.getSecondBattleManagerDeck();
                        break;
                    case 3:
                        theAiDeck = Story.getThirdBattleManagerDeck();
                        break;
                }
                if (theAiDeck == null) {
                    System.err.println("story number invalid");
                    return;
                }


                System.out.println(theAiDeck.getDeckName());
                System.out.println(theAiDeck.getCards().get(0).toString());

                break;
            case CustomGame:
                theAiDeck = SinglePlayer.getCustomGameDeck();
                if (theAiDeck == null) {
                    System.err.println("story number invalid");
                    return;
                }
                break;
        }
        SinglePlayer.makeAIAccount(theAiDeck);
        makeInstanceOfBattleManager(player1, SinglePlayer.getAiPlayer(), numberOfFlags, maxNumberOfHavingFlag, gameMode);
        BattlePageController.getInstance().setMe(player1);
        BattlePageController.getInstance().setOpponent(SinglePlayer.getAiPlayer());
    }


    private static void makeInstanceOfBattleManager(Player player1, Player player2, int numberOfFlags,
                                                    int maxTurnsHavingFlag, GameMode gameMode) {
        switch (gameMode) {
            case DeathMatch:
                battleManager = new BattleManager(player1, player2, 100, 100, GameMode.DeathMatch);
                break;
            case Flag:
                battleManager = new BattleManager(player1, player2, 100, maxTurnsHavingFlag, GameMode.Flag);
                break;
            case Domination:
                battleManager = new BattleManager(player1, player2, numberOfFlags, 100, GameMode.Domination);
                break;
        }
        player1.setBattle(battleManager);
        player2.setBattle(battleManager);

    }


    public static void doAllAtTheBeginningOfTurnThings() {
        for (Deployable deployable : battleManager.getCurrentPlayer().getCardsOnBattleField()) {
            deployable.setMoved(false);
        }

        battleManager.assignManaToPlayers();

        for (int theTurn : battleManager.getTurnsAppearingTheCollectibleFlags()) {
            if (theTurn == battleManager.getTurn()) {
                Collections.shuffle(Shop.getAllCollectibles());
                battleManager.putFlagOnMap(Shop.getAllCollectibles().get(0));
            }

        }
        //    battleManager.refreshTheStatusOfMap();

    }

    public BattleMenu(int id, String title) {
        super(id, title);
    }

    public void runTheGame() {
        battleManager.initialTheGame();
        boolean isPlayer1Turn = false;
        battleManager.getPlayer1().generateDeckArrangement();
        battleManager.getPlayer2().generateDeckArrangement();

        battleManager.setCurrentPlayer(battleManager.getPlayer2());

        battleManager.applyItemFunctions(battleManager.getCurrentPlayer().getHero(), FunctionType.GameStart);
        battleManager.setCurrentPlayer(battleManager.getPlayer1());
        battleManager.applyItemFunctions(battleManager.getCurrentPlayer().getHero(), FunctionType.GameStart);
        battleManager.setCurrentPlayer(battleManager.getPlayer2());
        //  initHeroes();
        while (true) {

            isPlayer1Turn = !isPlayer1Turn;
            battleManager.setCurrentPlayer(battleManager.getOtherPlayer());
            doAllAtTheBeginningOfTurnThings();
            if (battleManager.getCurrentPlayer().isAi()) {
                ((Ai) battleManager.getCurrentPlayer()).play();
            } else {
                areWeInMiddleOfTurn = true;
                boolean sit = true;
                while (sit == areWeInMiddleOfTurn) {
                    Input.handleCommandsInBattle(battleManager.getCurrentPlayer(),
                            battleManager.getCurrentPlayer().getSelectedCard() != null);
                }
                if (isGameFinished) {
                    battleManager = null;
                    return;
                }
            }
            doAllThingsInEndingOfTheTurns();
            Output.theTurnEnded();
        }
    }


    public static void doAllThingsInEndingOfTheTurns() {
        battleManager.makeIsMovedAndStunnedAndStuffFalse();
        battleManager.applyItemFunctions(battleManager.getPlayer1().getHero(), FunctionType.Passive);
        battleManager.getCurrentPlayer().placeNextCardToHand();
        battleManager.getCurrentPlayer().endOfTurnBuffsAndFunctions();
        battleManager.getOtherPlayer().endOfTurnBuffsAndFunctions();
        battleManager.checkTheEndSituation();
        if (BattleManager.getGameMode() == GameMode.Flag) {
            battleManager.getPlayer1().handleNumberOfTurnHavingFlagAtTheEndOfTurn();
            battleManager.getPlayer2().handleNumberOfTurnHavingFlagAtTheEndOfTurn();
        }
        battleManager.addTurn();
        battleManager.getPlayer1().getHero().getHeroSpell().decrementCooldonwRemaining();
        battleManager.getPlayer2().getHero().getHeroSpell().decrementCooldonwRemaining();
        //    battleManager.refreshTheStatusOfMap();
    }


    public static void prepareComboAttack(String[] strNumbers, int opponentCardId) {
        ArrayList<Deployable> validCards = new ArrayList<>();
        for (String number : strNumbers) {
            int cardId = Integer.parseInt(number);
            if (Map.findCellByCardId(cardId).getCardInCell() != null &&
                    Map.findCellByCardId(opponentCardId).getCardInCell() != null &&
                    BattleManager.isAttackTypeValidForAttack(Map.findCellByCardId(cardId).getCardInCell(),
                            Map.findCellByCardId(opponentCardId).getCardInCell())) {
                if (Map.findCellByCardId(cardId).getCardInCell().isCombo())
                    validCards.add(Map.findCellByCardId(cardId).getCardInCell());
            }
        }
        BattleMenu.getBattleManager().comboAtack(Map.findCellByCardId(opponentCardId).getCardInCell(), validCards);
    }

    public static void attack(int uniqueCardId) {
        Card selectedCard = BattleMenu.getBattleManager().getCurrentPlayer().getSelectedCard();
        if (selectedCard != null && battleManager.getCurrentPlayer().isSelectedCardDeployed()) {
            if (Map.findCellByCardId(uniqueCardId) != null &&
                    Map.findCellByCardId(uniqueCardId).getCardInCell() != null)
                battleManager.attack((Deployable) selectedCard
                        , Map.findCellByCardId(uniqueCardId).getCardInCell());
            else
                Output.enemyNotExist();
        } else {
            Output.badSelectedCard();
        }
    }

    public static void move(int x1, int x2) {
        if (battleManager.getCurrentPlayer().isSelectedCardDeployed())
            battleManager.move((Deployable) (battleManager.getCurrentPlayer().getSelectedCard()), x1, x2);
    }

    public static boolean insert(Card card, int x1, int x2) {
        if (card == null) {
            return false;
        }
        if (battleManager.cardInHandByCardId(card.getId()) != null) {


            if (card.getManaCost() > battleManager.getCurrentPlayer().getMana()) {
                Output.notHaveEnoughMana();
                System.err.println("Not enough mana");
                return false;
            }

            if (card.getType() == CardType.minion) {

                battleManager.playMinion((Minion) card, x1, x2);
            }
            if (card.getType() == CardType.spell) {
                card.setAccount(battleManager.getCurrentPlayer().getAccount());
                battleManager.playSpell((Spell) card, x1, x2);
            }
            if (card.getType() == CardType.item) {
                card.setAccount(battleManager.getCurrentPlayer().getAccount());
                battleManager.useItem((Item) card, x1, x2);
            }
            for (Function function : card.getFunctions()) {
                if (function.getFunctionType() == FunctionType.OnSpawn) {
                    battleManager.compileFunction(function, x1, x2);
                }
            }
        } else {
            Output.notInHand();
            System.err.println("Minion not in hand");
            return false;
        }
        return true;
    }

    public static void selectCollectibleItem(int cardUniqueId) {
        for (Deployable deployable : battleManager.getCurrentPlayer().getCardsOnBattleField()) {
            if (deployable != null && deployable.getItem() != null)
                if (deployable.getItem().getUniqueId() == cardUniqueId)
                    battleManager.getCurrentPlayer().setSelectedCard(deployable.getItem());
        }
    }

    public static void showGameInfo() {
        if (BattleManager.getGameMode() == GameMode.DeathMatch) {
            Output.print("Game Mode: Death Match\nPlayer1 Hero health: " + battleManager.getPlayer1().getHero().
                    getCurrentHealth() + "\nPlayer2 Hero health: " + battleManager.getPlayer2().getHero().
                    getCurrentHealth());
        } else if (BattleManager.getGameMode() == GameMode.Flag && BattleManager.flagPosition()[0] != -1) {
            Output.print("Game Mode: Flag\nFlag Position: " + BattleManager.flagPosition()[0] + " , " +
                    BattleManager.flagPosition()[1]);
        } else {
            Output.print("Team 1, cards with flags are:\n");
            for (Deployable deployable : battleManager.getPlayer1().getCardsOnBattleField()) {
                if (deployable.doesHaveFlag())
                    Output.print(deployable.infoToString());
            }
            Output.print("Team 2, cards with flags are:\n");
            for (Deployable deployable : battleManager.getPlayer2().getCardsOnBattleField()) {
                if (deployable.doesHaveFlag())
                    Output.print(deployable.infoToString());
            }
        }

    }

    public static void showSelectedCardInfo() {
        if (battleManager.getCurrentPlayer().getSelectedCard() != null)
            System.out.println(battleManager.getCurrentPlayer().getSelectedCard().infoToString());
        else
            System.out.println("Card not in hand");
    }

  /*  public static Deployable findDeadMinion(int uniqueId) {
        for (Deployable deployable : battleManager.getPlayer1().getGraveYard()) {
            if (deployable != null && deployable.getUniqueId() == uniqueId)
                return deployable;
        }
        for (Deployable deployable : battleManager.getPlayer2().getGraveYard()) {
            if (deployable != null && deployable.getUniqueId() == uniqueId)
                return deployable;
        }
        return null;

    }*/

    public static void replaceCardInHand(int cardId) {
        for (Card card : battleManager.getCurrentPlayer().getHand()) {
            if (card != null && card.getId() == cardId) {
                Card tempCard = card;
                battleManager.getCurrentPlayer().getHand().remove(card);
                battleManager.getCurrentPlayer().getCurrentDeck().addCard(tempCard);
                battleManager.getCurrentPlayer().getHand().add(battleManager.getCurrentPlayer().getCardInReplace());
                battleManager.getCurrentPlayer().getCurrentDeck().getCards().remove
                        (battleManager.getCurrentPlayer().getCardInReplace());
                battleManager.getCurrentPlayer().generateCardInReplace();
                return;
            }
        }
        System.err.println("you don't have this card in your hand.");
    }

    public static void showGlimpseOfMap(BattleManager battleManager) {
        System.out.println("Player 1 minions are:\n");
        for (Deployable deployable : battleManager.getPlayer1().getCardsOnBattleField()) {
            if (deployable != null)
                System.out.println(deployable.shortVersionString());
        }

        System.out.println("Player 2 minions are:\n");
        for (Deployable deployable : battleManager.getPlayer2().getCardsOnBattleField()) {
            if (deployable != null)
                System.out.println(deployable.shortVersionString());
        }
        for (Cell[] cells : Map.getMap()) {
            for (Cell cell : cells) {
                if (cell != null && cell.getItem() != null) {
                    System.out.println(cell.getItem().toString() + " coordination:  " + cell.getX1Coordinate() + "," + cell.getX2Coordinate());
                }
            }
        }
    }
}

