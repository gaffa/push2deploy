package de.upday.tools.pushbutton.button;

/**
 * Register this to listen for de.upday.tools.pushbutton.button events
 */
public interface ButtonEventListener {

    public default void lidOpened() {
        // no-op
    }

    public default void lidClosed() {
        // no-op
    }

    public default void buttonPressed() {
        // no-op
    }
}
