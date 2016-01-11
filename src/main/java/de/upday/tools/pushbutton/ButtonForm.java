package de.upday.tools.pushbutton;


import de.upday.tools.pushbutton.button.ButtonEventListener;
import de.upday.tools.pushbutton.jenkins.JenkinsClient;
import de.upday.tools.pushbutton.jenkins.JenkinsJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.List;

@Component
public class ButtonForm extends JFrame implements ButtonEventListener {

    @Autowired
    private JenkinsClient jenkinsClient;

    private JTextArea titleText;

    private List<JenkinsJob> jobs;

    private int iAmAnUglyInteger = 0;

    public ButtonForm() {

        super("CI Push Button");
        setVisible(true);
        setSize(1000, 800);
        titleText = new JTextArea("loading jenkins deploy jobs...");
        titleText.setFont(new Font("Verdana", Font.BOLD, 50));
        add(titleText);
    }

    @PostConstruct
    public void foo() {
        jobs = jenkinsClient.loadJobs().getJobs();
    }

    @Override
    public void lidOpened() {

        iAmAnUglyInteger++;
        if (iAmAnUglyInteger >= jobs.size()) {
            iAmAnUglyInteger = 0;
        }
        setText(jobs.get(iAmAnUglyInteger).getName());
    }

    @Override
    public void lidClosed() {
        setText("zzzzzz");
    }

    @Override
    public void buttonPressed() {
        JenkinsJob fuckingJob = jobs.get(iAmAnUglyInteger);
        jenkinsClient.startJob(fuckingJob.getUrl());
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI(fuckingJob.getUrl()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void setText(String text) {
        EventQueue.invokeLater(() -> {
            titleText.setText(text);
            forceToFront();
        });
    }

    private void forceToFront() {
        super.setVisible(true);
        int state = super.getExtendedState();
        state &= ~JFrame.ICONIFIED;
        super.setExtendedState(state);
        super.setAlwaysOnTop(true);
        super.toFront();
        super.requestFocus();
        super.setAlwaysOnTop(false);
    }
}
