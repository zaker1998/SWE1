package rules;

import java.util.Map;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import exceptions.InvalidMapException;
import game.map.helpers.Position;
import network.translation.NetworkHalfMapTranslator;
import rules.helpers.EHalfMapHelpers;

public class RuleHalfMapEdges implements IRules {

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfmap) {
		NetworkHalfMapTranslator halfMapTrans = new NetworkHalfMapTranslator();
		Map<Position, ETerrain> positionMap = halfMapTrans.extractNetorkHalfMapTerrainMap(halfmap);

		var mapset = positionMap.entrySet();

		// count occurecnces of water on edges
		long leftEdgeCount = mapset.stream().filter(ele -> ele.getKey().getx() == 0 && ele.getValue() == ETerrain.Water)
				.count();
		long rightEdgeCount = mapset.stream().filter(
				ele -> ele.getKey().getx() == EHalfMapHelpers.WIDTH.get() - 1 && ele.getValue() == ETerrain.Water)
				.count();
		long topEdgeCount = mapset.stream().filter(ele -> ele.getKey().gety() == 0 && ele.getValue() == ETerrain.Water)
				.count();
		long botEdgeCount = mapset.stream().filter(
				ele -> ele.getKey().gety() == EHalfMapHelpers.HEIGHT.get() - 1 && ele.getValue() == ETerrain.Water)
				.count();

		if (leftEdgeCount >= EHalfMapHelpers.HEIGHT_EDGE_MAX_WATER.get()
				|| rightEdgeCount >= EHalfMapHelpers.HEIGHT_EDGE_MAX_WATER.get()
				|| topEdgeCount >= EHalfMapHelpers.WIDTH_EDGE_MAX_WATER.get()
				|| botEdgeCount >= EHalfMapHelpers.WIDTH_EDGE_MAX_WATER.get()) {
			throw new InvalidMapException("The edges contained too much water!");
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
