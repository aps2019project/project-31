package view;

import controller.BattleMenu;
import model.BattleManager;
import model.Deployable;
import model.Map;
import model.Player;
import model.TargetStrings;

import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {
    static Scanner scanner = new Scanner(System.in);

    public static void handleSelectCardOrSelectComboCards(Player player) {
        String input = Input.scanner.nextLine();
        Pattern pattern = Pattern.compile("attack combo (\\d+)\\s+((\\d\\s*)+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            int opponentCardId = Integer.parseInt(matcher.group(1));
            String[] strNumbers = matcher.group(2).split("\\s");
            BattleMenu.prepareComboAttack(strNumbers,opponentCardId);
        }
        if (input.matches("select \\d+"))
            player.selectACard(Integer.parseInt(input.replace("select", "").trim()));
    }

    public static void moveAttackPlayCard() {
        String input = Input.scanner.nextLine();
        if (input.matches("attack\\s+\\d+")) {
            BattleMenu.attack(Integer.parseInt(input.replace("attack", "").trim()));
        }
        Pattern pattern = Pattern.compile("move to\\((\\d),(\\d)\\)\\s*");
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            BattleMenu.move( Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2)));
        }
        pattern = Pattern.compile("insert (\\d+) in \\((\\d),(\\d)\\)\\s*");
        matcher = pattern.matcher(input);
        if(matcher.matches()){
            BattleMenu.insert(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)));
        }

    }
}
