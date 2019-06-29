package controller;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import constants.AttackType;
import constants.CardType;
import constants.FunctionType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.*;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CardMakerController implements Initializable {
    @FXML
    private VBox displayVBox;
    @FXML
    private Label messageLabel;
    @FXML
    private ScrollPane selectionPane;
    @FXML
    private VBox selectionVBox;
    @FXML
    private Button minionButton;
    @FXML
    private Button heroButton;
    @FXML
    private Button spellButton;
    @FXML
    private Button backButton;

    @FXML
    private VBox centerVBox;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private StackPane mainContainer;
    @FXML
    private Button previewButton;
    @FXML
    private Button createButton;


    private static Scene cardMakerScene;
    private static CardMakerController cardMaker;

    //current card stats
    private boolean isCombo = false;
    private CardType currentCardType = null;
    private String currentCardText = "";
    private String currentCardName = "";
    private Integer currentCardHealth = null;
    private Integer currentCardAttack = null;
    private int currentCardPrice;
    private Integer currentManaCost = null;
    private FunctionType currentFunctionType = null;
    private String currentFunctionString = "";
    private String currentTargetString = "";
    private Buff.BuffType currentBuffType = null;
    private ArrayList<Function> currentCardFunctions = new ArrayList<>();
    private AttackType currentAttackType = null;
    private Card currentCard;
    private int currentCardRange = 0;
    private DisplayableCard displayableCard;


    public static CardMakerController getInstance() {
        if (cardMaker == null) {
            cardMaker = new CardMakerController();
        }
        return cardMaker;
    }

    public void setAsScene() {
        if (cardMakerScene == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/CardMaker.fxml"));

                cardMakerScene = new Scene(root, Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 3 / 5,
                        Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 2 / 5);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Initializer.setCurrentScene(cardMakerScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cardMaker = this;
        double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double scaleX = screenWidth / mainContainer.getPrefWidth() * 3 / 5;
        mainContainer.setScaleX(scaleX);
        mainContainer.setScaleY(scaleX);
        centerVBox.setScaleY(0.87);
        selectionVBox.setSpacing(10);

        createButton.setOnAction(actionEvent -> {
            if (currentCard == null)
                return;
            YaGson yaGson = new YaGsonBuilder().create();
            switch (currentCardType) {
                case minion:
                    String path = System.getProperty("user.dir") + "/Sources/Cards/Minions.txt";
                    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true))) {
                        bufferedWriter.write(yaGson.toJson(currentCard) + "\n");

                    } catch (IOException e) {
                        System.err.println("Exception!:" + e);

                    }
                    break;
                case spell:
                    String path1 = System.getProperty("user.dir") + "/Sources/Cards/Spells.txt";
                    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path1, true))) {
                        bufferedWriter.write(yaGson.toJson(currentCard) + "\n");

                    } catch (IOException e) {
                        System.err.println("Exception!:" + e);
                    }
                    break;
            }
            currentCard = null;
            displayVBox.getChildren().removeAll(displayableCard);

        });

        previewButton.setOnAction(actionEvent -> {
            switch (currentCardType) {
                case minion:
                    currentCard = new Minion(currentCardPrice, currentManaCost, currentCardText, currentCardFunctions, null
                            , currentCardName, 999, currentCardType, false,
                            false, false, null, currentCardRange, currentCardHealth,
                            currentCardAttack, 0, currentAttackType, isCombo, currentCardHealth,
                            currentCardAttack, currentCardHealth);
                    break;
                case spell:
                    currentCard = new Spell(currentCardPrice, currentManaCost, currentCardText, currentCardFunctions, null,
                            currentCardName, 999, currentCardType, false);
            }
            displayableCard = new DisplayableCard(currentCard, "");
            displayVBox.getChildren().add(displayableCard);
            System.out.println(currentFunctionString);
            System.out.println(currentTargetString);
        });

        spellButton.setOnAction(actionEvent -> makeSpell());
        minionButton.setOnAction(actionEvent -> makeMinion());
        backButton.setOnAction(event -> MainMenuController.getInstance().setAsScene());
    }

    private void makeSpell() {
        currentCardType = CardType.spell;
        messageLabel.setText("Enter card properties!");

        TextField nameField = makeTextField("enter name price cost");
        nameField.setOnAction(actionEvent -> {
            String[] things = nameField.getText().split(" ");
            currentCardName = "custom" + things[0];
            currentCardPrice = Integer.parseInt(things[1]);
            currentManaCost = Integer.parseInt(things[2]);
            getCardText(nameField);
        });
        selectionVBox.getChildren().add(nameField);
    }

    private void makeMinion() {
        currentCardType = CardType.minion;
        messageLabel.setText("Enter card properties!");

        TextField nameField = makeTextField("enter name health attack price cost");
        nameField.setOnAction(actionEvent -> {
            String[] things = nameField.getText().split(" ");
            currentCardName = "custom" + things[0];
            currentCardHealth = Integer.parseInt(things[1]);
            currentCardAttack = Integer.parseInt(things[2]);
            currentCardPrice = Integer.parseInt(things[3]);
            currentManaCost = Integer.parseInt(things[4]);
            nameField.setPromptText("enter hybrid/melee/ranged");
            nameField.clear();
            nameField.setOnAction(actionEvent1 -> {
                switch (nameField.getText()) {
                    case "melee":
                        currentAttackType = AttackType.melee;
                        break;
                    case "ranged":
                        currentAttackType = AttackType.ranged;
                        nameField.clear();
                        nameField.setPromptText("enter range");
                        nameField.setOnAction(actionEvent2 -> {
                            currentCardRange = Integer.parseInt(nameField.getText());
                            getCardText(nameField);

                        });
                        return;
                    case "hybrid":
                        currentAttackType = AttackType.hybrid;
                        nameField.clear();
                        nameField.setPromptText("enter range");
                        nameField.setOnAction(actionEvent2 -> {
                            currentCardRange = Integer.parseInt(nameField.getText());
                            getCardText(nameField);
                        });
                        return;
                }
                getCardText(nameField);
            });

        });

        /*TextField healthText = makeTextField("health");
        nameField.setOnAction(actionEvent -> {
            if (healthText.getText().matches("\\d+")) {
                currentCardHealth = Integer.parseInt(healthText.getText());
                selectionVBox.getChildren().remove(healthText);
            }
        });

        TextField attackText = makeTextField("attack");
        nameField.setOnAction(actionEvent -> {
            if (attackText.getText().matches("\\d+")) {
                currentCardAttack = Integer.parseInt(attackText.getText());
                selectionVBox.getChildren().remove(attackText);
            }
        });*/


        selectionVBox.getChildren().addAll(nameField/*, attackText, healthText*/);

    }

    private void getCardText(TextField nameField) {
        nameField.clear();
        nameField.setPromptText("enter card text");
        nameField.setOnAction(actionEvent2 -> {
            currentCardText = nameField.getText();
            selectionVBox.getChildren().removeAll(nameField);
            makeFunction();
        });
    }

    private void makeFunction() {
        int i = 0;
        for (FunctionType functionType : FunctionType.values()) {
            i++;
            selectionVBox.getChildren().add(LoginPageController.getInstance()
                    .makeMainLabel(i + ". " + functionType.toString(), 22));
        }
        TextField numTextField = makeTextField("Enter function type number!");
        centerVBox.getChildren().add(numTextField);
        centerVBox.setScaleY(0.8);
        numTextField.setOnAction(actionEvent -> {
            FunctionType functionType = FunctionType.OnDeath;
            switch (Integer.parseInt(numTextField.getText())) {
                case 1:
                    functionType = FunctionType.OnSpawn;
                    break;
                case 2:
                    functionType = FunctionType.OnAttack;
                    break;
                case 3:
                    functionType = FunctionType.OnDeath;
                    break;
                case 4:
                    functionType = FunctionType.OnDefend;
                    break;
                case 5:
                    functionType = FunctionType.Passive;
                    break;
                case 6:
                    functionType = FunctionType.Combo;
                    currentCardFunctions.add(new Function(functionType, "", ""));
                    isCombo = true;
                    return;
                case 7:
                    functionType = FunctionType.Vanilla;
                    currentCardFunctions.add(new Function(functionType, "", ""));
                    return;
                case 8:
                    functionType = FunctionType.Spell;
                    break;
                case 9:
                    functionType = FunctionType.GameStart;
                    break;
            }
            currentFunctionType = functionType;
            selectionVBox.getChildren().removeAll(selectionVBox.getChildren());
            for (String function : FunctionStrings.allFunctionStrings()) {
                selectionVBox.getChildren().add(LoginPageController.getInstance()
                        .makeMainLabel((FunctionStrings.allFunctionStrings().indexOf(function) + 1) +
                                ". " + function, 22));
            }
            numTextField.clear();
            numTextField.setPromptText("enter function number");
            numTextField.setOnAction(actionEvent1 -> {

                currentFunctionString += FunctionStrings.allFunctionStrings().
                        get(Integer.parseInt(numTextField.getText()) - 1);
                System.out.println(currentFunctionString);
                boolean isCellBased = false;
                switch (currentFunctionString) {
                    case FunctionStrings.APPLY_BUFF:
                        numTextField.clear();
                        numTextField.setPromptText("enter buff number");
                        selectionVBox.getChildren().removeAll(selectionVBox.getChildren());
                        for (Buff.BuffType buffType : Buff.getAllBuffs()) {
                            selectionVBox.getChildren().add(LoginPageController.getInstance()
                                    .makeMainLabel((Buff.getAllBuffs().indexOf(buffType) + 1) +
                                            ". " + buffType, 22));
                        }
                        selectionVBox.getChildren().add(LoginPageController.getInstance()
                                .makeMainLabel("Or any other number to add several holy buffs"
                                        , 22));
                        numTextField.setOnAction(actionEvent2 -> {
                            int num = Integer.parseInt(numTextField.getText());
                            Buff.BuffType buff;
                            if (num <= 8) {
                                buff = Buff.getAllBuffs().get(num - 1);
                            } else {
                                buff = Buff.BuffType.Holy;
                                numTextField.clear();
                                numTextField.setPromptText("enter 'amount turns' and -1 for continuous");
                                numTextField.setOnAction(actionEvent3 -> {
                                    String[] nums = numTextField.getText().split(" ");
                                    currentFunctionString += nums[0] + buff;
                                    if (nums[1].equals("-1")) {
                                        currentFunctionString += "continuous";
                                    } else currentFunctionString += nums[1];
                                    centerVBox.getChildren().removeAll(numTextField);
                                    makeTargetString(false);
                                });
                                return;
                            }
                            switch (buff) {
                                case Weakness:
                                    numTextField.clear();
                                    numTextField.setPromptText("enter 'health/attack amount turns'" +
                                            " and -1 for continuous");
                                    numTextField.setOnAction(actionEvent3 -> {
                                        String[] things = numTextField.getText().split(" ");
                                        currentFunctionString += "wk" + things[0] + things[1] + "for";
                                        if (things[2].equals("-1")) {
                                            currentFunctionString += "continuous";
                                        } else currentFunctionString += things[2];
                                        centerVBox.getChildren().remove(numTextField);
                                        makeTargetString(false);
                                    });
                                    break;
                                case Power:
                                    numTextField.clear();
                                    numTextField.setPromptText("enter 'health/attack amount turns'" +
                                            "  and -1 for continuous");
                                    numTextField.setOnAction(actionEvent3 -> {
                                        String[] things = numTextField.getText().split(" ");
                                        currentFunctionString += "pw" + things[0] + things[1] + "for";
                                        if (things[2].equals("-1")) {
                                            currentFunctionString += "continuous";
                                        } else currentFunctionString += things[2];
                                        centerVBox.getChildren().removeAll(numTextField);
                                        makeTargetString(false);
                                    });
                                    break;
                                case Bleed:
                                    numTextField.clear();
                                    numTextField.setPromptText("enter damages split by spaces");
                                    numTextField.setOnAction(actionEvent3 -> {
                                        currentFunctionString += FunctionStrings.BLEED + numTextField.getText().trim();
                                        centerVBox.getChildren().removeAll(numTextField);
                                        makeTargetString(false);
                                    });
                                    return;
                                case Unholy:
                                    currentFunctionString += "unholy";
                                    centerVBox.getChildren().removeAll(numTextField);
                                    makeTargetString(false);
                                    return;
                                default:
                                    currentFunctionString += buff.toString().toLowerCase();
                                    centerVBox.getChildren().removeAll(numTextField);
                                    makeTargetString(false);
                            }
                        });
                        break;
                    case FunctionStrings.ACCUMULATING_ATTACKS:
                    case FunctionStrings.DEAL_DAMAGE:
                    case FunctionStrings.HEAL:
                    case FunctionStrings.INCREASE_ATTACK:
                    case FunctionStrings.STUN:
                        numTextField.clear();
                        numTextField.setPromptText("enter amount/turns");
                        numTextField.setOnAction(actionEvent2 -> {
                            currentFunctionString += numTextField.getText().trim();
                            centerVBox.getChildren().removeAll(numTextField);
                            makeTargetString(false);
                        });
                        break;
                    case FunctionStrings.POISON_CELL:
                    case FunctionStrings.HOLY_CELL:
                    case FunctionStrings.SET_ON_FIRE:
                        numTextField.clear();
                        numTextField.setPromptText("enter turns");
                        numTextField.setOnAction(actionEvent2 -> {
                            currentFunctionString += numTextField.getText().trim();
                            makeTargetString(true);
                            centerVBox.getChildren().removeAll(numTextField);
                        });
                        break;
                    default:
                        makeTargetString(false);
                    /*case FunctionStrings.GIVE_FUNCTION:
                        Function function = makeFunction(scanner);
                        functionToAdd.append("type:");
                        functionToAdd.append(function.getFunctionType());
                        functionToAdd.append("function:");
                        functionToAdd.append(function.getFunction());
                        functionToAdd.append("target:");
                        functionToAdd.append(function.getTarget());
                        break;*/

                }


            });

        });
    }

    private void makeTargetString(boolean isCellBased) {
        selectionVBox.getChildren().removeAll(selectionVBox.getChildren());
        messageLabel.setText("Choose your targets!");
        TextField inputField = makeTextField("enter target number");
        centerVBox.getChildren().add(inputField);
        if (isCellBased) {
            inputField.setPromptText("enter target square length");
            inputField.setOnAction(actionEvent -> {
                currentTargetString += TargetStrings.SQUARE + inputField.getText();
                centerVBox.getChildren().removeAll(inputField);
            });

        } else {
            for (String target1 : TargetStrings.allTargets()) {
                selectionVBox.getChildren().add(LoginPageController.getInstance()
                        .makeMainLabel((TargetStrings.allTargets().indexOf(target1) + 1) +
                                ". " + target1, 22));
            }
            inputField.setOnAction(actionEvent -> {
                currentTargetString += TargetStrings.allTargets().get(Integer.parseInt(inputField.getText().trim()) - 1);
                if (currentTargetString.matches(TargetStrings.MINIONS_WITH_DISTANCE)) {
                    inputField.clear();
                    inputField.setPromptText("Enter distance (Manhattan)");
                    inputField.setOnAction(actionEvent1 -> {
                        currentTargetString += inputField.getText();
                        addFuntion(inputField);
                    });
                    return;
                }
                if (currentTargetString.matches(TargetStrings.SQUARE)) {
                    inputField.clear();
                    inputField.setPromptText("Enter length");
                    inputField.setOnAction(actionEvent1 -> {
                        currentTargetString += inputField.getText();
                        addFuntion(inputField);
                    });
                    return;
                }
                addFuntion(inputField);
            });
        }


    }

    private void addFuntion(TextField inputField) {
        currentCardFunctions.add(new Function(currentFunctionType, currentFunctionString, currentTargetString));
        centerVBox.getChildren().removeAll(inputField);
        selectionVBox.getChildren().removeAll(selectionVBox.getChildren());
    }

    private TextField makeTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.getStylesheets().add(getClass().getResource("/style/inputBox.css").toExternalForm());
        textField.setPrefWidth(20);
        textField.setPrefHeight(30);
        return textField;
    }
}
