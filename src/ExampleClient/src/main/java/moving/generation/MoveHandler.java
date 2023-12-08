package moving.generation;

import exceptions.CoordinatesOutOfBoundsException;
import map.fullMap.FullMapGetter;
import map.utils.Coordinates;
import map.utils.EPoi;
import moving.EDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

public class MoveHandler {

    private static final Logger logger = LoggerFactory.getLogger(MoveHandler.class);
    private final FullMapGetter fma;
    private NextNodePicker nextNodePicker;
    private Queue<EDirection> toMove; // Moves to return to get to the next destination
    private boolean changedToCastleFinding = false;

    public MoveHandler(FullMapGetter fma) {
        nextNodePicker = new NextNodePicker(fma, fma.getMyMapHalf(), EPoi.MYTREASURE);
        logger.debug("initializing node finder with map half = " + fma.getMyMapHalf() + " and seraching for = "
                + EPoi.MYTREASURE);
        this.fma = fma;
    }

    private static EDirection directionToAdjascentNode(Coordinates from, Coordinates to) {
        if (to.getx() - from.getx() == 1)
            return EDirection.RIGHT;
        else if (to.getx() - from.getx() == -1)
            return EDirection.LEFT;
        else if (to.gety() - from.gety() == 1)
            return EDirection.DOWN;
        else if (to.gety() - from.gety() == -1)
            return EDirection.UP;
        else {
            logger.error("Tried to get moves to a non adjascent node! Position from: " + from
                    + ", Position to: " + to);
            throw new IllegalArgumentException("Positions must be adjascent!");
        }
    }

    private static Queue<EDirection> movesToAdjascentNode(Coordinates to, FullMapGetter fma) {
        if (fma == null) {
            logger.error("Map accesser was passed as null!");
            throw new IllegalArgumentException("Map Accesser cannot be null!");
        }

        if (to.getx() >= fma.getWidth() || to.gety() >= fma.getHeight()) {
            logger.error("Position to out of bounds!");
            throw new CoordinatesOutOfBoundsException("Position to out of bounds", to);
        }

        Coordinates from = fma.getEntityPosition(EPoi.MYPLAYER);

        EDirection dir = directionToAdjascentNode(from, to);

        int repeat = fma.getTerrainAt(from).cost() + fma.getTerrainAt(to).cost();
        Queue<EDirection> ret = new LinkedList<>();

        for (int i = 0; i < repeat; ++i)
            ret.add(dir);

        return ret;

    }

    public EDirection getNextMove() {
        if (toMove == null || toMove.isEmpty()) {
            if (fma.treasureCollected() && !changedToCastleFinding) {
                changedToCastleFinding = true;
                nextNodePicker = new NextNodePicker(fma, fma.getMyMapHalf().getOppositeHalf(), EPoi.ENEMYCASTLE);
                logger.debug("changed to castle finding");
            }

            toMove = movesToAdjascentNode(nextNodePicker.getNextPosition(), fma);
        }

        return toMove.remove();
    }

}
