package de.upday.tools.pushbutton.application;

import de.upday.tools.pushbutton.application.jenkins.JenkinsClient;
import de.upday.tools.pushbutton.application.jenkins.JenkinsJob;
import de.upday.tools.pushbutton.application.ui.BrowserService;
import de.upday.tools.pushbutton.application.ui.MainForm;
import de.upday.tools.pushbutton.button.ButtonEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
// has state
public class PushButtonController implements ButtonEventListener {

    @Autowired
    private MainForm mainForm;

    @Autowired
    private JenkinsClient jenkinsClient;

    @Autowired
    private BrowserService browserService;

    private java.util.List<JenkinsJob> jobs;

    private int jobPointer = 0;

    @PostConstruct
    public void init() {
        jobs = jenkinsClient.loadJobs().getJobs();
    }

    @Override
    public void lidOpened() {

        mainForm.forceToFront();

        jobPointer++;
        if (jobPointer >= jobs.size()) {
            jobPointer = 0;
        }
        mainForm.setText(jobs.get(jobPointer).getName());
    }

    @Override
    public void lidClosed() {
        mainForm.setText("zzzzzz");
    }

    @Override
    public void buttonPressed() {
        JenkinsJob job = jobs.get(jobPointer);
        mainForm.setText("starting job: " + job.getName());
        jenkinsClient.startJob(job.getName());
        mainForm.setText("opening " + job.getUrl() + " in browser to display progress...");
        browserService.openUrl(job.getUrl());
    }
}
