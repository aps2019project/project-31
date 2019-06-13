package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import model.Account;
import model.Initializer;
import model.Player;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Polygon[][] polygons = new Polygon[5][9];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                polygons[i][j] = new Polygon();
                polygons[i][j].getPoints().addAll(0.0, 0.0, 600.0, 0.0, 0.0, 600.0, 600.0, 600.0);
                polygons[i][j].setFill(Color.RED);
            }
        }
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
