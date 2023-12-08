package utils;

import map.utils.EField;
import map.utils.Coordinates;

import java.util.HashMap;

public class Utils {
	public static HashMap<Coordinates, EField> createTestFullMap(int width, int height) {
		HashMap<Coordinates, EField> map = new HashMap<>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				map.put(new Coordinates(x, y), EField.GRASS); // Default to grass
			}
		}
		return map;
	}
}
