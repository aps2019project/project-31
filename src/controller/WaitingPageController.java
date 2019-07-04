package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class WaitingPageController implements Initializable {

    public Button cancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancel.setOnMouseClicked(mouseEvent -> {



        });
    }
}
