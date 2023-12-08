package ui;

import map.fullMap.FullMapEntity;
import map.utils.Coordinates;
import map.utils.EField;
import map.utils.EPoi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stateofgame.EPlayerState;
import stateofgame.GameState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CLI implements PropertyChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(CLI.class);
    private final List<List<Character>> terrain;
    private Map<EPoi, Coordinates> gameEntities;
    private boolean isTreasureCollected;
    private int roundCounter = 0;
    private EPlayerState gameState = EPlayerState.UNKNOWN;

    public CLI(FullMapEntity fm, GameState gd) {
        fm.addListener(this);
        gd.addListener(this);

        // this may need to get changed I am not sure this method is the best idea
        terrain = hashMapToListList(fm.getTerrainCopy(), fm.getWidth(), fm.getHeight());
        gameEntities = fm.getGameEntitiesCopy();
        isTreasureCollected = fm.getTreasureCollected();

        // maybe remove this and only print after the first move?
        printData();
    }

    private static List<List<Character>> hashMapToListList(Map<Coordinates, EField> map, int width, int height) {
        List<List<Character>> ret = new ArrayList<>();

        for (int y = 0; y < height; ++y) {
            ret.add(new ArrayList<>());
            for (int x = 0; x < width; ++x) {

                switch (map.get(new Coordinates(x, y))) {
                    case GRASS:
                        ret.get(ret.size() - 1).add('*');
                        break;
                    case MOUNTAIN:
                        ret.get(ret.size() - 1).add('-');
                        break;
                    case WATER:
                        ret.get(ret.size() - 1).add('#');
                        break;
                }
            }
        }

        return ret;
    }

    private List<List<Character>> assignedGameEntities() {
        List<List<Character>> ret = terrain.stream().map(row -> new ArrayList<>(row)).collect(Collectors.toList());
        gameEntities.entrySet().stream().forEach(ele -> {
            switch (ele.getKey()) {

                case ENEMYCASTLE:
                case MYCASTLE:
                    ret.get(ele.getValue().gety()).set(ele.getValue().getx(), 'B');
                    break;

                case MYTREASURE:
                    ret.get(ele.getValue().gety()).set(ele.getValue().getx(), 'T');
                    break;

                case MYPLAYER:
                    ret.get(ele.getValue().gety()).set(ele.getValue().getx(), '1');
                    break;

                case ENEMYPLAYER:
                    ret.get(ele.getValue().gety()).set(ele.getValue().getx(), '2');
                    break;
            }
        });

        return ret;
    }

    private void printData() {

        List<List<Character>> printing = assignedGameEntities();

        for (var y : printing) {
            for (var x : y) {
                System.out.print(x);
            }
            System.out.print('\n');
        }
        System.out.println("Your treasure state: " + (isTreasureCollected ? "collected" : "not collected"));
        System.out.println("Round number: " + roundCounter);
        System.out.println('\n');

        if (gameState != EPlayerState.UNKNOWN)
            printGameOver();
    }

    private void printGameOver() {
        System.out.println("\n\nGAME OVER");
        System.out.println("You: " + gameState + "\n");
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        // TODO Auto-generated method stub

        Object received = event.getNewValue();

        switch (event.getPropertyName()) {

            case "gameEntities":
                if (!(received instanceof Map<?, ?>)) {
                    logger.error(
                            "The event with name 'gameEntities' was triggered but the received object was not of type Map<EGameEntity, Position>");
                    throw new RuntimeException(
                            "The event with name 'gameEntities' was triggered but the received object was not of type Map<EGameEntity, Position>");
                }

                gameEntities = (Map<EPoi, Coordinates>) received;

                // if new gameEntities are received (the player position among them, reprint the
                // map
                break;

            case "treasureCollected":
                if (!(received instanceof Boolean)) {
                    logger.error(
                            "The event with name 'treasureCollected' was triggered but the received object was not of type Boolean");
                    throw new RuntimeException(
                            "The event with name 'treasureCollected' was triggered but the received object was not of type Boolean");
                }

                isTreasureCollected = (Boolean) received;
                break;

            case "ePlayerState":
                if (!(received instanceof EPlayerState)) {
                    logger.error(
                            "The event with name 'ePlayerState' was triggered but the received object was not of type EGameState");
                    throw new RuntimeException(
                            "The event with name 'ePlayerState' was triggered but the received object was not of type EGameState");
                }

                gameState = (EPlayerState) received;

                printData();
                break;

            case "myRound":
                if (!(received instanceof Boolean)) {
                    logger.error(
                            "The event with name 'myTurn' was triggered but the received object was not of type Boolean");
                    throw new RuntimeException(
                            "The event with name 'myTurn' was triggered but the received object was not of type Boolean");
                }

                boolean myRound = (Boolean) received;
                break;

            case "roundCount":
                if (!(received instanceof Integer)) {
                    logger.error(
                            "The event with name 'turnCount' was triggered but the received object was not of type Integer");
                    throw new RuntimeException(
                            "The event with name 'turnCount' was triggered but the received object was not of type Integer");
                }

                roundCounter = (Integer) received;

                printData();
                break;

            default:
                logger.error("An unrecognised propertyChange occured. Name: " + event.getPropertyName());
                throw new RuntimeException("Unrecogniszed property change! Change was: " + event.getPropertyName());
        }
    }

}
