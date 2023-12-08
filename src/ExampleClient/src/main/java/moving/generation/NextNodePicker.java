package moving.generation;

import map.fullMap.FullMapGetter;
import map.utils.Coordinates;
import map.utils.EMyHalfChecker;
import map.utils.EPoi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.Set;

public class NextNodePicker {
    private static final Logger logger = LoggerFactory.getLogger(NextNodePicker.class);
    private final FullMapGetter fma;
    private final Queue<Coordinates> visitOrder;
    private final EPoi lookingFor;
    private Queue<Coordinates> pathToNextNode = null;
    private boolean goingToTreasure = false;

    public NextNodePicker(FullMapGetter fma, EMyHalfChecker searchingOnHalf, EPoi lookingFor) {

        if (fma == null || searchingOnHalf == null || lookingFor == null) {
            logger.error("Arguments passed is null!");
            throw new IllegalArgumentException("one of the passed arguments is null");
        }

        this.fma = fma;
        this.lookingFor = lookingFor;

        KeyNodesFinder ine = new KeyNodesFinder(fma, searchingOnHalf);
        Set<Coordinates> toVisit = ine.getInterestingNodes();

        PathAlgorithm spe = new PathAlgorithm();
        this.visitOrder = spe.visitInOrder(toVisit, fma.getEntityPosition(EPoi.MYPLAYER));

        if (visitOrder.stream().distinct().count() != visitOrder.size())
            logger.warn("The algorithm wants to visit a node more then once!");

        logger.debug("Printing nodes to visit:");
        visitOrder.stream().forEach(ele -> logger.debug("Going to: " + ele.toString()));
    }

    public Coordinates getNextPosition() {

        PathFinder pf = new PathFinder();

        // if Position of MyTreasure is known, go there
        if (fma.getEntityPosition(lookingFor) != null && !goingToTreasure) {
            pathToNextNode = pf.pathTo(fma.getEntityPosition(EPoi.MYPLAYER), fma.getEntityPosition(lookingFor),
                    fma);
            goingToTreasure = true;
        }

        // check if I am not going anywhere
        if (pathToNextNode == null || pathToNextNode.isEmpty()) {
            // if this evaluates to true then i have just arrived on the treasure!
            if (goingToTreasure)
                logger.warn("Trying to find the treasure even though it has already been collected!");

            logger.debug("Next stop is: " + visitOrder.peek().toString());

            // throws if visitOrder is empty!
            pathToNextNode = pf.pathTo(fma.getEntityPosition(EPoi.MYPLAYER), visitOrder.remove(), fma);
        }

        return pathToNextNode.remove();
    }

}
