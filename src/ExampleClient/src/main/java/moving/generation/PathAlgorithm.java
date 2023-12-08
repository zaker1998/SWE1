package moving.generation;

import map.utils.Coordinates;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class PathAlgorithm {
    public Queue<Coordinates> visitInOrder(Set<Coordinates> toVisit, Coordinates start) {

        Queue<Coordinates> ret = new LinkedList<>();

        Set<Coordinates> toVisitCopy = new HashSet<>(toVisit);

        // if toVisit also contains the start node, remove it
        toVisit.remove(start);

        while (!toVisitCopy.isEmpty()) {
            // needed to be able to pass it into the lambda
            Coordinates cur = start;
            Coordinates closest = toVisitCopy.stream().min((Coordinates x1, Coordinates x2) -> {
                return Double.compare(Coordinates.distance(cur, x1), Coordinates.distance(cur, x2));
            }).get();

            ret.add(closest);
            toVisitCopy.remove(closest);
            start = closest;
        }

        return ret;

    }

}
