package de.upday.tools.pushbutton.config;

import de.upday.tools.pushbutton.jenkins.JenkinsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JenkinsConfig {

    @Bean
    public JenkinsClient jenkinsClient() {
        return new JenkinsClient("https://insert-url-he.re", "insert-username-here", "insert-pw-here", false);
    }
}
