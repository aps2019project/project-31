package controller;

import Server.ServerStrings;
import constants.CardType;
import constants.GameMode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import model.*;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MultiPlayerBattlePageController implements Initializable {

    public static final double SCALE = 0.25;
    public static final int OFFSET_X = -60;
    public static final int OFFSET_Y = -90;
    private static Scene scene;
    private static MultiPlayerBattlePageController multiPlayerBattle;
    public static final int HAND_CAPACITY = 6;
    public Button replace;
    public Label deckSize;
    public StackPane column1;
    public ImageView hand1;
    public Label manaCost1;
    public StackPane column2;
    public ImageView hand2;
    public Label manaCost2;
    public StackPane column3;
    public ImageView hand3;
    public Label manaCost3;
    public StackPane column4;
    public ImageView hand4;
    public Label manaCost4;
    public StackPane column5;
    public ImageView hand5;
    public Label manaCost5;
    public StackPane column6;
    public ImageView hand6;
    public Label manaCost6;
    public Button endTurn;
    public Button concede;
    public AnchorPane profile1Stack;
    public ImageView player1Profile;
    public ImageView profPic1;
    public Label health;
    public Label username;
    public Label gameMode;
    public ImageView mana1;
    public ImageView mana2;
    public ImageView mana3;
    public ImageView mana4;
    public ImageView mana5;
    public ImageView mana6;
    public ImageView mana7;
    public ImageView mana9;
    public ImageView mana8;
    public HBox hboxInTop;
    public Label myMana;
    public Label flag1;
    public Label flag2;
    public AnchorPane profile2Stack;
    public ImageView player2Profile;
    public ImageView profPic2;
    public Label opponentHealth;
    public Label opponentUsername;
    public Pane specialPane;
    public ImageView special;
    public ImageView specialSpell;
    public Label generalCoolDown;
    public Label generalSpellManaCost;
    public Button graveYard;
    public Pane nextCardField;
    public Button comboAttack;
    public Pane opponentSpecialPane;
    public ImageView opponentSpecialSpell;
    public ImageView opponentSpecial;
    public Label opponentGeneralCoolDown;
    public Label opponentGeneralSpellManaCost;
    public Label opponentHand;
    public Label opponentMana;
    public VBox messageBox;
    public Pane mainPane;
    public Polyline place51;
    public Polyline place52;
    public Polyline place53;
    public Polyline place54;
    public Polyline place55;
    public Polyline place56;
    public Polyline place57;
    public Polyline place58;
    public Polyline place59;
    public Polyline place41;
    public Polyline place42;
    public Polyline place43;
    public Polyline place44;
    public Polyline place45;
    public Polyline place46;
    public Polyline place47;
    public Polyline place48;
    public Polyline place31;
    public Polyline place32;
    public Polyline place33;
    public Polyline place34;
    public Polyline place35;
    public Polyline place36;
    public Polyline place37;
    public Polyline place38;
    public Polyline place29;
    public Polyline place27;
    public Polyline place26;
    public Polyline place25;
    public Polyline place24;
    public Polyline place23;
    public Polyline place22;
    public Polyline place21;
    public Polyline place11;
    public Polyline place12;
    public Polyline place13;
    public Polyline place14;
    public Polyline place15;
    public Polyline place16;
    public Polyline place17;
    public Polyline place18;
    public Polyline place19;
    public Polyline place39;
    public Polyline place28;
    public Polyline place49;
    public Button selectMItem;
    public Button infoBtn;


    private Player me;
    private Player opponent;
    private boolean isInGraveYard = false;
    private ArrayList<ImageView> manas = new ArrayList<>();
    private ColumnOfHand[] columnHands = new ColumnOfHand[6];
    private GameRecord gameRecord;

    public HBox getHboxInTop() {
        return hboxInTop;
    }

    public MultiPlayerBattlePageController() {
    }

    public void theThingsWeDoWheIitIsNotOurTime() {  // :'((((((
        System.out.println("i'm in the theThingsWeDoWheIitIsNotOurTime");
        new Thread(() -> {
            while (!isMyTurn()) {
                System.out.println("we are at do what server says !");
                String command = null;
                try {
                    command = Client.getClient().getIs().readUTF();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("we received this command : " + command);
                if (command != ServerStrings.NOTALLOWED) {
                    BattleMenu.getBattleManager().doWhatIAmToldTo(command);
                } else System.out.println("not allowed !!!");
                Platform.runLater(() -> refreshTheStatusOfMap(BattleMenu.getBattleManager()));
            }
            BattleMenu.showGlimpseOfMap();
            System.out.println("the thread in theThingsWeDoWheIitIsNotOurTime has ended just fine :)");
        }).start();

    }

    private void showMinions() {
        System.out.println("my minions are :");
        for (Deployable card : me.getCardsOnBattleField()) {
            System.out.println(card.shortVersionString());
        }
        System.out.println("opponent minions are :");
        for (Deployable card : opponent.getCardsOnBattleField())
            System.out.println(card.shortVersionString());
    }

    private void playTheActualGame(BattleManager battle) {
        graphicStuffAtBegin();
        System.out.println("we are going to play the actual game :D");
        Client.getClient().receiveMapAndBattle();


        if (!isMyTurn()) {
            theThingsWeDoWheIitIsNotOurTime();
        }


        infoBtn.setOnAction(actionEvent -> {
            if (!isMyTurn()) {
                displayMessage("this is not your turn =");
            } else
                infoButton();
        });
        selectMItem.setOnAction(event -> {
            if (!isMyTurn()) {
                displayMessage("this is not your turn =");
            } else
                selectItem();
        });
        replace.setOnAction(event -> {
            /*if (!isMyTurn()) {
                displayMessage("this is not your turn =");
            } else {
                if (isMyTurn() && battle.getCurrentPlayer().getSelectedCard() != null) {
                    MultiPlayerBattlePageController.getInstance().replaceCardInHand(battle.getCurrentPlayer().getSelectedCard().getId(), battle);

                }
            }*/
        });
        endTurn.setOnAction(event -> {
            if (!isMyTurn()) {
                displayMessage("this is not your turn =");
            } else {
                Client.getClient().sendEndTurnRequest();
            }

        });
        concede.setOnAction(event -> {
            if (!isMyTurn()) {
                displayMessage("this is not your turn =");
            } else {
                Client.getClient().sendConcedeRequest();
            }

        });
        graveYard.setOnAction(event -> {
            isInGraveYard = true;
            GraveYardController.getInstance().setAsScene();
        });
    }

    private void graphicStuffAtBegin() {
        initPlayers();
        makeColumnHands();
        setPolygonsInMap();
        setOnActionForEveryCell();
        nameAndStuff();
        initHeroes(BattleMenu.getBattleManager());
        setGameMode(BattleMenu.getBattleManager());
        refreshTheStatusOfMap(BattleMenu.getBattleManager());
        for (int i = 0; i < HAND_CAPACITY; i++) {
            handleHandPlace(i, BattleMenu.getBattleManager());
        }
    }

    private void nameAndStuff() {
        username.setText(me.getAccount().getUsername());
        opponentUsername.setText(opponent.getAccount().getUsername());
        generalSpellManaCost.setText("" + me.getHero().getHeroSpell().getManaCost());
        opponentGeneralSpellManaCost.setText("" + opponent.getHero().getHeroSpell().getManaCost());
        generalCoolDown.setText("" + me.getHero().getHeroSpell().getManaCost());
        opponentGeneralCoolDown.setText("" + opponent.getHero().getHeroSpell().getManaCost());
    }

    public void setInGraveYard(boolean inGraveYard) {
        isInGraveYard = inGraveYard;
    }

    public void setAsScene() {
        if (!isInGraveYard) {
            loadTheScene();
        } else {
            isInGraveYard = false;
        }
        Initializer.setCurrentScene(scene);
    }

    private void loadTheScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MultiPlayerBattlePage.fxml"));
            Double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
            scene = new Scene(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBattlePage() {
        multiPlayerBattle = null;
    }

    public static MultiPlayerBattlePageController getInstance() {
        if (multiPlayerBattle == null)
            multiPlayerBattle = new MultiPlayerBattlePageController();
        return multiPlayerBattle;
    }

    public void setMe(Player me) {
        this.me = me;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public void initPlayers() {
        if (BattleMenu.getBattleManager().getPlayer1().getAccount().getUsername().equals(Account.getMainAccount().getUsername())) {
            System.out.println("im player 1");
            me = BattleMenu.getBattleManager().getPlayer1();
            opponent = BattleMenu.getBattleManager().getPlayer2();
        } else {
            System.out.println("im player 2");
            me = BattleMenu.getBattleManager().getPlayer2();
            opponent = BattleMenu.getBattleManager().getPlayer1();
        }
        if (opponent == null) {
            System.err.println("nulllll");
            return;
        }
    }

    public void updateNextCard() {
        if (nextCardField.getChildren().size() > 1)
            nextCardField.getChildren().remove(1);
        if (me.getNextCard().getType() == CardType.minion) {
            nextCardField.getChildren().add(new DisplayableDeployable((Deployable) me.getNextCard()));
            nextCardField.getChildren().get(1).setTranslateX(30);
            nextCardField.getChildren().get(1).setTranslateY(10);
        }
        if (me.getNextCard().getType() == CardType.spell) {
            DisplayableCard displayableCard = new DisplayableCard(me.getNextCard(), "");
            displayableCard.setScaleX(SCALE);
            displayableCard.setScaleY(SCALE);
            displayableCard.setTranslateY(OFFSET_Y);
            displayableCard.setTranslateX(OFFSET_X);
            nextCardField.getChildren().add(displayableCard);
        }
    }

    public void removeSpellFromHand(DisplayableCard card, BattleManager battle) {
        for (int i = 0; i < 6; i++) {
            controller.ColumnOfHand column = columnHands[i];
            if (column.stackPane.getChildren().remove(card)) {
                return;
            }
        }
    }

    public void removeCardFromHand(Card card, BattleManager battle) {
        if (card.getType() == CardType.minion)
            removeMinionFromHand(((Minion) card).getFace());
        else if (card.getType() == CardType.spell)
            removeSpellFromHand(((Spell) card).getFace(), battle);
    }

    public void putNextCardInHand(BattleManager battle) {
        for (int i = 0; i < 6; i++) {
            if (columnHands[i].stackPane.getChildren().size() == 1) {
                if (nextCardField.getChildren().size() >= 2) {
                    if (nextCardField.getChildren().get(1) instanceof DisplayableDeployable) {
                        showMinionInHand(((DisplayableDeployable) nextCardField.getChildren().get(1)).getDeployable(), i, battle);
                        nextCardField.getChildren().remove(1);
                    } else {
                        showSpellInHand(((DisplayableCard) nextCardField.getChildren().get(1)).getCard(), i, battle);
                        nextCardField.getChildren().remove(1);
                    }
                }
            }
        }
        updateManaViewers(battle);
    }

    public void removeMinionFromHand(DisplayableDeployable face) {
        for (int i = 0; i < 6; i++) {
            controller.ColumnOfHand column = columnHands[i];
            if (column.stackPane.getChildren().remove(face)) {
                return;
            }
        }
    }

    public void displayMessage(String s) {
        LoginPageController.getInstance().displayMessage(s, 10, 2, messageBox);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        multiPlayerBattle = this;
        BattleManager battle = BattleMenu.getBattleManager();

        if (!battle.isThisRecordedGame())
            playTheActualGame(battle);
        else {
            gameRecord = Account.getMainAccount().getSelectedGameRecord();
            //    recordTheGame(battle);
        }
    }

    @FXML
    private void selectItem() {
        if (me.getSelectedCard() == null) {
            displayMessage("select a minion first");
        } else if (me.getSelectedCard().getType() != CardType.hero && me.getSelectedCard().getType() != CardType.minion)
            displayMessage("select a deployable for selecting collectible item");
        else if (((Deployable) me.getSelectedCard()).getItem() == null)
            displayMessage("your deployable doesn't have an item to select");
        else me.setSelectedCard(((Deployable) me.getSelectedCard()).getItem());
    }

    public void replaceCardInHand(int cardId, BattleManager battleManager) {
        for (Card card : MultiPlayerBattlePageController.getInstance().me.getHand()) {
            if (card != null && card.getId() == cardId) {
                Player me = MultiPlayerBattlePageController.getInstance().me;
                if (me.hasReplaced()) {
                    displayMessage("you have replaced once this turn!!");
                    return;
                }
                me.generateCardInReplace();
                MultiPlayerBattlePageController.getInstance().removeCardFromHand(card, battleManager);
                me.getHand().remove(card);
                me.getCurrentDeck().addCard(card);
                Card cardInReplace = me.getCardInReplace();
                me.getHand().add(cardInReplace);
                me.getCurrentDeck().getCards().remove
                        (cardInReplace);
                MultiPlayerBattlePageController.getInstance().showCardInHand(cardInReplace, battleManager);
                me.setHasReplaced(true);
                return;

            }
        }
        System.err.println("you don't have this card in your hand.");
    }

    public void addFaceToBattlePage(Minion theMinion, BattleManager battle) {
        DisplayableDeployable face = new DisplayableDeployable(theMinion);
        theMinion.setFace(face);
        face.updateStats();
        if (MultiPlayerBattlePageController.getInstance() != null) {
            if (MultiPlayerBattlePageController.getInstance().mainPane == null) {
                System.err.println("main pane is null");
                return;
            }
            MultiPlayerBattlePageController.getInstance().mainPane.getChildren().add(face);
        }
        face.setOnMouseClicked(event -> {
            if (!isMyTurn()) {
                displayMessage("this is not your turn =");
            } else {
                MultiPlayerBattlePageController.getInstance().setOnMouseDeployable(theMinion);
                face.updateStats();
            }
        });
    }

    public void removeASpellFromHand(Player currentPlayer, boolean isThisRecordedGame, Spell spell, BattleManager battleManager) {
        if (!currentPlayer.isAi() && !isThisRecordedGame)
            MultiPlayerBattlePageController.getInstance().removeSpellFromHand(spell.getFace(), battleManager);
    }

    public void showThatGameEnded() {
        MainMenuController.getInstance().setAsScene();
        BattleMenu.deleteBattleManagerAndMakeMap();
        MultiPlayerBattlePageController.deleteBattlePage();
    }

   /* private void recordTheGame(BattleManager battle) {
        concede.setText("Exit");
        concede.setOnAction(event -> showThatGameEnded());
        System.out.println(gameRecord.getGame());
        setPolygonsInMap();
        initPlayers();
        initHeroesSpecialPowers();
        atStartThings(battle);
        Thread showGame = new Thread(() -> {

        });
        showGame.start();
    }*/

    private void setOnActionForEveryCell() {
        for (int i = 1; i <= Map.MAP_X1_LENGTH; i++) {
            for (int j = 1; j <= Map.MAP_X2_LENGTH; j++) {
                Cell cell = Map.getInstance().getMap()[i][j];
                final Polyline polyline = cell.getPolygon();
                setOnMouseForPolygon(cell, polyline);
            }
        }
    }
/*

    public void endTurn(BattleManager battle) throws InterruptedException {
        System.out.println("called endTurn in Multi player");
        Thread.sleep(300);
        putNextCardInHand(battle);
        updateNextCard();
        BattleMenu.getBattleManager().doAllThingsInEndingOfTheTurns();
        if (isMyTurn()) {
            battle.setCurrentPlayer(battle.getOtherPlayer());
        }
        BattleMenu.getBattleManager().doAllAtTheBeginningOfTurnThings();
        updateManaViewers(battle);
    }
*/

    private void initHeroesSpecialPowers() {
        specialSpell.setImage(new javafx.scene.image.Image(getClass().getResource("/gifs/Bloodbound/warbird.gif").toExternalForm()));
        specialSpell.setScaleX(0.5);
        specialSpell.setScaleY(0.5);
        opponentSpecialSpell.setImage(
                new javafx.scene.image.Image(getClass().getResource("/gifs/Bloodbound/conscript.gif").toExternalForm()));
        opponentSpecialSpell.setScaleY(0.85);
        opponentSpecialSpell.setScaleX(0.85);
        specialSpell.setOnMouseClicked(mouseEvent -> {
            if (!isMyTurn()) {
                displayMessage("this is not your turn =");
            } else {
                me.selectACard(me.getHero().getHeroSpell().getId());
                System.out.println(opponent.getHero().getId());
                System.out.println(me.getHero().getHeroSpell().getId());
            }
        });
    }

    private void showSpellInHand(Card card, int i, BattleManager battle) {
        DisplayableCard face = new DisplayableCard(card, "");
        face.setScaleX(SCALE);
        face.setScaleY(SCALE);
        face.setTranslateX(OFFSET_X);
        face.setTranslateY(OFFSET_Y);
        columnHands[i].getStackPane().getChildren().get(0).setTranslateX(OFFSET_X);
        columnHands[i].getStackPane().getChildren().get(0).setTranslateY(OFFSET_Y);
        ((Spell) card).setFace(face);
        columnHands[i].getStackPane().getChildren().add(face);
        face.setOnMouseClicked(event -> {
            if (!isMyTurn()) {
                displayMessage("this is not your turn =");
            } else {
                System.out.println("clicked! on a card");
                if (battle.getCurrentPlayer() == me) {
                    System.out.println(card.getId());
                    me.selectACard(card.getId());
                } else {
                    displayMessage("not my turn");
                    System.out.println("not my turn");
                }
            }
        });
    }

    @FXML
    private void infoButton() {
        if (me.getSelectedCard() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Select a card first!");
            alert.show();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(me.getSelectedCard().getName() + ":");
        StringBuilder context = new StringBuilder(me.getSelectedCard().toString() + "\n function: \n");
        for (Function function : me.getSelectedCard().getFunctions()) {
            context.append(function.getFunctionType()).
                    append(" ").append(function.getFunction()).
                    append(": ").append(function.getTarget());
        }
        alert.setContentText(context.toString());
        alert.setResizable(true);
        alert.show();
    }

    private void showMinionInHand(Deployable deployable, int index, BattleManager battle) {
        DisplayableDeployable face = new DisplayableDeployable(deployable);
        deployable.setFace(face);
        columnHands[index].getStackPane().getChildren().add(face);
        face.setTranslateY(-10);
        face.setOnMouseClicked(event -> {
            if (!isMyTurn()) {
                displayMessage("this is not your turn =");
            } else {
                System.out.println(" me is :" + me.getAccount().getUsername() + " the current player isssss : " + BattleMenu.getBattleManager().getCurrentPlayer().getAccount().getUsername());
                System.out.println("clicked! on " + deployable.getName());
           /*     if (battle.getCurrentPlayer().equals(me)) {

                } else {
                    displayMessage("not my turn");
                    System.out.println("not my turn");
                }*/
                System.out.println(face.getDeployable().getId());
                me.selectACard(face.getDeployable().getId());

                ((Deployable) (me.getSelectedCard())).setFace(face);
            }
        });
        //
        columnHands[index].getManaCost().setText(deployable.getManaCost() + "");
    }

    public void showCardInHand(Card card, BattleManager battle) {
        int index = 0;
        for (int i = 0; i < 6; i++) {
            if (columnHands[i].getStackPane().getChildren().size() < 2) {
                index = i;
                break;
            }
        }
        if (card.getType() == CardType.minion)
            showMinionInHand(((Deployable) card), index, battle);
        else if (card.getType() == CardType.spell)
            showSpellInHand(card, index, battle);
    }

    public void initHeroes(BattleManager battleManager) {
        System.out.println("init heroes !");
        Hero hero1 = battleManager.getPlayer1().getHero();
        Hero hero2 = battleManager.getPlayer2().getHero();
        DisplayableDeployable faceHero1 = new DisplayableDeployable(hero1);
        DisplayableDeployable faceHero2 = new DisplayableDeployable(hero2);
        hero1.setFace(faceHero1);
        hero2.setFace(faceHero2);
        mainPane.getChildren().addAll(faceHero1, faceHero2);
        hero1.setCell(Map.getInstance().getCell(3, 1));
        hero2.setCell(Map.getInstance().getCell(3, 9));
        hero1.getCell().setCardInCell(hero1);
        hero2.getCell().setCardInCell(hero2);
        hero1.setFace(faceHero1);
        hero2.setFace(faceHero2);
        faceHero1.updateStats();
        faceHero2.updateStats();

        faceHero1.setOnMouseClicked(event -> {
            setOnMouseDeployable(hero1);
            faceHero1.updateStats();
        });
        faceHero2.setOnMouseClicked(event -> {
            setOnMouseDeployable(hero2);
            faceHero2.updateStats();
        });
    }

    public void setOnMouseDeployable(Deployable card ) {
        BattleManager battleManager = BattleMenu.getBattleManager();
        System.out.println(" me is :" + me.getAccount().getUsername() + " the current player isssss : " + battleManager.getCurrentPlayer().getAccount().getUsername());
        if (battleManager.getCurrentPlayer().equals(me)) {
            if (me.getSelectedCard() != null && me.getSelectedCard().getType() == CardType.spell) {
                Client.getClient().sendInsertRequest(me.getSelectedCard().getId(), card.getCell().getX1Coordinate(), card.getCell().getX2Coordinate());
            } else if (me.getSelectedCard() != null && me.getSelectedCard().getType() == CardType.item) {
                Client.getClient().sendInsertRequest(me.getSelectedCard().getId(), card.getCell().getX1Coordinate(), card.getCell().getX2Coordinate());
            } else if (me.getSelectedCard() != null &&
                    me.getSelectedCard().getType() != CardType.spell &&
                    !card.getAccount().getUsername().equals(me.getAccount().getUsername())) {
                System.err.println(me.getSelectedCard().getName() + " attacked " + card.getName());
                if (((Deployable) me.getSelectedCard()).getCell() == null)
                    System.out.println("selected card cell is null");
                else if (card.getCell() == null)
                    System.out.println("clicked card cell is null");
                else
                    System.out.println("wtf is null then mother fucker ?");


                Client.getClient().sendAttackRequest(((Deployable) me.getSelectedCard()).getCell().getX1Coordinate()
                        , ((Deployable) me.getSelectedCard()).getCell().getX2Coordinate(), card.getCell().getX1Coordinate(), card.getCell().getX2Coordinate());
                ((Deployable) me.getSelectedCard()).getFace().attack();
                card.getFace().getHit();
            } else {
                System.out.println("going to select a card !");
                System.out.println(card.getUniqueId());
                me.selectACard(card.getUniqueId());
            }
        }
    }

    private boolean isMyTurn() {
        return BattleMenu.getBattleManager().getCurrentPlayer().equals(me);
    }

    public Player getMe() {
        return me;
    }

    public Player getOpponent() {
        return opponent;
    }

    public ArrayList<ImageView> getManas() {
        return manas;
    }


    private void refreshFlagsSituation(BattleManager battle) {
        if (battle.getGameMode() == GameMode.DeathMatch) {
            hboxInTop.getChildren().remove(flag1);
            hboxInTop.getChildren().remove(flag2);
        }
        if (battle.getGameMode() == GameMode.Flag) {
            flag1.setText(me.getNumberOfTurnsHavingFlag() + "");
            flag2.setText(opponent.getNumberOfTurnsHavingFlag() + "");
        }
        if (battle.getGameMode() == GameMode.Domination) {
            flag1.setText(me.getNumberOfFlags() + "");
            flag2.setText(opponent.getNumberOfFlags() + "");
        }
    }

    private void healthAndCooldownStuff() {
        try {
            health.setText("" + me.getHero().theActualHealth());
            opponentHealth.setText("" + opponent.getHero().theActualHealth());
            opponentHand.setText("Hand: " + opponent.handSize() + " / 6");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("mohem nis :)");
        }
        try {
            updateManaViewers(BattleMenu.getBattleManager());
            generalCoolDown.setText("" + me.getHero().getHeroSpell().getCoolDownRemaining());
            opponentGeneralCoolDown.setText("" + opponent.getHero().getHeroSpell().getCoolDownRemaining());
            deckSize.setText("Deck: " + (me.deckSize() + 1) + "/18");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("migam ke mohem nis");
        }
    }

    public void refreshPartly() {
        for (Cell[] cells : Map.getInstance().getMap()) {
            for (Cell cell : cells) {
                if (cell != null && cell.getCardInCell() != null) {
//                    System.out.println("the cell has card in cell");
                    if (cell.getCardInCell().getFace() != null) {
//                        System.out.println("the cell has face !");
                        cell.getCardInCell().getFace().updateStats();
                    } else
                        System.out.println("the cell does not have face!!!!!!!!!!!!! fuuuuuuuuuuuuuuuuuuuuuuuuuuuck");
                }


            }
        }
    }

    private void createFaceForDeplyobale() {
        for (Cell[] cells : Map.getInstance().getMap()) {
            for (Cell cell : cells) {
                if (cell != null && cell.getCardInCell() != null) {
                    System.out.println("card cell nadasht :////////");
                    cell.getCardInCell().setCell(cell);
                }
                if (cell != null && cell.getCardInCell() != null && cell.getCardInCell().getFace() == null) {
                    System.out.println("we created face for cell: " + cell.getX1Coordinate() + cell.getX2Coordinate());
                    DisplayableDeployable face = new DisplayableDeployable(cell.getCardInCell());
                    cell.getCardInCell().setFace(face);
                    face.setOnMouseClicked(event -> {
                        setOnMouseDeployable(cell.getCardInCell());
                        face.updateStats();
                    });
                }
            }
        }
    }

    public void refreshTheStatusOfMap(BattleManager battleManager) {
        System.out.println("refresh reached!");
        initPlayers();
        createFaceForDeplyobale();
        refreshPartly();
        nameAndStuff();
        if (battleManager.isTheGameFinished())
            return;
        refreshFlagsSituation(battleManager);
        if (!battleManager.isThisRecordedGame()) {
            BattleMenu.getBattleManager().checkTheEndSituation();
            healthAndCooldownStuff();
            showItems();
        }
        showFlag();
        showMinions();
    }

    public void showFlag() {

        for (Cell[] cells : Map.getInstance().getMap()) {
            for (Cell cell : cells) {
                if (cell.hasFlag() && cell.getDisplayableFlag() == null) {
                    ImageView flag;
                    String imagePath = getClass().getResource("/gifs/flag.gif").toExternalForm();
                    flag = new ImageView(new Image(imagePath));
                    flag.setTranslateY(-50);
                    flag.setScaleX(1.3);
                    flag.setScaleY(1.3);
                    cell.setDisplayableFlag(flag);
                    flag.setTranslateX(cell.calculateCenter()[0]);
                    flag.setTranslateY(cell.calculateCenter()[1]);
                    mainPane.getChildren().add(flag);
                }

            }
        }


    }

    private void showItems() {
        for (Cell[] cells : Map.getInstance().getMap()) {
            for (Cell cell : cells) {
                if (cell.getItem() != null && cell.getDisplayableItem() == null) {
                    try {
                        DisplayableCard displayableCard = new DisplayableCard(cell.getItem(), "");
                        ImageView itemIcon = displayableCard.getMainIcon();
                        itemIcon.setScaleX(1);
                        itemIcon.setScaleY(1);
                        cell.setDisplayableItem(itemIcon);
                        itemIcon.setTranslateX(cell.calculateCenter()[0]);
                        itemIcon.setTranslateY(cell.calculateCenter()[1]);
                        mainPane.getChildren().add(itemIcon);
                    } catch (NullPointerException e) {
                        System.err.println("The item gif not found");
                    }
                }
            }
        }
    }

    public void removeItemInGround(Cell cell) {
        if (cell.getDisplayableItem() != null)
            mainPane.getChildren().remove(cell.getDisplayableItem());
    }

    public void removeFlagInGround(Cell cell) {
        if (cell.getDisplayableFlag() != null)
            mainPane.getChildren().remove(cell.getDisplayableFlag());
    }

    private void updateManaViewers(BattleManager battleManager) {
        int usualMana;
        if (battleManager.getTurn() <= 14) {
            usualMana = (battleManager.getTurn() - 1) / 2 + 2;
        } else {
            usualMana = 9;
        }
        if (me == battleManager.getCurrentPlayer()) {
            myMana.setText(me.getMana() + " / " + usualMana);
            opponentMana.setText(opponent.getMana() + " / " + usualMana);
        }
//        opponentMana.setText("" + (usualMana + opponent.getManaChangerInTurn()[battleManager.getTurn()]) + " / " + usualMana);
//        myMana.setText("" + (usualMana + me.getManaChangerInTurn()[battleManager.getTurn()]) + " / " + usualMana);
        /////getManaChangerInTurn[] has bug
    }

    private void handleHandPlace(int handNumber, BattleManager battle) {
        try {
            if (me.getHand().get(handNumber) != null) {
                if (me.getHand().get(handNumber).getType() == CardType.minion) {
                    showMinionInHand((Deployable) me.getHand().get(handNumber), handNumber, battle);
                }
                if (me.getHand().get(handNumber).getType() == CardType.spell) {
                    showSpellInHand(me.getHand().get(handNumber), handNumber, battle);
                }
                columnHands[handNumber].getManaCost().setText(me.getHand().get(handNumber).getManaCost() + "");
            }
        } catch (Exception e) {

        }
    }

    private void setGameMode(BattleManager battle) {
        if (battle.getGameMode() == GameMode.DeathMatch)
            gameMode.setText("DEATH MATCH");
        else if (battle.getGameMode() == GameMode.Domination)
            gameMode.setText("DOMINATION");
        else if (battle.getGameMode() == GameMode.Flag) {
            gameMode.setText("FLAG");
            gameMode.setTranslateX(45);
        }
    }

    private void makeColumnHands() {
        columnHands[0] = new controller.ColumnOfHand(column1, manaCost1);
        columnHands[1] = new controller.ColumnOfHand(column2, manaCost2);
        columnHands[2] = new controller.ColumnOfHand(column3, manaCost3);
        columnHands[3] = new controller.ColumnOfHand(column4, manaCost4);
        columnHands[4] = new controller.ColumnOfHand(column5, manaCost5);
        columnHands[5] = new controller.ColumnOfHand(column6, manaCost6);
    }

    private void setPolygonsInMap() {
        Map.getInstance().getMap()[1][1].setPolygon(place11);
        Map.getInstance().getMap()[1][2].setPolygon(place12);
        Map.getInstance().getMap()[1][3].setPolygon(place13);
        Map.getInstance().getMap()[1][4].setPolygon(place14);
        Map.getInstance().getMap()[1][5].setPolygon(place15);
        Map.getInstance().getMap()[1][6].setPolygon(place16);
        Map.getInstance().getMap()[1][7].setPolygon(place17);
        Map.getInstance().getMap()[1][8].setPolygon(place18);
        Map.getInstance().getMap()[1][9].setPolygon(place19);
        Map.getInstance().getMap()[2][1].setPolygon(place21);
        Map.getInstance().getMap()[2][2].setPolygon(place22);
        Map.getInstance().getMap()[2][3].setPolygon(place23);
        Map.getInstance().getMap()[2][4].setPolygon(place24);
        Map.getInstance().getMap()[2][5].setPolygon(place25);
        Map.getInstance().getMap()[2][6].setPolygon(place26);
        Map.getInstance().getMap()[2][7].setPolygon(place27);
        Map.getInstance().getMap()[2][8].setPolygon(place28);
        Map.getInstance().getMap()[2][9].setPolygon(place29);
        Map.getInstance().getMap()[3][1].setPolygon(place31);
        Map.getInstance().getMap()[3][2].setPolygon(place32);
        Map.getInstance().getMap()[3][3].setPolygon(place33);
        Map.getInstance().getMap()[3][4].setPolygon(place34);
        Map.getInstance().getMap()[3][5].setPolygon(place35);
        Map.getInstance().getMap()[3][6].setPolygon(place36);
        Map.getInstance().getMap()[3][7].setPolygon(place37);
        Map.getInstance().getMap()[3][8].setPolygon(place38);
        Map.getInstance().getMap()[3][9].setPolygon(place39);
        Map.getInstance().getMap()[4][1].setPolygon(place41);
        Map.getInstance().getMap()[4][2].setPolygon(place42);
        Map.getInstance().getMap()[4][3].setPolygon(place43);
        Map.getInstance().getMap()[4][4].setPolygon(place44);
        Map.getInstance().getMap()[4][5].setPolygon(place45);
        Map.getInstance().getMap()[4][6].setPolygon(place46);
        Map.getInstance().getMap()[4][7].setPolygon(place47);
        Map.getInstance().getMap()[4][8].setPolygon(place48);
        Map.getInstance().getMap()[4][9].setPolygon(place49);
        Map.getInstance().getMap()[5][1].setPolygon(place51);
        Map.getInstance().getMap()[5][2].setPolygon(place52);
        Map.getInstance().getMap()[5][3].setPolygon(place53);
        Map.getInstance().getMap()[5][4].setPolygon(place54);
        Map.getInstance().getMap()[5][5].setPolygon(place55);
        Map.getInstance().getMap()[5][6].setPolygon(place56);
        Map.getInstance().getMap()[5][7].setPolygon(place57);
        Map.getInstance().getMap()[5][8].setPolygon(place58);
        Map.getInstance().getMap()[5][9].setPolygon(place59);

    }

    private void setOnMouseForPolygon(Cell cell, Polyline polyline) {
        try {
            polyline.setOnMouseEntered(event -> {
                polyline.setFill(javafx.scene.paint.Color.rgb(0, 0, 0, 0.35));
            });
            polyline.setOnMouseExited(event -> {
                polyline.setFill(Color.rgb(0, 0, 0, 0.15));
            });
            polyline.setOnMouseClicked(event -> {
                if (!isMyTurn()) {
                    displayMessage("this is not your turn =");
                } else {
                    if (me.getSelectedCard() == null) {
                        displayMessage("select a card first");
                        System.err.println("no selected card");
                        return;
                    }
                    if (cell.getCardInCell() == null && (me.getSelectedCard().getType() == CardType.item)) { // spell can't insert on the ground ?'
                        Client.getClient().sendInsertRequest(me.getSelectedCard().getId(), cell.getX1Coordinate(), cell.getX2Coordinate());
                        //  BattleMenu.insert(me.getSelectedCard(), cell.getX1Coordinate(), cell.getX2Coordinate());
                    } else if (cell.getCardInCell() != null) {
                        displayMessage("destination is not empty");
                        System.out.println("card in this cell is : " + cell.getCardInCell().infoToString());
                        return;
                    }
                    if (me.isSelectedCardDeployed()) {
                        System.out.println("we called move method dude!");
                        Client.getClient().sendMoveRequest(((Deployable) me.getSelectedCard()).getCell().getX1Coordinate()
                                , ((Deployable) me.getSelectedCard()).getCell().getX2Coordinate(), cell.getX1Coordinate(), cell.getX2Coordinate());
                        //    BattleMenu.getBattleManager().move((Deployable) me.getSelectedCard(), cell.getX1Coordinate(), cell.getX2Coordinate());

                    } else if (!me.isSelectedCardDeployed()) {
                        Client.getClient().sendInsertRequest(me.getSelectedCard().getId(), cell.getX1Coordinate(), cell.getX2Coordinate());
                        //      BattleMenu.insert(me.getSelectedCard(), cell.getX1Coordinate(), cell.getX2Coordinate());
                    } else if (me.getSelectedCard() != null && me.getSelectedCard().getType() == CardType.spell) {
                        Client.getClient().sendInsertRequest(me.getSelectedCard().getId(), cell.getX1Coordinate(), cell.getX2Coordinate());
                        //      BattleMenu.insert(me.getSelectedCard(), cell.getX1Coordinate(), cell.getX2Coordinate());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}