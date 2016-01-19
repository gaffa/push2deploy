package de.upday.tools.pushbutton.application.jenkins;

/**
 * Created by igaff on 11.01.2016.
 */
public class JenkinsJob {

    private String name;

    private String url;

    public JenkinsJob() {
    }

    public JenkinsJob(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
