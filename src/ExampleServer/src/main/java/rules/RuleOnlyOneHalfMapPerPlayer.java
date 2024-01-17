package rules;

import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import exceptions.TooManyHalfMapsReceived;
import game.IGameControllerAccesser;
import network.translation.NetworkTranslator;

public class RuleOnlyOneHalfMapPerPlayer implements IRules {

	private final IGameControllerAccesser games;

	public RuleOnlyOneHalfMapPerPlayer(IGameControllerAccesser games) {
		this.games = games;
	}

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfmap) {
		// assume gameID is already validated

		NetworkTranslator translate = new NetworkTranslator();
		boolean mapReady = games.isMapReady(translate.networkGameIDToInternal(gameID));

		// if the map is ready the game has combined the two half maps and thus if the
		// game was turn based both players have sent one half map each
		if (mapReady) {
			throw new TooManyHalfMapsReceived("The game you triedd to send a halfmap to already had both half maps");
		}

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
