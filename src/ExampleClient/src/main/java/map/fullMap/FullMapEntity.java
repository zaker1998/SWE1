package map.fullMap;

import map.utils.Coordinates;
import map.utils.EField;
import map.utils.EPoi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

public class FullMapEntity {
    private static final Logger logger = LoggerFactory.getLogger(FullMapEntity.class);
    private final Map<Coordinates, EField> terrain;
    private final int width;
    private final int height;
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private Map<EPoi, Coordinates> gameEntityPosition;
    private boolean treasureCollected = false;

    public FullMapEntity(Map<Coordinates, EField> terrain, Map<EPoi, Coordinates> gameEntityPosition) {
        validateNonNull(terrain, "Terrain");
        validateNonNull(gameEntityPosition, "Game Entity Position");

        this.terrain = new HashMap<>(terrain);
        this.gameEntityPosition = new HashMap<>(gameEntityPosition);

        this.width = calculateDimension(terrain, true);
        this.height = calculateDimension(terrain, false);
    }

    private void validateNonNull(Object obj, String paramName) {
        if (obj == null) {
            String errorMsg = paramName + " cannot be null!";
            logger.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
    }

    private int calculateDimension(Map<Coordinates, EField> map, boolean isWidth) {
        int dimension = 0;
        Coordinates coord;
        do {
            coord = isWidth ? new Coordinates(dimension, 0) : new Coordinates(0, dimension);
            dimension++;
        } while (map.containsKey(coord));
        return dimension - 1;
    }

    public void updateEntities(Map<EPoi, Coordinates> gameEntities) {
        validateNonNull(gameEntities, "Game Entities");

        if (gameEntities.get(EPoi.MYPLAYER) == null) {
            throw new IllegalArgumentException("Game Entities must contain MYPLAYER");
        }

        Map<EPoi, Coordinates> old = new HashMap<>(gameEntityPosition);
        gameEntityPosition = new HashMap<>(gameEntities);

        if (old.containsKey(EPoi.MYTREASURE) && !gameEntityPosition.containsKey(EPoi.MYTREASURE)) {
            collectTreasure();
        }

        changes.firePropertyChange("gameEntities", old, gameEntityPosition);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isTreasureCollected() {
        return treasureCollected;
    }

    public void collectTreasure() {
        if (treasureCollected) {
            logger.warn("Treasure already collected, but collectTreasure called again!");
            return;
        }

        logger.debug("Treasure collected!");
        treasureCollected = true;
        changes.firePropertyChange("treasureCollected", false, true);
    }

    public Map<Coordinates, EField> getTerrainCopy() {
        return new HashMap<>(terrain);
    }

    public Map<EPoi, Coordinates> getGameEntitiesCopy() {
        return new HashMap<>(gameEntityPosition);
    }

    public void addListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    public boolean getTreasureCollected() {
        return treasureCollected;
    }
}
