import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        int points = maxPoints;
        List<Unit> armyList = new ArrayList<>();

        Comparator<Unit> unitComparator = Comparator
                .comparingDouble((Unit u) -> (double) u.getBaseAttack() / u.getCost()).reversed()
                .thenComparingDouble((Unit u) -> (double) u.getHealth() / u.getCost()).reversed();

        unitList.sort(unitComparator);

        int numberOfUnits = 0;
        for (Unit unit: unitList) {
            int rows = 3;
            int count = 0;
            while (count < 11 && points >= unit.getCost()) {
                Unit copy = new Unit(
                        unit.getName() + " " + numberOfUnits,
                        unit.getUnitType(),
                        unit.getHealth(),
                        unit.getBaseAttack(),
                        unit.getCost(),
                        unit.getAttackType(),
                        unit.getAttackBonuses(),
                        unit.getDefenceBonuses(),
                        numberOfUnits % rows,
                        numberOfUnits / rows
                );
                        armyList.add(copy);
                points -= unit.getCost();
                count += 1;
                numberOfUnits += 1;
            }
        }
        Army army = new Army();
        army.setUnits(armyList);
        army.setPoints(maxPoints - points);
        return army;
    }
}