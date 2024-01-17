package rules;

import java.util.HashSet;
import java.util.Set;

import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import exceptions.InvalidMapException;
import game.map.helpers.Position;
import rules.helpers.EHalfMapHelpers;

public class RuleHalfMapDimensions implements IRules {

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfmap) {
		var halfmapNodes = halfmap.getMapNodes();

		if (halfmapNodes.size() != EHalfMapHelpers.HEIGHT.get() * EHalfMapHelpers.WIDTH.get()) {
			throw new InvalidMapException("The received halfmap did not have the right number of nodes!");
		}

		Set<Position> positions = new HashSet<>();

		for (PlayerHalfMapNode node : halfmapNodes) {
			positions.add(new Position(node.getX(), node.getY()));
		}

		for (int y = 0; y < EHalfMapHelpers.HEIGHT.get(); ++y) {
			for (int x = 0; x < EHalfMapHelpers.WIDTH.get(); ++x) {
				if (!positions.contains(new Position(x, y))) {
					throw new InvalidMapException("The map did not contain the correct node coords.");
				}
			}
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
