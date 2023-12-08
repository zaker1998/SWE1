package position_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import map.utils.Coordinates;

class Coordinates_Tests {

	@ParameterizedTest
	@CsvSource({ "1,-1", "-5,1", "-110,-92" })
	public void Position_instantiatedWithNegativeCoordinates_ShouldThrow(int x, int y) {

		Executable instantiate = () -> {
			Coordinates pos = new Coordinates(x, y);
		};

		Assertions.assertThrows(IllegalArgumentException.class, instantiate);

	}

	@ParameterizedTest
	@CsvSource({ "1,1", "2,15", "4,2", "100,102" })
	public void Position_instanciateWithValidCoordinates_ShouldCreateValidObjects(int x, int y) {
		Coordinates pos = new Coordinates(x, y);

		Assertions.assertEquals(pos.getx(), x);
		Assertions.assertEquals(pos.gety(), y);
	}

	@ParameterizedTest
	@CsvSource({ "1,1,2,3", "2,15,4,5", "4,2,2,4", "100,102,99,101" })
	public void Position_differentPositions_ShouldNotEqual(int x1, int y1, int x2, int y2) {
		Coordinates pos1 = new Coordinates(x1, y1);
		Coordinates pos2 = new Coordinates(x2, y2);

		Assertions.assertNotEquals(pos1, pos2);
	}

	@ParameterizedTest
	@CsvSource({ "1,1,2,3", "2,15,4,5", "4,2,2,4", "100,102,99,101" })
	public void Position_differentPositions_ShouldHaveDifferentHashCode(int x1, int y1, int x2, int y2) {
		Coordinates pos1 = new Coordinates(x1, y1);
		Coordinates pos2 = new Coordinates(x2, y2);

		Assertions.assertNotEquals(pos1.hashCode(), pos2.hashCode());
	}

}
