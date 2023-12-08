package communication;

import exceptions.NetworkCommunicationException;
import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;

public class NetworkCommunicator {

    private static final Logger logger = LoggerFactory.getLogger(NetworkCommunicator.class);
    private final UniqueGameIdentifier gameID;
    private final WebClient baseWebClient;
    private GameState cached;
    private boolean gameStateValid = false;
    private long lastCommandTs = 0;


    public NetworkCommunicator(String gameURL, UniqueGameIdentifier gameID) {
        if (gameURL == null || gameID == null) {
            throw new IllegalArgumentException("Game URL and Game ID cannot be null");
        }

        this.gameID = gameID;

        baseWebClient = WebClient.builder().baseUrl(gameURL + "/games").defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE).defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
    }


    public UniquePlayerIdentifier registerPlayer(PlayerRegistration playerReg) throws NetworkCommunicationException {
        if (playerReg == null) {
            logger.error("PlayerRegistration passed to registerPlayer is null");
            throw new IllegalArgumentException("the playerReg passed is null");
        }

        waitForCommand();

        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
                // specify the data which is set to the server
                .uri("/" + gameID.getUniqueGameID() + "/players").body(BodyInserters.fromValue(playerReg)).retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

        // this can throw NetworkCommunicationException
        ResponseEnvelope<UniquePlayerIdentifier> result = getResults(webAccess);

        // return results
        return result.getData().get();
    }


    public GameState getGameState(UniquePlayerIdentifier playerID) throws NetworkCommunicationException {
        if (playerID == null) {
            logger.error("playerID was null");
            throw new IllegalArgumentException("The PlayerID passed is null");
        }

        waitForCommand();

        Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET).uri("/" + gameID.getUniqueGameID() + "/states/" + playerID.getUniquePlayerID()).retrieve().bodyToMono(ResponseEnvelope.class);

        // this can throw NetworkCommunicationException
        ResponseEnvelope<GameState> result = getResults(webAccess);

        // cache the results and make the cached results valid
        cached = result.getData().get();
        gameStateValid = true;

        // return results
        return cached;
    }

    public GameState getGameStateCached(UniquePlayerIdentifier playerID) throws NetworkCommunicationException {
        if (playerID == null) {
            throw new IllegalArgumentException("PlayerID cannot be null");
        }

        long currentTime = new Date().getTime();
        if (gameStateValid && (currentTime - lastCommandTs < 400)) {
            logger.debug("Returning cached GameState");
            return cached;
        }

        return getGameState(playerID);
    }

    public void sendHalfMap(PlayerHalfMap halfMap) throws NetworkCommunicationException {
        if (halfMap == null) {
            throw new IllegalArgumentException("HalfMap cannot be null");
        }

        waitForCommand();

        Mono<ResponseEnvelope> webAccess = baseWebClient.post().uri("/" + gameID.getUniqueGameID() + "/halfmaps").body(BodyInserters.fromValue(halfMap)).retrieve().bodyToMono(ResponseEnvelope.class);

        getResults(webAccess);
    }


    public void sendMove(PlayerMove playerMove) throws NetworkCommunicationException {
        if (playerMove == null) {
            throw new IllegalArgumentException("PlayerMove cannot be null");
        }

        waitForCommand();

        Mono<ResponseEnvelope> webAccess = baseWebClient.post().uri("/" + gameID.getUniqueGameID() + "/moves").header("accept", MediaType.APPLICATION_XML_VALUE).body(BodyInserters.fromValue(playerMove)).retrieve().bodyToMono(ResponseEnvelope.class);

        getResults(webAccess);

        gameStateValid = false;
    }


    private void waitForCommand() {
        long currentTime = new Date().getTime();
        long timeSinceLastCommand = currentTime - lastCommandTs;
        if (timeSinceLastCommand < 400) {//400ms between requests!
            try {
                long sleepTime = 400 - timeSinceLastCommand;
                logger.debug("Sleeping for: " + sleepTime);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                logger.error("Interrupted during sleep, ensure single-threaded execution");
                throw new RuntimeException("Thread interrupted during sleep", e);
            }
        }
        lastCommandTs = currentTime;
    }

    private <T> ResponseEnvelope<T> getResults(Mono<ResponseEnvelope> webAccess) throws NetworkCommunicationException {
        ResponseEnvelope result;
        try {
            result = webAccess.block();
        } catch (RuntimeException e) {
            logger.error("Network communication failed");
            throw new NetworkCommunicationException("Network communication failure");
        }

        if (result.getState() == ERequestState.Error) {
            String errorMsg = "Error from server: " + result.getExceptionMessage();
            logger.error(errorMsg);
            throw new NetworkCommunicationException(errorMsg);
        }
        return result;
    }

}
