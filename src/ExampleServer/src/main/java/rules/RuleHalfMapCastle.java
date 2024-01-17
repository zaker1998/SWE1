package rules;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import exceptions.InvalidMapException;

public class RuleHalfMapCastle implements IRules {

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfmap) {
		oneCastle(halfmap);
		castleAtGrass(halfmap);
	}

	@Override
	public void validateGetGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove) {
		// TODO Auto-generated method stub

	}

	private void oneCastle(PlayerHalfMap halfmap) {
		var halfmapNodes = halfmap.getMapNodes();

		long castleCount = halfmapNodes.stream().filter(node -> node.isFortPresent()).count();

		if (castleCount != 1) {
			throw new InvalidMapException("There was no or more then one castle present!");
		}
	}

	private void castleAtGrass(PlayerHalfMap halfmap) {
		var halfmapNodes = halfmap.getMapNodes();

		boolean castleAtGrass = halfmapNodes.stream().filter(node -> node.isFortPresent())
				.allMatch(node -> node.getTerrain() == ETerrain.Grass);

		if (!castleAtGrass) {
			throw new InvalidMapException("The castle was not placed on a grass field!");
		}
	}

}
