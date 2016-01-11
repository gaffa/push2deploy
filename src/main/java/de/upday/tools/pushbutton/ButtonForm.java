package de.upday.tools.pushbutton;

import java.awt.*;

import javax.swing.*;

import de.upday.tools.pushbutton.button.ButtonEventListener;
import de.upday.tools.pushbutton.jenkins.JenkinsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ButtonForm extends JFrame implements ButtonEventListener {

    @Autowired
    private JenkinsClient jenkinsClient;

    private JTextArea titleText;

    public ButtonForm() {

        super("CI Push Button");
        setVisible(true);
        setSize(1000, 800);
        titleText = new JTextArea("awaiting button events...");
        titleText.setFont(new Font("Verdana", Font.BOLD, 50));
        add(titleText);
    }

    @Override
    public void lidOpened() {
        setText("opened");
    }

    @Override
    public void lidClosed() {
        setText("closed");
    }

    @Override
    public void buttonPressed() {
        jenkinsClient.startJob("/job/test");
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
