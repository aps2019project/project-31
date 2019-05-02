package cardDesign;


import model.Buff;
import model.FunctionStrings;
import model.TargetStrings;
import model.Function;

import javax.sound.midi.Soundbank;
import java.util.Collections;
import java.util.Scanner;

public class cardAssembler {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select card type");
        Function function = makeFunction(scanner);


    }

    public static Function makeFunction(Scanner scanner){
        Function.FunctionType functionType = Function.FunctionType.OnDeath;
        System.out.println("Select activation type:\n" +
                "1.On Spawn 2.On Attack 3.On Death\n " +
                "4.On Defend 5.Passive 6.Combo");
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
                return new Function(functionType,"","");
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
                System.out.println("enter amount:");
                functionToAdd.append(scanner.nextInt());
                break;
            case FunctionStrings.POISON_CELL:
            case FunctionStrings.HOLY_CELL:
            case FunctionStrings.SET_ON_FIRE:
                isCellBased = true;

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
        }
        System.out.println(target.toString());
        return new Function(functionType, functionToAdd.toString(), target.toString());
    }

    private static void getBleed(Scanner scanner, StringBuilder functionToAdd) {
        functionToAdd.append(FunctionStrings.BLEED);
        System.out.println("Enter first and second damage");
        functionToAdd.append(scanner.nextInt());
        functionToAdd.append(",");
        functionToAdd.append(scanner.nextInt());
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
