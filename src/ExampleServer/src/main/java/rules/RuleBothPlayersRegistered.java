package rules;

import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import exceptions.GameNotReadyException;
import game.IGameControllerAccesser;
import game.helpers.EGameConstants;
import network.translation.NetworkTranslator;

public class RuleBothPlayersRegistered implements IRules {

	private final IGameControllerAccesser games;

	public RuleBothPlayersRegistered(IGameControllerAccesser games) {
		this.games = games;
	}

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfmap) {
		bothPlayersRegistered(gameID);
	}

	@Override
	public void validateGetGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove) {
		bothPlayersRegistered(gameID);

	}

	private void bothPlayersRegistered(UniqueGameIdentifier gameID) {
		// assume gameID is validate
		NetworkTranslator translate = new NetworkTranslator();

		var registeredPlayers = games.playersRegisteredInGame(translate.networkGameIDToInternal(gameID));

		if (registeredPlayers.size() != EGameConstants.MAX_PLAYER_COUNT.get()) {
			throw new GameNotReadyException("Both players are not ready yet!");
		}
	}

}
