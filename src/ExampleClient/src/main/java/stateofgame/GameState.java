package stateofgame;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GameState {

    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    private EPlayerState ePlayerState = EPlayerState.UNKNOWN;
    private int roundCounter = 0;
    private boolean isMyRound;

    public GameState(boolean isMyRound) {
        this.isMyRound = isMyRound;
    }

    public void nextTurn() {
        ++roundCounter;
        isMyRound = !isMyRound;
        changes.firePropertyChange("roundCount", (Integer) (roundCounter - 1), (Integer) roundCounter);
        changes.firePropertyChange("myRound", (Boolean) !isMyRound, (Boolean) isMyRound);
    }

    public void updateGameState(EPlayerState newState) {
        changes.firePropertyChange("gameState", ePlayerState, newState);
        ePlayerState = newState;
    }

    public EPlayerState getePlayerState() {
        return ePlayerState;
    }

    public void addListener(PropertyChangeListener view) {
        changes.addPropertyChangeListener(view);
    }

}
