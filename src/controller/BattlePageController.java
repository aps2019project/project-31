package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polyline;
import model.*;

import java.awt.*;
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
    public Button setting;
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
    private ArrayList<ImageView> manas;


    public void setAsScene() {
        if (scene == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/BattlePage.fxml"));
                Double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                scene = new Scene(root, screenWidth * 2 / 3, screenWidth * 4 / 9);

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

    public void initTheMapCells() {
        System.err.println("Initializing maps ...");
        Map.createTheMap();
        Map.getMap()[1][1] = new Cell(1, 1, null, place11);
        Map.getMap()[1][2] = new Cell(1, 2, null, place12);
        Map.getMap()[1][3] = new Cell(1, 3, null, place13);
        Map.getMap()[1][4] = new Cell(1, 4, null, place14);
        Map.getMap()[1][5] = new Cell(1, 5, null, place15);
        Map.getMap()[1][6] = new Cell(1, 6, null, place16);
        Map.getMap()[1][7] = new Cell(1, 7, null, place17);
        Map.getMap()[1][8] = new Cell(1, 8, null, place18);
        Map.getMap()[1][9] = new Cell(1, 9, null, place19);
        Map.getMap()[2][1] = new Cell(2, 1, null, place21);
        Map.getMap()[2][2] = new Cell(2, 2, null, place22);
        Map.getMap()[2][3] = new Cell(2, 3, null, place23);
        Map.getMap()[2][4] = new Cell(2, 4, null, place24);
        Map.getMap()[2][5] = new Cell(2, 5, null, place25);
        Map.getMap()[2][6] = new Cell(2, 6, null, place26);
        Map.getMap()[2][7] = new Cell(2, 7, null, place27);
        Map.getMap()[2][8] = new Cell(2, 8, null, place28);
        Map.getMap()[2][9] = new Cell(2, 9, null, place29);
        Map.getMap()[3][1] = new Cell(3, 1, null, place31);
        Map.getMap()[3][2] = new Cell(3, 2, null, place32);
        Map.getMap()[3][3] = new Cell(3, 3, null, place33);
        Map.getMap()[3][4] = new Cell(3, 4, null, place34);
        Map.getMap()[3][5] = new Cell(3, 5, null, place35);
        Map.getMap()[3][6] = new Cell(3, 6, null, place36);
        Map.getMap()[3][7] = new Cell(3, 7, null, place37);
        Map.getMap()[3][8] = new Cell(3, 8, null, place38);
        Map.getMap()[3][9] = new Cell(3, 9, null, place39);
        Map.getMap()[4][1] = new Cell(4, 1, null, place41);
        Map.getMap()[4][2] = new Cell(4, 2, null, place42);
        Map.getMap()[4][3] = new Cell(4, 3, null, place43);
        Map.getMap()[4][4] = new Cell(4, 4, null, place44);
        Map.getMap()[4][5] = new Cell(4, 5, null, place45);
        Map.getMap()[4][6] = new Cell(4, 6, null, place46);
        Map.getMap()[4][7] = new Cell(4, 7, null, place47);
        Map.getMap()[4][8] = new Cell(4, 8, null, place48);
        Map.getMap()[4][9] = new Cell(4, 9, null, place49);
        Map.getMap()[5][1] = new Cell(5, 1, null, place51);
        Map.getMap()[5][2] = new Cell(5, 2, null, place52);
        Map.getMap()[5][3] = new Cell(5, 3, null, place53);
        Map.getMap()[5][4] = new Cell(5, 4, null, place54);
        Map.getMap()[5][5] = new Cell(5, 5, null, place55);
        Map.getMap()[5][6] = new Cell(5, 6, null, place56);
        Map.getMap()[5][7] = new Cell(5, 7, null, place57);
        Map.getMap()[5][8] = new Cell(5, 8, null, place58);
        Map.getMap()[5][9] = new Cell(5, 9, null, place59);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTheMapCells();


        /*manas.add(mana1);
        manas.add(mana2);
        manas.add(mana3);
        manas.add(mana4);
        manas.add(mana5);
        manas.add(mana6);
        manas.add(mana7);
        manas.add(mana8);
        manas.add(mana9);
        initPlayers();
        username.setText(me.getAccount().getUsername());
        opponentUsername.setText(opponent.getAccount().getUsername());
        generalSpellManaCost.setText("" + me.getHero().getHeroSpell().getManaCost());
        opponentGeneralSpellManaCost.setText("" + opponent.getHero().getHeroSpell().getManaCost());
        generalCoolDown.setText("" + me.getHero().getHeroSpell().getManaCost());
        opponentGeneralCooldown.setText("" + opponent.getHero().getHeroSpell().getManaCost());*/

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


}
