package rules;

import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import exceptions.MapNotReadyException;
import game.IGameControllerAccesser;
import game.helpers.SUniqueGameIdentifier;
import network.translation.NetworkTranslator;

public class RuleMapReady implements IRules {

	private final IGameControllerAccesser games;

	public RuleMapReady(IGameControllerAccesser games) {
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
		// TODO Auto-generated method stub

	}

	@Override
	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove) {
		// assert gameID is validated
		NetworkTranslator translate = new NetworkTranslator();
		SUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);

		if (!games.isMapReady(serverGameID)) {
			throw new MapNotReadyException(
					"The game you tried to send a move to has no finnished map! (Both players did not send the halfmap)");
		}
	}

}
