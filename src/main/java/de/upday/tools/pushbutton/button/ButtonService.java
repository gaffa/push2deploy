package de.upday.tools.pushbutton.button;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Interaction with the USB Button and program
 * <p>
 * Attention: has state
 */
@Service
public class ButtonService {

    private final static int vendorId = 7476;
    private final static int productId = 13;
    private final static byte[] statusCommand = {0, 0, 0, 0, 0, 0, 0, 0, 2};

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final HIDDevice buttonDevice;

    private Integer lastState = 0;

    @Autowired
    private List<ButtonEventListener> buttonEventListeners = new ArrayList<>();

    public ButtonService() throws IOException {

        ClassPathLibraryLoader.loadNativeHIDLibrary();
        final HIDManager manager = HIDManager.getInstance();

        buttonDevice = manager.openById(vendorId, productId, null);

        if (buttonDevice == null) {
            throw new RuntimeException("button de.upday.tools.pushbutton.button not found");
        }
    }

    @Scheduled(fixedDelay = 100L)
    private void checkStatus() throws IOException {

        int state = state();
        if (lastState != state) {

            switch (state) {
                case 21:
                    // currently closed
                    buttonEventListeners.forEach(ButtonEventListener::lidClosed);
                    lastState = state;
                    break;

                case 23:
                    // currently open, only fire event if not button pressed before (its natural to have an open lid after a button press)
                    if (lastState != 22) {
                        buttonEventListeners.forEach(ButtonEventListener::lidOpened);
                        lastState = state;
                    }
                    break;

                case 22:
                    // currently pressed
                    buttonEventListeners.forEach(ButtonEventListener::buttonPressed);
                    lastState = state;
                    break;

                case 0:
                    // unknown (de.upday.tools.pushbutton.button)
                    log.debug("button reported unknown state");
                    break;

                case 1:
                    // error
                    log.info("button reported error state");
                    break;

                default:
                    // unknown (application)
                    log.info("state (" + state + ") unmapped");
                    break;
            }
        }

    }

    private int state() throws IOException {

        // ask status
        buttonDevice.write(statusCommand);

        // get state
        byte[] data = new byte[8];
        buttonDevice.readTimeout(data, 100);
        return data[0];
    }
}
