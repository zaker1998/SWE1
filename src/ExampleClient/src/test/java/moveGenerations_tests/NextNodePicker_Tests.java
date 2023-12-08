/*
package moveGenerations_tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import map.fullMap.FullMapGetter;
import map.utils.EPoi;
import map.utils.EMyHalfChecker;
import map.utils.EField;
import map.utils.Coordinates;
import moving.generation.NextNodePicker;

public class NextNodePicker_Tests {

	@Test
	public void NodeFinder_CastleNextToMe_shouldGoThere() {
		FullMapGetter mocked = Mockito.mock(FullMapGetter.class);

		Mockito.when(mocked.getHeight()).thenReturn(4);
		Mockito.when(mocked.getEntityPosition(eq(EPoi.MYCASTLE))).thenReturn(new Coordinates(15, 0));
		Mockito.when(mocked.getEntityPosition(eq(EPoi.ENEMYCASTLE))).thenReturn(new Coordinates(0, 1));
		Mockito.when(mocked.getEntityPosition(EPoi.MYPLAYER)).thenReturn(new Coordinates(0, 0));
		Mockito.when(mocked.getTerrainAt(any())).thenReturn(EField.GRASS);

		NextNodePicker cf = new NextNodePicker(mocked, EMyHalfChecker.LONGMAPOPPOSITE, EPoi.ENEMYCASTLE);

		Assertions.assertEquals(cf.getNextPosition(), new Coordinates(0, 1));
	}

}
*/
