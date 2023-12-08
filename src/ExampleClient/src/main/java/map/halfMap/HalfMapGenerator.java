package map.halfMap;

import exceptions.BadHalfMapGeneratedException;
import map.utils.Coordinates;
import map.utils.EField;
import map.utils.EMapBorders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class HalfMapGenerator {

    private static final Logger logger = LoggerFactory.getLogger(HalfMapGenerator.class);

    public static HalfMapEntity generateMap() {
        Map<Coordinates, EField> terrain;
        Coordinates castle;
        HalfMapEntity halfMapEntity;

        while (true) {
            terrain = generateTerrain();
            castle = placeCastle(terrain);

            try {
                halfMapEntity = new HalfMapEntity(terrain, castle);
                break; // Break the loop if map creation is successful
            } catch (BadHalfMapGeneratedException e) {
                logger.warn("Map generation failed, attempting to regenerate");
                // Loop will continue to attempt a new map generation
            }
        }
        return halfMapEntity;
    }
    private static Coordinates placeCastle(Map<Coordinates, EField> mapLayout) {
        List<Coordinates> grassFields = mapLayout.entrySet().stream()
                .filter(entry -> entry.getValue() == EField.GRASS)
                .map(Map.Entry::getKey)
                .toList();

        if (grassFields.isEmpty()) {
            throw new IllegalStateException("No grass fields available for placing the castle");
        }

        Random random = new Random();
        return grassFields.get(random.nextInt(grassFields.size()));
    }

    private static Map<Coordinates, EField> generateTerrain() {
        int totalFields = EMapBorders.HALFMAP.width() * EMapBorders.HALFMAP.height();
        int grassFields = Math.max((int) (totalFields * 0.48), 1); // 48% grass
        int mountainFields = Math.max((int) (totalFields * 0.10), 1); // 10% mountains
        int waterFields = Math.max((int) (totalFields * 0.14), 1); // 14% water

        List<EField> fieldTypes = new ArrayList<>();

        fieldTypes.addAll(Collections.nCopies(grassFields, EField.GRASS));
        fieldTypes.addAll(Collections.nCopies(mountainFields, EField.MOUNTAIN));
        fieldTypes.addAll(Collections.nCopies(waterFields, EField.WATER));

        // Fill the remaining fields with a mix of types
        fillRemainingFields(fieldTypes, totalFields);

        Collections.shuffle(fieldTypes);

        return buildTerrainMap(fieldTypes);
    }

    private static void fillRemainingFields(List<EField> fieldTypes, int totalFields) {
        Random random = new Random();
        while (fieldTypes.size() < totalFields) {
            if (random.nextBoolean()) {
                fieldTypes.add(EField.MOUNTAIN);
            } else {
                fieldTypes.add(EField.WATER);
            }
        }
    }

    private static Map<Coordinates, EField> buildTerrainMap(List<EField> fieldTypes) {
        Map<Coordinates, EField> terrain = new LinkedHashMap<>();
        int index = 0;
        for (int y = 0; y < EMapBorders.HALFMAP.height(); y++) {
            for (int x = 0; x < EMapBorders.HALFMAP.width(); x++) {
                terrain.put(new Coordinates(x, y), fieldTypes.get(index++));
            }
        }
        return terrain;
    }
}
