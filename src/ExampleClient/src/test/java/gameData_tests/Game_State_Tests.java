package gameData_tests;

import stateofgame.GameState;
import stateofgame.EPlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

class Game_State_Tests {

	private PropertyChangeListener pcl;
	private GameState gd;

	@BeforeEach
	public void setupMockedListener() {
		pcl = Mockito.mock(PropertyChangeListener.class);
	}

	@BeforeEach
	public void setupGameData() {
		gd = new GameState(true);
	}

	@Test
	public void GameData_updateGameState_ShouldFireChange() {
		gd.addListener(pcl);
		gd.updateGameState(EPlayerState.LOST);

		// PropertyChangeEvent pce = new PropertyChangeEvent(gd, , pcl, gd)

		ArgumentMatcher<PropertyChangeEvent> gameStateLostMatcher = new ArgumentMatcher<PropertyChangeEvent>() {
			@Override
			public boolean matches(PropertyChangeEvent argument) {
				return argument.getNewValue().equals(EPlayerState.LOST);
			};
		};

		Mockito.verify(pcl, Mockito.times(1)).propertyChange(Mockito.argThat(gameStateLostMatcher));
	}

	@Test
	public void GameData_nextTurn_ShouldFireChangeTwice() {
		gd.addListener(pcl);
		gd.nextTurn();

		// PropertyChangeEvent pce = new PropertyChangeEvent(gd, , pcl, gd)

		ArgumentMatcher<PropertyChangeEvent> gameStateInteger = new ArgumentMatcher<PropertyChangeEvent>() {
			@Override
			public boolean matches(PropertyChangeEvent argument) {
				return argument.getNewValue() instanceof Integer;
			};
		};
		ArgumentMatcher<PropertyChangeEvent> gameStateBoolean = new ArgumentMatcher<PropertyChangeEvent>() {
			@Override
			public boolean matches(PropertyChangeEvent argument) {
				return argument.getNewValue() instanceof Boolean;
			};
		};

		Mockito.verify(pcl, Mockito.times(1)).propertyChange(Mockito.argThat(gameStateInteger));
		Mockito.verify(pcl, Mockito.times(1)).propertyChange(Mockito.argThat(gameStateBoolean));

	}

}
