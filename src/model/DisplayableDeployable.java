package model;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class DisplayableDeployable extends StackPane {
    Deployable deployable;
    ImageView idle;
    ImageView run;
    ImageView attack;
    ImageView hit;
    ImageView death;
    ImageView currentStance;
    Label health;
    Label atttack;
    VBox stats;

    public DisplayableDeployable(Deployable deployable) {
        String imagePath;
        this.deployable = deployable;
        if (deployable instanceof Minion) {
            imagePath = getClass().getResource("/gifs/Minion/" + deployable.getName()).toExternalForm();
        }else {
            imagePath = getClass().getResource("/gifs/Hero/" + deployable.getName()).toExternalForm();
        }
        idle = new ImageView(new Image(imagePath + "/idle.gif"));
        idle.setScaleY(1.5);
        idle.setScaleX(1.5);

        attack = new ImageView(new Image(imagePath + "/attack.gif"));
        attack.setScaleY(1.5);
        attack.setScaleX(1.5);

        death = new ImageView(new Image(imagePath + "/death.gif"));
        death.setScaleY(1.5);
        death.setScaleX(1.5);

        hit = new ImageView(new Image(imagePath + "/hit.gif"));
        hit.setScaleY(1.5);
        hit.setScaleX(1.5);

        run = new ImageView(new Image(imagePath + "/run.gif"));
        run.setScaleY(1.5);
        run.setScaleX(1.5);


        this.getChildren().addAll(idle);

    }

}
