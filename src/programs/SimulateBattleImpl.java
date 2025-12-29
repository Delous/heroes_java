import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        List<Unit> playerUnits = playerArmy.getUnits();
        playerArmy.getUnits().sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

        computerArmy.getUnits().sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

        while (!playerArmy.getUnits().isEmpty() && !computerArmy.getUnits().isEmpty()) {
            int playerCurrentUnit = 0;
            int computerCurrentUnit = 0;
            boolean isPlayerTurn = true;

            for (int i = 0; i < playerArmy.getUnits().size() + computerArmy.getUnits().size(); i++) {
                if (isPlayerTurn && playerCurrentUnit < playerArmy.getUnits().size()) {
                    Unit unit = playerArmy.getUnits().get(playerCurrentUnit);
                    playerCurrentUnit += 1;
                    if (unit.isAlive()) {
                        Unit target = unit.getProgram().attack();
                        printBattleLog.printBattleLog(unit, target);
                    }
                    else continue;
                }
                else if (computerCurrentUnit < computerArmy.getUnits().size()) {
                    Unit unit = computerArmy.getUnits().get(computerCurrentUnit);
                    computerCurrentUnit += 1;
                    if (unit.isAlive()) {
                        Unit target = unit.getProgram().attack();
                        printBattleLog.printBattleLog(unit, target);
                    }
                    else continue;
                }
                isPlayerTurn = !isPlayerTurn;
            }

            List<Unit> deadPlayerArmy = new ArrayList<>();
            for (Unit unit: playerArmy.getUnits()) {
                if (!unit.isAlive()) deadPlayerArmy.add(unit);
            }
            playerArmy.getUnits().removeAll(deadPlayerArmy);

            List<Unit> deadComputerArmy = new ArrayList<>();
            for (Unit unit: computerArmy.getUnits()) {
                if (!unit.isAlive()) deadComputerArmy.add(unit);
            }
            computerArmy.getUnits().removeAll(deadComputerArmy);
        }
    }
}