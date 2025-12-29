import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> result = new ArrayList<>();
        List<List<Unit>> grid = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<Unit> row = new ArrayList<>();
            for (int j = 0; j < 21; j++) {
                row.add(null);
            }
            grid.add(row);
        }
        for (List<Unit> row: unitsByRow) {
            for (Unit unit: row) {
                int x = unit.getxCoordinate();
                int y = unit.getyCoordinate();
                if (!isLeftArmyTarget) x -= 24;
                grid.get(x).set(y, unit);
            }
        }

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 21; y++) {
                if (grid.get(x).get(y) != null) {
                    if (isLeftArmyTarget) {
                        if (x != 2 && grid.get(x+1).get(y) != null) {
                            result.add(grid.get(x).get(y));
                        }
                    }
                    else {
                        if (x != 0 && grid.get(x-1).get(y) != null) {
                            result.add(grid.get(x).get(y));
                        }
                    }
                }
            }
        }
        return result;
    }
}
