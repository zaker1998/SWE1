package rules;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import exceptions.InvalidMapException;
import rules.helpers.EHalfMapHelpers;

public class RuleHalfMapTerrainCount implements IRules {

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfmap) {
		var halfmapNodes = halfmap.getMapNodes();

		long grass = halfmapNodes.stream().map(node -> node.getTerrain()).filter(ele -> ele == ETerrain.Grass).count();
		long water = halfmapNodes.stream().map(node -> node.getTerrain()).filter(ele -> ele == ETerrain.Water).count();
		long mountain = halfmapNodes.stream().map(node -> node.getTerrain()).filter(ele -> ele == ETerrain.Mountain)
				.count();

		if (grass < EHalfMapHelpers.MIN_GRASS_COUNT.get() || water < EHalfMapHelpers.MIN_WATER_COUNT.get()
				|| mountain < EHalfMapHelpers.MIN_MOUNTAIN_COUNT.get()) {
			throw new InvalidMapException("Invalid minimum count of fields!");
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
