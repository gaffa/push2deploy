package de.upday.tools.pushbutton;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@EnableScheduling
@SpringBootApplication
public class CiPushButton {

    public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(CiPushButton.class).headless(false).run(args);
    }
}
