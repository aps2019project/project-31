package model;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class DisplayableDeployable extends StackPane {
    Deployable deployable;
    ImageView idle;
    ImageView run;
    ImageView attack;
    ImageView hit;
    ImageView death;
    ImageView currentStance;
    Label healthLabel;
    Label attackLabel;
    VBox stats;

    public DisplayableDeployable(Deployable deployable) {
        this.setAlignment(Pos.CENTER);
        String imagePath;
        this.deployable = deployable;
        if (deployable instanceof Minion) {
            imagePath = getClass().getResource("/gifs/Minion/" + deployable.getName()).toExternalForm();
        } else {
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

        attackLabel = new Label(deployable.getCurrentAttack() + "");
        healthLabel = new Label(deployable.getCurrentHealth() + "");

        attackLabel.setFont(Font.font(17));
        healthLabel.setFont(Font.font(17));
        attackLabel.setTextFill(Color.WHITE);
        healthLabel.setTextFill(Color.WHITE);

        attackLabel.setTranslateX(-40);
        attackLabel.setTranslateY(50);
        healthLabel.setTranslateY(50);
        healthLabel.setTranslateX(40);

        ImageView attackIcon = new ImageView(new Image(getClass().
                getResource("/assets/ui/icon_atk.png").toExternalForm()));
        ImageView healthIcon = new ImageView(new Image(getClass().
                getResource("/assets/ui/icon_hp.png").toExternalForm()));
        attackIcon.setTranslateY(50);
        attackIcon.setTranslateX(-40);
        attackIcon.setScaleX(0.7);
        attackIcon.setScaleY(0.7);
        healthIcon.setTranslateY(50);
        healthIcon.setTranslateX(40);
        healthIcon.setScaleX(0.7);
        healthIcon.setScaleY(0.7);

        currentStance = idle;
        this.getChildren().addAll(idle, healthIcon, attackIcon, healthLabel, attackLabel);

    }

    public void run() {
        this.getChildren().remove(currentStance);
        currentStance = run;
        getChildren().add(currentStance);
    }

    public void setIdle() {
        getChildren().remove(currentStance);
        currentStance = idle;
        getChildren().add(currentStance);
    }

    public void action(ImageView action, double duration) {
        getChildren().remove(currentStance);
        if (action.equals(death)) {
            currentStance = null;
        }
        action = new ImageView(new Image(action.getImage().getUrl()));
        action.setScaleY(1.5);
        action.setScaleX(1.5);
        getChildren().add(action);
        final ImageView temp = action;

        new AnimationTimer() {
            long time = 0;

            @Override
            public void handle(long l) {
                if (time == 0) time = l;
                if (l - time > duration * Math.pow(10, 9)) {
                    getChildren().remove(temp);
                    if (currentStance != null)
                        getChildren().add(currentStance);
                    updateStats();
                    stop();
                }
            }
        }.start();

    }

    public void attack() {
        action(attack, 0.8);
    }

    public void getHit() {
        action(hit, 0.15);
    }

    public void die() {
        action(death, 0.55);
    }


    public void updateStats() {
        attackLabel.setText(deployable.getCurrentAttack() + "");
        if (deployable.getCurrentHealth() < deployable.maxHealth) {
            healthLabel.setTextFill(Color.RED);
        } else if (deployable.getCurrentHealth() > deployable.maxHealth) {
            healthLabel.setTextFill(Color.GREEN);
        } else {
            healthLabel.setTextFill(Color.WHITE);
        }
        healthLabel.setText(deployable.getCurrentHealth() + "");
        for (Buff buff: deployable.getBuffs()){
            Label label = new Label(buff.buffType + "");
            label.setFont(Font.font(12));
            label.setTextFill(Color.CYAN);
        }
    }

}
