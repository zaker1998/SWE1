package rules;

import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import exceptions.TooManyPlayersRegistered;
import game.IGameControllerAccesser;
import game.helpers.EGameConstants;
import network.translation.NetworkTranslator;

public class RuleGameNotFull implements IRules {

	private final IGameControllerAccesser games;

	public RuleGameNotFull(IGameControllerAccesser games) {
		this.games = games;
	}

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// assume gameID is already validated

		NetworkTranslator translate = new NetworkTranslator();
		var registeredPlayers = games.playersRegisteredInGame(translate.networkGameIDToInternal(gameID));

		if (registeredPlayers.size() >= EGameConstants.MAX_PLAYER_COUNT.get()) {
			throw new TooManyPlayersRegistered("The game you tried to register to is already full!");
		}
	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfmap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateGetGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove) {
		// TODO Auto-generated method stub

	}

}
