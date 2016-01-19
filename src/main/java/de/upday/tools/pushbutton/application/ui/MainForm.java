package de.upday.tools.pushbutton.application.ui;


import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainForm extends JFrame {

    private JTextArea titleText;

    public MainForm() {

        super("CI Push Button");
        setVisible(true);
        setSize(1000, 800);
        titleText = new JTextArea("loading jenkins deploy jobs...");
        titleText.setFont(new Font("Verdana", Font.BOLD, 50));
        add(titleText);
    }

    public void setText(String text) {
        EventQueue.invokeLater(() -> {
            titleText.setText(text);
        });
    }

    public void forceToFront() {
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
