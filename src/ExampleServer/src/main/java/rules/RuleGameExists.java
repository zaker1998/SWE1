package rules;

import java.util.Collection;
import java.util.stream.Collectors;

import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import exceptions.GameNotFoundException;
import game.IGameControllerAccesser;

public class RuleGameExists implements IRules {

	private final IGameControllerAccesser games;

	public RuleGameExists(IGameControllerAccesser games) {
		this.games = games;
	}

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		validateGame(gameID);

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfmap) {
		validateGame(gameID);

	}

	@Override
	public void validateGetGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		validateGame(gameID);

	}

	@Override
	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove) {
		validateGame(gameID);

	}

	private void validateGame(UniqueGameIdentifier gameID) {
		Collection<String> gameIDsString = games.getUsedGameID().stream().map(ele -> ele.asString())
				.collect(Collectors.toSet());

		String strGameID = gameID.getUniqueGameID();

		if (!gameIDsString.contains(strGameID)) {
			throw new GameNotFoundException("The passed gameID was not found among the created games!");
		}
	}

}
