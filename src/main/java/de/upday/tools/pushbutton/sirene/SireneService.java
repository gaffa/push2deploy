package de.upday.tools.pushbutton.sirene;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SireneService {

    private final GpioPinDigitalOutput pin = GpioFactory.getInstance()
                                                        .provisionDigitalOutputPin(RaspiPin.GPIO_01,
                                                                                   "SirenePin",
                                                                                   PinState.LOW);

    @Scheduled(fixedDelay = 100L)
    public void tick() {
        pin.toggle();
    }
}
