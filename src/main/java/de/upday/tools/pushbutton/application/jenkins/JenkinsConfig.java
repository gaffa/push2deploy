package de.upday.tools.pushbutton.application.jenkins;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JenkinsConfig {

    @Bean
    public JenkinsClient jenkinsClient() {
        return new JenkinsClient("https://jenkins.yana.asideas.de/", "baustelle", "A)%dR89)4u)RZhcrCB2A", false);
    }
}
