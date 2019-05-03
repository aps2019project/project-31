package cardDesign;


import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import constants.AttackType;
import constants.CardType;
import model.*;
import org.graalvm.compiler.replacements.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class cardAssembler {

    public static void main(String[] args) {
        YaGson yaGson = new YaGsonBuilder().create();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type in card name:");
        String name = scanner.nextLine();
        System.out.println("Select card type");
        for (Card.CardType cardType : Card.CardType.getAll()) {
            System.out.println((Card.CardType.getAll().indexOf(cardType) + 1) + ". " + cardType);
        }
        Card.CardType cardType = Card.CardType.getAll().get(scanner.nextInt() - 1);
        switch (cardType) {
            case minion:
                Minion minion = makeMinion(scanner, cardType, name);
                String path = System.getProperty("user.dir") + "/Sources/Cards/Minions.txt";
                try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true))) {
                    bufferedWriter.write(yaGson.toJson(minion) + "\n");

                }catch (IOException e){
                    Log.println("Exception!:" + e);

                }
                break;
            case spell:
                Spell spell = makeSpell(scanner, cardType, name);
                String path1 = System.getProperty("user.dir") + "/Sources/Cards/Spells.txt";
                try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path1, true))) {
                    bufferedWriter.write(yaGson.toJson(spell) + "\n");

                }catch (IOException e){
                    Log.println("Exception!:" + e);
                }
                break;
            case hero:
                Hero hero = makeHero(scanner, cardType, name);
                String path2 = System.getProperty("user.dir") + "/Sources/Cards/Heroes.txt";
                try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path2, true))) {
                    bufferedWriter.write(yaGson.toJson(hero) + "\n");

                }catch (IOException e){
                    Log.println("Exception!:" + e);
                }
                break;

        }


    }

    public  static Hero makeHero(Scanner scanner, Card.CardType cardType, String name){
        Minion minion = makeMinion(scanner, Card.CardType.minion, name);
        System.out.println("Enter the ability spell name:");
        String spellname = scanner.nextLine();
        Spell spell = makeSpell(scanner, Card.CardType.spell, spellname);
        System.out.println("Enter ability cooldown:");
        int coolDown = scanner.nextInt();

        return new Hero(minion.getPrice(), minion.getManaCost(), minion.getCardText(),minion.getFunctions(),
                minion.getAccount(), minion.getName(), minion.getId(), Card.CardType.hero, false, false,
                    false, null, minion.getAttackRange(), minion.getHealth(), minion.getAttack(),
                0, minion.getAttackType(), minion.isCombo(), minion.getHealth(), minion.getAttack(),
                minion.getHealth(), new HeroSpell(0,spell.getManaCost(),spell.getCardText(),
                spell.getFunctions(), spell.getAccount(), spell.getName(), spell.getId(), spell.getType(),
                        false,coolDown, 0));
    }

    public static Spell makeSpell(Scanner scanner, Card.CardType cardType, String name){
        System.out.println("Enter mana cost and then price:");
        int mana = scanner.nextInt();
        int price = scanner.nextInt();
        System.out.println("Enter card text");
        scanner.nextLine();
        String cardText = scanner.nextLine();
        System.out.println("Enter card ID");
        int cardID = scanner.nextInt();
        ArrayList<Function> functions = makeFunctionsList(scanner);
        return new Spell(price,mana,cardText,functions,null,name,cardID,cardType,
                false);
    }

    private static ArrayList<Function> makeFunctionsList(Scanner scanner) {
        ArrayList<Function> functions = new ArrayList<>();
        do {
            System.out.println("Make function:");
            functions.add(makeFunction(scanner));
            System.out.println("type end to finish making functions, anything else to make a new one");
            scanner.nextLine();
        } while (!scanner.nextLine().equals("end"));
        return functions;
    }

    public static Minion makeMinion(Scanner scanner, Card.CardType cardType, String name){
        System.out.println("Enter attack and then health and then mana cost and then price");
        int attack = scanner.nextInt();
        int health = scanner.nextInt();
        int manaCost = scanner.nextInt();
        int price = scanner.nextInt();
        System.out.println("1.melee\n 2.ranged\n 3.hybrid");
        AttackType attackType = null;
        int attackRange = 0;
        switch (scanner.nextInt()){
            case 1:
                attackType = AttackType.melee;
                break;
            case 2:
                attackType = AttackType.ranged;
                System.out.println("Enter range:");
                attackRange = scanner.nextInt();
                break;
            case 3:
                attackType = AttackType.hybrid;
                System.out.println("Enter range:");
                attackRange = scanner.nextInt();
        }
        System.out.println("Enter card text");
        scanner.nextLine();
        String cardText = scanner.nextLine();
        System.out.println("Enter Card ID");
        int cardID = scanner.nextInt();
        ArrayList<Function> functions = makeFunctionsList(scanner);
        boolean isCombo = false;
        for (Function function: functions){
            if (function.getFunctionType() == Function.FunctionType.Combo){
                isCombo = true;
                break;
            }
        }
        return new Minion(price, manaCost, cardText, functions,null,
                name,cardID,cardType,false,false,false,
                null, attackRange, health, attack,0,
                attackType,isCombo,health,attack,health);

    }

    public static Function makeFunction(Scanner scanner) {
        Function.FunctionType functionType = Function.FunctionType.OnDeath;
        System.out.println("Select activation type:\n" +
                "1.On Spawn 2.On Attack 3.On Death\n " +
                "4.On Defend 5.Passive 6.Combo 7.Vanilla 8.Spell");
        switch (scanner.nextInt()) {
            case 1:
                functionType = Function.FunctionType.OnSpawn;
                break;
            case 2:
                functionType = Function.FunctionType.OnAttack;
                break;
            case 3:
                functionType = Function.FunctionType.OnDeath;
                break;
            case 4:
                functionType = Function.FunctionType.OnDefend;
                break;
            case 5:
                functionType = Function.FunctionType.Passive;
                break;
            case 6:
                functionType = Function.FunctionType.Combo;
                return new Function(functionType, "", "");
            case 7:
                functionType = Function.FunctionType.Vanilla;
                return new Function(functionType,"","");
            case 8:
                functionType = Function.FunctionType.Spell;
        }
        System.out.println("Select desired function with number");
        for (String function : FunctionStrings.allFunctionStrings()) {
            System.out.println((FunctionStrings.allFunctionStrings().indexOf(function) + 1) +
                    ". " + function);
        }
        StringBuilder functionToAdd = new StringBuilder();
        functionToAdd.append(FunctionStrings.allFunctionStrings().get(scanner.nextInt() - 1));
        boolean isCellBased = false;
        switch (functionToAdd.toString()) {
            case FunctionStrings.APPLY_BUFF:
                getBuffs(scanner, functionToAdd);
                break;
            case FunctionStrings.ACCUMULATING_ATTACKS:
            case FunctionStrings.DEAL_DAMAGE:
            case FunctionStrings.HEAL:
            case FunctionStrings.INCREASE_ATTACK:
                System.out.println("enter amount:");
                functionToAdd.append(scanner.nextInt());
                break;
            case FunctionStrings.POISON_CELL:
            case FunctionStrings.HOLY_CELL:
            case FunctionStrings.SET_ON_FIRE:
                isCellBased = true;
                System.out.println("Enter turns:");
                functionToAdd.append(scanner.nextInt());
                break;
            case FunctionStrings.GIVE_FUNCTION:
                Function function = makeFunction(scanner);
                functionToAdd.append("type:");
                functionToAdd.append(function.getFunctionType());
                functionToAdd.append("function:");
                functionToAdd.append(function.getFunction());
                functionToAdd.append("target:");
                functionToAdd.append(function.getTarget());
                break;

        }
        System.out.println(functionToAdd.toString());
        StringBuilder target = new StringBuilder();
        System.out.println("Select targets:");
        if (isCellBased) {
            System.out.println("1." + TargetStrings.SQUARE);
            scanner.nextInt();
            target.append(TargetStrings.SQUARE);
            System.out.println("Enter length:");
            target.append(scanner.nextInt());

        } else {
            for (String target1 : TargetStrings.allTargets()) {
                System.out.println((TargetStrings.allTargets().indexOf(target1) + 1) +
                        ". " + target1);
            }
            target.append(TargetStrings.allTargets().get(scanner.nextInt() - 1));
            if (target.toString().matches(TargetStrings.MINIONS_WITH_DISTANCE)) {
                System.out.println("Enter distance (Manhattan)");
                target.append(scanner.nextInt());
            }
            if (target.toString().matches(TargetStrings.SQUARE)) {
                System.out.println("Enter length");
                target.append(scanner.nextInt());
            }
        }
        System.out.println(target.toString());
        return new Function(functionType, functionToAdd.toString(), target.toString());
    }

    private static void getBleed(Scanner scanner, StringBuilder functionToAdd) {
        functionToAdd.append(FunctionStrings.BLEED);
        System.out.println("Enter all the damages and then -1:");
        int num = scanner.nextInt();
        while (num!= -1){
            functionToAdd.append(num);
            num = scanner.nextInt();
        }
    }

    private static void getBuffs(Scanner scanner, StringBuilder functionToAdd) {
        System.out.println("Select Buff:");
        for (Buff.BuffType buffType : Buff.getAllBuffs()) {
            System.out.println((Buff.getAllBuffs().indexOf(buffType) + 1) +
                    ". " + buffType);
        }
        Buff.BuffType buff = Buff.getAllBuffs().get(scanner.nextInt() - 1);
        switch (buff) {
            case Weakness:
                System.out.println("1.health\n" + "2.attack");
                switch (scanner.nextInt()) {
                    case 1:
                        System.out.println("enter amount");
                        functionToAdd.append("wkhealth" + scanner.nextInt() + "for");
                        break;
                    case 2:
                        System.out.println("enter amount");
                        functionToAdd.append("wkattack" + scanner.nextInt() + "for");
                }
                break;
            case Power:
                System.out.println("1.health\n" + "2.attack");
                switch (scanner.nextInt()) {
                    case 1:
                        System.out.println("enter amount");
                        functionToAdd.append("pwhealth" + scanner.nextInt() + "for");
                        break;
                    case 2:
                        System.out.println("enter amount");
                        functionToAdd.append("pwattack" + scanner.nextInt() + "for");
                }
                break;
            case Bleed:
                getBleed(scanner, functionToAdd);
                break;
            case Unholy:
                functionToAdd.append("unholy");
                return;
            default:
                functionToAdd.append(buff.toString().toLowerCase());
        }
        System.out.println("enter turns or -1 for CONTINUOUS");
        int turns = scanner.nextInt();
        if (turns == -1) {
            functionToAdd.append("continuous");
        } else {
            functionToAdd.append(turns);
        }
    }
}
