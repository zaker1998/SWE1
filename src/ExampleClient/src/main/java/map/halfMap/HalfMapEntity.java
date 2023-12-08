package map.halfMap;

import exceptions.BadHalfMapGeneratedException;
import exceptions.CoordinatesOutOfBoundsException;
import map.utils.Coordinates;
import map.utils.EField;
import map.utils.EMapBorders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Stream;

public class HalfMapEntity {

    private static final Logger logger = LoggerFactory.getLogger(HalfMapEntity.class);
    private final Map<Coordinates, EField> terrain;
    private final Coordinates myCastleCoordinates;

    public HalfMapEntity(Map<Coordinates, EField> mapTerrain, Coordinates castleCoords)
            throws BadHalfMapGeneratedException {
        validateParameters(mapTerrain, castleCoords);
        ensureCastleWithinBounds(castleCoords);
        validateMapTerrain(mapTerrain, castleCoords);

        this.terrain = mapTerrain;
        this.myCastleCoordinates = castleCoords;
    }

    public Stream<Map.Entry<Coordinates, EField>> getStream() {
        return terrain.entrySet().stream();
    }

    public Coordinates castlePosition() {
        return myCastleCoordinates;
    }

    private void validateParameters(Map<Coordinates, EField> mapTerrain, Coordinates castleCoords) {
        if (mapTerrain == null || castleCoords == null) {
            logger.error("Constructor parameters for HalfMapEntity cannot be null");
            throw new IllegalArgumentException("Terrain map and castle coordinates must be provided");
        }
    }

    private void ensureCastleWithinBounds(Coordinates castleCoords) {
        if (castleCoords.getx() >= EMapBorders.HALFMAP.width() || castleCoords.gety() >= EMapBorders.HALFMAP.height()) {
            logger.error("Castle position is out of half map boundaries: " + castleCoords);
            throw new CoordinatesOutOfBoundsException("Castle position must be within the half map", castleCoords);
        }
    }

    private void validateMapTerrain(Map<Coordinates, EField> mapTerrain, Coordinates castleCoords) throws BadHalfMapGeneratedException {
        ValidMapChecker checker = new ValidMapChecker(mapTerrain);
        if (!checker.validateTerrain()) {
            logger.error("Invalid terrain map provided to HalfMapEntity");
            throw new BadHalfMapGeneratedException();
        }
        if (!checker.validateCastlePosition(castleCoords)) {
            logger.error("Invalid castle position on the terrain");
            throw new BadHalfMapGeneratedException();
        }
    }

}
