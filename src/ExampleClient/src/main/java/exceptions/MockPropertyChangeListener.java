package exceptions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MockPropertyChangeListener implements PropertyChangeListener {
    private boolean notified = false;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        notified = true;
    }

    public boolean isNotified() {
        return notified;
    }
}
