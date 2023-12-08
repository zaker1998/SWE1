package communication;

import exceptions.NetworkCommunicationException;
import map.fullMap.FullMapEntity;
import map.halfMap.HalfMapEntity;
import map.utils.Coordinates;
import map.utils.EField;
import map.utils.EPoi;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.*;
import messagesbase.messagesfromserver.*;
import moving.EDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stateofgame.EPlayerState;

import java.util.*;
import java.util.stream.Collectors;

public class NetworkConverter {
    private static final Logger logger = LoggerFactory.getLogger(NetworkConverter.class);
    private final NetworkCommunicator netComm;
    private UniquePlayerIdentifier playerUID;

    public NetworkConverter(String gameURL, String gameID) {
        netComm = new NetworkCommunicator(gameURL, new UniqueGameIdentifier(gameID));
    }

    //PRIVATE
    private static EField networkTerrainToInternal(messagesbase.messagesfromclient.ETerrain ele) {
        switch (ele) {
            case Grass -> {
                return EField.GRASS;
            }
            case Water -> {
                return EField.WATER;
            }
            case Mountain -> {
                return EField.MOUNTAIN;
            }
            default -> {
                logger.error("Received an unknown terrain type from server");
                throw new RuntimeException("Received an unknown terrain type from server");
            }
        }
    }

    private static messagesbase.messagesfromclient.ETerrain internalTerrainToNetwork(EField ele) {
        switch (ele) {
            case GRASS -> {
                return messagesbase.messagesfromclient.ETerrain.Grass;
            }
            case WATER -> {
                return messagesbase.messagesfromclient.ETerrain.Water;
            }
            case MOUNTAIN -> {
                return messagesbase.messagesfromclient.ETerrain.Mountain;
            }
            default -> {
                logger.error("HalfMap object contains a terrain type that is not defined in the network protocol");
                throw new RuntimeException("HalfMap object contains a terrain type that is not defined in the network protocol!");
            }
        }
    }

    private static List<EPoi> extractGameEntitiesFromNode(messagesbase.messagesfromserver.FullMapNode node) {
        List<EPoi> entities = new ArrayList<>();

        switch (node.getFortState()) {
            case MyFortPresent:
                entities.add(EPoi.MYCASTLE);
                break;
            case EnemyFortPresent:
                entities.add(EPoi.ENEMYCASTLE);
                break;
            default:
                // Handle other cases or leave empty
        }

        switch (node.getPlayerPositionState()) {
            case MyPlayerPosition:
            case BothPlayerPosition:
                entities.add(EPoi.MYPLAYER);
                // Fall through for BothPlayerPosition
            case EnemyPlayerPosition:
                entities.add(EPoi.ENEMYPLAYER);
                break;
            default:
                // Handle other cases or leave empty
        }

        if (node.getTreasureState() == messagesbase.messagesfromserver.ETreasureState.MyTreasureIsPresent) {
            entities.add(EPoi.MYTREASURE);
            logger.debug("I see my treasure!!!");
        }

        return entities;
    }

    private static FullMapEntity networkMapToInternalMap(Collection<messagesbase.messagesfromserver.FullMapNode> nodes) {
        if (nodes == null) {
            throw new IllegalArgumentException("Nodes collection cannot be null");
        }

        Map<Coordinates, EField> terrainMap = new HashMap<>();
        Map<EPoi, List<Coordinates>> gameEntitiesMap = new HashMap<>();

        for (var node : nodes) {
            EField terrainType = networkTerrainToInternal(node.getTerrain());
            Coordinates position = new Coordinates(node.getX(), node.getY());

            terrainMap.put(position, terrainType);

            List<EPoi> entitiesInNode = extractGameEntitiesFromNode(node);
            for (EPoi entity : entitiesInNode) {
                gameEntitiesMap.computeIfAbsent(entity, k -> new ArrayList<>()).add(position);
            }
        }

        return new FullMapEntity(terrainMap, convertToSingleCoordinatesMap(gameEntitiesMap));
    }

    private static Map<EPoi, Coordinates> convertToSingleCoordinatesMap(Map<EPoi, List<Coordinates>> gameEntitiesMap) {
        Map<EPoi, Coordinates> singleCoordinatesMap = new HashMap<>();
        for (var entry : gameEntitiesMap.entrySet()) {
            // Currently takes the first position of the entity; modify as per game logic if needed
            singleCoordinatesMap.put(entry.getKey(), entry.getValue().get(0));
        }
        return singleCoordinatesMap;
    }

    private static messagesbase.messagesfromclient.EMove internalMovetoNetwork(EDirection ele) {
        switch (ele) {
            case UP -> {
                return messagesbase.messagesfromclient.EMove.Up;
            }
            case DOWN -> {
                return messagesbase.messagesfromclient.EMove.Down;
            }
            case LEFT -> {
                return messagesbase.messagesfromclient.EMove.Left;
            }
            case RIGHT -> {
                return messagesbase.messagesfromclient.EMove.Right;
            }
            default -> {
                logger.error("Received unexpected EMove value that could not be mapped to any network representation");
                throw new IllegalArgumentException("ele cannot be translated to the network representation");
            }
        }
    }

    public void registerPlayer(String firstName, String lastName, String id) throws NetworkCommunicationException {
        playerUID = netComm.registerPlayer(new PlayerRegistration(firstName, lastName, id));
    }

    public void sendHalfMap(HalfMapEntity hmdata) throws NetworkCommunicationException {
        PlayerHalfMap hm = internalHalfMapToNetworkHalfMap(hmdata);
        netComm.sendHalfMap(hm);
    }

    public boolean collectedTreasure() throws NetworkCommunicationException {
        GameState gameState = netComm.getGameStateCached(playerUID);
        if (gameState == null || gameState.getPlayers() == null) {
            logger.error("GameState or players set is null for playerID: " + playerUID);
            throw new NetworkCommunicationException("GameState or players set cannot be null");
        }

        return gameState.getPlayers().stream().filter(playerState -> playerState.equals(playerUID)).findFirst().map(PlayerState::hasCollectedTreasure).orElseThrow(() -> new NetworkCommunicationException("Player state not found for playerID: " + playerUID));
    }

    public EPlayerState getGameState() throws NetworkCommunicationException {
        GameState gameState = netComm.getGameStateCached(playerUID);
        if (gameState == null || gameState.getPlayers() == null) {
            logger.error("GameState or players set is null for playerID: " + playerUID);
            throw new NetworkCommunicationException("GameState or players set cannot be null");
        }

        return gameState.getPlayers().stream().filter(playerState -> playerState.equals(playerUID)).findFirst().map(PlayerState::getState).map(this::networkStateToInternal).orElseThrow(() -> new NetworkCommunicationException("Player state not found for playerID: " + playerUID));
    }

    public boolean isMyRound() throws NetworkCommunicationException {
        GameState gameState = netComm.getGameState(playerUID);
        if (gameState == null || gameState.getPlayers() == null) {
            logger.error("GameState or players set is null for playerID: " + playerUID);
            throw new NetworkCommunicationException("GameState or players set cannot be null");
        }

        return gameState.getPlayers().stream().filter(playerState -> playerState.equals(playerUID)).findFirst().map(PlayerState::getState).map(state -> state == EPlayerGameState.MustAct).orElseThrow(() -> new NetworkCommunicationException("Player state not found for playerID: " + playerUID));
    }

    public FullMapEntity getFullMap() throws NetworkCommunicationException {
        FullMap fullMap = netComm.getGameState(playerUID).getMap();
        if (fullMap == null) {
            logger.error("No FullMap available in the returned GameState for playerID: " + playerUID);
            throw new NetworkCommunicationException("FullMap is not available in the returned GameState");
        }
        return networkMapToInternalMap(fullMap.getMapNodes());
    }

    public void sendMove(EDirection dir) throws NetworkCommunicationException {
        if (dir == null) {
            logger.error("sendMove method received a null direction argument");
            throw new IllegalArgumentException("Direction (dir) cannot be null");
        }
        EMove networkMove = internalMovetoNetwork(dir);
        if (networkMove == null) {
            logger.error("Internal direction to network move conversion failed for direction: " + dir);
            throw new NetworkCommunicationException("Failed to convert internal direction to network move");
        }
        netComm.sendMove(PlayerMove.of(playerUID, networkMove));
    }

    public Map<EPoi, Coordinates> getEntities() throws NetworkCommunicationException {
        FullMap fullMap = netComm.getGameStateCached(playerUID).getMap();
        if (fullMap == null) {
            logger.error("No FullMap available in the returned GameState for playerID: " + playerUID);
            throw new NetworkCommunicationException("FullMap is not available in the returned GameState");
        }

        Map<EPoi, List<Coordinates>> entities = new HashMap<>();
        for (FullMapNode node : fullMap.getMapNodes()) {
            Coordinates position = new Coordinates(node.getX(), node.getY());
            for (EPoi entity : extractGameEntitiesFromNode(node)) {
                entities.computeIfAbsent(entity, k -> new ArrayList<>()).add(position);
            }
        }
        return simplifyEntityMap(entities);
    }

    private Map<EPoi, Coordinates> simplifyEntityMap(Map<EPoi, List<Coordinates>> entities) {
        Map<EPoi, Coordinates> simplifiedMap = new HashMap<>();
        entities.forEach((entity, positions) -> simplifiedMap.put(entity, positions.get(0))); // Assuming first position is sufficient
        return simplifiedMap;
    }

    private PlayerHalfMap internalHalfMapToNetworkHalfMap(HalfMapEntity hmdata) {
        Coordinates castlePosition = hmdata.castlePosition();
        List<PlayerHalfMapNode> networkMapNodes = hmdata.getStream().map(node -> convertToNetworkNode(node, castlePosition)).collect(Collectors.toList());

        return new PlayerHalfMap(playerUID, networkMapNodes);
    }

    private PlayerHalfMapNode convertToNetworkNode(Map.Entry<Coordinates, EField> node, Coordinates castlePosition) {
        messagesbase.messagesfromclient.ETerrain terrain = internalTerrainToNetwork(node.getValue());
        boolean isCastle = castlePosition.equals(node.getKey());
        Coordinates position = node.getKey();
        return new PlayerHalfMapNode(position.getx(), position.gety(), isCastle, terrain);
    }

    private EPlayerState networkStateToInternal(messagesbase.messagesfromserver.EPlayerGameState state) {
        return switch (state) {
            case Lost -> EPlayerState.LOST;
            case Won -> EPlayerState.WON;
            default -> EPlayerState.UNKNOWN;
        };
    }

}