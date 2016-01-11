package de.upday.tools.pushbutton.button;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

/**
 * Interaction with the USB Button and program
 * <p/>
 * Attention: has state
 */
@Service
public class ButtonService {

	private final static int vendorId = 0x1d34;
	private final static int productId = 0xd;
	private final static byte[] statusCommand = {0, 0, 0, 0, 0, 0, 0, 2};

	private final Logger log = LoggerFactory.getLogger(getClass());

	private HidDevice buttonDevice;

	private int lastState;

	@Autowired
	private List<ButtonEventListener> buttonEventListeners;

	@PostConstruct
	public void setupHid() {
		final HidServices hidServices = HidManager.getHidServices();
		buttonDevice = hidServices.getHidDevice(vendorId, productId, null);
		if (buttonDevice == null) {
			throw new IllegalStateException("USB Button not found");
		}
	}

	@Scheduled(fixedDelay = 100L)
	private void checkStatus() throws IOException {

		final int state = state();
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

		buttonDevice.write(statusCommand, 8, (byte) 0);

		final byte[] buf = new byte[8];
		buttonDevice.read(buf, 100);
		return buf[0];
	}
}
