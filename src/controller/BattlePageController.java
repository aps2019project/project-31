package controller;

import constants.CardType;
import constants.FunctionType;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import model.*;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BattlePageController implements Initializable {
    private static Scene scene;
    private static BattlePageController battlePageController;


    public Button graveYard;
    public StackPane column1;
    public StackPane column2;
    public ImageView hand1;
    public ImageView hand2;
    public StackPane column3;
    public ImageView hand3;
    public StackPane column4;
    public ImageView hand4;
    public StackPane column5;
    public ImageView hand5;
    public StackPane column6;
    public ImageView hand6;

    public Button endTurn;
    public Button replace;
    public StackPane lastStackPane; // it's scaled to (0.01,0.01) ...
    public Label opponentHand;
    public Label deckSize;
    public Label health;
    public Label username;
    public Label generalCoolDown;
    public Pane motherFuckinPane;
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
    public Polyline place49;
    public Polyline place31;
    public Polyline place32;
    public Polyline place33;
    public Polyline place34;
    public Polyline place35;
    public Polyline place36;
    public Polyline place37;
    public Polyline place38;
    public Polyline place39;
    public Polyline place29;
    public Polyline place28;
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
    public Button setting;

    private StackPane showingGraveYard; // for showing it: lastStackPane = showingGraveYard; showingGraveYard is a designed scene

    private Player me;
    private Player opponent;


    public Label opponentMana;


    public ImageView nextCardField;
    public ImageView mana1;
    public ImageView mana2;
    public ImageView mana3;
    public ImageView mana4;
    public ImageView mana5;
    public ImageView mana6;
    public ImageView mana7;
    public ImageView mana8;
    public ImageView mana9;
    public Label manaCost1;
    public Label manaCost2;
    public Label manaCost3;
    public Label manaCost4;
    public Label manaCost5;
    public Label manaCost6;


    public Label opponentHealth;
    public Label opponentUsername;
    public Label opponentGeneralCooldown;
    public Label generalSpellManaCost;
    public Label opponentGeneralSpellManaCost;
    private ArrayList<ImageView> manas = new ArrayList<>();

    public BattlePageController() {
        System.err.println("HLLLO");
    }

    public void setAsScene() {
        if (scene == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/BattlePage.fxml"));
                Double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                scene = new Scene(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(scene);
    }

    public static BattlePageController getInstance() {
        if (battlePageController == null) {
            battlePageController = new BattlePageController();
        }
        return battlePageController;
    }

    private void initPlayers() {
        if (BattleMenu.getBattleManager().getPlayer1().getAccount().getUsername().equals(Account.getMainAccount())) {
            me = BattleMenu.getBattleManager().getPlayer1();
            opponent = BattleMenu.getBattleManager().getPlayer2();
        } else {
            me = BattleMenu.getBattleManager().getPlayer2();
            opponent = BattleMenu.getBattleManager().getPlayer1();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ColumnOfHand[] columnHands = new ColumnOfHand[6];

        try {
            columnHands[0] = new ColumnOfHand(column1, manaCost1);
            columnHands[1] = new ColumnOfHand(column2, manaCost2);
            columnHands[2] = new ColumnOfHand(column3, manaCost3);
            columnHands[3] = new ColumnOfHand(column4, manaCost4);
            columnHands[4] = new ColumnOfHand(column5, manaCost5);
            columnHands[5] = new ColumnOfHand(column6, manaCost6);

            BattlePageController.getInstance().initTheMapCells();

            Map.getMap()[1][1].setPolygon(place11);
            Map.getMap()[1][2].setPolygon(place12);
            Map.getMap()[1][3].setPolygon(place13);
            Map.getMap()[1][4].setPolygon(place14);
            Map.getMap()[1][5].setPolygon(place15);
            Map.getMap()[1][6].setPolygon(place16);
            Map.getMap()[1][7].setPolygon(place17);
            Map.getMap()[1][8].setPolygon(place18);
            Map.getMap()[1][9].setPolygon(place19);
            Map.getMap()[2][1].setPolygon(place21);
            Map.getMap()[2][2].setPolygon(place22);
            Map.getMap()[2][3].setPolygon(place23);
            Map.getMap()[2][4].setPolygon(place24);
            Map.getMap()[2][5].setPolygon(place25);
            Map.getMap()[2][6].setPolygon(place26);
            Map.getMap()[2][7].setPolygon(place27);
            Map.getMap()[2][8].setPolygon(place28);
            Map.getMap()[2][9].setPolygon(place29);
            Map.getMap()[3][1].setPolygon(place31);
            Map.getMap()[3][2].setPolygon(place32);
            Map.getMap()[3][3].setPolygon(place33);
            Map.getMap()[3][4].setPolygon(place34);
            Map.getMap()[3][5].setPolygon(place35);
            Map.getMap()[3][6].setPolygon(place36);
            Map.getMap()[3][7].setPolygon(place37);
            Map.getMap()[3][8].setPolygon(place38);
            Map.getMap()[3][9].setPolygon(place39);
            Map.getMap()[4][1].setPolygon(place41);
            Map.getMap()[4][2].setPolygon(place42);
            Map.getMap()[4][3].setPolygon(place43);
            Map.getMap()[4][4].setPolygon(place44);
            Map.getMap()[4][5].setPolygon(place45);
            Map.getMap()[4][6].setPolygon(place46);
            Map.getMap()[4][7].setPolygon(place47);
            Map.getMap()[4][8].setPolygon(place48);
            Map.getMap()[4][9].setPolygon(place49);
            Map.getMap()[5][1].setPolygon(place51);
            Map.getMap()[5][2].setPolygon(place52);
            Map.getMap()[5][3].setPolygon(place53);
            Map.getMap()[5][4].setPolygon(place54);
            Map.getMap()[5][5].setPolygon(place55);
            Map.getMap()[5][6].setPolygon(place56);
            Map.getMap()[5][7].setPolygon(place57);
            Map.getMap()[5][8].setPolygon(place58);
            Map.getMap()[5][9].setPolygon(place59);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            for (int i = 1; i <= 5; i++) {
                for (int j = 1; j <= 9; j++) {
                    final Polyline polyline = Map.getMap()[i][j].getPolygon();
                    try {
                        polyline.setOnMouseEntered(event -> {
                            polyline.setFill(Color.GREEN);
                        });
                        polyline.setOnMouseClicked(mouseEvent -> {
                            System.out.println("selected this: " + polyline.toString());
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BattleManager battle = BattleMenu.getBattleManager();
        try {

            initPlayers();
            battle.initialTheGame();
            for (int i = 0; i < 6; i++) {
                if (me.getHand().get(i) != null) {
                    if (me.getHand().get(i).getType() == CardType.minion) {
                        DisplayableDeployable face = new DisplayableDeployable((Deployable) me.getHand().get(i));
                        ((Deployable) me.getHand().get(i)).setFace(face);
                        columnHands[i].getStackPane().getChildren().add(face);
                        face.setTranslateY(95.0);
                        //
                        face.setOnMouseClicked(event -> {
                            System.out.println("clicked!");
                            if (battle.getCurrentPlayer() == me)
                                me.selectACard(face.getDeployable().getId());
                            else
                                System.out.println("not my turn");
                        });
                        //
                    }


                    columnHands[i].getManaCost().setText(me.getHand().get(i).getManaCost() + "");
                }
            }

            username.setText(me.getAccount().getUsername());
            opponentUsername.setText(opponent.getAccount().getUsername());
            generalSpellManaCost.setText("" + me.getHero().getHeroSpell().getManaCost());
            opponentGeneralSpellManaCost.setText("" + opponent.getHero().getHeroSpell().getManaCost());
            generalCoolDown.setText("" + me.getHero().getHeroSpell().getManaCost());
            opponentGeneralCooldown.setText("" + opponent.getHero().getHeroSpell().getManaCost());
            battle.getPlayer1().generateDeckArrangement();
            battle.getPlayer2().generateDeckArrangement();
            battle.setCurrentPlayer(BattleMenu.getBattleManager().getPlayer2());
            battle.applyItemFunctions(BattleMenu.getBattleManager().getCurrentPlayer().getHero(), FunctionType.GameStart);
            battle.setCurrentPlayer(BattleMenu.getBattleManager().getPlayer1());
            battle.applyItemFunctions(BattleMenu.getBattleManager().getCurrentPlayer().getHero(), FunctionType.GameStart);
            battle.setCurrentPlayer(BattleMenu.getBattleManager().getPlayer2());
            initHeroes(battle, motherFuckinPane);
            refreshTheStatusOfMap(battle);
            manas.add(mana1);
            manas.add(mana2);
            manas.add(mana3);
            manas.add(mana4);
            manas.add(mana5);
            manas.add(mana6);
            manas.add(mana7);
            manas.add(mana8);
            manas.add(mana9);
        } catch (Exception e) {
            e.printStackTrace();
        }
        replace.setOnAction(event -> {
            if (isMyTrun() && battle.getCurrentPlayer().getSelectedCard() != null) {
                BattleMenu.replaceCardInHand(battle.getCurrentPlayer().getSelectedCard().getId());
            }
        });
        endTurn.setOnAction(event -> {
            BattleMenu.doAllThingsInEndingOfTheTurns();
            if (isMyTrun()) {
                battle.setCurrentPlayer(battle.getOtherPlayer());
            }
            if (battle.getCurrentPlayer().isAi()) {
                ((Ai) battle.getCurrentPlayer()).play();
                battle.setCurrentPlayer(battle.getOtherPlayer());
            }
            BattleMenu.doAllAtTheBeginningOfTurnThings();
        });
        // select a card ydt nre
        setting.setOnAction(event -> {
            if (me == battle.getPlayer1())
                battle.player2Won();
            else
                battle.player1Won();
            MainMenuController.getInstance().setAsScene();
        });
        graveYard.setOnAction(event -> {
            GraveYardController.getInstance().setAsScene();
        });


    }

    public static void initHeroes(BattleManager battleManager, Pane motherFuckinPane) {
        Hero hero1 = battleManager.getPlayer1().getHero();
        Hero hero2 = battleManager.getPlayer2().getHero();
        hero1.getCell().setCardInCell(hero1);
        hero2.getCell().setCardInCell(hero2);
        battleManager.getPlayer1().addCardToBattlefield(hero1);
        battleManager.getPlayer2().addCardToBattlefield(hero2);
        DisplayableDeployable faceHero1 = new DisplayableDeployable(hero1);
        DisplayableDeployable faceHero2 = new DisplayableDeployable(hero2);
        hero1.setFace(faceHero1);
        hero2.setFace(faceHero2);
        motherFuckinPane.getChildren().addAll(faceHero1, faceHero2);
        faceHero1.updateStats();
        faceHero2.updateStats();


    }

    private boolean isMyTrun() {
        return BattleMenu.getBattleManager().getCurrentPlayer().
                getAccount().getUsername().equals(me.getAccount().getUsername());
    }

    public static BattlePageController getBattlePageController() {
        return battlePageController;
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

    public void refreshTheStatusOfMap(BattleManager battleManager) {
        BattleMenu.getBattleManager().checkTheEndSituation();
        Player player1 = battleManager.getPlayer1();
        Player player2 = battleManager.getPlayer2();
        for (Deployable card : player1.getCardsOnBattleField()) {
            card.getFace().updateStats();
        }
        for (Deployable card : player2.getCardsOnBattleField()) {
            card.getFace().updateStats();
        }

        try {
            health.setText("" + me.getHero().theActualHealth());
            opponentHealth.setText("" + opponent.getHero().theActualHealth());
            opponentHand.setText("Hand: " + opponent.handSize() + " / 6");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("mohem nis :)");
        }
        try {
            if (me == battleManager.getCurrentPlayer()) {
                for (int i = 0; i < battleManager.getCurrentPlayer().getMana(); i++) {
                    manas.get(i).setImage
                            (new javafx.scene.image.Image(new FileInputStream("@assets/ui/icon_mana@2x.png")));
                }
            } else {
                for (ImageView imageView : manas) {
                    imageView.setImage(new Image(new FileInputStream("@assets/ui/icon_mana_inactive.png")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            int usualMana;
            if (battleManager.getTurn() <= 14) {
                usualMana = (battleManager.getTurn() - 1) / 2 + 2;
            } else {
                usualMana = 9;
            }
            if (opponent == battleManager.getCurrentPlayer())
                opponentMana.setText("" + usualMana + player1.getManaChangerInTurn()[battleManager.getTurn()] + " / " + usualMana);
            else {
                opponentMana.setText("0 / 0");
            }
            generalCoolDown.setText("" + me.getHero().getHeroSpell().getCoolDownRemaining());
            opponentGeneralCooldown.setText("" + opponent.getHero().getHeroSpell().getCoolDownRemaining());
            deckSize.setText("Deck: " + me.deckSize() + "/20");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("migam ke mohem nis");
        }
        /*if(battleController.getMe().hand.get(0)!=null)
        {

        }*/
        // next card , cards in hand , all deployedCard in battle with their attack and health should be refreshed too

    }

    public void initTheMapCells() {

    }

}

class ColumnOfHand {
    StackPane stackPane;
    Label manaCost;

    public StackPane getStackPane() {
        return stackPane;
    }

    public Label getManaCost() {
        return manaCost;
    }

    public ColumnOfHand(StackPane stackPane, Label manaCost) {
        this.stackPane = stackPane;
        this.manaCost = manaCost;
    }
}