package de.upday.tools.pushbutton.application.jenkins;

import java.util.List;

public class JenkinsJobListResponse {

    private List<JenkinsJob> jobs;

    public JenkinsJobListResponse() {
    }

    public JenkinsJobListResponse(List<JenkinsJob> jobs) {
        this.jobs = jobs;
    }

    public List<JenkinsJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<JenkinsJob> jobs) {
        this.jobs = jobs;
    }
}
