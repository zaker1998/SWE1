/*
package halfmap_tests;

import exceptions.BadHalfMapGeneratedException;
import utils.Utils;
import map.halfMap.HalfMapEntity;
import map.utils.EField;
import map.utils.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.HashMap;

public class HalfMapValidation_Tests {

	@Test
	public void ParseMap_withIncorrectNumberOfNodes_shouldThrowError() {

		char[][] nodes = { { 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' } };
		HashMap<Coordinates, EField> testMap = Utils.arrayToMap(nodes);

		Executable halfMapInit = () -> {
			var t = new HalfMapEntity(testMap, new Coordinates(0, 0));
		};

		Assertions.assertThrows(BadHalfMapGeneratedException.class, halfMapInit,
				"An exception was expected because the passed terrain nodes are not of sufficient size!");
	}

	@Test
	public void ParseMap_withWrongPositionIndex_shouldThrowError() {

		char[][] nodes = { { 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' } };
		HashMap<Coordinates, EField> testMap = Utils.arrayToMap(nodes);

		// remove one node and insert a false one
		testMap.remove(new Coordinates(5, 2));
		testMap.put(new Coordinates(10, 3), EField.WATER);

		Executable halfMapInit = () -> {
			var t = new HalfMapEntity(testMap, new Coordinates(0, 0));
		};

		Assertions.assertThrows(BadHalfMapGeneratedException.class, halfMapInit,
				"An exception was expected because a Position index in the passed map was out of bounds!");
	}

	@Test
	public void ParseMap_withIncorrectNumberOfWaterNodes_shouldThrowError() {

		char[][] nodes = { { 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'w', 'm', 'm' },
				{ 'g', 'g', 'm', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'm', 'g', 'g', 'g', 'm', 'm' } };
		HashMap<Coordinates, EField> testMap = Utils.arrayToMap(nodes);

		Executable halfMapInit = () -> {
			var t = new HalfMapEntity(testMap, new Coordinates(0, 0));
		};

		Assertions.assertThrows(BadHalfMapGeneratedException.class, halfMapInit,
				"An exception was expected because there were not enough WATER fields parsed!");
	}

	@Test
	public void ParseMap_withTooMuchWaterOnEdge_shouldThrowError() {

		char[][] nodes = { { 'g', 'w', 'w', 'w', 'm', 'w', 'g', 'g' }, { 'g', 'm', 'g', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' } };
		HashMap<Coordinates, EField> testMap = Utils.arrayToMap(nodes);

		Executable halfMapInit = () -> {
			var t = new HalfMapEntity(testMap, new Coordinates(0, 0));
		};

		Assertions.assertThrows(BadHalfMapGeneratedException.class, halfMapInit,
				"An exception was expected because the passed terrain nodes contain too much water on the northern edge!");
	}

	@Test
	public void ParseMap_withIslandType1_shouldThrowError() {

		char[][] nodes = { { 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' } };
		HashMap<Coordinates, EField> testMap = Utils.arrayToMap(nodes);

		Executable halfMapInit = () -> {
			var t = new HalfMapEntity(testMap, new Coordinates(0, 0));
		};

		Assertions.assertThrows(BadHalfMapGeneratedException.class, halfMapInit,
				"An exception was expected because the passed terrain contains an Island!");
	}

	@Test
	public void ParseMap_CastleOnMountain_shouldThrowError() {

		char[][] nodes = { { 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'm', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' } };
		HashMap<Coordinates, EField> testMap = Utils.arrayToMap(nodes);

		Executable halfMapInit = () -> {
			var t = new HalfMapEntity(testMap, new Coordinates(0, 0));
		};

		Assertions.assertThrows(RuntimeException.class, halfMapInit,
				"An exception was expected because the the castle was placed on a mountain!");
	}

}
*/
