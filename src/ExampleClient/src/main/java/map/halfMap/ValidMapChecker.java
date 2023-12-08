package map.halfMap;

import map.utils.Coordinates;
import map.utils.EField;
import map.utils.EMapBorders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ValidMapChecker {

    private static final Logger logger = LoggerFactory.getLogger(ValidMapChecker.class);
    private final Map<Coordinates, EField> testing;

    public ValidMapChecker(Map<Coordinates, EField> testing) {
        if (testing == null) {
            logger.error("The passed map was null!");
            throw new IllegalArgumentException("Map cannot be null!");
        }
        this.testing = testing;
    }

    private boolean validateTerrainDimensions() {
        Set<Coordinates> coordinates = testing.keySet();

        if (coordinates.size() != EMapBorders.HALFMAP.width() * EMapBorders.HALFMAP.height()) {
            logger.error("HalfMap size is not 50, size is: " + coordinates.size());
            return false;
        }

        if (!coordinates.stream().allMatch(
                pos -> pos.getx() < EMapBorders.HALFMAP.width() && pos.gety() < EMapBorders.HALFMAP.height())) {
            logger.error("HalfMap terrain hashmap position invalid");
            return false;
        }
        return true;
    }

    private boolean validateTerrainFieldCount() {

        Collection<EField> t = testing.values();
        int totalFields = EMapBorders.HALFMAP.width() * EMapBorders.HALFMAP.height();

        long countGrass = t.stream().filter(ter -> ter == EField.GRASS).count();
        long countWater = t.stream().filter(ter -> ter == EField.WATER).count();
        long countMountain = t.stream().filter(ter -> ter == EField.MOUNTAIN).count();

        if (countGrass < EField.GRASS.minAmount() || countWater < EField.WATER.minAmount()
                || countMountain < EField.MOUNTAIN.minAmount()) {
            logger.error("HalfMap terrain does not contain enough of a certain terrain type countGrass: " + countGrass
                    + ", countWater: " + countWater + ", countMountain:" + countMountain);
            return false;
        }

        return true;
    }

    private boolean validateTerrainEdges() {
        // check if half of the edge is made up of water

        var mapset = testing.entrySet();

        long leftEdgeCount = mapset.stream().filter(ele -> ele.getKey().getx() == 0 && ele.getValue() == EField.WATER)
                .count();
        long rightEdgeCount = mapset.stream().filter(
                        ele -> ele.getKey().getx() == EMapBorders.HALFMAP.width() - 1 && ele.getValue() == EField.WATER)
                .count();
        long topEdgeCount = mapset.stream().filter(ele -> ele.getKey().gety() == 0 && ele.getValue() == EField.WATER)
                .count();
        long botEdgeCount = mapset.stream().filter(
                        ele -> ele.getKey().gety() == EMapBorders.HALFMAP.height() - 1 && ele.getValue() == EField.WATER)
                .count();

        final int SHORTEDGE_MAX = EMapBorders.HALFMAP.height() / 2;
        final int LONGEDGE_MAX = EMapBorders.HALFMAP.width() / 2;

        if (leftEdgeCount >= SHORTEDGE_MAX || rightEdgeCount >= SHORTEDGE_MAX || topEdgeCount >= LONGEDGE_MAX
                || botEdgeCount >= LONGEDGE_MAX) {
            logger.error("HalfMap terrain generated too much on one of the edges left: " + leftEdgeCount + ", right: "
                    + rightEdgeCount + ", top: " + topEdgeCount + ", bottom: " + botEdgeCount);
            return false;
        }
        return true;
    }

    private boolean validateTerrainIslands() {
        // provide a copy of the hashmap to floodfill
        var copy = new HashMap<Coordinates, EField>(testing);

        // start floodfill from 0, 0
        floodfill(new Coordinates(0, 0), copy);

        // map contains islands if it is not empty and at least one node inside is not
        // water
        if (copy.size() != 0 && !copy.values().stream().allMatch(ele -> ele == EField.WATER)) {
            logger.error("HalfMap contains an Island!");
            return false;
        }

        return true;
    }

    private void floodfill(Coordinates node, Map<Coordinates, EField> nodes) {
        // THE HASHMAP PASSES AS ARGUMENT HERE WILL BE MODIFIED. PROVIDE A COPY
        // THE START NODE CANNOT BE WATER
        if (!nodes.containsKey(node) || nodes.get(node) == EField.WATER)
            return;

        nodes.remove(node);

        // north
        if (node.gety() - 1 >= 0)
            floodfill(new Coordinates(node.getx(), node.gety() - 1), nodes);

        floodfill(new Coordinates(node.getx(), node.gety() + 1), nodes);
        // east
        if (node.getx() - 1 >= 0)
            floodfill(new Coordinates(node.getx() - 1, node.gety()), nodes);
        // west
        floodfill(new Coordinates(node.getx() + 1, node.gety()), nodes);
    }

    public boolean validateTerrain() {
        return validateTerrainDimensions() && validateTerrainFieldCount() && validateTerrainEdges()
                && validateTerrainIslands();
    }

    public boolean validateCastlePosition(Coordinates myCastleCoordinates) {
        return testing.get(myCastleCoordinates) == EField.GRASS;
    }

}