package execution;

import communication.NetworkConverter;
import exceptions.NetworkCommunicationException;
import map.fullMap.FullMapEntity;
import map.fullMap.FullMapGetter;
import map.halfMap.HalfMapGenerator;
import moving.generation.MoveHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stateofgame.EPlayerState;
import stateofgame.GameState;
import ui.CLI;

public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public static void main(String[] args) {
        if (!areArgumentsValid(args)) {
            return;
        }

        final String serverBaseUrl = args[1];
        final String gameId = args[2];
        final NetworkConverter net = new NetworkConverter(serverBaseUrl, gameId);

        try {
            registerAndStartGame(net);
        } catch (NetworkCommunicationException e) {
            logger.error("Network communication error: {}", e.getMessage(), e);
        }
    }

    private static boolean areArgumentsValid(String[] args) {
        if (args.length < 3) {
            logger.error("Insufficient arguments. Required: serverBaseUrl and gameId.");
            return false;
        }
        return true;
    }

    private static void registerAndStartGame(NetworkConverter net) throws NetworkCommunicationException {
        registerPlayer(net);
        waitUntilRoundStarts(net);

        sendHalfMapAndPrepareGame(net);
        playGame(net);
    }

    private static void registerPlayer(NetworkConverter net) throws NetworkCommunicationException {
        net.registerPlayer("Marat", "Dussaliyev", "maratd77");
    }

    private static void waitUntilRoundStarts(NetworkConverter net) throws NetworkCommunicationException {
        while (!net.isMyRound()) {
            // Waiting for the round to start
        }
        logger.debug("Both players registered. Starting game...");
    }

    private static void sendHalfMapAndPrepareGame(NetworkConverter net) throws NetworkCommunicationException {
        net.sendHalfMap(HalfMapGenerator.generateMap());
        logger.debug("HalfMap sent to server");

        while (!net.isMyRound()) {
            // Waiting for the other player to send HalfMap
        }
        logger.debug("Both players sent HalfMaps. Retrieving FullMap from server");
    }

    private static void playGame(NetworkConverter net) throws NetworkCommunicationException {
        final FullMapEntity map = net.getFullMap();
        final GameState gameState = new GameState(true);
        final CLI ui = new CLI(map, gameState);
        final MoveHandler moveHandler = new MoveHandler(new FullMapGetter(map));

        while (gameState.getePlayerState() == EPlayerState.UNKNOWN) {
            executeRound(net, gameState, map, moveHandler);
        }
    }

    private static void executeRound(NetworkConverter net, GameState gameState, FullMapEntity map, MoveHandler moveHandler) throws NetworkCommunicationException {
        if (net.isMyRound() && net.getGameState() == EPlayerState.UNKNOWN) {
            net.sendMove(moveHandler.getNextMove());
            map.updateEntities(net.getEntities());
            if (net.collectedTreasure()) {
                map.collectTreasure();
            }
            gameState.nextTurn();
        }
    }
}
