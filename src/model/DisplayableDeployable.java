package model;

import constants.CardType;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class DisplayableDeployable extends StackPane {
    public Deployable getDeployable() {
        return deployable;
    }

    Deployable deployable;
    boolean isMoving = false;
    ImageView idle;
    ImageView run;
    ImageView attack;
    ImageView hit;
    ImageView death;
    ImageView currentStance;
    Label healthLabel;
    Label attackLabel;
    VBox stats;
    ImageView attackIcon;
    ImageView healthIcon;
    double SCALE = 1.2;

    public DisplayableDeployable(Deployable deployable) {
        this.setAlignment(Pos.CENTER);
        setOnMouseClicked(mouseEvent -> {
            BattleManager.currentPlayer.setSelectedCard(deployable);
        });
        String imagePath;
        this.deployable = deployable;
        if (deployable.getType() == CardType.minion) {
            imagePath = getClass().getResource("/gifs/Minion/" + deployable.getName()).toExternalForm();
        } else {
            imagePath = getClass().getResource("/gifs/Hero/" + deployable.getName()).toExternalForm();

        }

        idle = new ImageView(new Image(imagePath + "/idle.gif"));
        idle.setScaleY(SCALE);
        idle.setScaleX(SCALE);

        attack = new ImageView(new Image(imagePath + "/attack.gif"));
        attack.setScaleY(SCALE);
        attack.setScaleX(SCALE);

        death = new ImageView(new Image(imagePath + "/death.gif"));
        death.setScaleY(SCALE);
        death.setScaleX(SCALE);

        hit = new ImageView(new Image(imagePath + "/hit.gif"));
        hit.setScaleY(SCALE);
        hit.setScaleX(SCALE);

        run = new ImageView(new Image(imagePath + "/run.gif"));
        run.setScaleY(SCALE);
        run.setScaleX(SCALE);

        attackLabel = new Label(deployable.getCurrentAttack() + "");
        healthLabel = new Label(deployable.getCurrentHealth() + "");

        attackLabel.setFont(Font.font(15));
        healthLabel.setFont(Font.font(15));
        attackLabel.setTextFill(Color.WHITE);
        healthLabel.setTextFill(Color.WHITE);

        attackLabel.setTranslateX(-40);
        attackLabel.setTranslateY(50);
        healthLabel.setTranslateY(50);
        healthLabel.setTranslateX(25);

        attackIcon = new ImageView(new Image(getClass().
                getResource("/assets/ui/icon_atk.png").toExternalForm()));
        healthIcon = new ImageView(new Image(getClass().
                getResource("/assets/ui/icon_hp.png").toExternalForm()));
        attackIcon.setTranslateY(50);
        attackIcon.setTranslateX(-40);
        attackIcon.setScaleX(0.5);
        attackIcon.setScaleY(0.5);
        healthIcon.setTranslateY(50);
        healthIcon.setTranslateX(25);
        healthIcon.setScaleX(0.5);
        healthIcon.setScaleY(0.5);

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
        action.setScaleY(SCALE);
        action.setScaleX(SCALE);
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

    public void moveToCurrentCell() {
        double amountX = (deployable.getCell().calculateCenter()[0] - getTranslateX()) / 15;
        double amountY = (deployable.getCell().calculateCenter()[1] - getTranslateY()) / 15;
        if (deployable.getName().equals("Kaveh")) {
            amountX -= 30/15;
            amountY -= 40/15;
        }
        if (amountX < 0.5 && amountY < 0.5) return;
        if (isMoving) return;
        run();
        isMoving = true;
        double finalAmountX = amountX;
        double finalAmountY = amountY;
        new AnimationTimer() {
            int counter = 0;
            long now = 0;

            @Override
            public void handle(long l) {

                if (now == 0) now = l;
                if (l - now > Math.pow(10, 8)) {
                    now = l;
                    counter++;

                    setTranslateX(getTranslateX() + finalAmountX);
                    setTranslateY(getTranslateY() + finalAmountY);
                    if (counter == 15) {
                        setIdle();
                        isMoving = false;
                        stop();
                    }
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
        for (Buff buff : deployable.getBuffs()) {
            Label label = new Label(buff.buffType + "");
            label.setFont(Font.font(12));
            label.setTextFill(Color.CYAN);
        }
        moveToCurrentCell();
        moveToCurrentCell();
    }

}
