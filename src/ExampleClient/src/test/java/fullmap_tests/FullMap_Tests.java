package fullmap_tests;

import exceptions.MockPropertyChangeListener;
import map.fullMap.FullMapEntity;
import map.utils.Coordinates;
import map.utils.EField;
import map.utils.EPoi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import utils.Utils;

import java.util.HashMap;

public class FullMap_Tests {

	@Test
	public void testMapDimensions_10x10() {
		HashMap<Coordinates, EField> testMap = Utils.createTestFullMap(10, 10);
		FullMapEntity fm = new FullMapEntity(testMap, new HashMap<>());
		Assertions.assertEquals(10, fm.getWidth());
		Assertions.assertEquals(10, fm.getHeight());
	}


	@Test
	public void updateEntities_validInput_shouldUpdate() {
		FullMapEntity fm = new FullMapEntity(Utils.createTestFullMap(5, 5), new HashMap<>());
		HashMap<EPoi, Coordinates> newEntities = new HashMap<>();
		newEntities.put(EPoi.MYPLAYER, new Coordinates(2, 2));
		fm.updateEntities(newEntities);
		Assertions.assertEquals(new Coordinates(2, 2), fm.getGameEntitiesCopy().get(EPoi.MYPLAYER));
	}
	
	@Test
	public void collectTreasure_whenNotCollected_shouldSetFlag() {
		FullMapEntity fm = new FullMapEntity(Utils.createTestFullMap(5, 5), new HashMap<>());
		fm.collectTreasure();
		Assertions.assertTrue(fm.getTreasureCollected());
	}

	@Test
	public void listenerNotification_onEntityUpdate_shouldNotify() {
		FullMapEntity fm = new FullMapEntity(Utils.createTestFullMap(5, 5), new HashMap<>());
		// Assuming MockPropertyChangeListener is a mock implementation
		MockPropertyChangeListener mockListener = new MockPropertyChangeListener();
		fm.addListener(mockListener);

		fm.updateEntities(new HashMap<>()); // Trigger update

		Assertions.assertTrue(mockListener.isNotified());
	}

	@Test
	public void updateEntities_invalidInput_shouldThrowException() {
		FullMapEntity fm = new FullMapEntity(Utils.createTestFullMap(5, 5), new HashMap<>());
		Executable updateCall = () -> fm.updateEntities(null);
		Assertions.assertThrows(IllegalArgumentException.class, updateCall);
	}


}
