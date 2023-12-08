/*
package moveGenerations_tests;

import utils.Utils;
import map.fullMap.FullMapGetter;
import map.fullMap.FullMapEntity;
import map.utils.EPoi;
import map.utils.EField;
import map.utils.Coordinates;
import moving.generation.MoveHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MoveGeneration_Tests {

	private FullMapGetter validMap;

	@BeforeEach
	public void getMapAccesser() {
		char[][] nodes = { { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'm' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'm' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' } };
		HashMap<Coordinates, EField> testMap = Utils.arrayToMap(nodes);

		Map<EPoi, Coordinates> entities = new HashMap<>();
		entities.put(EPoi.MYPLAYER, new Coordinates(0, 0));
		entities.put(EPoi.MYCASTLE, new Coordinates(0, 5));
		entities.put(EPoi.MYTREASURE, new Coordinates(0, 10));
		entities.put(EPoi.ENEMYCASTLE, new Coordinates(0, 15));
		validMap = new FullMapGetter(new FullMapEntity(testMap, entities));
	}

	@Test
	@Disabled
	public void MoveGeneration_From_OutOfBoundsOfMap() {
		// TODO: Finnish this!!
		MoveHandler mg = new MoveHandler(validMap);

		mg.getNextMove();

	}

}
*/
