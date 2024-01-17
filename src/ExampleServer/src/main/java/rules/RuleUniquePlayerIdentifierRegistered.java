package rules;

import java.util.stream.Collectors;

import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import exceptions.PlayerNotFoundException;
import game.IGameControllerAccesser;
import network.translation.NetworkTranslator;

public class RuleUniquePlayerIdentifierRegistered implements IRules {

	private final IGameControllerAccesser games;

	public RuleUniquePlayerIdentifierRegistered(IGameControllerAccesser games) {
		this.games = games;
	}

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfmap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateGetGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		// assume gameID is already validated

		NetworkTranslator translate = new NetworkTranslator();

		var registeredPlayersString = games.playersRegisteredInGame(translate.networkGameIDToInternal(gameID)).stream()
				.map(pID -> pID.asString()).collect(Collectors.toSet());

		String strPlayerID = playerID.getUniquePlayerID();

		if (!registeredPlayersString.contains(strPlayerID)) {
			throw new PlayerNotFoundException("The sent playerID was not found in the provided game");
		}

	}

	@Override
	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove) {
		// TODO Auto-generated method stub

	}

}
