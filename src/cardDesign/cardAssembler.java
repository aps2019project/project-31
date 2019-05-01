package cardDesign;


import model.Buff;
import model.FunctionStrings;

import java.util.Collections;
import java.util.Scanner;

public class cardAssembler extends FunctionStrings{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select desired function with number");
        for (String function: FunctionStrings.allFunctionStrings()){
            System.out.println((FunctionStrings.allFunctionStrings().indexOf(function) + 1) +
                    ". " + function);
        }
        StringBuilder functionToAdd = new StringBuilder();
        System.out.println(Buff.BuffType.Disarm.toString());
        functionToAdd.append(FunctionStrings.allFunctionStrings().get(scanner.nextInt() - 1));
        switch (functionToAdd.toString()){
            case FunctionStrings.APPLY_BUFF:
                getBuffs(scanner, functionToAdd);
        }

    }

    private static void getBuffs(Scanner scanner, StringBuilder functionToAdd) {
        System.out.println("Select Buff:");
        for (Buff.BuffType buffType: Buff.getAllBuffs()){
            System.out.println((Buff.getAllBuffs().indexOf(buffType) + 1) +
                    ". " + buffType);
        }
        Buff.BuffType buff = Buff.getAllBuffs().get(scanner.nextInt() - 1);
        switch (buff){
            case Weakness:
                System.out.println("1.health\n" + "2.attack");
                switch (scanner.nextInt()){
                    case 1:
                        System.out.println("enter amount and turns");
                        functionToAdd.append("wkhealth" + scanner.nextInt() + "for");
                        break;
                    case 2:
                        System.out.println("enter amount and turns");
                        functionToAdd.append("wkattack" + scanner.nextInt() + "for");
                }
                break;
            case Power:
                System.out.println("1.health\n" + "2.attack");
                switch (scanner.nextInt()){
                    case 1:
                        System.out.println("enter amount and turns");
                        functionToAdd.append("pwhealth" + scanner.nextInt() + "for");
                        break;
                    case 2:
                        System.out.println("enter amount and turns");
                        functionToAdd.append("pwattack" + scanner.nextInt() + "for");
                }
                break;
            default:
                System.out.println("enter turns or -1 for CONTINUOUS");
                int turns = scanner.nextInt();
                if (turns == -1){
                    functionToAdd.append("continuous");
                }else {
                    functionToAdd.append(turns);
                }

        }
    }
}
