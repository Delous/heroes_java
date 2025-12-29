import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    static int distance(Edge a, Edge b) {
        return Math.max(
                Math.abs(a.getX() - b.getX()),
                Math.abs(a.getY() - b.getY())
        );
    }

    static String key(Edge e) {
        return e.getX() + " " + e.getY();
    }

    static List<Edge> buildPath(Map<String, Edge> cameFrom, Edge current) {
        List<Edge> path = new ArrayList<>();
        path.add(current);

        while (cameFrom.containsKey(key(current))) {
            current = cameFrom.get(key(current));
            path.add(current);
        }

        Collections.reverse(path);
        return path;
    }


    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        Edge start = new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate());
        Edge goal = new Edge(targetUnit.getxCoordinate(), targetUnit.getyCoordinate());

        int[][] map = new int[27][21];
        int width = map.length;
        int height = map[0].length;

        for (Unit unit: existingUnitList) {
            if (unit != targetUnit) {
                map[unit.getxCoordinate()][unit.getyCoordinate()] = 1;
            }
        }

        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        Map<String, Integer> gScore = new HashMap<>();
        Map<String, Edge> cameFrom = new HashMap<>();

        PriorityQueue<Edge> openSet = new PriorityQueue<>(
                Comparator.comparingInt(
                        e -> gScore.get(key(e)) + distance(e, goal)
                )
        );

        gScore.put(key(start), 0);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Edge current = openSet.poll();

            if (current.getX() == goal.getX()
                    && current.getY() == goal.getY()) {
                return buildPath(cameFrom, current);
            }

            int currentG = gScore.get(key(current));

            for (int[] d: directions) {
                int nx = current.getX() + d[0];
                int ny = current.getY() + d[1];

                if (nx < 0 || ny < 0 || nx >= width || ny >= height)
                    continue;

                if (map[nx][ny] == 1)
                    continue;

                Edge neighbor = new Edge(nx, ny);
                String nKey = key(neighbor);

                int tentativeG = currentG + 1;

                if (!gScore.containsKey(nKey) || tentativeG < gScore.get(nKey)) {
                    cameFrom.put(nKey, current);
                    gScore.put(nKey, tentativeG);
                    openSet.add(neighbor);
                }
            }
        }

        return new ArrayList<>();
    }
}
