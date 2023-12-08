package moving.generation;

import map.fullMap.FullMapGetter;
import map.utils.Coordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PathFinder {

    private static final Logger logger = LoggerFactory.getLogger(PathFinder.class);

    private static Set<Coordinates> getNeighbours(Coordinates pos, FullMapGetter fma) {
        Set<Coordinates> ret = new HashSet<>();

        // up
        if (pos.gety() > 0)
            ret.add(new Coordinates(pos.getx(), pos.gety() - 1));

        // down
        if (pos.gety() < fma.getHeight() - 1)
            ret.add(new Coordinates(pos.getx(), pos.gety() + 1));

        // right
        if (pos.getx() < fma.getWidth() - 1)
            ret.add(new Coordinates(pos.getx() + 1, pos.gety()));

        // left
        if (pos.getx() > 0)
            ret.add(new Coordinates(pos.getx() - 1, pos.gety()));

        return ret;

    }

    public Queue<Coordinates> pathTo(Coordinates start, Coordinates dest, FullMapGetter fma) {

        logger.debug("trying to find path from:" + start.toString() + " to: " + dest.toString());

        // this can technically be grouped to one; maybe ofload to different class?
        Map<Coordinates, Integer> cost = new HashMap<>();
        Map<Coordinates, Queue<Coordinates>> pathTo = new HashMap<>();

        Set<Coordinates> visited = new HashSet<>();
        Set<Coordinates> frontier = new HashSet<>();

        // initialize the cost with 0 at the start
        cost.put(start, 0);
        // initialize the path with an queue containing just the start at the start
        pathTo.put(start, new LinkedList<>());
        // pathTo.get(start).add(start);

        while (!visited.contains(dest)) {
            // get all unvisited neighbors
            Set<Coordinates> neighbours = getNeighbours(start, fma);
            neighbours.removeAll(visited);

            // add current node as visited
            visited.add(start);

            // update costs of neighbors
            for (Coordinates p : neighbours) {
                if (cost.get(p) == null || cost.get(p) >= cost.get(start) + fma.getTerrainAt(start).cost()
                        + fma.getTerrainAt(p).cost()) {

                    cost.put(p, cost.get(start) + fma.getTerrainAt(start).cost() + fma.getTerrainAt(p).cost());

                    // I need to create a copy of this so I can add the new node without side
                    // effects
                    Queue<Coordinates> temp = new LinkedList<>(pathTo.get(start));
                    temp.add(p);

                    pathTo.put(p, temp);
                }
                frontier.add(p);
            }

            start = frontier.stream().min((Coordinates lhs, Coordinates rhs) -> {
                return cost.get(lhs) - cost.get(rhs);
            }).get();

            logger.debug("start node in dijkstra: " + start + " Cost: " + cost.get(start));

            frontier.remove(start);

        }

        logger.debug("Dijkstra cost of destination node: " + cost.get(dest));

        return pathTo.get(dest);
    }

}
