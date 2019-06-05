package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import model.Card;
import model.DisplayableCard;
import model.Initializer;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ShopController implements Initializable {
    private static Scene scene;
    private static ShopController shop;
    public ListView<VBox> heroesList;
    public ListView<VBox> minionsList;
    public ListView<VBox> spellsList;
    public ListView<VBox> usableItemsList;

    public static ShopController getInstance() {
        if (shop == null) {
            shop = new ShopController();
        }
        return shop;
    }

    public void setAsScene() {
        if (scene == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Shop.fxml"));
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                Double screenWidth = screen.getWidth();
                scene = new Scene(root, screenWidth * 2 / 3, screenWidth * 4 / 9);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolean first = true;
        DisplayableCard displayableCard = null;
        VBox vBox = new VBox();
        for(Card card : Shop.getAllMinions()){
            displayableCard = new DisplayableCard(card, "");
            if(first){
                if(vBox.getChildren().size() != 0){
                    minionsList.getItems().add(vBox);
                    vBox = new VBox();
                }
                vBox = new VBox(displayableCard);
                first = false;
            }
            else{
                vBox.getChildren().add(displayableCard);
                first = true;
            }
        }
        if (vBox.getChildren().size() != 0){
            minionsList.getItems().add(vBox);
            vBox = new VBox();
        }
    }
}