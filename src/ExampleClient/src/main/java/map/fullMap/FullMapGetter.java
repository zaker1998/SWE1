package map.fullMap;

import exceptions.CoordinatesOutOfBoundsException;
import map.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class FullMapGetter {

    private static final Logger logger = LoggerFactory.getLogger(FullMapGetter.class);
    private final FullMapEntity fmd;

    public FullMapGetter(FullMapEntity fmd) {
        this.fmd = validateNotNull(fmd, "FullMapEntity");
    }

    private <T> T validateNotNull(T obj, String name) {
        if (obj == null) {
            String errorMsg = name + " cannot be null";
            logger.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        return obj;
    }

    public EField getTerrainAt(Coordinates pos) {
        validateNotNull(pos, "Coordinates");
        validatePositionWithinBounds(pos);

        return fmd.getTerrainCopy().get(pos);
    }
    public int getTerrainCost(Coordinates position) {
        EField field = getTerrainAt(position);
        switch (field) {
            case GRASS:
                return 1; // Normal cost
            case WATER:
                return 1000; // High cost to avoid water
            case MOUNTAIN:
                return 5; // Example cost for mountain
            default:
                return 1;
        }
    }



    private void validatePositionWithinBounds(Coordinates pos) {
        if (pos.getx() >= fmd.getWidth() || pos.gety() >= fmd.getHeight()) {
            String errorMsg = "Position out of bounds: " + pos;
            logger.error(errorMsg);
            throw new CoordinatesOutOfBoundsException(errorMsg, pos);
        }
    }

    public Coordinates getEntityPosition(EPoi entityType) {
        validateNotNull(entityType, "EPoi");
        return fmd.getGameEntitiesCopy().get(entityType);
    }

    public int getWidth() {
        return fmd.getWidth();
    }

    public int getHeight() {
        return fmd.getHeight();
    }

    public boolean treasureCollected() {
        return fmd.isTreasureCollected();
    }

    public EMyHalfChecker getMyMapHalf() {
        Coordinates myCastlePos = getEntityPosition(EPoi.MYCASTLE);
        return isLongMap() ? determineLongMapHalf(myCastlePos) : determineSquareMapHalf(myCastlePos);
    }

    public Set<Coordinates> getNeighbors(Coordinates pos) {
        Set<Coordinates> neighbors = new HashSet<>();

        // Check and add the neighbor above
        if (pos.gety() > 0) {
            neighbors.add(new Coordinates(pos.getx(), pos.gety() - 1));
        }

        // Check and add the neighbor below
        if (pos.gety() < getHeight() - 1) {
            neighbors.add(new Coordinates(pos.getx(), pos.gety() + 1));
        }

        // Check and add the neighbor to the left
        if (pos.getx() > 0) {
            neighbors.add(new Coordinates(pos.getx() - 1, pos.gety()));
        }

        // Check and add the neighbor to the right
        if (pos.getx() < getWidth() - 1) {
            neighbors.add(new Coordinates(pos.getx() + 1, pos.gety()));
        }

        return neighbors;
    }
    private boolean isLongMap() {
        return getHeight() == EMapBorders.LONGMAP.height();
    }

    private EMyHalfChecker determineLongMapHalf(Coordinates position) {
        int xsep = getWidth() / 2;
        return position.getx() < xsep ? EMyHalfChecker.LONGMAPORIGIN : EMyHalfChecker.LONGMAPOPPOSITE;
    }

    private EMyHalfChecker determineSquareMapHalf(Coordinates position) {
        int ysep = getHeight() / 2;
        return position.gety() < ysep ? EMyHalfChecker.SQUAREMAPORIGIN : EMyHalfChecker.SQUAREMAPOPPOSITE;
    }
}
