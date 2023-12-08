/*
package moveGenerations_tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import map.fullMap.FullMapGetter;
import map.utils.EPoi;
import map.utils.EMyHalfChecker;
import map.utils.EField;
import map.utils.Coordinates;
import moving.generation.MoveStrategy;
import moving.EDirection;

public class MoveGenerator_Tests {
	private Map<Coordinates, EField> testMap;

	@Test
	public void MoveGeneration_TreasureNextToMe_shouldGoThere() {
		FullMapGetter mocked = Mockito.mock(FullMapGetter.class);

		Mockito.when(mocked.getHeight()).thenReturn(4);
		Mockito.when(mocked.getEntityPosition(EPoi.MYPLAYER)).thenReturn(new Coordinates(5, 5));
		Mockito.when(mocked.getEntityPosition(eq(EPoi.MYTREASURE))).thenReturn(new Coordinates(5, 6));
		Mockito.when(mocked.getEntityPosition(eq(EPoi.MYCASTLE))).thenReturn(new Coordinates(5, 5));
		Mockito.when(mocked.getTerrainAt(any())).thenReturn(EField.GRASS);
		Mockito.when(mocked.getMyMapHalf()).thenReturn(EMyHalfChecker.LONGMAPOPPOSITE);
		Mockito.when(mocked.treasureCollected()).thenReturn(false);
		Mockito.when(mocked.getHeight()).thenReturn(8);
		Mockito.when(mocked.getWidth()).thenReturn(8);

		MoveHandler mg = new MoveHandler(mocked);

		Assertions.assertEquals(mg.getNextMove(), EDirection.DOWN);
	}

}
*/
