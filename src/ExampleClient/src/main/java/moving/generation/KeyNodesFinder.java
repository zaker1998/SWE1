package moving.generation;

import map.fullMap.FullMapGetter;
import map.utils.Coordinates;
import map.utils.EField;
import map.utils.EMyHalfChecker;
import map.utils.EPoi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class KeyNodesFinder {

    private static final Logger logger = LoggerFactory.getLogger(KeyNodesFinder.class);
    private final FullMapGetter fma;
    private final EMyHalfChecker myHalf;

    public KeyNodesFinder(FullMapGetter fma, EMyHalfChecker myHalf) {
        if (fma == null || myHalf == null) {
            logger.error("One of the passed arguments was null!");
            throw new IllegalArgumentException("arguments cannot be null!");
        }

        this.fma = fma;
        this.myHalf = myHalf;
    }

    private Set<Coordinates> extractGrassNodes() {
        Set<Coordinates> ret = new HashSet<>();

        for (int y = myHalf.getyLowerBound(); y <= myHalf.getyUpperBound(); ++y) {
            for (int x = myHalf.getxLowerBound(); x <= myHalf.getxUpperBound(); ++x) {
                Coordinates cur = new Coordinates(x, y);
                if (fma.getTerrainAt(cur) == EField.GRASS && !fma.getEntityPosition(EPoi.MYCASTLE).equals(cur))
                    ret.add(cur);
            }
        }

        return ret;
    }

    private Set<Coordinates> addMountainsWhereEfficient(Set<Coordinates> allGrassNodes) {

        Set<Coordinates> ret = new HashSet<>(allGrassNodes);

        // then add some mountains and remove grass around them where this is more
        // efficient
        for (int y = myHalf.getyLowerBound(); y <= myHalf.getyUpperBound(); ++y) {
            for (int x = myHalf.getxLowerBound(); x <= myHalf.getxUpperBound(); ++x) {

                Coordinates cur = new Coordinates(x, y);

                if (fma.getTerrainAt(cur) == EField.MOUNTAIN) {
                    Set<Coordinates> surroundingGrass = new HashSet<>();

                    // loop over all adjacent positions
                    for (int xDiff = -1; xDiff <= 1; ++xDiff) {
                        for (int yDiff = -1; yDiff <= 1; ++yDiff) {
                            if (xDiff == 0 && yDiff == 0)
                                continue;
                            if (x + xDiff >= 0 && x + xDiff < fma.getWidth() && y + yDiff >= 0
                                    && y + yDiff < fma.getHeight()
                                    && fma.getTerrainAt(new Coordinates(x + xDiff, y + yDiff)) == EField.GRASS
                                    && ret.contains(new Coordinates(x + xDiff, y + yDiff)))
                                surroundingGrass.add(new Coordinates(x + xDiff, y + yDiff));
                        }
                    }

                    // if more than 2 grass tiles surround a mountain it is efficient to go up the
                    // mountain
                    if (surroundingGrass.size() > 2) {
                        ret.removeAll(surroundingGrass);
                        ret.add(cur);
                    }
                }
            }
        }
        return ret;
    }

    // used to help the constructor find nodes to visit
    public Set<Coordinates> getInterestingNodes() {

        Set<Coordinates> allGrassNodes = extractGrassNodes();

        return addMountainsWhereEfficient(allGrassNodes);
    }
}
