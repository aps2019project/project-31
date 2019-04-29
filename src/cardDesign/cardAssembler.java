package cardDesign;


import model.FunctionStrings;

import java.util.Scanner;

public class cardAssembler {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select desired function with number");
        for (String function: FunctionStrings.allFunctionStrings()){
            System.out.println((FunctionStrings.allFunctionStrings().indexOf(function) + 1) +
                    ". " + function);
        }
        StringBuilder functionToAdd = new StringBuilder();
        functionToAdd.append(FunctionStrings.allFunctionStrings().get(scanner.nextInt() - 1));

    }
}
