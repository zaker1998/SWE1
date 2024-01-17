package game.player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.TooManyPlayersRegistered;
import game.helpers.EGameConstants;
import game.player.helpers.ESPlayerGameState;
import game.player.helpers.PlayerInformation;
import game.player.helpers.SUniquePlayerIdentifier;
import game.propertychange.IRegisterForEvent;
import game.propertychange.PropertyChangeListener;
import game.propertychange.PropertyChangeSupport;

public class PlayersController {

	// private final PlayerStorage players = new PlayerStorage();
	private final Set<Player> registeredPlayers = new HashSet<>();
	private final Queue<Player> playerTurn = new LinkedList<>();

	private Optional<SUniquePlayerIdentifier> winner = Optional.empty();
	private int turn = 0;

	private final PropertyChangeSupport<Void> playersReady = new PropertyChangeSupport<>();

	private static Logger logger = LoggerFactory.getLogger(PlayersController.class);

	// add objects this listens to
	public void listenToAllRequiredProperties(IRegisterForEvent<SUniquePlayerIdentifier> steppedOnTreassure,
			IRegisterForEvent<SUniquePlayerIdentifier> steppedOnCastle) {
		listenToTreassureCollected(steppedOnTreassure);
		listenToSteppedOnCastle(steppedOnCastle);
	}

	private void listenToTreassureCollected(IRegisterForEvent<SUniquePlayerIdentifier> steppedOnTreassure) {
		steppedOnTreassure.register(this::collectTreassure);
	}

	private void listenToSteppedOnCastle(IRegisterForEvent<SUniquePlayerIdentifier> steppedOnCastle) {
		steppedOnCastle.register(this::steppedOnCastle);
	}

	public SUniquePlayerIdentifier registerPlayer(PlayerInformation playerInf) {

		Player newPlayer = Player.getRandomPlayer(playerInf);
		if (registeredPlayers.size() == EGameConstants.MAX_PLAYER_COUNT.get()) {
			logger.warn("Tried registering a player even though the game is already full!");
			throw new TooManyPlayersRegistered("The game you tried to register for is already full");
		}

		registeredPlayers.add(newPlayer);

		if (registeredPlayers.size() == EGameConstants.MAX_PLAYER_COUNT.get()) {
			logger.debug("Reached max players. Picking player to go first!");
			pickPlayerOrder();
		}

		return newPlayer;
	}

	public void registerListenForPlayersReady(PropertyChangeListener<Void> listener) {
		playersReady.register(listener);
	}

	public boolean checkPlayerTurn(SUniquePlayerIdentifier playerID) {
		// allPlayersRegisteredOrThrow();
		assert (allPlayersRegistered());

		SUniquePlayerIdentifier current = playerTurn.element();
		if (!current.equals(playerID)) {
			return false;
		}
		return true;
	}

	public boolean isPlayerRegistered(SUniquePlayerIdentifier playerID) {
		return registeredPlayers.contains(playerID);
	}

	public void nextTurn() {
		++turn;
		playerTurn.add(playerTurn.remove());
	}

	public void setAsWinner(SUniquePlayerIdentifier winnerID) {
		winner = Optional.of(winnerID);
	}

	public void setAsLooser(SUniquePlayerIdentifier looserID) {
		setAsWinner(getOtherPlayer(looserID));
	}

	public int getTurn() {
		return turn;
	}

	public Collection<IPlayerAccesser> getRegisteredPlayers() {
		// make a copy so the collection cannot be modified
		return registeredPlayers.stream().map(ele -> ele).collect(Collectors.toList());
	}

	public SUniquePlayerIdentifier currentTurn() {
		assert (!playerTurn.isEmpty());
		return playerTurn.element();
	}

	public ESPlayerGameState getPlayerState(SUniquePlayerIdentifier playerID) {

		if (!allPlayersRegistered()) {
			return ESPlayerGameState.SHOULD_WAIT;
		}

		if (winner.isPresent()) {
			if (playerID.equals(winner.get())) {
				return ESPlayerGameState.WON;
			} else {
				return ESPlayerGameState.LOST;
			}
		}

		if (playerID.equals(playerTurn.element())) {
			return ESPlayerGameState.SHOULD_ACT_NEXT;
		}

		return ESPlayerGameState.SHOULD_WAIT;
	}

	private void collectTreassure(SUniquePlayerIdentifier playerID) {
		assert (isPlayerRegistered(playerID));
		Player player = getPlayer(playerID);
		player.collectTreasure();
	}

	private void steppedOnCastle(SUniquePlayerIdentifier playerID) {
		assert (isPlayerRegistered(playerID));
		Player player = getPlayer(playerID);
		if (player.getCollectedTreasure()) {
			setAsWinner(player);
		}
	}

	private Player getPlayer(SUniquePlayerIdentifier playerID) {
		assert (isPlayerRegistered(playerID));
		return registeredPlayers.stream().filter(player -> player.equals(playerID)).findFirst().get();
	}

	private SUniquePlayerIdentifier getOtherPlayer(SUniquePlayerIdentifier myPlayer) {
		// allPlayersRegisteredOrThrow();
		assert (allPlayersRegistered());

		return registeredPlayers.stream().filter(player -> !player.equals(myPlayer)).findFirst().get();
	}

	private boolean allPlayersRegistered() {
		return !playerTurn.isEmpty();
	}

	/*
	 * private void allPlayersRegisteredOrThrow() { if (!allPlayersRegistered()) {
	 * logger.
	 * warn("Tried to perform an action even though the both players are not registered!"
	 * ); throw new GameNotReadyException(
	 * "Tried to perform an action even though the both players are not registered!"
	 * ); } }
	 */

	private void pickPlayerOrder() {
		List<Player> players = registeredPlayers.stream().collect(Collectors.toList());
		// shuffle the registered playerIDs
		Collections.shuffle(players);
		playerTurn.addAll(players);

		playersReady.fire();
	}

}